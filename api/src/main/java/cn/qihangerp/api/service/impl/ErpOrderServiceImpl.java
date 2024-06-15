package cn.qihangerp.api.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cn.qihangerp.api.mapper.ErpShipOrderFeeMapper;
import cn.qihangerp.api.mapper.ErpShipOrderMapper;
import cn.qihangerp.api.service.SShopService;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.utils.DateUtils;
import cn.qihangerp.domain.ErpSaleOrder;
import cn.qihangerp.domain.ErpSaleOrderItem;
import cn.qihangerp.domain.Shop;
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
    @Autowired
    private SShopService shopService;

    /**
     * 查询订单
     * 
     * @param id 订单主键
     * @return 订单
     */
    @Override
    public ErpSaleOrder selectErpOrderById(Long id)
    {
        return erpOrderMapper.selectErpOrderById(id);
    }

    /**
     * 查询订单列表
     * 
     * @param erpSaleOrder 订单
     * @return 订单
     */
    @Override
    public List<ErpSaleOrder> selectErpOrderList(ErpSaleOrder erpSaleOrder)
    {
        List<ErpSaleOrder> orderList = erpOrderMapper.selectErpOrderList(erpSaleOrder);
        for (var o:orderList) {
            List<ErpSaleOrderItem> items = erpOrderMapper.selectOrderItemByOrderId(o.getId());
            o.setItemList(items);
        }
        return orderList;
    }

    /**
     * 新增订单
     * 
     * @param erpSaleOrder 订单
     * @return 结果
     */
    @Transactional
    @Override
    public int insertErpOrder(ErpSaleOrder erpSaleOrder)
    {
        ErpSaleOrder order = erpOrderMapper.selectErpOrderByNum(erpSaleOrder.getOrderNum());
        if (order!=null&& order.getId()>0) return -1;// 订单号已存在
//        erpOrder.setCreateTime(DateUtils.getNowDate());
//        int rows = erpOrderMapper.insertErpOrder(erpOrder);
//        insertErpOrderItem(erpOrder);
//        return rows;
        if(erpSaleOrder.getItemList() == null || erpSaleOrder.getItemList().size() == 0) return -2;
        else{
            // 循环查找是否缺少specId
            for (ErpSaleOrderItem erpSaleOrderItem : erpSaleOrder.getItemList())
            {
                if(erpSaleOrderItem.getSpecId()==null || erpSaleOrderItem.getSpecId()<=0) return -3;
            }
        }

        Shop shop = shopService.getById(erpSaleOrder.getShopId());
        if(shop!=null){
            erpSaleOrder.setShopType(shop.getPlatform());
        }else return -4;

//        if(erpOrder.getShopId() == 1) erpOrder.setShopType(99);
//        else if(erpOrder.getShopId() == 5) erpOrder.setShopType(5);
//        else if(erpOrder.getShopId() == 6) erpOrder.setShopType(4);
//        else if(erpOrder.getShopId() == 13) erpOrder.setShopType(13);
//        else if(erpOrder.getShopId() == 21) erpOrder.setShopType(7);
//        else if(erpOrder.getShopId() == 22) erpOrder.setShopType(6);
        erpSaleOrder.setCreateTime(new Date());
        erpSaleOrder.setShipStatus(0);
        erpSaleOrder.setOrderStatus(1);
        erpSaleOrder.setRefundStatus(1);
//        erpOrder.setOrderTime(DateUtils.getTime());
        erpSaleOrder.setOrderTime(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", erpSaleOrder.getCreateTime()));

        erpSaleOrder.setGoodsAmount(erpSaleOrder.getGoodsAmount());
        if(erpSaleOrder.getPostage() == null) erpSaleOrder.setPostage(0.0);
        erpSaleOrder.setSellerDiscount(0.0);
        erpSaleOrder.setPlatformDiscount(0.0);
//        if(erpSaleOrder.getDiscountAmount() == null) erpSaleOrder.setDiscountAmount(BigDecimal.ZERO);

        // 实际金额 = 商品金额 - 折扣金额 + 运费
        erpSaleOrder.setOrderAmount(erpSaleOrder.getGoodsAmount()+erpSaleOrder.getPostage());

        if(erpSaleOrder.getPayAmount() == null)erpSaleOrder.setPayAmount(0.0);
//        if(erpOrder.getAuditStatus() == null) shopOrder.setAuditStatus(1L);

        erpSaleOrder.setCreateBy(erpSaleOrder.getCreateBy());
        erpSaleOrder.setCreateTime(DateUtils.getNowDate());
        int rows = erpOrderMapper.insertErpOrder(erpSaleOrder);
        insertErpOrderItem(erpSaleOrder);
//        insertSShopOrderItem(shopOrder);
        return rows;
    }



    /**
     * 修改订单
     * 
     * @param erpSaleOrder 订单
     * @return 结果
     */
    @Transactional
    @Override
    public int updateErpOrder(ErpSaleOrder erpSaleOrder)
    {
        erpSaleOrder.setUpdateTime(DateUtils.getNowDate());
        erpOrderMapper.deleteErpOrderItemByOrderId(erpSaleOrder.getId());
        insertErpOrderItem(erpSaleOrder);
        return erpOrderMapper.updateErpOrder(erpSaleOrder);
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
     * @param erpSaleOrder 订单对象
     */
    public void insertErpOrderItem(ErpSaleOrder erpSaleOrder)
    {
        List<ErpSaleOrderItem> erpSaleOrderItemList = erpSaleOrder.getItemList();
        Long id = erpSaleOrder.getId();
        if (StringUtils.isNotNull(erpSaleOrderItemList))
        {
            List<ErpSaleOrderItem> list = new ArrayList<ErpSaleOrderItem>();
            for (int i = 0; i < erpSaleOrderItemList.size(); i++) {
                ErpSaleOrderItem erpSaleOrderItem = erpSaleOrderItemList.get(i);
                erpSaleOrderItem.setOriginalOrderId(erpSaleOrder.getOrderNum());
                if(erpSaleOrderItemList.size()==1) {
                    erpSaleOrderItem.setOriginalOrderItemId(erpSaleOrder.getOrderNum());
                }else{
                    erpSaleOrderItem.setOriginalOrderItemId(erpSaleOrder.getOrderNum()+i);
                }
                erpSaleOrderItem.setOriginalSkuId("");
                erpSaleOrderItem.setOrderStatus(erpSaleOrder.getOrderStatus());
                erpSaleOrderItem.setShopId(erpSaleOrder.getShopId());
                erpSaleOrderItem.setOrderId(id);
                erpSaleOrderItem.setRefundCount(0);
                erpSaleOrderItem.setRefundStatus(1);
                erpSaleOrderItem.setShipStatus(0);
                erpSaleOrderItem.setCreateBy(erpSaleOrder.getCreateBy());
                erpSaleOrderItem.setCreateTime(new Date());
                list.add(erpSaleOrderItem);
            }
            for (ErpSaleOrderItem erpSaleOrderItem : erpSaleOrderItemList)
            {

            }
            if (list.size() > 0)
            {
                erpOrderMapper.batchErpOrderItem(list);
            }
        }
    }

    @Transactional
    @Override
    public ResultVo<Integer> saveOrderMessage(ErpSaleOrder order) {
        System.out.println("Tao订单消息处理"+order.getOrderNum());
        if(order == null) {
            // 没有找到订单信息
            return ResultVo.error(ResultVoEnum.ParamsError,"参数数据不能为空：");
        }


        ErpSaleOrder erpSaleOrder = erpOrderMapper.selectErpOrderByNum(order.getOrderNum());
        if(erpSaleOrder == null) {
            // 新增订单
            order.setCreateBy("手动确认订单");
            order.setCreateTime(DateUtils.getNowDate());
            int rows = erpOrderMapper.insertErpOrder(order);
//            insertErpOrderItem(erpOrder);
//            List<ErpOrderItem> list = new ArrayList<ErpOrderItem>();
            //插入orderItem
            for (ErpSaleOrderItem erpSaleOrderItem : order.getItemList())
            {
                erpSaleOrderItem.setOrderId(order.getId());
                erpSaleOrderItem.setShopId(order.getShopId());
                erpSaleOrderItem.setCreateBy(order.getCreateBy());
                erpSaleOrderItem.setCreateTime(new Date());
//                list.add(erpOrderItem);
            }
            if (order.getItemList().size() > 0)
            {
                erpOrderMapper.batchErpOrderItem(order.getItemList());
            }
        }else {
            // 修改订单 (修改：)
            ErpSaleOrder update = new ErpSaleOrder();
            update.setId(erpSaleOrder.getId());
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
            erpOrderMapper.deleteErpOrderItemByOrderId(erpSaleOrder.getId());
            // 插入orderItem
//            addOrderItem(update.getId(), taoOrder.getTid());
            for (ErpSaleOrderItem erpSaleOrderItem : order.getItemList())
            {
                erpSaleOrderItem.setOrderId(erpSaleOrder.getId());
                erpSaleOrderItem.setShopId(erpSaleOrder.getShopId());
                erpSaleOrderItem.setCreateBy("手动确认");
                erpSaleOrderItem.setCreateTime(new Date());
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
