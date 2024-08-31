package cn.qihangerp.api.jd.controller;

import cn.qihangerp.api.jd.bo.PullRequest;
import cn.qihangerp.api.jd.common.ApiCommon;
import cn.qihangerp.api.jd.domain.OmsJdOrder;
import cn.qihangerp.api.jd.domain.OmsJdOrderItem;
import cn.qihangerp.api.jd.service.OmsJdOrderService;
import cn.qihangerp.common.ApiResultEnum;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.constant.HttpStatus;
import cn.qihangerp.common.utils.StringUtils;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.open.jd.OrderApiHelper;
import cn.qihangerp.open.jd.common.ApiResultVo;
import cn.qihangerp.open.jd.model.OrderDetail;
import cn.qihangerp.open.jd.model.OrderInfo;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log
@RequestMapping("/jd-api/order")
@RestController
@AllArgsConstructor
public class JdOrderApiController {
    private final ApiCommon apiCommon;
////    private final RedisCache redisCache;
//    private final MqUtils mqUtils;
    private final OmsJdOrderService orderService;
//    private final SysShopPullLasttimeService pullLasttimeService;
//    private final SysShopPullLogsService pullLogsService;

    @RequestMapping(value = "/pull_order_jd", method = RequestMethod.POST)
    public AjaxResult pullList(@RequestBody PullRequest params) throws Exception {
        if (params.getShopId() == null || params.getShopId() <= 0) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
        }

        Date currDateTime = new Date();
        long beginTime = System.currentTimeMillis();

