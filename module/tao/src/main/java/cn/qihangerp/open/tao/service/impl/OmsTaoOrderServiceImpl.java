package cn.qihangerp.open.tao.service.impl;

import cn.qihangerp.common.*;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.domain.ErpSaleOrder;
import cn.qihangerp.domain.ErpSaleOrderItem;
import cn.qihangerp.mq.MQRequest;
import cn.qihangerp.mq.MQRequestType;
import cn.qihangerp.open.tao.bo.TaoOrderBo;
import cn.qihangerp.open.tao.bo.TaoOrderConfirmBo;
import cn.qihangerp.open.tao.common.TaoOrderStateEnum;
import cn.qihangerp.open.tao.domain.OmsTaoGoodsSku;
import cn.qihangerp.open.tao.domain.OmsTaoOrderItem;
import cn.qihangerp.open.tao.mapper.OmsTaoGoodsSkuMapper;
import cn.qihangerp.open.tao.mapper.OmsTaoOrderItemMapper;
import cn.qihangerp.open.tao.server.SimpleClientHandler;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.open.tao.domain.OmsTaoOrder;
import cn.qihangerp.open.tao.service.OmsTaoOrderService;
import cn.qihangerp.open.tao.mapper.OmsTaoOrderMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author TW
* @description 针对表【oms_tao_order(淘宝订单表)】的数据库操作Service实现
* @createDate 2024-04-30 13:52:20
*/
@AllArgsConstructor
@Service
public class OmsTaoOrderServiceImpl extends ServiceImpl<OmsTaoOrderMapper, OmsTaoOrder>
    implements OmsTaoOrderService{
    private final OmsTaoOrderMapper mapper;
    private final OmsTaoOrderItemMapper itemMapper;
    private final OmsTaoGoodsSkuMapper skuMapper;
    private final SimpleClientHandler simpleClientHandler;
    @Transactional
    @Override
    public ResultVo<Integer> saveOrder(Long shopId, OmsTaoOrder order) {
        if(order == null ) return ResultVo.error(ResultVoEnum.SystemException);
        try {
            List<OmsTaoOrder> taoOrders = mapper.selectList(new LambdaQueryWrapper<OmsTaoOrder>().eq(OmsTaoOrder::getTid, order.getTid()));
            if (taoOrders != null && taoOrders.size() > 0) {
                // 存在，修改
                OmsTaoOrder update = new OmsTaoOrder();
                update.setId(taoOrders.get(0).getId());
                update.setReceiverName(order.getReceiverName());
                update.setReceiverMobile(order.getReceiverMobile());
                update.setReceiverAddress(order.getReceiverAddress());
                update.setSid(order.getSid());
                update.setSellerRate(order.getSellerRate());
                update.setBuyerRate(order.getBuyerRate());
                update.setStatus(order.getStatus());
                update.setModified(order.getModified());
                update.setEndTime(order.getEndTime());
                update.setConsignTime(order.getConsignTime());

                update.setUpdateTime(new Date());
                update.setReceivedPayment(order.getReceivedPayment());
                update.setAvailableConfirmFee(order.getAvailableConfirmFee());
                mapper.updateById(update);
                // 更新item
                for (var item : order.getItems()) {
                    List<OmsTaoOrderItem> taoOrderItems = itemMapper.selectList(new LambdaQueryWrapper<OmsTaoOrderItem>().eq(OmsTaoOrderItem::getOid, item.getOid()));
                    if (taoOrderItems != null && taoOrderItems.size() > 0) {
                        // 更新
                        OmsTaoOrderItem itemUpdate = new OmsTaoOrderItem();
                        itemUpdate.setId(taoOrderItems.get(0).getId());
                        itemUpdate.setRefundId(item.getRefundId());
                        itemUpdate.setRefundStatus(item.getRefundStatus());
                        itemUpdate.setStatus(item.getStatus());
                        itemUpdate.setBuyerRate(item.getBuyerRate());
                        itemUpdate.setSellerRate(item.getSellerRate());
                        itemUpdate.setEndTime(item.getEndTime());
                        itemUpdate.setConsignTime(item.getConsignTime());
                        itemUpdate.setShippingType(item.getShippingType());
                        itemUpdate.setLogisticsCompany(item.getLogisticsCompany());
                        itemUpdate.setInvoiceNo(item.getInvoiceNo());
                        itemMapper.updateById(itemUpdate);
                    } else {
                        // 新增
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
                for (var item : order.getItems()) {
                    itemMapper.insert(item);
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

    @Override
    public ResultVo<Integer> updateOrderStatus(OmsTaoOrder order) {
        if(order == null ) return ResultVo.error(ResultVoEnum.SystemException);
        try {
            List<OmsTaoOrder> taoOrders = mapper.selectList(new LambdaQueryWrapper<OmsTaoOrder>().eq(OmsTaoOrder::getTid, order.getTid()));
            if (taoOrders != null && taoOrders.size() > 0) {
                // 存在，修改
                OmsTaoOrder update = new OmsTaoOrder();
                update.setId(taoOrders.get(0).getId());
                update.setReceiverName(order.getReceiverName());
                update.setReceiverMobile(order.getReceiverMobile());
                update.setReceiverAddress(order.getReceiverAddress());
                update.setSid(order.getSid());
                update.setSellerRate(order.getSellerRate());
                update.setBuyerRate(order.getBuyerRate());
                update.setStatus(order.getStatus());
                update.setModified(order.getModified());
                update.setEndTime(order.getEndTime());
                update.setConsignTime(order.getConsignTime());
                update.setUpdateTime(new Date());
                update.setReceivedPayment(order.getReceivedPayment());
                update.setAvailableConfirmFee(order.getAvailableConfirmFee());
                mapper.updateById(update);
                // 更新item
                for (var item : order.getItems()) {
                    List<OmsTaoOrderItem> taoOrderItems = itemMapper.selectList(new LambdaQueryWrapper<OmsTaoOrderItem>().eq(OmsTaoOrderItem::getOid, item.getOid()));
                    if (taoOrderItems != null && taoOrderItems.size() > 0) {
                        // 更新
                        OmsTaoOrderItem itemUpdate = new OmsTaoOrderItem();
                        itemUpdate.setId(taoOrderItems.get(0).getId());
                        itemUpdate.setRefundId(item.getRefundId());
                        itemUpdate.setRefundStatus(item.getRefundStatus());
                        itemUpdate.setStatus(item.getStatus());
                        itemUpdate.setBuyerRate(item.getBuyerRate());
                        itemUpdate.setSellerRate(item.getSellerRate());
                        itemUpdate.setEndTime(item.getEndTime());
                        itemUpdate.setConsignTime(item.getConsignTime());
                        itemUpdate.setShippingType(item.getShippingType());
                        itemUpdate.setLogisticsCompany(item.getLogisticsCompany());
                        itemUpdate.setInvoiceNo(item.getInvoiceNo());
                        itemMapper.updateById(itemUpdate);
                    }
                }
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            System.out.println("保存订单数据错误："+e.getMessage());
            return ResultVo.error(ResultVoEnum.SystemException, "系统异常：" + e.getMessage());
        }
        return ResultVo.success();
    }

    @Override
    public PageResult<OmsTaoOrder> queryPageList(TaoOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OmsTaoOrder> queryWrapper = new LambdaQueryWrapper<OmsTaoOrder>()
                .eq(bo.getShopId()!=null,OmsTaoOrder::getShopId,bo.getShopId())
                .eq(StringUtils.hasText(bo.getTid()),OmsTaoOrder::getTid,bo.getTid())
                .eq(StringUtils.hasText(bo.getStatus()),OmsTaoOrder::getStatus,bo.getStatus())
                ;

        Page<OmsTaoOrder> taoGoodsPage = mapper.selectPage(pageQuery.build(), queryWrapper);
        if(taoGoodsPage.getRecords()!=null){
            for (var order:taoGoodsPage.getRecords()) {
                order.setItems(itemMapper.selectList(new LambdaQueryWrapper<OmsTaoOrderItem>().eq(OmsTaoOrderItem::getTid,order.getTid())));
            }
        }
        return PageResult.build(taoGoodsPage);
    }

    @Override
    public OmsTaoOrder queryDetailById(Long id) {
        OmsTaoOrder omsTaoOrder = mapper.selectById(id);
        omsTaoOrder.setItems(itemMapper.selectList(new LambdaQueryWrapper<OmsTaoOrderItem>().eq(OmsTaoOrderItem::getTid,omsTaoOrder.getTid())));
        return omsTaoOrder;
    }


    /**
     * 确认订单
     * @param bo
     * @return
     */
    @Transactional
    @Override
    public int confirmOrder(TaoOrderConfirmBo bo) throws InterruptedException {
//        if(cn.qihangerp.common.utils.StringUtils.isNull(taoOrder.getShipType())){
//            return -3;
//        }
//        if(taoOrder.getShipType() != 0 && taoOrder.getShipType() != 1){
//            // 1 供应商发货 0 仓库发货
//            return -4;
//        }

//        TaoOrder original = taoOrderMapper.selectTaoOrderById(taoOrder.getId());
        OmsTaoOrder original = mapper.selectById(bo.getId());;
        if(original.getAuditStatus()!=null &&  original.getAuditStatus() != 0) return -1;//无需审核

//        ErpOrder erpOrder = erpOrderMapper.selectErpOrderByNum(taoOrder.getId());
//        if(erpOrder!=null) return -2;

        // 新增ErpOrder
        // 确认订单（操作：插入数据到s_shop_order、s_shop_order_item）
        ErpSaleOrder so = new ErpSaleOrder();
        so.setOrderNum(original.getTid().toString());
        so.setOrderTime(original.getCreated());
        so.setShopId(original.getShopId());
        so.setShopType(EnumShopType.TAO.getIndex());

//        so.setRemark(original.getRemark());
        so.setBuyerMemo(original.getBuyerMemo());
        so.setSellerMemo(original.getSellerMemo());
//        so.setTag(original.getTag());
        // 状态
        int orderStatus = TaoOrderStateEnum.getIndex(original.getStatus());
        if (orderStatus == 11) {
            so.setRefundStatus(2);
        } else if (orderStatus == -1) {
            so.setRefundStatus(-1);
        } else {
            so.setRefundStatus(1);
        }
        so.setOrderStatus(orderStatus);
        so.setShipStatus(0);
//        so.setShipType(bo.getShipType());
//        so.setGoodsAmount(original.getTotalFee());
//        so.setPostage(original.getPostFee());
//        so.setAmount(original.getPayment().doubleValue());
//        so.setDiscountAmount(original.getDiscountFee());
//        so.setPayment(taoOrder.getPayment().doubleValue());
        so.setGoodsAmount(original.getPrice()!=null?original.getPrice():0.0);
        so.setPlatformDiscount(0.0);
        so.setSellerDiscount(original.getDiscountFee()!=null?original.getDiscountFee().doubleValue():0.0);
        so.setPayAmount(original.getPayment()!=null?original.getPayment().doubleValue():0.0);
        so.setOrderAmount(original.getTotalFee()!=null?original.getTotalFee():0.0);
        so.setPostage(original.getPostFee()!=null?original.getPostFee().doubleValue():0.0);

        so.setPayTime(original.getPayTime());
        so.setConfirmTime(new Date());
        so.setCreateTime(new Date());
        so.setCreateBy(bo.getUpdateBy());
        so.setReceiverName(bo.getReceiverName());
        so.setReceiverPhone(bo.getReceiverMobile());
        so.setAddress(bo.getReceiverAddress());
        so.setCountry("中国");
        so.setProvince(original.getReceiverState());
        so.setCity(original.getReceiverCity());
        so.setTown(original.getReceiverDistrict());
        so.setShipStatus(0);
//        erpOrderMapper.insertErpOrder(so);
        // 新增ErpOrderItem
//        List<OmsTaoOrderItem> taoOrderItems = taoOrderMapper.selectOrderItemByOrderId(taoOrder.getId());
        List<OmsTaoOrderItem> taoOrderItems = itemMapper.selectList(new LambdaQueryWrapper<OmsTaoOrderItem>().eq(OmsTaoOrderItem::getTid,original.getTid()));
        if(taoOrderItems!=null&&taoOrderItems.size()>0) {
            List<ErpSaleOrderItem> items = new ArrayList<>();
            for (var i : taoOrderItems) {
                Long erpGoodsId = 0L;
                Long erpSkuId = 0L;
                Long erpSupplierId = 0L;
                if(StringUtils.hasText(i.getSkuId())) {
                    List<OmsTaoGoodsSku> skuList = skuMapper.selectList(new LambdaQueryWrapper<OmsTaoGoodsSku>().eq(OmsTaoGoodsSku::getSkuId, i.getSkuId()));
                    if(skuList!=null && skuList.size()>0){
                        erpGoodsId = skuList.get(0).getErpGoodsId();
                        erpSkuId = skuList.get(0).getErpGoodsSkuId();
//                        erpSupplierId = skuList.get(0).getErpSupplierId();
                    }
                }

                ErpSaleOrderItem item = new ErpSaleOrderItem();
                item.setShipStatus(0);
//                item.setShipType(taoOrder.getShipType());
                item.setShopId(original.getShopId());
                item.setOrderId(so.getId());
                item.setOriginalOrderId(original.getTid().toString());
                item.setOriginalOrderItemId(i.getOid().toString());
                item.setOriginalSkuId(i.getSkuId());
                item.setSupplierId(erpSupplierId);
                item.setGoodsId(erpGoodsId);
                item.setSpecId(erpSkuId);
                item.setGoodsTitle(i.getTitle());
                item.setGoodsImg(i.getPicPath());
                item.setGoodsNum(i.getOuterIid());
                item.setSpecNum(i.getOuterSkuId());
                item.setGoodsSpec(i.getSkuPropertiesName());
                item.setGoodsPrice(i.getPrice().doubleValue());
//            item.setGoodsPurPrice(spec.getPurPrice());
                item.setItemAmount(i.getTotalFee().doubleValue());
                item.setQuantity(i.getNum());
                item.setIsGift(0);
//                item.setRefundCount(0);
//                item.setRefundStatus(1);
                if(i.getRefundStatus().equals("WAIT_SELLER_AGREE")
                        || i.getRefundStatus().equals("WAIT_BUYER_RETURN_GOODS")
                        || i.getRefundStatus().equals("WAIT_SELLER_CONFIRM_GOODS")
                        || i.getRefundStatus().equals("SELLER_REFUSE_BUYER")){
                    item.setRefundStatus(2);
                    item.setRefundCount(i.getNum());
                } else if (i.getRefundStatus().equals("SUCCESS")) {
                    item.setRefundCount(i.getNum());
                    item.setRefundStatus(4);
                }else if (i.getRefundStatus().equals("CLOSED") || i.getRefundStatus().equals("NO_REFUND")) {
                    item.setRefundStatus(1);
                    item.setRefundCount(0);
                }
                // 状态
//                int orderStatus1 = TaoOrderStateEnum.getIndex(i.getStatus());
                item.setOrderStatus(orderStatus);
                item.setCreateBy(bo.getUpdateBy());
                item.setCreateTime(new Date());
                items.add(item);
            }
            so.setItemList(items);
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
        req.setData(so);
        ApiResult s = simpleClientHandler.sendRequestAndWaitForResponse(req);
        if(s.getResult()==ApiResultEnum.SUCCESS.getIndex()) {
            //更新自己
            OmsTaoOrder update = new OmsTaoOrder();
            update.setId(original.getId());
            update.setAuditStatus(1);
            update.setAuditTime(new Date());
            update.setUpdateBy(bo.getUpdateBy());
            update.setUpdateTime(new Date());
            update.setReceiverAddress(bo.getReceiverAddress());
            update.setReceiverMobile(bo.getReceiverMobile());
            update.setReceiverName(bo.getReceiverName());
            mapper.updateById(update);
        }
        return 1;
    }
}




