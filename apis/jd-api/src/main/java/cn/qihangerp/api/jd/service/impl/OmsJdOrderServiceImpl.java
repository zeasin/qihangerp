package cn.qihangerp.api.jd.service.impl;

import cn.qihangerp.api.jd.bo.JdOrderBo;
import cn.qihangerp.api.jd.bo.JdOrderConfirmBo;
import cn.qihangerp.api.jd.common.JdOrderStateEnum;
import cn.qihangerp.api.jd.domain.OmsJdGoodsSku;
import cn.qihangerp.api.jd.domain.OmsJdOrderItem;
import cn.qihangerp.api.jd.mapper.OmsJdGoodsSkuMapper;
import cn.qihangerp.api.jd.mapper.OmsJdOrderItemMapper;
import cn.qihangerp.common.*;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.domain.ErpSaleOrder;
import cn.qihangerp.domain.ErpSaleOrderItem;
import cn.qihangerp.mq.MQRequest;
import cn.qihangerp.mq.MQRequestType;
import cn.qihangerp.mq.client.MQClientService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.api.jd.domain.OmsJdOrder;
import cn.qihangerp.api.jd.service.OmsJdOrderService;
import cn.qihangerp.api.jd.mapper.OmsJdOrderMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author qilip
* @description 针对表【oms_jd_order(京东订单表)】的数据库操作Service实现
* @createDate 2024-05-03 12:21:08
*/
@AllArgsConstructor
@Service
public class OmsJdOrderServiceImpl extends ServiceImpl<OmsJdOrderMapper, OmsJdOrder>
    implements OmsJdOrderService{
    private final OmsJdOrderMapper mapper;
    private final OmsJdGoodsSkuMapper jdGoodsSkuMapper;
    private final OmsJdOrderItemMapper itemMapper;
    private final MQClientService mqClientService;
    @Override
    public PageResult<OmsJdOrder> queryPageList(JdOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OmsJdOrder> queryWrapper = new LambdaQueryWrapper<OmsJdOrder>()
                .eq(bo.getShopId()!=null,OmsJdOrder::getShopId,bo.getShopId())
                .eq(StringUtils.hasText(bo.getOrderId()),OmsJdOrder::getOrderId,bo.getOrderId())
                .eq(StringUtils.hasText(bo.getOrderState()),OmsJdOrder::getOrderState,bo.getOrderState())
                ;

        Page<OmsJdOrder> page = mapper.selectPage(pageQuery.build(), queryWrapper);
        if(page.getRecords()!=null){
            for (var order:page.getRecords()) {
                order.setItemList(itemMapper.selectList(new LambdaQueryWrapper<OmsJdOrderItem>().eq(OmsJdOrderItem::getOrderId,order.getOrderId())));
            }
        }
        return PageResult.build(page);
    }

    @Transactional
    @Override
    public ResultVo<Integer> saveOrder(Long shopId, OmsJdOrder order) {
        try {
            List<OmsJdOrder> jdOrders = mapper.selectList(new LambdaQueryWrapper<OmsJdOrder>().eq(OmsJdOrder::getOrderId, order.getOrderId()));
            if (jdOrders != null && jdOrders.size() > 0) {
                // 存在，修改
                OmsJdOrder update = new OmsJdOrder();
                update.setId(jdOrders.get(0).getId());
                update.setOrderState(order.getOrderState());
                update.setOrderStateRemark(order.getOrderStateRemark());
                update.setInvoiceCode(order.getInvoiceCode());
                update.setOrderEndTime(order.getOrderEndTime());
                update.setModified(order.getModified());
                update.setVenderRemark(order.getVenderRemark());
                update.setReturnOrder(order.getReturnOrder());
                update.setPaymentConfirmTime(order.getPaymentConfirmTime());
                update.setWaybill(order.getWaybill());
                update.setLogisticsId(order.getLogisticsId());

                //临时
                update.setFullname(order.getFullname());
                update.setFullAddress(order.getFullAddress());
                update.setProvince(order.getProvince());
                update.setCity(order.getCity());
                update.setTown(order.getTown());
                update.setMobile(order.getMobile());
                update.setTelephone(order.getTelephone());

                mapper.updateById(update);
                // 更新item
                for (var item : order.getItemList()) {
                    List<OmsJdOrderItem> taoOrderItems = itemMapper.selectList(new LambdaQueryWrapper<OmsJdOrderItem>().eq(OmsJdOrderItem::getSkuId, item.getSkuId()));
                    if (taoOrderItems != null && taoOrderItems.size() > 0) {
                        // 不处理
                    } else {
                        // 新增
                        item.setOrderId(jdOrders.get(0).getOrderId());
                        itemMapper.insert(item);
                    }
                }
                return ResultVo.error(ResultVoEnum.DataExist, "订单已经存在，更新成功");
            } else {
                // 不存在，新增
                order.setAuditStatus(0);
                order.setShopId(shopId);
                order.setCreateTime(new Date());
                mapper.insert(order);
                // 添加item
                for (var item : order.getItemList()) {
                    item.setOrderId(order.getOrderId());
                    itemMapper.insert(item);
                }

                // 添加优惠信息
//                if(order.getCoupons()!= null){
//                    for (var coupon:order.getCoupons()) {
//                        if(coupon.getOrderId()==null){
//                            coupon.setOrderId(Long.parseLong(order.getOrderId()));
//                        }
//                        couponMapper.insert(coupon);
//                    }
//                }
                return ResultVo.success();
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return ResultVo.error(ResultVoEnum.SystemException, "系统异常：" + e.getMessage());
        }
    }

    @Override
    public OmsJdOrder queryDetailById(Long id) {
        OmsJdOrder orderDetail = mapper.selectById(id);
        orderDetail.setItemList(itemMapper.selectList(new LambdaQueryWrapper<OmsJdOrderItem>().eq(OmsJdOrderItem::getOrderId,orderDetail.getOrderId())));
        return orderDetail;
    }


    /**
     * 确认订单
     * @param bo
     * @return
     */
    @Transactional
    @Override
    public int confirmOrder(JdOrderConfirmBo bo) throws InterruptedException {
//        if(cn.qihangerp.common.utils.StringUtils.isNull(taoOrder.getShipType())){
//            return -3;
//        }
//        if(taoOrder.getShipType() != 0 && taoOrder.getShipType() != 1){
//            // 1 供应商发货 0 仓库发货
//            return -4;
//        }

//        TaoOrder original = taoOrderMapper.selectTaoOrderById(taoOrder.getId());
        OmsJdOrder original = mapper.selectById(bo.getId());;
        if(original.getAuditStatus()!=null &&  original.getAuditStatus() != 0) return -1;//无需审核

//        ErpOrder erpOrder = erpOrderMapper.selectErpOrderByNum(taoOrder.getId());
//        if(erpOrder!=null) return -2;

        // 新增ErpOrder
        // 确认订单（操作：插入数据到s_shop_order、s_shop_order_item）
        ErpSaleOrder insert = new ErpSaleOrder();
        insert.setOrderNum(original.getOrderId());
        insert.setOrderTime(original.getOrderStartTime());
        insert.setShopId(original.getShopId());
        insert.setShopType(EnumShopType.JD.getIndex());
//        insert.setRemark(original.getVenderRemark());
        insert.setBuyerMemo(original.getOrderRemark());
        insert.setSellerMemo(original.getVenderRemark());
//        so.setTag(original.getTag());
        // 状态
        int orderStatus = JdOrderStateEnum.getIndex(original.getOrderState());
        if (orderStatus == 11) {
            insert.setRefundStatus(4);//已取消的订单
        } else if (orderStatus == -1) {
            insert.setRefundStatus(-1);
        } else {
            insert.setRefundStatus(1);
        }
        insert.setOrderStatus(orderStatus);
//        insert.setGoodsAmount(StringUtils.isEmpty(original.getOrderTotalPrice()) ? 0.0 : Double.parseDouble(original.getOrderTotalPrice()));
//        double orderSellerPrice = StringUtils.isEmpty(original.getOrderSellerPrice()) ? 0.0 : Double.parseDouble(original.getOrderSellerPrice());
//        double freightPrice = StringUtils.isEmpty(original.getFreightPrice()) ? 0.0 : Double.parseDouble(original.getFreightPrice());
//        double sellerDiscount = StringUtils.isEmpty(original.getSellerDiscount())?0.0:Double.parseDouble(original.getSellerDiscount());
//
//        insert.setDiscountAmount(BigDecimal.valueOf(sellerDiscount));
//        insert.setAmount(orderSellerPrice + freightPrice);
        insert.setGoodsAmount(StringUtils.isEmpty(original.getOrderTotalPrice()) ? 0.0 : Double.parseDouble(original.getOrderTotalPrice()));
        double orderSellerPrice = StringUtils.isEmpty(original.getOrderSellerPrice()) ? 0.0 : Double.parseDouble(original.getOrderSellerPrice());
        double freightPrice = StringUtils.isEmpty(original.getFreightPrice()) ? 0.0 : Double.parseDouble(original.getFreightPrice());
        insert.setOrderAmount(orderSellerPrice + freightPrice);
        insert.setPostage(freightPrice);
        insert.setPlatformDiscount(0.0);
        try {
            insert.setSellerDiscount(org.springframework.util.StringUtils.hasText(original.getSellerDiscount())?Double.parseDouble(original.getSellerDiscount()):0.0);
        }catch (Exception e){
            insert.setSellerDiscount(0.0);
        }

        insert.setReceiverName(bo.getFullname());
        insert.setReceiverPhone(bo.getMobile());
        insert.setAddress(bo.getFullAddress());
        insert.setProvince(original.getProvince());
        insert.setCity(original.getCity());
        insert.setTown(original.getCounty());
        insert.setShipStatus(0);
        insert.setCreateTime(new Date());
        insert.setCreateBy("ORDER_MESSAGE");

        List<OmsJdOrderItem> jdOrderItems = itemMapper.selectList(new LambdaQueryWrapper<OmsJdOrderItem>().eq(OmsJdOrderItem::getOrderId, original.getOrderId()));
        if(jdOrderItems!=null && jdOrderItems.size()>0) {
            List<ErpSaleOrderItem> items = new ArrayList<>();
            for (var item : jdOrderItems) {
                ErpSaleOrderItem orderItem = new ErpSaleOrderItem();
                orderItem.setOrderId(insert.getId());
                orderItem.setOriginalOrderId(original.getOrderId());
                orderItem.setOriginalOrderItemId(item.getId().toString());
                orderItem.setOriginalSkuId(item.getSkuId());
                orderItem.setShipStatus(0);
                // TODO：这里将订单商品skuid转换成erp系统的skuid
                Long erpGoodsId = 0L;
                Long erpSkuId = 0L;

                List<OmsJdGoodsSku> jdGoodsSkus = jdGoodsSkuMapper.selectList(new LambdaQueryWrapper<OmsJdGoodsSku>().eq(OmsJdGoodsSku::getSkuId, item.getSkuId()));
                if (jdGoodsSkus != null && !jdGoodsSkus.isEmpty()) {
                    erpGoodsId = jdGoodsSkus.get(0).getErpGoodsId();
                    erpSkuId = jdGoodsSkus.get(0).getErpGoodsSkuId();
                    orderItem.setGoodsImg(jdGoodsSkus.get(0).getLogo());
                    orderItem.setGoodsSpec(jdGoodsSkus.get(0).getSkuName());
                    orderItem.setSpecNum(jdGoodsSkus.get(0).getOuterId());
                }
                orderItem.setGoodsId(erpGoodsId);
                orderItem.setSpecId(erpSkuId);
                orderItem.setGoodsTitle(item.getSkuName());
                orderItem.setGoodsPrice(StringUtils.isEmpty(item.getJdPrice())?0.0:Double.parseDouble(item.getJdPrice()));
                Integer quantity = StringUtils.isEmpty(item.getItemTotal())?0: Integer.parseInt(item.getItemTotal());
                orderItem.setItemAmount(orderItem.getGoodsPrice() *quantity);
                orderItem.setQuantity(quantity);
                if(orderStatus == 11){
                    orderItem.setRefundStatus(2);
                    orderItem.setRefundCount(quantity);
                }else if (orderStatus == -1) {

                }else{
                    orderItem.setRefundStatus(1);
                    orderItem.setRefundCount(0);
                }
                orderItem.setCreateTime(new Date());
                orderItem.setCreateBy("ORDER_MESSAGE");
                orderItem.setIsGift(0);
                items.add(orderItem);
            }
            insert.setItemList(items);
        }

        // 添加了赠品
//        if(taoOrder.getTaoOrderItemList()!=null && !taoOrder.getTaoOrderItemList().isEmpty()){
//            for (var g:taoOrder.getTaoOrderItemList()) {
//                if(StringUtils.isEmpty(g.getSpecNumber())) {
//                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                    return -11;
//                }
//                GoodsSpec spec = goodsSpecMapper.selectGoodsSpecBySpecNum(g.getSpecNumber());
//                if (spec == null) {
//                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                    return -11;
//                }
//                Goods goods = goodsMapper.selectGoodsById(spec.getGoodsId());
//                if(goods == null) {
//                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                    return -12;
//                }
//
//                ErpOrderItem item = new ErpOrderItem();
//                item.setShipStatus(0);
//                item.setShipType(taoOrder.getShipType());
//                item.setShopId(original.getShopId().intValue());
//                item.setOrderId(so.getId());
//                item.setOrderNum(original.getId());
//                item.setOrderItemNum(original.getId()+"_");
//                item.setSupplierId(goods.getSupplierId().intValue());
//                item.setGoodsId(spec.getGoodsId());
//                item.setSpecId(spec.getId());
//                item.setGoodsTitle(g.getGoodsTitle());
//                item.setGoodsImg(g.getProductImgUrl());
//                item.setGoodsNum(g.getGoodsNumber());
//                item.setSpecNum(g.getSpecNumber());
//                item.setGoodsSpec(g.getSkuInfo());
//                item.setGoodsPrice(g.getPrice().doubleValue());
////                item.setGoodsPurPrice(spec.getPurPrice());
//                item.setItemAmount(g.getItemAmount().doubleValue());
//                item.setQuantity(g.getQuantity());
//                item.setIsGift(1);
//                item.setRefundCount(0);
//                item.setRefundStatus(1);
//                item.setCreateBy(taoOrder.getUpdateBy());
//                item.setCreateTime(new Date());
//                items.add(item);
//            }
//        }
//        erpOrderMapper.batchErpOrderItem(items);

        // 远程调用
        MQRequest<ErpSaleOrder> req = new MQRequest<>();
        req.setMqRequestType(MQRequestType.ORDER_CONFIRM);
        req.setData(insert);
        ApiResult s = mqClientService.confirmOrder(req);
        if(s.getResult()==ApiResultEnum.SUCCESS.getIndex()) {
            //更新自己
            OmsJdOrder update = new OmsJdOrder();
            update.setId(original.getId());
            update.setAuditStatus(1);
            update.setAuditTime(new Date());
//            update.setUpdateBy(bo.getUpdateBy());
//            update.setUpdateTime(new Date());
            update.setFullAddress(bo.getFullAddress());
            update.setMobile(bo.getMobile());
            update.setFullname(bo.getFullname());
            mapper.updateById(update);
        }
        return 1;
    }
}