        var checkResult = apiCommon.checkBefore(params.getShopId());
        if (checkResult.getResult() != ApiResultEnum.SUCCESS.getIndex()) {
            return AjaxResult.error(checkResult.getResult(), checkResult.getMsg(),checkResult.getData());
        }
        String accessToken = checkResult.getData().getAccessToken();
        String appKey = checkResult.getData().getAppKey();
        String appSecret = checkResult.getData().getAppSecret();
//        // 获取最后更新时间
//        LocalDateTime startTime = null;
//        LocalDateTime  endTime = null;
//        SysShopPullLasttime lasttime = pullLasttimeService.getLasttimeByShop(params.getShopId(), "ORDER");
//        if(lasttime == null){
//            endTime = LocalDateTime.now();
//            startTime = endTime.minusDays(1);
//        }else{
//            startTime = lasttime.getLasttime().minusHours(1);//取上次结束一个小时前
//            endTime = startTime.plusDays(1);//取24小时
//            if(endTime.isAfter(LocalDateTime.now())){
//                endTime = LocalDateTime.now();
//            }
//        }

        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(1);
        //获取
        ApiResultVo<OrderInfo> upResult = OrderApiHelper.pullOrder(startTime,endTime,appKey,appSecret,accessToken);
        if(upResult.getCode()!=0) return AjaxResult.error(upResult.getMsg());
        int insertSuccess = 0;//新增成功的订单
        int totalError = 0;
        int hasExistOrder = 0;//已存在的订单数
        //循环插入订单数据到数据库
        for (var orderInfo : upResult.getList()) {
            //插入订单数据
            OmsJdOrder order = new OmsJdOrder();
            BeanUtils.copyProperties(orderInfo, order);
            order.setFullname(orderInfo.getConsigneeInfo().getFullname());
            order.setFullAddress(orderInfo.getConsigneeInfo().getFullAddress());
            order.setTelephone(orderInfo.getConsigneeInfo().getTelephone());
            order.setMobile(orderInfo.getConsigneeInfo().getMobile());
            order.setProvince(orderInfo.getConsigneeInfo().getProvince());
            order.setProvinceId(orderInfo.getConsigneeInfo().getProvinceId());
            order.setCity(orderInfo.getConsigneeInfo().getCity());
            order.setCityId(orderInfo.getConsigneeInfo().getCityId());
            order.setTown(orderInfo.getConsigneeInfo().getTown());
            order.setTownId(orderInfo.getConsigneeInfo().getTownId());
            List<OmsJdOrderItem> itemList = new ArrayList<>();
            for(var orderInfoItem :orderInfo.getItemInfoList()) {
                OmsJdOrderItem jdOrderItem = new OmsJdOrderItem();
                BeanUtils.copyProperties(orderInfoItem, jdOrderItem);
                itemList.add(jdOrderItem);
            }
            order.setItemList(itemList);
            var result = orderService.saveOrder(params.getShopId(), order);
            if (result.getCode() == ResultVoEnum.DataExist.getIndex()) {
                //已经存在
                hasExistOrder++;
            } else if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
                insertSuccess++;
            } else {
                totalError++;
            }
        }
        String msg = "成功，总共找到：" + upResult.getTotalRecords() + "条订单，新增：" + insertSuccess + "条，添加错误：" + totalError + "条，更新：" + hasExistOrder + "条";
        log.info("/**************主动更新jd订单：END：" + msg + "****************/");
        return AjaxResult.success();
    }

    /**
     * 拉取详情
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pull_order_detail", method = RequestMethod.POST)
    public AjaxResult pullDetail(@RequestBody PullRequest params) throws Exception {
        if (params.getShopId() == null || params.getShopId() <= 0) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
        }
        if (params.getOrderId()==null || params.getOrderId()==0) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，缺少orderId");
        }
        Date currDateTime = new Date();
        long beginTime = System.currentTimeMillis();

        var checkResult = apiCommon.checkBefore(params.getShopId());
        if (checkResult.getResult() != HttpStatus.SUCCESS) {
            return AjaxResult.error(checkResult.getResult(), checkResult.getMsg(),checkResult.getData());
        }
        String accessToken = checkResult.getData().getAccessToken();
        String appKey = checkResult.getData().getAppKey();
        String appSecret = checkResult.getData().getAppSecret();

        ApiResultVo<OrderDetail> apiResultVo = OrderApiHelper.pullOrderDetail(params.getOrderId(), appKey, appSecret, accessToken);
        if(apiResultVo.getCode() == 0){
            OmsJdOrder jdOrder = new OmsJdOrder();
            BeanUtils.copyProperties(apiResultVo.getData(),jdOrder);
            jdOrder.setFullname(apiResultVo.getData().getConsigneeInfo().getFullname());
            jdOrder.setFullAddress(apiResultVo.getData().getConsigneeInfo().getFullAddress());
            jdOrder.setTelephone(apiResultVo.getData().getConsigneeInfo().getTelephone());
            jdOrder.setMobile(apiResultVo.getData().getConsigneeInfo().getMobile());
            jdOrder.setProvince(apiResultVo.getData().getConsigneeInfo().getProvince());
            jdOrder.setProvinceId(apiResultVo.getData().getConsigneeInfo().getProvinceId());
            jdOrder.setCity(apiResultVo.getData().getConsigneeInfo().getCity());
            jdOrder.setCityId(apiResultVo.getData().getConsigneeInfo().getCityId());
            jdOrder.setTown(apiResultVo.getData().getConsigneeInfo().getTown());
            jdOrder.setTownId(apiResultVo.getData().getConsigneeInfo().getTownId());
            List<OmsJdOrderItem> itemList = new ArrayList<>();
            for(var orderInfoItem :apiResultVo.getData().getItemInfoList()) {
                OmsJdOrderItem jdOrderItem = new OmsJdOrderItem();
                BeanUtils.copyProperties(orderInfoItem, jdOrderItem);
                itemList.add(jdOrderItem);
            }
            jdOrder.setItemList(itemList);

            // 更新Order
            var result = orderService.saveOrder(params.getShopId(), jdOrder);
            return AjaxResult.success();
        }else{
            return AjaxResult.error();
        }
    }
}


