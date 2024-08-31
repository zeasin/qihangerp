package cn.qihangerp.api.kwai.controller;

import cn.qihangerp.api.kwai.bo.PullRequest;
import cn.qihangerp.common.ApiResult;
import cn.qihangerp.common.ApiResultEnum;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.utils.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import com.kuaishou.merchant.open.api.client.AccessTokenKsMerchantClient;
import com.kuaishou.merchant.open.api.request.order.OpenOrderCursorListRequest;
import com.kuaishou.merchant.open.api.response.order.OpenOrderCursorListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.util.*;

@RequestMapping("/kwai_api")
@RestController
public class OrderApiController {
    private static Logger log = LoggerFactory.getLogger(OrderApiController.class);
//    @Autowired
//    private SysThirdSettingService thirdSettingService;
//    @Autowired
//    private DcKwaiOrderService kwaiOrderService;
//    @Autowired
//    private ShopService shopService;
    private static String appKey="ks701717119425407331";
    // 对应授权商家快手账号
    private static long  sellerId = 1372638315L;

    @RequestMapping(value = "/pull_order", method = RequestMethod.POST)
    public ApiResult<Long> getOrderList(@RequestBody PullRequest req){
        if (req.getShopId() == null || req.getShopId() <= 0) {
            return new ApiResult<>(ResultVoEnum.ParamsError.getIndex(), "参数错误，没有店铺Id");
        }
//        Integer shopId = 13;
//        var shop = shopService.getShop(shopId);
//        var settingEntity = thirdSettingService.getEntity(shop.getType());
//        String accessToken = settingEntity.getAccess_token();
//        String startDate = reqData.getString("startTime");
//        String endDate = reqData.getString("endTime");


//        Long endTime = System.currentTimeMillis() / 1000;//订单更新结束时间
//        Long startTime = endTime-(60 * 60 * 24 * 1);//订单更新开始时间
//
//        if(!StringUtils.isEmpty(startDate))startTime = DateUtil.dateToStamp(startDate).longValue();
//
//        if (!StringUtils.isEmpty(endDate)) endTime = DateUtil.dateTimeToStamp(endDate + " 23:59:00").longValue();
//
//
//        long kaishidaojiesu = endTime - startTime;
//        long forSize = (kaishidaojiesu % (60 * 60 * 24) == 0) ? kaishidaojiesu / (60 * 60 * 24) : kaishidaojiesu / (60 * 60 * 24) + 1;//计算需要循环的次数
long forSize =1;
//        log.info("开始循环更新快手订单。开始时间：" + DateUtil.unixTimeStampToDate(startTime) + "结束时间：" + DateUtil.unixTimeStampToDate(endTime) + "总共循环" + forSize);
        int pageIndex = 1;
        int pageSize = 50;

        ApiResult<Long> result=null;
        for (int i = 0; i < forSize; i++) {
//            Long startTime1 = startTime + i * 60 * 60 * 24;
//            Long endTime1 = startTime1 + 60 * 60 * 24;
            result = this.pullKwaiOrder(pageIndex,pageSize,"", 0L, 0L);
            if(result.getResult()>0) return new ApiResult<>(result.getResult(), result.getMsg());
            //计算总页数
            int totalPage = (result.getData().intValue() % pageSize == 0) ? result.getData().intValue() / pageSize : (result.getData().intValue() / pageSize) + 1;

            while (pageIndex < totalPage) {
                pageIndex++;
                result = this.pullKwaiOrder(pageIndex,pageSize,"", 0L, 0L);
            }
            pageIndex=1;
        }
        return result;
    }

