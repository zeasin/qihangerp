package cn.qihangerp.api.service.impl;

import cn.qihangerp.api.domain.ErpShipOrder;
import cn.qihangerp.api.domain.Goods;
import cn.qihangerp.api.domain.GoodsSpec;
import cn.qihangerp.api.domain.bo.OrderItemSpecIdUpdateBo;
import cn.qihangerp.api.domain.bo.SupplierShipDistBo;
import cn.qihangerp.api.mapper.*;
import cn.qihangerp.common.enums.ErpOrderStatusEnum;
import cn.qihangerp.domain.ErpOrder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.domain.ErpOrderItem;
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
public class ErpOrderItemServiceImpl extends ServiceImpl<ErpOrderItemMapper, ErpOrderItem>
    implements ErpOrderItemService{
    private final ErpOrderItemMapper mapper;
    private final ErpOrderMapper orderMapper;
    private final GoodsSpecMapper specMapper;
    private final GoodsMapper goodsMapper;
    private final ErpShipOrderMapper shipOrderMapper;

    @Override
    public PageResult<ErpOrderItem> queryPageList(ErpOrderStatusEnum status,Integer shipStatus, ErpOrderItem bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ErpOrderItem> queryWrapper = new LambdaQueryWrapper<ErpOrderItem>()
                .eq(org.springframework.util.StringUtils.hasText(bo.getOrderNum()),ErpOrderItem::getOrderNum,bo.getOrderNum())
                .eq(org.springframework.util.StringUtils.hasText(bo.getSpecNum()),ErpOrderItem::getSpecNum,bo.getSpecNum())
                .eq(ErpOrderItem::getOrderStatus, status.WAIT_SELLER_SEND_GOODS.getIndex())
                .eq(ErpOrderItem::getShipStatus,shipStatus)
                ;
        Page<ErpOrderItem> pages = mapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(pages);
    }

    @Override
    public int orderItemSpecIdUpdate(OrderItemSpecIdUpdateBo bo) {
        ErpOrderItem erpOrderItem = mapper.selectById(bo.getOrderItemId());
        if(erpOrderItem == null )return -1;
        GoodsSpec goodsSpec = specMapper.selectGoodsSpecById(bo.getErpGoodsSpecId());
        if(goodsSpec== null) return -2;
        Goods goods = goodsMapper.selectGoodsById(goodsSpec.getGoodsId());
        if(goods==null) return -2;

        ErpOrderItem update = new ErpOrderItem();
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
            ErpOrderItem erpOrderItem = mapper.selectById(id);
            if(erpOrderItem==null) return -2;
            if(erpOrderItem.getShipStatus()!=null && erpOrderItem.getShipStatus() !=0)return -1001;
            if(erpOrderItem.getSupplierId()==null || erpOrderItem.getSupplierId()==0) return -1002;

            ErpOrder erpOrder = orderMapper.selectErpOrderById(erpOrderItem.getOrderId());
            if(erpOrder==null) return -2;

            // 添加到发货记录表
            ErpShipOrder shipOrder = new ErpShipOrder();
            shipOrder.setShopId(erpOrder.getShopId());
            shipOrder.setShopType(erpOrder.getShopType());
            shipOrder.setSupplierId(erpOrderItem.getSupplierId());
            shipOrder.setOrderNum(erpOrder.getOrderNum());
            shipOrder.setOrderTime(erpOrder.getOrderTime());
            shipOrder.setErpOrderId(erpOrder.getId());
            shipOrder.setErpOrderItemId(erpOrderItem.getId());
            shipOrder.setGoodsId(erpOrderItem.getGoodsId());
            shipOrder.setSpecId(erpOrderItem.getSpecId());
            shipOrder.setSpecNum(erpOrderItem.getSpecNum());
            shipOrder.setQuantity(erpOrderItem.getQuantity());
            shipOrder.setShipType(1);//供应商发货类型1
            shipOrder.setShipStatus(1);
            shipOrder.setReceiverName(erpOrder.getReceiverName());
            shipOrder.setReceiverPhone(erpOrder.getReceiverPhone());
            shipOrder.setCountry(erpOrder.getCountry());
            shipOrder.setProvince(erpOrder.getProvince());
            shipOrder.setCity(erpOrder.getCity());
            shipOrder.setTown(erpOrder.getTown());
            shipOrder.setAddress(erpOrder.getAddress());
            shipOrder.setCreateBy("分配给供应商发货");
            shipOrder.setCreateTime(new Date());
            shipOrderMapper.insert(shipOrder);

            // 更新订单子表
            ErpOrderItem itemUpdate = new ErpOrderItem();
            itemUpdate.setId(erpOrderItem.getId());
            itemUpdate.setShipType(1);
            itemUpdate.setShipStatus(1);
            itemUpdate.setUpdateTime(new Date());
            itemUpdate.setUpdateBy("分配给供应商发货");
            mapper.updateById(itemUpdate);

            // 更新订单主表
            // 查询是否全部出库中
            List<ErpOrderItem> erpOrderItems = orderMapper.selectOrderItemByOrderId(erpOrderItem.getOrderId());
            List<ErpOrderItem> waitShipList = erpOrderItems.stream().filter(x -> (x.getShipStatus()==null||x.getShipStatus() == 0)&& !x.getId().equals(erpOrderItem.getId())).collect(Collectors.toList());
            if(waitShipList==null || waitShipList.size()==0) {
                // 全部不是待备货状态，更新主表状态
                ErpOrder orderUpdate = new ErpOrder();
                orderUpdate.setId(erpOrderItem.getOrderId());
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




