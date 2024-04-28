package cn.qihangerp.api.service.impl;

import cn.qihangerp.domain.ErpOrder;
import cn.qihangerp.domain.ErpOrderItem;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.api.domain.*;
import cn.qihangerp.api.mapper.ErpOrderItemMapper;
import cn.qihangerp.api.mapper.ErpOrderMapper;
import cn.qihangerp.api.mapper.WeiOrderItemMapper;
import cn.qihangerp.api.service.WeiOrderService;
import cn.qihangerp.api.mapper.WeiOrderMapper;
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
* @author TW
* @description 针对表【wei_order】的数据库操作Service实现
* @createDate 2024-04-15 17:28:27
*/
@AllArgsConstructor
@Service
public class WeiOrderServiceImpl extends ServiceImpl<WeiOrderMapper, WeiOrder>
    implements WeiOrderService{
    private final WeiOrderMapper mapper;
    private final WeiOrderItemMapper itemMapper;
    private final ErpOrderMapper erpOrderMapper;
    private final ErpOrderItemMapper erpOrderItemMapper;

    @Override
    public PageResult<WeiOrder> queryPageList(WeiOrder bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WeiOrder> queryWrapper = new LambdaQueryWrapper<WeiOrder>()
                .eq(bo.getShopId()!=null,WeiOrder::getShopId,bo.getShopId())
                .eq(StringUtils.hasText(bo.getOrderId()),WeiOrder::getOrderId,bo.getOrderId())
                ;

        Page<WeiOrder> taoGoodsPage = mapper.selectPage(pageQuery.build(), queryWrapper);
        if(taoGoodsPage.getRecords()!=null){
            for (var order:taoGoodsPage.getRecords()) {
                order.setItems(itemMapper.selectList(new LambdaQueryWrapper<WeiOrderItem>().eq(WeiOrderItem::getWeiOrderId,order.getId())));
            }
        }
        return PageResult.build(taoGoodsPage);
    }

    @Transactional
    @Override
    public ResultVo<Integer> saveOrder(Integer shopId, WeiOrder order) {
        try {
            List<WeiOrder> orders = mapper.selectList(new LambdaQueryWrapper<WeiOrder>().eq(WeiOrder::getOrderId, order.getOrderId()));
            if (orders != null && orders.size() > 0) {
                // 存在，修改
                WeiOrder update = new WeiOrder();
                update.setId(orders.get(0).getId());
                update.setStatus(order.getStatus());
                update.setUpdateTime(order.getUpdateTime());
                update.setPayInfo(order.getPayInfo());
                update.setAftersaleDetail(order.getAftersaleDetail());
                update.setDeliveryProductInfo(order.getDeliveryProductInfo());

                mapper.updateById(update);
                // 更新item
                for (var item : order.getItems()) {
                    List<WeiOrderItem> taoOrderItems = itemMapper.selectList(new LambdaQueryWrapper<WeiOrderItem>().eq(WeiOrderItem::getSkuId, item.getSkuId()));
                    if (taoOrderItems != null && taoOrderItems.size() > 0) {
                        // 不处理
                    } else {
                        // 新增
                        item.setWeiOrderId(Long.parseLong(update.getId()));
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
                    item.setWeiOrderId(Long.parseLong(order.getId()));
                    itemMapper.insert(item);
                }
                return ResultVo.success();
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultVo.error(ResultVoEnum.SystemException, "系统异常：" + e.getMessage());
        }
    }

//    @Transactional
//    @Override
//    public ResultVo<Integer> orderConfirm(String[] ids) {
//        if(ids!=null && ids.length >0) {
//            Integer success =0;
//            for(var id : ids){
//                if(StringUtils.hasText(id)) {
//                    // 查询订单
//                    WeiOrder weiOrder = mapper.selectById(id);
//                    if(weiOrder!=null){
//                        // 查询是否确认过
//                        if(weiOrder.getConfirmStatus() == null || weiOrder.getConfirmStatus() == 0){
//                            // 确认状态是null或者0
////                            List<ErpOrder> erpOrders = erpOrderMapper.selectList(new LambdaQueryWrapper<ErpOrder>().eq(ErpOrder::getOrderNum, weiOrder.getOrderId()).eq(ErpOrder::getShopId, weiOrder.getShopId()));
////                            new LambdaQueryWrapper<ErpOrder>().eq(ErpOrder::getOrderNum, weiOrder.getOrderId()).eq(ErpOrder::getShopId, weiOrder.getShopId())
//                            ErpOrder qw = new ErpOrder();
//                            qw.setOrderNum(weiOrder.getOrderId());
//                            qw.setShopId(weiOrder.getShopId());
//                            List<ErpOrder> erpOrders = erpOrderMapper.selectErpOrderList(qw);
//
//
//                            if(erpOrders==null||erpOrders.size()==0){
//                                // 没有数据，开始插入订单数据
//                                ErpOrder insert = new ErpOrder();
//                                insert.setOrderNum(weiOrder.getOrderId());
//                                insert.setShopType(EnumShopType.WEI.getIndex());
//                                insert.setShopId(weiOrder.getShopId());
//                                insert.setRefundStatus(1);
//                                insert.setOrderStatus(0);
//                                insert.setShipStatus(0);
//                                insert.setGoodsAmount(weiOrder.getProductPrice().doubleValue()/100);
//                                insert.setAmount(weiOrder.getOrderPrice().doubleValue()/100);
//                                insert.setReceiverName(weiOrder.getUserName());
//                                insert.setReceiverPhone(weiOrder.getTelNumber());
//                                insert.setProvince(weiOrder.getProvinceName());
//                                insert.setCity(weiOrder.getCityName());
//                                insert.setTown(weiOrder.getCountyName());
//                                insert.setAddress(weiOrder.getDetailInfo());
//                                insert.setShipType(-1);
//                                insert.setOrderTime(new Date(weiOrder.getCreateTime() *1000));
//                                insert.setCreateTime(new Date());
//                                insert.setCreateBy("手动确认");
//                                erpOrderMapper.insertErpOrder(insert);
//
//                                // 插入order_item
//                                List<WeiOrderItem> weiOrderItems = itemMapper.selectList(new LambdaQueryWrapper<WeiOrderItem>().eq(WeiOrderItem::getWeiOrderId, weiOrder.getId()));
//                                for(var item :weiOrderItems) {
//                                    ErpOrderItem itemInsert = new ErpOrderItem();
//                                    itemInsert.setShipStatus(0);
//                                    itemInsert.setShipType(-1);
//                                    itemInsert.setShopId(weiOrder.getShopId());
//
//                                    itemInsert.setOrderId(insert.getId());
//                                    itemInsert.setOrderNum(weiOrder.getOrderId());
//                                    itemInsert.setOrderItemNum(item.getId()+"");
////                                    itemInsert.setSkuId(item.getSkuId());
//                                    // TODO:skuId需要查询erp系统的
////                                    itemInsert.setErpGoodsId(0L);
////                                    itemInsert.setErpSkuId(0L);
//                                    itemInsert.setGoodsTitle(item.getTitle());
//                                    itemInsert.setGoodsImg(item.getThumbImg());
//                                    itemInsert.setGoodsNum("");
////                                    itemInsert.setSkuNum("");
//                                    itemInsert.setGoodsSpec(item.getSkuAttrs());
//                                    if(item.getMarketPrice()!=null) {
//                                        itemInsert.setGoodsPrice(item.getMarketPrice().doubleValue() / 100);
//                                    }
//                                    if(item.getRealPrice()!=null){
//                                        itemInsert.setItemAmount(item.getRealPrice().doubleValue()/100);
//                                    }
//                                    itemInsert.setQuantity(item.getSkuCnt());
//                                    itemInsert.setRefundCount(0);
//                                    itemInsert.setRefundStatus(1);
//                                    erpOrderItemMapper.insert(itemInsert);
//                                }
//                                // 更新wei_order确认状态
//                                WeiOrder update = new WeiOrder();
//                                update.setId(weiOrder.getId());
//                                update.setConfirmStatus(1);
//                                update.setConfirmTime(new Date());
//                                mapper.updateById(update);
//                                success++;
//                            }
//                        }
//                    }
//                }
//            }
//            return ResultVo.success(success);
//        }else {
//            return ResultVo.error(ResultVoEnum.ParamsError, "没有订单ID：");
//        }
//    }

    @Override
    public WeiOrder queryDetailById(Long id) {
        WeiOrder weiOrder = mapper.selectById(id);
        if(weiOrder!=null){
            weiOrder.setItems(itemMapper.selectList(new LambdaQueryWrapper<WeiOrderItem>().eq(WeiOrderItem::getWeiOrderId,weiOrder.getId())));
        }
        return weiOrder;
    }

    @Transactional
    @Override
    public int confirmOrder(WeiOrder bo) {
        if(cn.qihangerp.common.utils.StringUtils.isNull(bo.getShipType())){
            return -3;
        }
        if(bo.getShipType() != 0 && bo.getShipType() != 1){
            // 1 供应商发货 0 仓库发货
            return -4;
        }
        WeiOrder original = mapper.selectById(bo.getId());
        if(original.getConfirmStatus() != 0) return -1;

        ErpOrder erpOrder = erpOrderMapper.selectErpOrderByNum(original.getOrderId());
        if(erpOrder!=null) return -2;

        // 新增ErpOrder
        // 确认订单（操作：插入数据到s_shop_order、s_shop_order_item）
        ErpOrder so = new ErpOrder();
        so.setOrderNum(original.getId());
        so.setShopId(original.getShopId().intValue());
        so.setShopType(EnumShopType.WEI.getIndex());
//        so.setRemark(original.getRemark());
//        so.setBuyerMemo(original.getBuyerFeedback());
//        so.setTag(original.getTag());
        so.setRefundStatus(1);
        so.setOrderStatus(1);
        so.setShipStatus(0);
        so.setShipType(bo.getShipType());
        so.setGoodsAmount(original.getProductPrice().doubleValue() /100);
        if(original.getDiscountedPrice()!=null) {
            so.setDiscountAmount(BigDecimal.valueOf(original.getDiscountedPrice().doubleValue() / 100));
        }else{
            so.setDiscountAmount(BigDecimal.ZERO);
        }
        so.setAmount(original.getOrderPrice().doubleValue()/100);
        so.setPostage(BigDecimal.valueOf(original.getFreight()/100));

//        so.setPayTime(original.getPayTime());
        so.setConfirmTime(new Date());
        so.setCreateTime(new Date());
        so.setCreateBy("确认订单");
        so.setReceiverName(bo.getReceiver());
        so.setReceiverPhone(bo.getPhone());
        so.setAddress(bo.getAddress());
        so.setCountry("中国");
        so.setProvince(bo.getProvince());
        so.setCity(bo.getCity());
        so.setTown(bo.getDistrict());

        erpOrderMapper.insertErpOrder(so);
        // 新增ErpOrderItem
        List<WeiOrderItem> weiOrderItems = itemMapper.selectList(new LambdaQueryWrapper<WeiOrderItem>().eq(WeiOrderItem::getWeiOrderId, original.getId()));

        List<ErpOrderItem> items = new ArrayList<>();
        for (var i:weiOrderItems) {
//            if(com.qihang.common.utils.StringUtils.isEmpty(i.getSkuCode())) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                return -11;
//            }
//            GoodsSpec spec = goodsSpecMapper.selectGoodsSpecBySpecNum(i.getSpecNumber());
//            if (spec == null) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                return -11;
//            }
//            Goods goods = goodsMapper.selectGoodsById(spec.getGoodsId());
//            if(goods == null){
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                return -12;
//            }

            ErpOrderItem item = new ErpOrderItem();
            item.setShipStatus(0);
            item.setShipType(bo.getShipType());
            item.setShopId(original.getShopId());
            item.setOrderId(so.getId());
            item.setOrderNum(original.getOrderId());
            item.setOrderItemNum(i.getId().toString());
            //TODO:未处理商品信息
//            item.setSupplierId(goods.getSupplierId().intValue());
            item.setSupplierId(0);
//            item.setGoodsId(spec.getGoodsId());
            item.setGoodsId(0L);
//            item.setSpecId(spec.getId());
            item.setSpecId(0L);
            item.setGoodsTitle(i.getTitle());
            item.setGoodsImg(i.getThumbImg());
            item.setGoodsNum(i.getProductId());
            item.setSpecNum(i.getSkuCode());
            item.setGoodsSpec(i.getSkuAttrs());
            item.setGoodsPrice(i.getSalePrice().doubleValue()/100);
//            item.setGoodsPurPrice(spec.getPurPrice());
            item.setItemAmount(i.getRealPrice().doubleValue()/100);
            item.setQuantity(i.getSkuCnt().longValue());
            item.setIsGift(0);
            item.setRefundCount(0);
            item.setRefundStatus(1);
            item.setCreateBy("确认订单");
            item.setCreateTime(new Date());
            items.add(item);
        }
        // 添加了赠品
//        if(taoOrder.getTaoOrderItemList()!=null && !taoOrder.getTaoOrderItemList().isEmpty()){
//            for (var g:taoOrder.getTaoOrderItemList()) {
//                if(com.qihang.common.utils.StringUtils.isEmpty(g.getSpecNumber())) {
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
//                item.setQuantity(g.getQuantity().intValue());
//                item.setIsGift(1);
//                item.setRefundCount(0);
//                item.setRefundStatus(1);
//                item.setCreateBy(taoOrder.getUpdateBy());
//                item.setCreateTime(new Date());
//                items.add(item);
//            }
//        }
        erpOrderMapper.batchErpOrderItem(items);

        //更新自己
        WeiOrder update = new WeiOrder();
        update.setId(original.getId());
        update.setConfirmStatus(1);
        update.setConfirmTime(new Date());
//        update.setUpdateBy("确认订单");
        Long ut = (System.currentTimeMillis()/1000);
        update.setUpdateTime(ut.intValue());
        mapper.updateById(update);
        return 1;
    }
}




