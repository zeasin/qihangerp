package cn.qihangerp.api.service.impl;

import cn.qihangerp.api.domain.ErpShipOrderAgentFee;
import cn.qihangerp.api.domain.ErpShipOrderFee;
import cn.qihangerp.api.domain.bo.ShipOrderSupplierShipBo;
import cn.qihangerp.api.domain.bo.ShipOrderSupplierShipItemBo;
import cn.qihangerp.api.mapper.*;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.utils.DateUtils;
import cn.qihangerp.domain.ErpSaleOrder;
import cn.qihangerp.domain.ErpSaleOrderItem;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.api.domain.ErpShipOrder;
import cn.qihangerp.api.service.ErpShipOrderService;
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
    private final ErpShipOrderFeeMapper shipOrderFeeMapper;
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
        ErpSaleOrder erpSaleOrder = orderMapper.selectErpOrderById(bo.getErpOrderId());
        if(erpSaleOrder ==null)return -3;

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
            ErpSaleOrderItem orderItem = new ErpSaleOrderItem();
            orderItem.setShipType(1);
            orderItem.setShipStatus(3);
            orderItem.setShipTime(bo.getShipTime());
            orderItem.setLogisticsCompany(bo.getLogisticsCompany());
            orderItem.setLogisticsCode(bo.getLogisticsCode());
            orderItemMapper.updateById(orderItem);

            // 记录供应商代发账单 erp_ship_order_agent_fee
            ErpShipOrderAgentFee agentFee = new ErpShipOrderAgentFee();
            agentFee.setOrderNum(erpSaleOrder.getOrderNum());
            agentFee.setShopId(erpSaleOrder.getShopId());
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
        ErpSaleOrder orderUpdate = new ErpSaleOrder();
        orderUpdate.setId(erpSaleOrder.getId());
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


    @Transactional
    @Override
    public int wmsShip(ShipOrderSupplierShipBo bo) {
        // 判断数据完整性
        if(bo.getErpOrderId()==null||bo.getErpOrderId()==0) return -2;
        ErpSaleOrder erpSaleOrder = orderMapper.selectErpOrderById(bo.getErpOrderId());
        if(erpSaleOrder ==null)return -3;

        Float singleFee =  bo.getLogisticsFee()/bo.getItemList().size();
        for (int i=0;i<bo.getItemList().size();i++) {
            ShipOrderSupplierShipItemBo item = bo.getItemList().get(i);
            if (item.getId() == null || item.getId() == 0) return -1;
            ErpShipOrder shipOrder = mapper.selectById(item.getId());
            if (shipOrder == null) return -1001;
            if (shipOrder.getShipType() != 0) return -1002;
            if (shipOrder.getShipStatus() != 2) return -1003;

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
            ErpSaleOrderItem orderItem = new ErpSaleOrderItem();
            orderItem.setShipType(1);
            orderItem.setShipStatus(3);
            orderItem.setShipTime(bo.getShipTime());
            orderItem.setLogisticsCompany(bo.getLogisticsCompany());
            orderItem.setLogisticsCode(bo.getLogisticsCode());
            orderItemMapper.updateById(orderItem);

            // 生成物流费用 fms_payable_ship_fee
            ErpShipOrderFee sf = new ErpShipOrderFee();
            sf.setDate(new Date());
            sf.setOrderNum(shipOrder.getOrderNum());
            sf.setShopId(shipOrder.getShopId());
            sf.setLogisticsCompany(bo.getLogisticsCompany());
            sf.setLogisticsNum(bo.getLogisticsCode());
            sf.setAmount(bo.getLogisticsFee());
            sf.setStatus(0);
            sf.setCreateTime(new Date());
            sf.setCreateBy("发货");
//            sf.setWidth(erpOrder.getWidth());
//            sf.setWeight(erpOrder.getWeight());
//            sf.setHeight(erpOrder.getHeight());
//            sf.setLength(erpOrder.getLength());
            sf.setReceiverName(shipOrder.getReceiverName());
            sf.setReceiverPhone(shipOrder.getReceiverPhone());
            sf.setProvince(shipOrder.getProvince());
            sf.setCity(shipOrder.getCity());
            sf.setTown(shipOrder.getTown());

            shipOrderFeeMapper.insert(sf);

        }

        // 更新订单状态 erp_sale_order
        ErpSaleOrder orderUpdate = new ErpSaleOrder();
        orderUpdate.setId(erpSaleOrder.getId());
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


    /**
     * 发货
     * @param erpOrder
     * @return
     */
//    @Transactional
//    @Override
//    public int shipErpOrder(ErpOrder erpOrder) {
//        ErpOrder order = erpOrderMapper.selectErpOrderById(erpOrder.getId());
//        if (order==null) return -1;// 订单不存在
//        else if (order.getShipStatus()!=null && order.getShipStatus()==3) {
//            return -3;//发货状态不对
//        }
//        else if(order.getOrderStatus() != 1 && order.getOrderStatus() != 2) return -2;//状态不对
//        // 更新订单表状态
//        ErpOrder update = new ErpOrder();
//        update.setId(order.getId());
//        update.setUpdateTime(new Date());
//        update.setUpdateBy(erpOrder.getUpdateBy());
//        update.setShippingTime(new Date());
//        update.setShippingMan(erpOrder.getShippingMan());
//        update.setShippingCompany(erpOrder.getShippingCompany());
//        update.setShippingNumber(erpOrder.getShippingNumber());
//        update.setShippingCost(erpOrder.getShippingCost());
//        update.setWidth(erpOrder.getWidth());
//        update.setWeight(erpOrder.getWeight());
//        update.setHeight(erpOrder.getHeight());
//        update.setLength(erpOrder.getLength());
//        update.setOrderStatus(3);
//        erpOrderMapper.updateErpOrder(update);
//        // 更新订单子表状态
//
//        // 更新 wms_order_shipping
////        ErpShipOrder shipOrder = new ErpShipOrder();
////        shipOrder.setErpOrderId(order.getId());
////        List<ErpShipOrder> shipList = shipOrderMapper.selectWmsOrderShippingList(select);
////        if(shipList!=null){
////            for (WmsOrderShipping ship:shipList) {
////                WmsOrderShipping up = new WmsOrderShipping();
////                up.setId(ship.getId());
////                up.setStatus(3L);
////                up.setUpdateTime(new Date());
////                up.setUpdateBy(erpOrder.getUpdateBy());
////                wmsOrderShippingMapper.updateWmsOrderShipping(up);
////            }
////        }
//
//
//        // 生成物流费用 fms_payable_ship_fee
//        ErpShipOrderFee sf = new ErpShipOrderFee();
//        sf.setDate(new Date());
//        sf.setOrderNum(order.getOrderNum());
//        sf.setShopId(order.getShopId());
//        sf.setLogisticsCompany(erpOrder.getShippingCompany());
//        sf.setLogisticsNum(erpOrder.getShippingNumber());
//        sf.setAmount(erpOrder.getShippingCost());
//        sf.setStatus(0);
//        sf.setCreateTime(new Date());
//        sf.setCreateBy(erpOrder.getUpdateBy());
//        sf.setWidth(erpOrder.getWidth());
//        sf.setWeight(erpOrder.getWeight());
//        sf.setHeight(erpOrder.getHeight());
//        sf.setLength(erpOrder.getLength());
//        sf.setReceiverName(order.getReceiverName());
//        sf.setReceiverPhone(order.getReceiverPhone());
//        sf.setProvince(order.getProvince());
//        sf.setCity(order.getCity());
//        sf.setTown(order.getTown());
//
//        shipOrderFeeMapper.insert(sf);
//        return 1;
//    }
}




