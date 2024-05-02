package cn.qihangerp.api.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cn.qihangerp.api.domain.*;
import cn.qihangerp.api.mapper.ErpShipOrderFeeMapper;
import cn.qihangerp.api.mapper.ErpShipOrderMapper;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.utils.DateUtils;
import cn.qihangerp.domain.ErpOrder;
import cn.qihangerp.domain.ErpOrderItem;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import cn.qihangerp.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import cn.qihangerp.api.mapper.ErpOrderMapper;
import cn.qihangerp.api.service.IErpOrderService;

/**
 * 订单Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-04
 */
@Service
public class ErpOrderServiceImpl implements IErpOrderService 
{
    @Autowired
    private ErpOrderMapper erpOrderMapper;
//    @Autowired
//    private FmsReceivableOrderMapper fmsReceivableOrderMapper;
    @Autowired
    private ErpShipOrderFeeMapper shipOrderFeeMapper;
    @Autowired
    private ErpShipOrderMapper shipOrderMapper;

    /**
     * 查询订单
     * 
     * @param id 订单主键
     * @return 订单
     */
    @Override
    public ErpOrder selectErpOrderById(Long id)
    {
        return erpOrderMapper.selectErpOrderById(id);
    }

    /**
     * 查询订单列表
     * 
     * @param erpOrder 订单
     * @return 订单
     */
    @Override
    public List<ErpOrder> selectErpOrderList(ErpOrder erpOrder)
    {
        List<ErpOrder> orderList = erpOrderMapper.selectErpOrderList(erpOrder);
        for (var o:orderList) {
            List<ErpOrderItem> items = erpOrderMapper.selectOrderItemByOrderId(o.getId());
            o.setItemList(items);
        }
        return orderList;
    }

    /**
     * 新增订单
     * 
     * @param erpOrder 订单
     * @return 结果
     */
    @Transactional
    @Override
    public int insertErpOrder(ErpOrder erpOrder)
    {
        ErpOrder order = erpOrderMapper.selectErpOrderByNum(erpOrder.getOrderNum());
        if (order!=null&& order.getId()>0) return -1;// 订单号已存在
//        erpOrder.setCreateTime(DateUtils.getNowDate());
//        int rows = erpOrderMapper.insertErpOrder(erpOrder);
//        insertErpOrderItem(erpOrder);
//        return rows;
        if(erpOrder.getItemList() == null || erpOrder.getItemList().size() == 0) return -1;

        if(erpOrder.getShopId() == 1) erpOrder.setShopType(99);
        else if(erpOrder.getShopId() == 5) erpOrder.setShopType(5);
        else if(erpOrder.getShopId() == 6) erpOrder.setShopType(4);
        else if(erpOrder.getShopId() == 13) erpOrder.setShopType(13);
        else if(erpOrder.getShopId() == 21) erpOrder.setShopType(7);
        else if(erpOrder.getShopId() == 22) erpOrder.setShopType(6);

        erpOrder.setOrderStatus(1);
        erpOrder.setRefundStatus(1);
        if(erpOrder.getPostage() == null) erpOrder.setPostage(BigDecimal.ZERO);
        if(erpOrder.getDiscountAmount() == null) erpOrder.setDiscountAmount(BigDecimal.ZERO);

        // 实际金额 = 商品金额 - 折扣金额 + 运费
        erpOrder.setAmount(BigDecimal.valueOf( erpOrder.getGoodsAmount()).subtract(erpOrder.getDiscountAmount()).add(erpOrder.getPostage()).doubleValue());

//        if(erpOrder.getPayAmount() == null)shopOrder.setPayAmount(0L);
//        if(erpOrder.getAuditStatus() == null) shopOrder.setAuditStatus(1L);

        erpOrder.setCreateBy(erpOrder.getCreateBy());
        erpOrder.setCreateTime(DateUtils.getNowDate());
        int rows = erpOrderMapper.insertErpOrder(erpOrder);
        insertErpOrderItem(erpOrder);
//        insertSShopOrderItem(shopOrder);
        return rows;
    }

