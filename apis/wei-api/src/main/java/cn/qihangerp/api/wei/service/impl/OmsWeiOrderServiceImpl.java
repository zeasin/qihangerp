package cn.qihangerp.api.wei.service.impl;

import cn.qihangerp.api.wei.domain.OmsWeiOrderItem;
import cn.qihangerp.api.wei.mapper.OmsWeiOrderItemMapper;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.common.ResultVoEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.api.wei.domain.OmsWeiOrder;
import cn.qihangerp.api.wei.service.OmsWeiOrderService;
import cn.qihangerp.api.wei.mapper.OmsWeiOrderMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* @author TW
* @description 针对表【oms_wei_order】的数据库操作Service实现
* @createDate 2024-05-06 19:23:39
*/
@AllArgsConstructor
@Service
public class OmsWeiOrderServiceImpl extends ServiceImpl<OmsWeiOrderMapper, OmsWeiOrder>
    implements OmsWeiOrderService{
    private final OmsWeiOrderMapper mapper;
    private final OmsWeiOrderItemMapper itemMapper;

    @Override
    public PageResult<OmsWeiOrder> queryPageList(OmsWeiOrder bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OmsWeiOrder> queryWrapper = new LambdaQueryWrapper<OmsWeiOrder>()
                .eq(bo.getShopId()!=null,OmsWeiOrder::getShopId,bo.getShopId())
                .eq(StringUtils.hasText(bo.getOrderId()),OmsWeiOrder::getOrderId,bo.getOrderId())
                ;

        Page<OmsWeiOrder> page = mapper.selectPage(pageQuery.build(), queryWrapper);
        if(page.getRecords()!=null){
            for (var order:page.getRecords()) {
                order.setItems(itemMapper.selectList(new LambdaQueryWrapper<OmsWeiOrderItem>().eq(OmsWeiOrderItem::getOrderId,order.getOrderId())));
            }
        }
        return PageResult.build(page);
    }

    @Transactional
    @Override
    public ResultVo<Integer> saveOrder(Long shopId, OmsWeiOrder order) {
        try {
            List<OmsWeiOrder> orders = mapper.selectList(new LambdaQueryWrapper<OmsWeiOrder>().eq(OmsWeiOrder::getOrderId, order.getOrderId()));
            if (orders != null && orders.size() > 0) {
                // 存在，修改
                OmsWeiOrder update = new OmsWeiOrder();
                update.setId(orders.get(0).getId());
                update.setOrderId(order.getOrderId());
                update.setStatus(order.getStatus());
                update.setUpdateTime(order.getUpdateTime());
                update.setPayInfo(order.getPayInfo());
                update.setAftersaleDetail(order.getAftersaleDetail());
                update.setDeliveryProductInfo(order.getDeliveryProductInfo());

                mapper.updateById(update);
                // 更新item
                for (var item : order.getItems()) {
                    List<OmsWeiOrderItem> taoOrderItems = itemMapper.selectList(
                            new LambdaQueryWrapper<OmsWeiOrderItem>().eq(OmsWeiOrderItem::getSkuId, item.getSkuId()).eq(OmsWeiOrderItem::getOrderId,order.getOrderId())
                    );

                    if (taoOrderItems != null && taoOrderItems.size() > 0) {
                        // 不处理
                    } else {
                        // 新增
                        item.setShopId(shopId);
                        item.setOrderId(order.getOrderId());
                        itemMapper.insert(item);
                    }
                }
                return ResultVo.error(ResultVoEnum.DataExist, "订单已经存在，更新成功");
            } else {
                // 不存在，新增
                order.setShopId(shopId);
                mapper.insert(order);
                // 添加item
                for (var item : order.getItems()) {
                    item.setShopId(shopId);
                    item.setOrderId(order.getOrderId());
                    itemMapper.insert(item);
                }
                return ResultVo.success();
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultVo.error(ResultVoEnum.SystemException, "系统异常：" + e.getMessage());
        }
    }


    @Override
    public OmsWeiOrder queryDetailById(Long id) {
        OmsWeiOrder weiOrder = mapper.selectById(id);
        if(weiOrder!=null){
            weiOrder.setItems(itemMapper.selectList(new LambdaQueryWrapper<OmsWeiOrderItem>().eq(OmsWeiOrderItem::getOrderId,weiOrder.getOrderId())));
        }
        return weiOrder;
    }

    @Transactional
    @Override
    public int confirmOrder(OmsWeiOrder bo) {

//        ErpOrder erpOrder = erpOrderMapper.selectErpOrderByNum(original.getOrderId());
//        if(erpOrder!=null) return -2;
//
//        // 新增ErpOrder
//        // 确认订单（操作：插入数据到s_shop_order、s_shop_order_item）
//        ErpOrder so = new ErpOrder();
//        so.setOrderNum(original.getId());
//        so.setShopId(original.getShopId().intValue());
//        so.setShopType(EnumShopType.WEI.getIndex());
////        so.setRemark(original.getRemark());
////        so.setBuyerMemo(original.getBuyerFeedback());
////        so.setTag(original.getTag());
//        so.setRefundStatus(1);
//        so.setOrderStatus(1);
//        so.setShipStatus(0);
//        so.setShipType(bo.getShipType());
//        so.setGoodsAmount(original.getProductPrice().doubleValue() /100);
//        if(original.getDiscountedPrice()!=null) {
//            so.setDiscountAmount(BigDecimal.valueOf(original.getDiscountedPrice().doubleValue() / 100));
//        }else{
//            so.setDiscountAmount(BigDecimal.ZERO);
//        }
//        so.setAmount(original.getOrderPrice().doubleValue()/100);
//        so.setPostage(BigDecimal.valueOf(original.getFreight()/100));
//
////        so.setPayTime(original.getPayTime());
//        so.setConfirmTime(new Date());
//        so.setCreateTime(new Date());
//        so.setCreateBy("确认订单");
//        so.setReceiverName(bo.getReceiver());
//        so.setReceiverPhone(bo.getPhone());
//        so.setAddress(bo.getAddress());
//        so.setCountry("中国");
//        so.setProvince(bo.getProvince());
//        so.setCity(bo.getCity());
//        so.setTown(bo.getDistrict());
//
//        erpOrderMapper.insertErpOrder(so);
//        // 新增ErpOrderItem
//        List<WeiOrderItem> weiOrderItems = itemMapper.selectList(new LambdaQueryWrapper<WeiOrderItem>().eq(WeiOrderItem::getWeiOrderId, original.getId()));
//
//        List<ErpOrderItem> items = new ArrayList<>();
//        for (var i:weiOrderItems) {
////            if(com.qihang.common.utils.StringUtils.isEmpty(i.getSkuCode())) {
////                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
////                return -11;
////            }
////            GoodsSpec spec = goodsSpecMapper.selectGoodsSpecBySpecNum(i.getSpecNumber());
////            if (spec == null) {
////                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
////                return -11;
////            }
////            Goods goods = goodsMapper.selectGoodsById(spec.getGoodsId());
////            if(goods == null){
////                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
////                return -12;
////            }
//
//            ErpOrderItem item = new ErpOrderItem();
//            item.setShipStatus(0);
//            item.setShipType(bo.getShipType());
//            item.setShopId(original.getShopId());
//            item.setOrderId(so.getId());
//            item.setOrderNum(original.getOrderId());
//            item.setOrderItemNum(i.getId().toString());
//            //TODO:未处理商品信息
////            item.setSupplierId(goods.getSupplierId().intValue());
//            item.setSupplierId(0L);
////            item.setGoodsId(spec.getGoodsId());
//            item.setGoodsId(0L);
////            item.setSpecId(spec.getId());
//            item.setSpecId(0L);
//            item.setGoodsTitle(i.getTitle());
//            item.setGoodsImg(i.getThumbImg());
//            item.setGoodsNum(i.getProductId());
//            item.setSpecNum(i.getSkuCode());
//            item.setGoodsSpec(i.getSkuAttrs());
//            item.setGoodsPrice(i.getSalePrice().doubleValue()/100);
////            item.setGoodsPurPrice(spec.getPurPrice());
//            item.setItemAmount(i.getRealPrice().doubleValue()/100);
//            item.setQuantity(i.getSkuCnt());
//            item.setIsGift(0);
//            item.setRefundCount(0);
//            item.setRefundStatus(1);
//            item.setCreateBy("确认订单");
//            item.setCreateTime(new Date());
//            items.add(item);
//        }
//        // 添加了赠品
////        if(taoOrder.getTaoOrderItemList()!=null && !taoOrder.getTaoOrderItemList().isEmpty()){
////            for (var g:taoOrder.getTaoOrderItemList()) {
////                if(com.qihang.common.utils.StringUtils.isEmpty(g.getSpecNumber())) {
////                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
////                    return -11;
////                }
////                GoodsSpec spec = goodsSpecMapper.selectGoodsSpecBySpecNum(g.getSpecNumber());
////                if (spec == null) {
////                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
////                    return -11;
////                }
////                Goods goods = goodsMapper.selectGoodsById(spec.getGoodsId());
////                if(goods == null) {
////                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
////                    return -12;
////                }
////
////                ErpOrderItem item = new ErpOrderItem();
////                item.setShipStatus(0);
////                item.setShipType(taoOrder.getShipType());
////                item.setShopId(original.getShopId().intValue());
////                item.setOrderId(so.getId());
////                item.setOrderNum(original.getId());
////                item.setOrderItemNum(original.getId()+"_");
////                item.setSupplierId(goods.getSupplierId().intValue());
////                item.setGoodsId(spec.getGoodsId());
////                item.setSpecId(spec.getId());
////                item.setGoodsTitle(g.getGoodsTitle());
////                item.setGoodsImg(g.getProductImgUrl());
////                item.setGoodsNum(g.getGoodsNumber());
////                item.setSpecNum(g.getSpecNumber());
////                item.setGoodsSpec(g.getSkuInfo());
////                item.setGoodsPrice(g.getPrice().doubleValue());
//////                item.setGoodsPurPrice(spec.getPurPrice());
////                item.setItemAmount(g.getItemAmount().doubleValue());
////                item.setQuantity(g.getQuantity().intValue());
////                item.setIsGift(1);
////                item.setRefundCount(0);
////                item.setRefundStatus(1);
////                item.setCreateBy(taoOrder.getUpdateBy());
////                item.setCreateTime(new Date());
////                items.add(item);
////            }
////        }
//        erpOrderMapper.batchErpOrderItem(items);
//
//        //更新自己
//        WeiOrder update = new WeiOrder();
//        update.setId(original.getId());
//        update.setConfirmStatus(1);
//        update.setConfirmTime(new Date());
////        update.setUpdateBy("确认订单");
//        Long ut = (System.currentTimeMillis()/1000);
//        update.setUpdateTime(ut.intValue());
//        mapper.updateById(update);
        return 1;
    }
}




