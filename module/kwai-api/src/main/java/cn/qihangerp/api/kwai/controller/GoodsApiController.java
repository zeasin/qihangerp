package cn.qihangerp.api.kwai.controller;

import cn.qihangerp.api.kwai.bo.PullRequest;
import cn.qihangerp.common.ApiResult;
import cn.qihangerp.common.ApiResultEnum;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.utils.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import com.kuaishou.merchant.open.api.client.AccessTokenKsMerchantClient;
import com.kuaishou.merchant.open.api.request.item.OpenItemListGetRequest;
import com.kuaishou.merchant.open.api.request.order.OpenOrderCursorListRequest;
import com.kuaishou.merchant.open.api.response.item.OpenItemListGetResponse;
import com.kuaishou.merchant.open.api.response.order.OpenOrderCursorListResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@RequestMapping("/kwai_api")
@RestController
public class GoodsApiController {

    @RequestMapping(value = "/pull_goods", method = RequestMethod.POST)
    public ApiResult<Long> getOrderList(@RequestBody PullRequest req){
        if (req.getShopId() == null || req.getShopId() <= 0) {
            return new ApiResult<>(ResultVoEnum.ParamsError.getIndex(), "参数错误，没有店铺Id");
        }


        ApiResult<Long> result=this.pullGoods("", 0L, 0L);

        return result;
    }

    public ApiResult<Long> pullGoods(String token,Long startTime,Long endTime) {
        String url = "https://openapi.kwaixiaodian.com";
        String appKey = "ks700872692254768517";
        String appSecret = "7Bmb4KSuo3SB9sX7JNUETQ";
        String signSecret = "b690afccbefc07697782cad097e51e40";
        token = "ChFvYXV0aC5hY2Nlc3NUb2tlbhJAwLbU3YZ0R7tNlyzxSJoWgIkwI_-8xYXIsE9CVIdP4lF7ZB02YVZXN7WVyIYrBRIZrjs02WHXZ4NyJQqXNPxbSBoSj7CN238WSuq-kBNyCQnaCfBmIiAMS3zl83Rrc8iWsHFNmBf8AEaJH1ZtzQrio4rtmKHbCCgFMAE";


        AccessTokenKsMerchantClient client = new AccessTokenKsMerchantClient(url, appKey, signSecret);
        OpenItemListGetRequest request = new OpenItemListGetRequest();
        request.setAccessToken(token);
        request.setApiMethodVersion(1L);

//        request.setKwaiItemId(401936319911);
//        request.setRelItemId(401936319923);
        request.setItemStatus(1);
        request.setItemType(1);
        request.setPageNumber(1);
        request.setPageSize(20);
//        request.setOnOfflineStatus(1);


        try {
            OpenItemListGetResponse response = client.execute(request);
            if (response.getResult() == 12)
                return new ApiResult<>(ApiResultEnum.TokenFail.getIndex(), "异常：Token不能为空");
            if (!StringUtils.isEmpty(response.getErrorMsg()) && response.getResult() == 24)
                return new ApiResult<>(ApiResultEnum.TokenFail.getIndex(), "异常：" + response.getErrorMsg());
            if (response.getResult() == 1) {
                if (response.getData().getItems() == null || response.getData().getItems().length == 0) {
                    return new ApiResult<>(ApiResultEnum.SUCCESS.getIndex(), "成功，没有数据", 0L);
                } else {
                    var list = response.getData().getItems();
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
}
