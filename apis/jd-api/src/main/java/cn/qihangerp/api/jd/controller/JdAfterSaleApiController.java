package cn.qihangerp.api.jd.controller;


import cn.qihangerp.api.jd.bo.PullRequest;
import cn.qihangerp.api.jd.common.ApiCommon;
import cn.qihangerp.api.jd.domain.OmsJdAfterSale;
import cn.qihangerp.api.jd.service.OmsJdAfterSaleService;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.constant.HttpStatus;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.common.utils.DateUtils;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.open.jd.AfterSaleApiHelper;
import cn.qihangerp.open.jd.common.ApiResultVo;
import cn.qihangerp.open.jd.model.AfterSale;
import cn.qihangerp.open.jd.model.Refund;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log
@RequestMapping("/jd-api/afterSale")
@RestController
@AllArgsConstructor
public class JdAfterSaleApiController {
    private final ApiCommon apiCommon;
//    private final SysShopPullLogsService pullLogsService;
//    private final MqUtils mqUtils;
//    private final SysShopPullLasttimeService pullLasttimeService;
    private final OmsJdAfterSaleService afterSaleService;

    /**
     * 拉取售后数据
     *
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pull_list", method = RequestMethod.POST)
    public AjaxResult pullList(@RequestBody PullRequest params) throws Exception {
        if (params.getShopId() == null || params.getShopId() <= 0) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
        }
        Date currDateTime = new Date();
        long beginTime = System.currentTimeMillis();
        var checkResult = apiCommon.checkBefore(params.getShopId());
        if (checkResult.getResult() != HttpStatus.SUCCESS) {
            return AjaxResult.error(checkResult.getResult(), checkResult.getMsg(), checkResult.getData());
        }
        String accessToken = checkResult.getData().getAccessToken();
        String serverUrl = checkResult.getData().getServerUrl();
        String appKey = checkResult.getData().getAppKey();
        String appSecret = checkResult.getData().getAppSecret();
        String sellerId = checkResult.getData().getSellerId();

        // 获取最后更新时间
//        LocalDateTime startTime = null;
//        LocalDateTime endTime = null;
//        SysShopPullLasttime lasttime = pullLasttimeService.getLasttimeByShop(params.getShopId(), "REFUND");
//        if (lasttime == null) {
//            endTime = LocalDateTime.now();
//            startTime = endTime.minusDays(1);
//        } else {
//            startTime = lasttime.getLasttime().minusHours(1);//取上次结束一个小时前
//            Duration duration = Duration.between(startTime, LocalDateTime.now());
//            long hours = duration.toHours();
//            if (hours > 24) {
//                // 大于24小时，只取24小时
//                endTime = startTime.plusHours(24);
//            } else {
//                endTime = LocalDateTime.now();
//            }
////            endTime = startTime.plusDays(1);//取24小时
////            if (endTime.isAfter(LocalDateTime.now())) {
////                endTime = LocalDateTime.now();
////            }
//        }
//
////        endTime = LocalDateTime.now();
////        endTime = endTime.minusHours(12);
////        startTime = endTime.minusDays(1);

        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(1);

        String startTimeStr = startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endTimeStr = endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        //获取退款
        ApiResultVo<Refund> refundVo = AfterSaleApiHelper.pullRefundList(startTime, endTime, appKey, appSecret, accessToken);


//        List<OmsJdAfterSale> jdAfterSaleList = new ArrayList<>();

        int insertSuccess = 0;//新增成功的订单
        int totalError = 0;
        int hasExist = 0;//已存在的订单数

        //循环插入订单数据到数据库
        /*******处理取消订单*****/
        for (var item : refundVo.getList()) {
            OmsJdAfterSale afterSale = new OmsJdAfterSale();
            afterSale.setApplyId(item.getId());
            afterSale.setServiceId(item.getId());
            afterSale.setRefundId(item.getId());
            afterSale.setOrderId(Long.parseLong(item.getOrderId()));
            afterSale.setApplyTime(item.getApplyTime());
            afterSale.setCustomerExpect(1);//售前退款
            afterSale.setCustomerName("售前退款");
            afterSale.setApplyRefundSum(item.getApplyRefundSum());
            afterSale.setBuyerId(item.getBuyerId());
            afterSale.setBuyerName(item.getBuyerName());
            afterSale.setRefundCheckTime(item.getCheckTime());
            afterSale.setRefundStatus(item.getStatus());
            afterSale.setRefundCheckUsername(item.getCheckUserName());
            afterSale.setRefundCheckRemark(item.getCheckRemark());
            afterSale.setRefundReason(item.getReason());
            afterSale.setRefundSystemId(item.getSystemId());
//            jdAfterSaleList.add(afterSale);
            var result = afterSaleService.saveAfter(params.getShopId(), afterSale);
            if (result.getCode() == ResultVoEnum.DataExist.getIndex()) {
                //已经存在
                hasExist++;
//                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.JD, MqType.REFUND_MESSAGE, after.getServiceId().toString()));
            } else if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
                insertSuccess++;
//                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.JD, MqType.REFUND_MESSAGE, after.getServiceId().toString()));
            } else {
                totalError++;
            }
        }

        //获取售后
        ApiResultVo<AfterSale> afterSaleVo = AfterSaleApiHelper.pullAfterSaleList(Long.parseLong(sellerId), startTime, endTime, appKey, appSecret, accessToken);
        /*******处理售后list*****/
        for (var after : afterSaleVo.getList()) {
            OmsJdAfterSale afterSale = new OmsJdAfterSale();
            BeanUtils.copyProperties(after, afterSale);
//            afterSale.setOrderId(after.getOrderId() + "");
            afterSale.setApplyTime(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", new Date(after.getApplyTime())));
            afterSale.setRefundId(0L);
            afterSale.setShopId(params.getShopId());
            var result = afterSaleService.saveAfter(params.getShopId(), afterSale);
            if (result.getCode() == ResultVoEnum.DataExist.getIndex()) {
                //已经存在
                hasExist++;
//                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.JD, MqType.REFUND_MESSAGE, after.getServiceId().toString()));
            } else if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
                insertSuccess++;
//                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.JD, MqType.REFUND_MESSAGE, after.getServiceId().toString()));
            } else {
                totalError++;
            }
        }