    /**
     * 发货
     * @param erpOrder
     * @return
     */
    @Transactional
    @Override
    public int shipErpOrder(ErpOrder erpOrder) {
        ErpOrder order = erpOrderMapper.selectErpOrderById(erpOrder.getId());
        if (order==null) return -1;// 订单不存在
        else if (order.getShipStatus()!=null && order.getShipStatus()==3) {
            return -3;//发货状态不对
        }
        else if(order.getOrderStatus() != 1 && order.getOrderStatus() != 2) return -2;//状态不对
        // 更新订单表状态
        ErpOrder update = new ErpOrder();
        update.setId(order.getId());
        update.setUpdateTime(new Date());
        update.setUpdateBy(erpOrder.getUpdateBy());
        update.setShippingTime(new Date());
        update.setShippingMan(erpOrder.getShippingMan());
        update.setShippingCompany(erpOrder.getShippingCompany());
        update.setShippingNumber(erpOrder.getShippingNumber());
        update.setShippingCost(erpOrder.getShippingCost());
        update.setWidth(erpOrder.getWidth());
        update.setWeight(erpOrder.getWeight());
        update.setHeight(erpOrder.getHeight());
        update.setLength(erpOrder.getLength());
        update.setOrderStatus(3);
        erpOrderMapper.updateErpOrder(update);
         // 更新订单子表状态

        // 更新 wms_order_shipping
//        ErpShipOrder shipOrder = new ErpShipOrder();
//        shipOrder.setErpOrderId(order.getId());
//        List<ErpShipOrder> shipList = shipOrderMapper.selectWmsOrderShippingList(select);
//        if(shipList!=null){
//            for (WmsOrderShipping ship:shipList) {
//                WmsOrderShipping up = new WmsOrderShipping();
//                up.setId(ship.getId());
//                up.setStatus(3L);
//                up.setUpdateTime(new Date());
//                up.setUpdateBy(erpOrder.getUpdateBy());
//                wmsOrderShippingMapper.updateWmsOrderShipping(up);
//            }
//        }


        // 生成物流费用 fms_payable_ship_fee
        ErpShipOrderFee sf = new ErpShipOrderFee();
        sf.setDate(new Date());
        sf.setOrderNum(order.getOrderNum());
        sf.setShopId(order.getShopId());
        sf.setLogisticsCompany(erpOrder.getShippingCompany());
        sf.setLogisticsNum(erpOrder.getShippingNumber());
        sf.setAmount(erpOrder.getShippingCost());
        sf.setStatus(0);
        sf.setCreateTime(new Date());
        sf.setCreateBy(erpOrder.getUpdateBy());
        sf.setWidth(erpOrder.getWidth());
        sf.setWeight(erpOrder.getWeight());
        sf.setHeight(erpOrder.getHeight());
        sf.setLength(erpOrder.getLength());
        sf.setReceiverName(order.getReceiverName());
        sf.setReceiverPhone(order.getReceiverPhone());
        sf.setProvince(order.getProvince());
        sf.setCity(order.getCity());
        sf.setTown(order.getTown());

        shipOrderFeeMapper.insert(sf);
        return 1;
    }

    /**
     * 修改订单
     * 
     * @param erpOrder 订单
     * @return 结果
     */
    @Transactional
    @Override
    public int updateErpOrder(ErpOrder erpOrder)
    {
        erpOrder.setUpdateTime(DateUtils.getNowDate());
        erpOrderMapper.deleteErpOrderItemByOrderId(erpOrder.getId());
        insertErpOrderItem(erpOrder);
        return erpOrderMapper.updateErpOrder(erpOrder);
    }

