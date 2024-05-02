package cn.qihangerp.api.service.impl;

import cn.qihangerp.api.domain.ErpShipOrderAgentFee;
import cn.qihangerp.api.domain.bo.ShipOrderSupplierShipBo;
import cn.qihangerp.api.domain.bo.ShipOrderSupplierShipItemBo;
import cn.qihangerp.api.mapper.ErpOrderItemMapper;
import cn.qihangerp.api.mapper.ErpOrderMapper;
import cn.qihangerp.api.mapper.ErpShipOrderAgentFeeMapper;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.utils.DateUtil;
import cn.qihangerp.common.utils.DateUtils;
import cn.qihangerp.domain.ErpOrder;
import cn.qihangerp.domain.ErpOrderItem;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.api.domain.ErpShipOrder;
import cn.qihangerp.api.service.ErpShipOrderService;
import cn.qihangerp.api.mapper.ErpShipOrderMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
* @author qilip
* @description 针对表【erp_ship_order(订单发货表)】的数据库操作Service实现
* @createDate 2024-05-02 10:15:15
*/
@AllArgsConstructor
@Service
public class ErpShipOrderServiceImpl extends ServiceImpl<ErpShipOrderMapper, ErpShipOrder>
    implements ErpShipOrderService{
    private final ErpShipOrderMapper mapper;
    private final ErpOrderItemMapper orderItemMapper;
    private final ErpOrderMapper orderMapper;
    private final ErpShipOrderAgentFeeMapper agentFeeMapper;
    @Override
    public PageResult<ErpShipOrder> queryPageList(ErpShipOrder bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ErpShipOrder> queryWrapper = new LambdaQueryWrapper<ErpShipOrder>()
                .eq(org.springframework.util.StringUtils.hasText(bo.getOrderNum()),ErpShipOrder::getOrderNum,bo.getOrderNum())
                .eq(org.springframework.util.StringUtils.hasText(bo.getSpecNum()),ErpShipOrder::getSpecNum,bo.getSpecNum())
                .eq(bo.getShipType()!=null, ErpShipOrder::getShipType, bo.getShipType())
                .eq(ErpShipOrder::getShipStatus,bo.getShipStatus())
                ;
        Page<ErpShipOrder> pages = mapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(pages);
    }

    @Override
    public List<ErpShipOrder> queryOrderListById(Long id) {
        ErpShipOrder shipOrder = mapper.selectById(id);
        if(shipOrder!=null){
            List<ErpShipOrder> erpShipOrders = mapper.selectList(new LambdaQueryWrapper<ErpShipOrder>().eq(ErpShipOrder::getErpOrderId, shipOrder.getErpOrderId()));
            return erpShipOrders;
        }
        return null;
    }

    @Transactional
    @Override
    public int supplierShip(ShipOrderSupplierShipBo bo) {
        // 判断数据完整性
        if(bo.getErpOrderId()==null||bo.getErpOrderId()==0) return -2;
        ErpOrder erpOrder = orderMapper.selectErpOrderById(bo.getErpOrderId());
        if(erpOrder==null)return -3;

        Float singleFee =  bo.getLogisticsFee()/bo.getItemList().size();
        for (int i=0;i<bo.getItemList().size();i++) {
            ShipOrderSupplierShipItemBo item = bo.getItemList().get(i);
            if (item.getId() == null || item.getId() == 0) return -1;
            ErpShipOrder shipOrder = mapper.selectById(item.getId());
            if (shipOrder == null) return -1001;
            if (shipOrder.getShipType() != 1) return -1002;
            if (shipOrder.getShipStatus() != 1) return -1003;

            // 更新自己 更新erp_ship_order
            ErpShipOrder update = new ErpShipOrder();
            update.setId(item.getId().toString());
            update.setShipStatus(3);
            update.setUpdateBy("发货");
            update.setUpdateTime(new Date());
            update.setShipTime(bo.getShipTime());
            update.setLogisticsCompany(bo.getLogisticsCompany());
            update.setLogisticsCode(bo.getLogisticsCode());
            update.setLogisticsFee(i == bo.getItemList().size() - 1 ? (bo.getLogisticsFee() - singleFee * (bo.getItemList().size() - 1)) : singleFee);
            mapper.updateById(update);

            // 更新erp_sale_order_item
            ErpOrderItem orderItem = new ErpOrderItem();
            orderItem.setShipType(1);
            orderItem.setShipStatus(3);
            orderItem.setShipTime(bo.getShipTime());
            orderItem.setLogisticsCompany(bo.getLogisticsCompany());
            orderItem.setLogisticsCode(bo.getLogisticsCode());
            orderItemMapper.updateById(orderItem);

            // 记录供应商代发账单 erp_ship_order_agent_fee
            ErpShipOrderAgentFee agentFee = new ErpShipOrderAgentFee();
            agentFee.setOrderNum(erpOrder.getOrderNum());
            agentFee.setShopId(erpOrder.getShopId());
            agentFee.setSupplierId(shipOrder.getSupplierId());
            try {
                agentFee.setDate(StringUtils.hasText(bo.getShipTime()) ? DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", bo.getShipTime()) : new Date());
            }catch (Exception e){
                agentFee.setDate(new Date());
            }
            agentFee.setLogisticsCompany(bo.getLogisticsCompany());
            agentFee.setLogisticsCode(bo.getLogisticsCode());
            try {
                agentFee.setShipAmount(i == bo.getItemList().size() - 1 ? (bo.getLogisticsFee() - singleFee * (bo.getItemList().size() - 1)) : singleFee);
            }catch (Exception e){
                agentFee.setShipAmount(0.0F);
            }
            try {
                agentFee.setGoodsAmount(item.getPurAmount());
            }catch (Exception e){
                agentFee.setGoodsAmount(0.0F);
            }
            agentFee.setTotalAmount(agentFee.getGoodsAmount()+agentFee.getShipAmount());
            agentFee.setStatus(0);
            agentFee.setCreateBy("发货生成账单");
            agentFee.setCreateTime(new Date());
            agentFeeMapper.insert(agentFee);
        }

        // 更新订单状态 erp_sale_order
        ErpOrder orderUpdate = new ErpOrder();
        orderUpdate.setId(erpOrder.getId());
        orderUpdate.setShipType(1);
        orderUpdate.setShipStatus(3);
        try {
            orderUpdate.setShippingTime(StringUtils.hasText(bo.getShipTime())? DateUtils.dateTime("yyyy-MM-dd HH:mm:ss",bo.getShipTime()):new Date());
        }catch (Exception e){}
        orderUpdate.setShippingCompany(bo.getLogisticsCompany());
        orderUpdate.setShippingNumber(bo.getLogisticsCode());
        orderUpdate.setShippingCost(bo.getLogisticsFee());
        orderMapper.updateErpOrder(orderUpdate);

        return 0;
    }
}