//        if (lasttime == null) {
//            // 新增
//            SysShopPullLasttime insertLasttime = new SysShopPullLasttime();
//            insertLasttime.setShopId(params.getShopId());
//            insertLasttime.setCreateTime(new Date());
//            insertLasttime.setLasttime(endTime);
//            insertLasttime.setPullType("REFUND");
//            pullLasttimeService.save(insertLasttime);
//
//        } else {
//            // 修改
//            SysShopPullLasttime updateLasttime = new SysShopPullLasttime();
//            updateLasttime.setId(lasttime.getId());
//            updateLasttime.setUpdateTime(new Date());
//            updateLasttime.setLasttime(endTime);
//            pullLasttimeService.updateById(updateLasttime);
//        }
//        SysShopPullLogs logs = new SysShopPullLogs();
//        logs.setShopId(params.getShopId());
//        logs.setShopType(EnumShopType.JD.getIndex());
//        logs.setPullType("REFUND");
//        logs.setPullWay("主动拉取");
//        logs.setPullParams("{ApplyTimeBegin:" + startTimeStr + ",ApplyTimeEnd:" + endTimeStr + ",PageIndex:1,PageSize:100}");
//        logs.setPullResult("{total:" + insertSuccess + ",hasExist:" + hasExist + ",totalError:" + totalError + "}");
//        logs.setPullTime(currDateTime);
//        logs.setDuration(System.currentTimeMillis() - beginTime);
//        pullLogsService.save(logs);
//        mqUtils.sendApiMessage(MqMessage.build(EnumShopType.JD, MqType.REFUND_MESSAGE,item.getId()));
        String msg = "成功，总共找到：" + (refundVo.getTotalRecords()+afterSaleVo.getTotalRecords()) + "条订单，新增：" + insertSuccess + "条，添加错误：" + totalError + "条，更新：" + hasExist + "条";
        log.info("/**************主动更新jd售后：END：" + msg + "****************/");
        return AjaxResult.success(msg);
    }

}