    public ApiResult<Long> pullKwaiOrder(Integer pageIndex,Integer pageSize,String token,Long startTime,Long endTime) {
        String url = "https://openapi.kwaixiaodian.com";
        String appKey = "ks700872692254768517";
        String appSecret = "7Bmb4KSuo3SB9sX7JNUETQ";
        String signSecret = "b690afccbefc07697782cad097e51e40";
        token = "ChFvYXV0aC5hY2Nlc3NUb2tlbhJAwLbU3YZ0R7tNlyzxSJoWgIkwI_-8xYXIsE9CVIdP4lF7ZB02YVZXN7WVyIYrBRIZrjs02WHXZ4NyJQqXNPxbSBoSj7CN238WSuq-kBNyCQnaCfBmIiAMS3zl83Rrc8iWsHFNmBf8AEaJH1ZtzQrio4rtmKHbCCgFMAE";
        Map<String, String> params = new HashMap<>();
        params.put("appkey", appKey);
        params.put("version", "1");
        params.put("access_token", token);
        params.put("timestamp", DateUtil.getCurrentDateTime());
        params.put("method", "open.order.cursor.list");
        params.put("signMethod", "HMAC_SHA256");

        Map<String, Object> p = new HashMap<>();
        p.put("orderViewStatus", 1);
        p.put("pageSize", 50);
        p.put("queryType", 2);
        p.put("beginTime", 1709222400000L);
        p.put("endTime", 1714297996000L);
        p.put("cursor", "");
        String jsonString = JSONObject.toJSONString(p);
//        String s="%7B%22orderViewStatus%22%3A1%2C%22pageSize%22%3A10%2C%22sort%22%3A1%2C%22queryType%22%3A1%2C%22beginTime%22%3A1543817629000%2C%22endTime%22%3A1543817629000%2C%22cpsType%22%3A1%2C%22cursor%22%3A%22157356441188%5F2021345676543%22%7D";
        params.put("param", jsonString);
        try {
            String sign = SignUtil.sign(SignUtil.getSignParam(params), signSecret);
            params.put("sign", sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 组合url参数
        StringJoiner joiner = new StringJoiner("&");
        params.forEach((key, value) -> joiner.add(key + "=" + URLEncoder.encode(value)));
        String urlP = joiner.toString();
        url = url + "?" + urlP;

        String serverUrl = "https://openapi.kwaixiaodian.com";
        // 调用接口
        OrderApiService remoting = RemoteUtil.Remoting(url, OrderApiService.class);
        String resultString = remoting.getOrderList();
//        JSONObject result = JSONObject.parseObject(resultString);

        AccessTokenKsMerchantClient client = new AccessTokenKsMerchantClient(serverUrl, appKey, signSecret);
        OpenOrderCursorListRequest request = new OpenOrderCursorListRequest();
        request.setAccessToken(token);
        request.setApiMethodVersion(1L);

        request.setOrderViewStatus(1);
        request.setPageSize(10);
        request.setSort(1);
        request.setQueryType(1);
        request.setBeginTime(1703715200000L);
        request.setEndTime(1714297996000L);
        request.setCpsType(1);
        request.setCursor("");
        try {
            OpenOrderCursorListResponse response = client.execute(request);
            if (response.getResult() == 12)
                return new ApiResult<>(ApiResultEnum.TokenFail.getIndex(), "异常：Token不能为空");
            if (!StringUtils.isEmpty(response.getErrorMsg()) && response.getResult() == 24)
                return new ApiResult<>(ApiResultEnum.TokenFail.getIndex(), "异常：" + response.getErrorMsg());
            if (response.getResult() == 1) {
                if (response.getData().getOrderList() == null || response.getData().getOrderList().length == 0) {
                    return new ApiResult<>(ApiResultEnum.SUCCESS.getIndex(), "成功，没有数据", 0L);
                } else {
                    var list = response.getData().getOrderList();
                    for (var obj : list) {
//                DcKwaiOrderEntity order= new DcKwaiOrderEntity();
//                var address = JsonUtil.strToObject(obj.getAddress(),DcKwaiAddressVo.class);
//                order.setOid(obj.getOid());
//                Long discountFee=obj.getDiscountFee();
//                order.setDiscountfee(obj.getDiscountFee()>0 ? new BigDecimal(discountFee/100) : new BigDecimal(0));
//                Long expressFee=obj.getExpressFee();
//                order.setExpressfee(obj.getExpressFee()>0 ? new BigDecimal(expressFee.doubleValue() /100) : new BigDecimal(0));
//                order.setNum(obj.getNum());
//                order.setConsignee(address.getConsignee());
//                order.setMobile(address.getMobile());
//                order.setPaytime(obj.getPayTime()>0 ? obj.getPayTime()/1000 : 0L);
//                order.setBuyerRemark(obj.getRemark());
//                order.setSellerRemark(obj.getSellerNoteList().toString());
//                order.setStatus(obj.getStatus());
//                order.setRefund(obj.getRefund());
//                Long totalFee=obj.getTotalFee();
//                order.setTotalfee(totalFee>0 ? new BigDecimal(totalFee.doubleValue()/100) : new BigDecimal(0));
//                order.setProvince(address.getProvince());
//                order.setCity(address.getCity());
//                order.setDistrict(address.getDistrict());
//                StringBuilder sb= new StringBuilder(address.getProvince()).append(address.getCity()).append(address.getDistrict()).append(address.getAddress());
//                order.setAddress(sb.toString());
//                order.setCreatetime(obj.getCreateTime()>0 ? obj.getCreateTime()/1000 :0L);
//                List<DcKwaiOrdersItemEntity> items = new ArrayList<>();
//                for(var kwaiItem:obj.getOrderProductInfoList()){
//                    DcKwaiOrdersItemEntity item=new DcKwaiOrdersItemEntity();
//                    item.setItemid(kwaiItem.getItemId());
//                    item.setItempicurl(kwaiItem.getItemPicUrl());
//                    Long price=kwaiItem.getPrice();
//                    item.setPrice(price>0 ? new BigDecimal(price.doubleValue()/100) : new BigDecimal(0));
//                    item.setRefundId(kwaiItem.getRefundId());
//                    item.setRefundStatus(kwaiItem.getRefundStatus());
//                    item.setSkunick(StringUtils.isEmpty(kwaiItem.getSkuNick()) ? "" : kwaiItem.getSkuNick());
//                    item.setItemtitle(kwaiItem.getItemTitle());
//                    item.setNum(kwaiItem.getNum());
//                    item.setGoodsspec(kwaiItem.getSkuDesc());
//                    items.add(item);
//                }
//                order.setItems(items);
//                var result= kwaiOrderService.editKwaiOrder(order);
//                log.info(order.getOid()+"更新:"+result.getMsg());
                    }
                    return new ApiResult<>(ApiResultEnum.SUCCESS.getIndex(), "成功", 0L);
//                return new ApiResult<>(ApiResultEnum.SUCCESS.getIndex(), "成功", response.getMerchantOrderListData().getTotalSize());
                }
            } else {
                return new ApiResult<>(ApiResultEnum.SystemException.getIndex(), "异常：" + response.getErrorMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResult<>(ApiResultEnum.Fail.getIndex(), "异常：" + e.getMessage());
        }
    }
//    /**
//     * 订单确认
//     * @return
//     */
//    @RequestMapping(value = "/affirm_order", method = RequestMethod.POST)
//    public ApiResult<Integer> orderAffirm(@RequestBody OrderConfirmReq req){
//        if (req.getOrderId() == null || req.getOrderId() <= 0)
//            return new ApiResult<>(ApiResultEnum.ParamsError.getIndex(), "参数错误，缺少orderId");
//
//        if (StringUtils.isEmpty(req.getClientId()))req.setClientId(0);
//
//        if (StringUtils.isEmpty(req.getReceiver()))
//            return new ApiResult<>(ApiResultEnum.ParamsError.getIndex(), "参数错误，缺少receiver");
//        if (StringUtils.isEmpty(req.getMobile()))
//            return new ApiResult<>(ApiResultEnum.ParamsError.getIndex(), "参数错误，缺少mobile");
//        if (StringUtils.isEmpty(req.getAddress()))
//            return new ApiResult<>(ApiResultEnum.ParamsError.getIndex(), "参数错误，缺少address");
//        var result = kwaiOrderService.kwaiOrderAffirm(req.getOrderId(),req.getClientId(),req.getReceiver(), req.getMobile(), req.getAddress(), req.getSellerMemo());
//        return new ApiResult<>(result.getCode(), result.getMsg());
//    }
//    /**
//     * 订单发货
//     * @param req
//     * @return
//     */
//    @RequestMapping(value = "/send_order", method = RequestMethod.POST)
//    public ApiResult<Integer> orderSend(@RequestBody DataRow req){
//        if(StringUtils.isEmpty(req.get("orderId")))return new ApiResult<>(EnumResultVo.ParamsError.getIndex(),"参数错误，订单id不能为空");
//        if(StringUtils.isEmpty(req.get("code")))return new ApiResult<>(EnumResultVo.ParamsError.getIndex(),"参数错误，快递单号不能为空");
//        Integer shopId = 13;
//        var shop = shopService.getShop(shopId);
//        var settingEntity = thirdSettingService.getEntity(shop.getType());
//        AccessTokenKsMerchantClient tokenKsMerchantClient = new AccessTokenKsMerchantClient(appKey);
//        KsMerchantOrderLogisticsUpdateRequest ksMerchantLogisticsRequest=new KsMerchantOrderLogisticsUpdateRequest();
//        try {
//            ksMerchantLogisticsRequest.setAccessToken(settingEntity.getAccess_token());
//            ksMerchantLogisticsRequest.setUid(sellerId);
//            ksMerchantLogisticsRequest.setApiMethodVersion(1);
//            ksMerchantLogisticsRequest.setOrderId(req.getLong("orderId"));
//            ksMerchantLogisticsRequest.setExpressCode(EnumKwaiExpressCodeVo.getIndex(req.getString("name")));
//            ksMerchantLogisticsRequest.setExpressNo(req.getString("code"));
//            ksMerchantLogisticsRequest.setSellerId(sellerId);
//            var ksResponse = tokenKsMerchantClient.execute(ksMerchantLogisticsRequest);
//            if(ksResponse.getResult()==1){
//                kwaiOrderService.updKwaiOrderStatus(req.getLong("orderId"));
//                return new ApiResult<>(ApiResultEnum.SUCCESS.getIndex(), "成功");
//            }else return new ApiResult<>(ApiResultEnum.Fail.getIndex(), ksResponse.getErrorMsg());
//        }catch (KsMerchantApiException e) {
//             return new ApiResult<>(ApiResultEnum.Fail.getIndex(), "异常："+e.getErrorMsg());
//        }
//    }
}
