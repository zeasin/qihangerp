package cn.qihangerp.open.tao.controller;

import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.enums.EnumTmallOrderStatus;
import cn.qihangerp.open.tao.OrderApiHelper;
import cn.qihangerp.open.tao.common.ApiResultVo;
import cn.qihangerp.open.tao.domain.OmsTaoOrder;
import cn.qihangerp.open.tao.domain.TaoOrder;
import cn.qihangerp.open.tao.domain.TaoOrderItem;
import cn.qihangerp.open.tao.domain.TaoOrderRefund;
import cn.qihangerp.open.tao.model.TradeList;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class TaoBaoOpenOrderUpdHelper {


//    /**
//     * 更新订单（循环分页）
//     *
//     * @param pageNo
//     * @param pageSize
//     * @param sessionKey
//     * @return
//     */
//    public static TaoBaoOpenOrderUpdResult<TaoOrder> updTmallOrder(String appKey, String appSecret, String sessionKey) throws IOException {
//        ApiResultVo<TradeList> tradeBeanApiResultVo = OrderApiHelper.pullTradeList(appKey, appSecret, sessionKey);
//       if (tradeBeanApiResultVo.getCode()== ResultVoEnum.SUCCESS.getIndex()){
//           //组合的订单列表
//           List<TaoOrder> orderList = new ArrayList<>();
//
//           //有数据
//           for (var trade : tradeBeanApiResultVo.getList()) {
//               OmsTaoOrder omsTaoOrder = OrderAssembleHelper.assembleOrder(trade);
//               try {
//                   TaoOrder order = new TaoOrder();
//                   order.setId(trade.getTid().toString());
////                   order.setOrderCreateTime(trade.getCreated());
////                   order.setOrderModifyTime(trade.getModified());
////                   order.setPayTime(trade.getPay_time());
//                   order.setTotalAmount(BigDecimal.valueOf(Double.parseDouble(trade.getPayment())));
////                   order.setShippingFee(BigDecimal.valueOf(Double.parseDouble(trade.getPostFee())));
//                   order.setPayAmount(BigDecimal.valueOf(Double.parseDouble(trade.getPayment())));
////                   order.setBuyerName(trade.getBuyerNick());
////                   order.setSellerMemo(trade.getSellerMemo());
////                   order.setProvince(trade.getReceiverState());
////                   order.setCity(trade.getReceiverCity());
////                   order.setDistrict(trade.getReceiverDistrict());
//                   order.setStatus(EnumTmallOrderStatus.getStatus(trade.getStatus()));
//                   order.setStatusStr(trade.getStatus());
////                   order.setReceiver(trade.getReceiverName());
////                   order.setProvince(trade.getReceiverState());
////                   order.setCity(trade.getReceiverCity());
////                   order.setDistrict(trade.getReceiverDistrict());
////                   order.setAddress(trade.getReceiverAddress());
////                   order.setPhone(trade.getReceiverMobile());
//                   List<TaoOrderItem> items = new ArrayList<>();
//                   for (var item : trade.getOrders()) {
//                       TaoOrderItem orderItem = new TaoOrderItem();
//                       orderItem.setOrderId(order.getId());
//                       orderItem.setSubItemId(item.getOid().toString());
//                       Long refundStatus = -1L;
//                       if(item.getRefund_status().equals("NO_REFUND")){
//                           refundStatus = 0L;
//                       }else {
//                           refundStatus = 1L;
//                       }
//                       orderItem.setRefundStatus(refundStatus);
////                       orderItem.setProductId(item.getNumIid());
////                       orderItem.setSkuId(Long.parseLong(item.getSkuId()));
////                       orderItem.setSpecNumber(item.getOuterSkuId());
////                       orderItem.setGoodsNumber(item.getOuterIid());
////                       orderItem.setProductImgUrl(item.getPicPath());
//                       orderItem.setGoodsTitle(item.getTitle());
//                       orderItem.setPrice(BigDecimal.valueOf(Double.parseDouble(item.getPrice())));
////                       orderItem.setQuantity(item.getNum());
////                       orderItem.setSkuInfo(item.getSkuPropertiesName());
//                       orderItem.setItemAmount(BigDecimal.valueOf(Double.parseDouble(item.getPayment())));
////                       orderItem.setDiscountFee(new BigDecimal(item.getDiscountFee()));
////                       orderItem.setAdjustFee(new BigDecimal(item.getAdjustFee()));
////                       orderItem.setRefundStatusStr(item.getRefundStatus());
//
//                       orderItem.setNewSpecId(0L);
//                       orderItem.setIsGift(0);
//                       orderItem.setIsSwap(0);
//                       items.add(orderItem);
//                   }
//                   order.setTaoOrderItemList(items);
//
//                   orderList.add(order);
//               } catch (Exception e) {
//               }
//           }
//
//           return new TaoBaoOpenOrderUpdResult(tradeBeanApiResultVo.getTotalRecords(), orderList);
//       }else{
//           return new TaoBaoOpenOrderUpdResult(tradeBeanApiResultVo.getCode(), tradeBeanApiResultVo.getMsg());
//       }
//    }


    /**
     * 拉取淘系退货订单
     * @param pageNo
     * @param pageSize
     * @param url
     * @param appKey
     * @param appSecret
     * @param sessionKey
     * @return
     * @throws ApiException
     */
    public static TaoBaoOpenOrderUpdResult<TaoOrderRefund> updTmallRefunOrder(Long pageNo, Long pageSize, String url, String appKey, String appSecret, String sessionKey)   {
//        TaobaoClient client = new DefaultTaobaoClient(url, appKey, appSecret);
//        List<TaoOrderRefund> list = new ArrayList<>();
//
////        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
////        RefundGetRequest req1 = new RefundGetRequest();
////        req1.setFields("title,address,good_return_time,created");
////        req1.setRefundId(44929186648087305L);
////        RefundGetResponse rsp1 = client.execute(req1, sessionKey);
////        System.out.println(rsp1.getBody());
////        RefundsApplyGetRequest raReq = new RefundsApplyGetRequest();
////        raReq.setFields("refund_id, tid, title, buyer_nick, seller_nick, total_fee, status, created, refund_fee");
////        raReq.setPageNo(1L);
////        raReq.setPageSize(10L);
////////        raReq.setStatus("SUCCESS");
//////
////        RefundsApplyGetResponse raRsp = client.execute(raReq,sessionKey);
//
//        RefundsReceiveGetRequest req = new RefundsReceiveGetRequest();
//        req.setFields("refund_id, tid, title, buyer_nick, seller_nick, total_fee, status, created,num, refund_fee, oid, good_status," +
//                " company_name, sid, payment, reason, desc, has_good_return, modified, order_status,refund_phase,sku");
////        req.setStatus("WAIT_SELLER_AGREE");
////        req.setSellerNick("hz0799");
////        req.setBuyerNick("juan20108810");
////        req.setType("fixed");
////        req.setType("fixed");
//        req.setPageNo(pageNo);
//        req.setPageSize(pageSize);
//        RefundsReceiveGetResponse rsp = client.execute(req, sessionKey);
//        if (rsp.getTotalResults() > 0) {
//            //查到了数据
//            for (var item : rsp.getRefunds()) {
//                //循环插入退货数据
//                TaoOrderRefund tmallOrderRefund = new TaoOrderRefund();
////                tmallOrderRefund.setBuyer_nick(item.getBuyerNick());
//                tmallOrderRefund.setCreated(DateUtil.dateToStamp(item.getCreated()));
//                tmallOrderRefund.setRemark(item.getDesc());
//                tmallOrderRefund.setGoodStatus(item.getGoodStatus());
//                tmallOrderRefund.setHasGoodReturn(item.getHasGoodReturn() == true ? 1 : 0);
//                tmallOrderRefund.setLogisticsCode(item.getSid());
//                tmallOrderRefund.setLogisticsCompany(item.getCompanyName());
//                tmallOrderRefund.setModified(DateUtil.dateToStamp(item.getModified()));
//                tmallOrderRefund.setOid(item.getOid());
////                tmallOrderRefund.setOrderStatus(item.getOrderStatus());
//                tmallOrderRefund.setReason(item.getReason());
//                tmallOrderRefund.setRefundFee(BigDecimal.valueOf(Double.parseDouble(item.getRefundFee())));
//                tmallOrderRefund.setRefundId(item.getRefundId());
//                tmallOrderRefund.setStatus(item.getStatus());
//                tmallOrderRefund.setTid(item.getTid());
////                tmallOrderRefund.setTotalFee(item.getTotalFee());
//                tmallOrderRefund.setNum(item.getNum());
//                tmallOrderRefund.setRefundPhase(item.getRefundPhase());
//                list.add(tmallOrderRefund);
//            }
//        }
//        return new TaoBaoOpenOrderUpdResult(rsp.getTotalResults().intValue(), list);
        return new TaoBaoOpenOrderUpdResult(0, new ArrayList());
    }
}
