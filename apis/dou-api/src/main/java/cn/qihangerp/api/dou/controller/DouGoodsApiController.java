package cn.qihangerp.api.dou.controller;

import cn.qihangerp.api.dou.*;
import cn.qihangerp.common.ApiResult;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.utils.DateUtil;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.open.dou.DouGoodsApiHelper;
import cn.qihangerp.open.dou.DouTokenApiHelper;
import cn.qihangerp.open.dou.common.ApiResultVo;
import cn.qihangerp.open.dou.model.GoodsListResultVo;
import cn.qihangerp.open.dou.model.Token;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RequestMapping("/dou-api/goods")
@RestController
public class DouGoodsApiController {
    private static Logger log = LoggerFactory.getLogger(DouGoodsApiController.class);
//    @Autowired
//    private IShopService shopService;
//    @Autowired
//    private DouyinOrderService douyinOrderService;
//
//    @Autowired
//    private ErpSalesOrderService salesOrderService;

    @RequestMapping(value = "/pull_goods", method = RequestMethod.POST)
    public AjaxResult getOrderList(DouRequest reqData) throws Exception {
        Integer updType = reqData.getUpdType();//更新类型0拉取新订单1更新订单
        String startDate = reqData.getStartDate();//reqData.getString("startTime");
        String endDate = reqData.getEndDate();//reqData.getString("endTime");

        ApiResult<ErpSalesPullCountResp> result=null;//返回结果


        Long endTime = System.currentTimeMillis() / 1000;//订单更新结束时间
        Long startTime = endTime-(60 * 60 * 24 * 7);//订单更新开始时间

//        if(updType.intValue()==0){

        if (StringUtils.isEmpty(startDate)) {
//            var pullOrderLog=salesOrderService.getErpOrderPullLogByShopId(shopId,updType);
//            if(pullOrderLog!=null) {
//                startTime = pullOrderLog.getEndTime() - 60 * 10;
//            }
        } else {
            //选择了开始日期,从开始时间直接循环更新到结束时间
            startTime = DateUtil.dateToStamp(startDate).longValue();

            if (StringUtils.isEmpty(endDate)) endTime = System.currentTimeMillis() / 1000;
            else endTime = DateUtil.dateTimeToStamp(endDate + " 23:59:00").longValue();
//        startTime = 1587966628L;endTime = startTime + 60 * 30;
        }
//        DouGoodsApiHelper.getGoodsList()
        String appKey = "";
        String appSercet="";
        Long shopId =90158786L;
        ApiResultVo<Token> token = DouTokenApiHelper.getToken(appKey, appSercet,shopId);
        if(token.getCode()==0) {
            ApiResultVo<GoodsListResultVo> goodsList = DouGoodsApiHelper.getGoodsList(appKey, appSercet, token.getData().getAccessToken(), 1, 10);
            String s="";
        }

        return AjaxResult.success();
    }


}
