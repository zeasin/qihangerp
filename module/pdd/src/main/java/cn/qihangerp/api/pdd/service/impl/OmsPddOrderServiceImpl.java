package cn.qihangerp.api.pdd.service.impl;

import cn.qihangerp.api.pdd.domain.OmsPddGoods;
import cn.qihangerp.api.pdd.domain.OmsPddOrderItem;
import cn.qihangerp.api.pdd.mapper.OmsPddOrderItemMapper;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.common.ResultVoEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.api.pdd.domain.OmsPddOrder;
import cn.qihangerp.api.pdd.service.OmsPddOrderService;
import cn.qihangerp.api.pdd.mapper.OmsPddOrderMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
* @author TW
* @description 针对表【oms_pdd_order(拼多多订单表)】的数据库操作Service实现
* @createDate 2024-05-28 15:27:42
*/
@AllArgsConstructor
@Service
public class OmsPddOrderServiceImpl extends ServiceImpl<OmsPddOrderMapper, OmsPddOrder>
    implements OmsPddOrderService{
    private final OmsPddOrderMapper orderMapper;
    private final OmsPddOrderItemMapper orderItemMapper;

    @Override
    public PageResult<OmsPddOrder> queryPageList(OmsPddOrder bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OmsPddOrder> queryWrapper = new LambdaQueryWrapper<OmsPddOrder>()
                .eq(bo.getShopId()!=null,OmsPddOrder::getShopId,bo.getShopId());

        Page<OmsPddOrder> goodsPage = orderMapper.selectPage(pageQuery.build(), queryWrapper);
        if(goodsPage.getRecords()!=null){
            for (var o: goodsPage.getRecords()) {
                o.setItemList(orderItemMapper.selectList(new LambdaQueryWrapper<OmsPddOrderItem>().eq(OmsPddOrderItem::getOrderSn,o.getOrderSn())));
            }
        }
        return PageResult.build(goodsPage);
    }

    @Transactional
    @Override
    public ResultVo<Integer> saveOrder(Long shopId, OmsPddOrder order) {
        if(order == null ) return ResultVo.error(ResultVoEnum.SystemException);
        try {
            List<OmsPddOrder> taoOrders = orderMapper.selectList(new LambdaQueryWrapper<OmsPddOrder>().eq(OmsPddOrder::getOrderSn, order.getOrderSn()));
            if (taoOrders != null && taoOrders.size() > 0) {
                // 存在，修改
                OmsPddOrder update = new OmsPddOrder();
                update.setId(taoOrders.get(0).getId());
                if(StringUtils.hasText(order.getReceiverName())){
                    update.setReceiverName(order.getReceiverName());
                }
                if(StringUtils.hasText(order.getReceiverPhone())){
                    update.setReceiverPhone(order.getReceiverPhone());
                }
                if(StringUtils.hasText(order.getReceiverAddress())){
                    update.setReceiverAddress(order.getReceiverAddress());
                }
                if(StringUtils.hasText(order.getAddress())){
                    update.setAddress(order.getAddress());
                }
                update.setReceiverNameMask(order.getReceiverNameMask());
                update.setReceiverPhoneMask(order.getReceiverPhoneMask());
                update.setReceiverAddressMask(order.getReceiverAddressMask());
                update.setAddressMask(order.getAddressMask());

                update.setGroupStatus(order.getGroupStatus());
                update.setConfirmStatus(order.getConfirmStatus());
                update.setOrderStatus(order.getOrderStatus());
                update.setRefundStatus(order.getRefundStatus());
                update.setCapitalFreeDiscount(order.getCapitalFreeDiscount());
                update.setSellerDiscount(order.getSellerDiscount());
                update.setPlatformDiscount(order.getPlatformDiscount());
                update.setGoodsAmount(order.getGoodsAmount());
                update.setDiscountAmount(order.getDiscountAmount());
                update.setPayAmount(order.getPayAmount());
                update.setPostage(order.getPostage());
                update.setOrderChangeAmount(order.getOrderChangeAmount());
                update.setRemark(order.getRemark());
                update.setRemarkTag(order.getRemarkTag());
                update.setRemarkTagName(order.getRemarkTagName());
                update.setBuyerMemo(order.getBuyerMemo());
                update.setUpdatedAt(order.getUpdatedAt());
                update.setShippingTime(order.getShippingTime());
                update.setTrackingNumber(order.getTrackingNumber());
                update.setPayType(order.getPayType());
                update.setPayNo(order.getPayNo());
                update.setPayTime(order.getPayTime());
                update.setConfirmTime(order.getConfirmTime());
                update.setReceiveTime(order.getReceiveTime());
                update.setAfterSalesStatus(order.getAfterSalesStatus());

                update.setLastShipTime(order.getLastShipTime());
                update.setLogisticsId(order.getLogisticsId());
                update.setRiskControlStatus(order.getRiskControlStatus());
                update.setUrgeShippingTime(order.getUrgeShippingTime());
                update.setUpdateTime(new Date());
                orderMapper.updateById(update);
                // 更新item
                for (var item : order.getItemList()) {
                    List<OmsPddOrderItem> taoOrderItems = orderItemMapper.selectList(new LambdaQueryWrapper<OmsPddOrderItem>().eq(OmsPddOrderItem::getOrderSn, order.getOrderSn()).eq(OmsPddOrderItem::getSkuId,item.getSkuId()));
                    if (taoOrderItems != null && taoOrderItems.size() > 0) {
                        // 存在，不操作
                    } else {
                        // 新增
                        // TODO:子订单价格计算
                        item.setIsGift(0);
                        item.setRefundCount(0);
                        item.setRefundStatus(1);
                        item.setShopId(shopId);
                        item.setOrderSn(order.getOrderSn());
                        item.setOrderId(update.getId());
                        item.setCreateTime(new Date());
                        orderItemMapper.insert(item);
                    }
                }
                return ResultVo.error(ResultVoEnum.DataExist, "订单已经存在，更新成功");
            } else {
                // 不存在，新增
                order.setShopId(shopId);
                order.setAuditStatus(0);
                order.setCreateTime(new Date());
                orderMapper.insert(order);
                // 添加item
                for (var item : order.getItemList()) {
                    // 新增
                    // TODO:子订单价格计算
                    item.setIsGift(0);
                    item.setRefundCount(0);
                    item.setRefundStatus(1);
                    item.setShopId(shopId);
                    item.setOrderSn(order.getOrderSn());
                    item.setOrderId(order.getId());
                    item.setCreateTime(new Date());
                    orderItemMapper.insert(item);
                }
                return ResultVo.success();
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            System.out.println("保存订单数据错误："+e.getMessage());
            return ResultVo.error(ResultVoEnum.SystemException, "系统异常：" + e.getMessage());
        }

    }

    /**
     * 确认订单
     * @param pddOrder
     * @return
     */
    @Transactional
    @Override
    public int confirmOrder(OmsPddOrder pddOrder) {
        OmsPddOrder original = orderMapper.selectById(pddOrder.getId());
        if(original == null) return -1;
        else if(original.getAuditStatus() != 0) return -2;
        else if(original.getRefundStatus() != 1) return -3;
//        if(pddOrder.getShipType() != 0 && pddOrder.getShipType() != 1){
//            // 1 供应商发货 0 仓库发货
//            return -5;
//        }
//        // 判断是否存在
//        ErpOrder erpo = erpOrderMapper.selectErpOrderByNum(original.getOrderSn());
//        if(erpo !=null ) return -4;
//
//        // 确认订单（操作：插入数据到s_shop_order、s_shop_order_item）
//        ErpOrder so = new ErpOrder();
//        so.setOrderNum(original.getOrderSn());
//        so.setShopId(original.getShopId().intValue());
//        so.setShopType(EnumShopType.Pdd.getIndex());
//        so.setShipType(pddOrder.getShipType());
//        so.setRemark(original.getRemark());
//        so.setBuyerMemo(original.getBuyerMemo());
//        so.setTag(original.getTag());
//        so.setRefundStatus(1);
//        so.setOrderStatus(1);
//        so.setShipStatus(0);
//        so.setGoodsAmount(original.getGoodsAmount());
//        so.setDiscountAmount(BigDecimal.valueOf(original.getDiscountAmount()));
//        so.setAmount(original.getPayAmount());
//        so.setPostage(BigDecimal.valueOf(original.getPostage()));
//        try {
//            //2022-07-17 17:10:57
//            Date payDate = DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", original.getPayTime());
//            so.setPayTime(payDate);
//        }catch (Exception e){}
//
//        so.setReceiverName(original.getReceiverName1());
//        so.setReceiverPhone(original.getReceiverPhone1());
//        so.setAddress(original.getReceiverAddress1());
//        so.setCountry("中国");
//        so.setProvince(original.getProvince());
//        so.setCity(original.getCity());
//        so.setTown(original.getTown());
//        so.setConfirmTime(new Date());
//        so.setCreateTime(new Date());
//        so.setCreateBy(pddOrder.getUpdateBy());
//        erpOrderMapper.insertErpOrder(so);
//
//        // 添加Erp_order_item
//        List<PddOrderItem> orderItems = pddOrderMapper.selectOrderItemByOrderId(pddOrder.getId());
//        List<ErpOrderItem> items = new ArrayList<>();
//        for (var i:orderItems) {
//            if(StringUtils.isEmpty(i.getSpecNum())){
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                return -11;
//            }
//            GoodsSpec spec = goodsSpecMapper.selectGoodsSpecBySpecNum(i.getSpecNum());
//            if (spec == null) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                return -11;
//            }
//            Goods goods = goodsMapper.selectGoodsById(spec.getGoodsId());
//            if(goods == null) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                return -12;
//            }
//
//            ErpOrderItem item = new ErpOrderItem();
//            item.setShipStatus(0);
//            item.setShipType(pddOrder.getShipType());
//            item.setShopId(original.getShopId().intValue());
//            item.setOrderId(so.getId());
//            item.setOrderNum(original.getOrderSn());
//            item.setOrderItemNum(i.getId()+"");
//            item.setSupplierId(goods.getSupplierId().intValue());
//            item.setGoodsId(spec.getGoodsId());
//            item.setSpecId(spec.getId());
//            item.setGoodsTitle(i.getGoodsName());
//            item.setGoodsImg(i.getGoodsImage());
//            item.setGoodsNum(i.getGoodsNum());
//            item.setSpecNum(i.getSpecNum());
//            item.setGoodsSpec(i.getGoodsSpec());
//            item.setGoodsPrice(i.getGoodsPrice());
////            item.setGoodsPurPrice(spec.getPurPrice());
//            item.setItemAmount(i.getItemAmount());
//            item.setQuantity(i.getQuantity());
//            item.setIsGift(i.getIsGift().intValue());
//            item.setRefundCount(0);
//            item.setRefundStatus(1);
//            item.setCreateBy(pddOrder.getUpdateBy());
//            item.setCreateTime(new Date());
//            items.add(item);
//        }
//        // 添加了赠品
//        if(pddOrder.getPddOrderItemList()!=null && !pddOrder.getPddOrderItemList().isEmpty()) {
//            for (var i : pddOrder.getPddOrderItemList()) {
//                if(StringUtils.isEmpty(i.getSpecNum())) {
//                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                    return -11;
//                }
//                GoodsSpec spec = goodsSpecMapper.selectGoodsSpecBySpecNum(i.getSpecNum());
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
//                item.setShipType(pddOrder.getShipType());
//                item.setShopId(original.getShopId().intValue());
//                item.setOrderNum(original.getOrderSn());
//                item.setOrderId(so.getId());
//                item.setOrderItemNum(pddOrder.getId()+"_");
//                item.setSupplierId(goods.getSupplierId().intValue());
//                item.setGoodsId(spec.getGoodsId());
//                item.setSpecId(spec.getId());
//                item.setGoodsTitle(i.getGoodsName());
//                item.setGoodsImg(i.getGoodsImage());
//                item.setGoodsNum(i.getGoodsNum());
//                item.setSpecNum(i.getSpecNum());
//                item.setGoodsSpec(i.getGoodsSpec());
//                item.setGoodsPrice(i.getGoodsPrice());
////                item.setGoodsPurPrice(spec.getPurPrice());
//                item.setItemAmount(i.getItemAmount());
//                item.setQuantity(i.getQuantity());
//                item.setIsGift(1);
//                item.setRefundCount(0);
//                item.setRefundStatus(1);
//                item.setCreateBy(pddOrder.getUpdateBy());
//                item.setCreateTime(new Date());
//                items.add(item);
//            }
//        }
//        erpOrderMapper.batchErpOrderItem(items);
//
//        //更新自己
//        PddOrder po =new PddOrder();
//        po.setId(original.getId());
//        po.setAuditStatus(1L);
//        po.setUpdateBy(pddOrder.getUpdateBy());
//        po.setUpdateTime(new Date());
//        pddOrderMapper.updatePddOrder(po);
        return 1;
    }
}