    /**
     * 批量删除订单
     * 
     * @param ids 需要删除的订单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteErpOrderByIds(Long[] ids)
    {
        erpOrderMapper.deleteErpOrderItemByOrderIds(ids);
        return erpOrderMapper.deleteErpOrderByIds(ids);
    }

    /**
     * 删除订单信息
     * 
     * @param id 订单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteErpOrderById(Long id)
    {
        erpOrderMapper.deleteErpOrderItemByOrderId(id);
        return erpOrderMapper.deleteErpOrderById(id);
    }

    /**
     * 新增订单明细信息
     * 
     * @param erpOrder 订单对象
     */
    public void insertErpOrderItem(ErpOrder erpOrder)
    {
        List<ErpOrderItem> erpOrderItemList = erpOrder.getItemList();
        Long id = erpOrder.getId();
        if (StringUtils.isNotNull(erpOrderItemList))
        {
            List<ErpOrderItem> list = new ArrayList<ErpOrderItem>();
            for (ErpOrderItem erpOrderItem : erpOrderItemList)
            {


                erpOrderItem.setOrderId(id);
                erpOrderItem.setRefundCount(0);
                erpOrderItem.setRefundStatus(1);
                erpOrderItem.setCreateBy(erpOrder.getCreateBy());
                erpOrderItem.setCreateTime(new Date());
                list.add(erpOrderItem);
            }
            if (list.size() > 0)
            {
                erpOrderMapper.batchErpOrderItem(list);
            }
        }
    }

    @Transactional
    @Override
    public ResultVo<Integer> taoOrderMessage(ErpOrder order) {
        System.out.println("Tao订单消息处理"+order.getOrderNum());
        if(order == null) {
            // 没有找到订单信息
            return ResultVo.error(ResultVoEnum.ParamsError,"参数数据不能为空：");
        }


        ErpOrder erpOrder = erpOrderMapper.selectErpOrderByNum(order.getOrderNum());
        if(erpOrder == null) {
            // 新增订单
            order.setCreateBy("手动确认订单");
            order.setCreateTime(DateUtils.getNowDate());
            int rows = erpOrderMapper.insertErpOrder(order);
//            insertErpOrderItem(erpOrder);
//            List<ErpOrderItem> list = new ArrayList<ErpOrderItem>();
            //插入orderItem
            for (ErpOrderItem erpOrderItem : order.getItemList())
            {
                erpOrderItem.setOrderId(order.getId());
                erpOrderItem.setShopId(order.getShopId());
                erpOrderItem.setCreateBy(order.getCreateBy());
                erpOrderItem.setCreateTime(new Date());
//                list.add(erpOrderItem);
            }
            if (order.getItemList().size() > 0)
            {
                erpOrderMapper.batchErpOrderItem(order.getItemList());
            }
        }else {
            // 修改订单 (修改：)
            ErpOrder update = new ErpOrder();
            update.setId(erpOrder.getId());
            // 状态
//            int orderStatus = TaoOrderStateEnum.getIndex(taoOrder.getStatus());
//            if (orderStatus == 11) {
//                update.setRefundStatus(2);
//            } else if (orderStatus == -1) {
//                update.setRefundStatus(-1);
//            } else {
//                update.setRefundStatus(1);
//            }
            update.setReceiverName(order.getReceiverName());
            update.setReceiverPhone(order.getReceiverPhone());
            update.setAddress(order.getAddress());
            update.setOrderStatus(order.getOrderStatus());
            update.setRefundStatus(order.getRefundStatus());
            update.setUpdateTime(new Date());
            update.setUpdateBy("ORDER_MESSAGE");
//            orderMapper.updateById(update);
            erpOrderMapper.updateErpOrder(update);
            // 删除orderItem
//            orderItemMapper.delete(new LambdaQueryWrapper<OOrderItem>().eq(OOrderItem::getOrderId, update.getId()));
            erpOrderMapper.deleteErpOrderItemByOrderId(erpOrder.getId());
            // 插入orderItem
//            addOrderItem(update.getId(), taoOrder.getTid());
            for (ErpOrderItem erpOrderItem : order.getItemList())
            {
                erpOrderItem.setOrderId(erpOrder.getId());
                erpOrderItem.setShopId(erpOrder.getShopId());
                erpOrderItem.setCreateBy("手动确认");
                erpOrderItem.setCreateTime(new Date());
//                list.add(erpOrderItem);
            }
            if (order.getItemList().size() > 0)
            {
                erpOrderMapper.batchErpOrderItem(order.getItemList());
            }
        }
        return ResultVo.success();
    }
}
