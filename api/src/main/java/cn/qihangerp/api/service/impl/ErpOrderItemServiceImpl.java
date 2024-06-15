package cn.qihangerp.api.service.impl;

import cn.qihangerp.api.domain.ErpShipOrder;
import cn.qihangerp.api.domain.Goods;
import cn.qihangerp.api.domain.GoodsSpec;
import cn.qihangerp.api.domain.bo.OrderItemSpecIdUpdateBo;
import cn.qihangerp.api.domain.bo.SupplierShipDistBo;
import cn.qihangerp.api.mapper.*;
import cn.qihangerp.common.enums.ErpOrderStatusEnum;
import cn.qihangerp.domain.ErpSaleOrder;
import cn.qihangerp.domain.ErpSaleOrderItem;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.api.service.ErpOrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author TW
* @description 针对表【erp_order_item(订单明细表)】的数据库操作Service实现
* @createDate 2024-04-15 13:35:02
*/
@AllArgsConstructor
@Service
public class ErpOrderItemServiceImpl extends ServiceImpl<ErpOrderItemMapper, ErpSaleOrderItem>
    implements ErpOrderItemService{
    private final ErpOrderItemMapper mapper;
    private final ErpOrderMapper orderMapper;
    private final GoodsSpecMapper specMapper;
    private final GoodsMapper goodsMapper;
    private final ErpShipOrderMapper shipOrderMapper;

    @Override
    public PageResult<ErpSaleOrderItem> queryPageList(ErpOrderStatusEnum status, Integer shipStatus, ErpSaleOrderItem bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ErpSaleOrderItem> queryWrapper = new LambdaQueryWrapper<ErpSaleOrderItem>()
                .eq(org.springframework.util.StringUtils.hasText(bo.getOriginalOrderId()), ErpSaleOrderItem::getOriginalOrderId,bo.getOriginalOrderId())
                .eq(org.springframework.util.StringUtils.hasText(bo.getSpecNum()), ErpSaleOrderItem::getSpecNum,bo.getSpecNum())
                .eq(ErpSaleOrderItem::getOrderStatus, status.WAIT_SELLER_SEND_GOODS.getIndex())
                .eq(ErpSaleOrderItem::getShipStatus,shipStatus)
                ;
        Page<ErpSaleOrderItem> pages = mapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(pages);
    }

    @Override
    public int orderItemSpecIdUpdate(OrderItemSpecIdUpdateBo bo) {
        ErpSaleOrderItem erpSaleOrderItem = mapper.selectById(bo.getOrderItemId());
        if(erpSaleOrderItem == null )return -1;
        GoodsSpec goodsSpec = specMapper.selectGoodsSpecById(bo.getErpGoodsSpecId());
        if(goodsSpec== null) return -2;
        Goods goods = goodsMapper.selectGoodsById(goodsSpec.getGoodsId());
        if(goods==null) return -2;

        ErpSaleOrderItem update = new ErpSaleOrderItem();
        update.setId(bo.getOrderItemId().toString());
        update.setGoodsId(goodsSpec.getGoodsId());
        update.setSpecId(goodsSpec.getId());
        update.setSpecNum(goodsSpec.getSpecNum());
        update.setSupplierId(goods.getSupplierId());
        update.setUpdateBy("手动修改SkuId");
        update.setUpdateTime(new Date());
        mapper.updateById(update);
        return 0;
    }

    /**
     * 分配给供应商发货
     * @param bo
     * @return
     */
    @Transactional
    @Override
    public int distributeSupplierShip(SupplierShipDistBo bo) {
        if(bo.getOrderItemIds()==null || bo.getOrderItemIds().length==0) return -1;//参数为空
        for (Long id:bo.getOrderItemIds()){
            if(id==null||id==0) return -2;
            ErpSaleOrderItem erpSaleOrderItem = mapper.selectById(id);
            if(erpSaleOrderItem ==null) return -2;
            if(erpSaleOrderItem.getShipStatus()!=null && erpSaleOrderItem.getShipStatus() !=0)return -1001;
            if(erpSaleOrderItem.getSupplierId()==null || erpSaleOrderItem.getSupplierId()==0) return -1002;

            ErpSaleOrder erpSaleOrder = orderMapper.selectErpOrderById(erpSaleOrderItem.getOrderId());
            if(erpSaleOrder ==null) return -2;

            // 添加到发货记录表
            ErpShipOrder shipOrder = new ErpShipOrder();
            shipOrder.setShopId(erpSaleOrder.getShopId());
            shipOrder.setShopType(erpSaleOrder.getShopType());
            shipOrder.setSupplierId(erpSaleOrderItem.getSupplierId());
            shipOrder.setOrderNum(erpSaleOrder.getOrderNum());
            shipOrder.setOrderTime(erpSaleOrder.getOrderTime());
            shipOrder.setErpOrderId(erpSaleOrder.getId());
            shipOrder.setErpOrderItemId(erpSaleOrderItem.getId());
            shipOrder.setGoodsId(erpSaleOrderItem.getGoodsId());
            shipOrder.setSpecId(erpSaleOrderItem.getSpecId());
            shipOrder.setSpecNum(erpSaleOrderItem.getSpecNum());
            shipOrder.setQuantity(erpSaleOrderItem.getQuantity());
            shipOrder.setShipType(1);//供应商发货类型1
            shipOrder.setShipStatus(1);
            shipOrder.setReceiverName(erpSaleOrder.getReceiverName());
            shipOrder.setReceiverPhone(erpSaleOrder.getReceiverPhone());
            shipOrder.setCountry(erpSaleOrder.getCountry());
            shipOrder.setProvince(erpSaleOrder.getProvince());
            shipOrder.setCity(erpSaleOrder.getCity());
            shipOrder.setTown(erpSaleOrder.getTown());
            shipOrder.setAddress(erpSaleOrder.getAddress());
            shipOrder.setCreateBy("分配给供应商发货");
            shipOrder.setCreateTime(new Date());
            shipOrderMapper.insert(shipOrder);

            // 更新订单子表
            ErpSaleOrderItem itemUpdate = new ErpSaleOrderItem();
            itemUpdate.setId(erpSaleOrderItem.getId());
            itemUpdate.setShipType(1);
            itemUpdate.setShipStatus(1);
            itemUpdate.setUpdateTime(new Date());
            itemUpdate.setUpdateBy("分配给供应商发货");
            mapper.updateById(itemUpdate);

            // 更新订单主表
            // 查询是否全部出库中
            List<ErpSaleOrderItem> erpSaleOrderItems = orderMapper.selectOrderItemByOrderId(erpSaleOrderItem.getOrderId());
            List<ErpSaleOrderItem> waitShipList = erpSaleOrderItems.stream().filter(x -> (x.getShipStatus()==null||x.getShipStatus() == 0)&& !x.getId().equals(erpSaleOrderItem.getId())).collect(Collectors.toList());
            if(waitShipList==null || waitShipList.size()==0) {
                // 全部不是待备货状态，更新主表状态
                ErpSaleOrder orderUpdate = new ErpSaleOrder();
                orderUpdate.setId(erpSaleOrderItem.getOrderId());
                orderUpdate.setShipStatus(1);
                orderUpdate.setShipType(1);
                orderUpdate.setUpdateTime(new Date());
                orderUpdate.setUpdateBy("分配给供应商发货");
                orderMapper.updateErpOrder(orderUpdate);
            }
        }
        return 0;
    }
}




