package cn.qihangerp.api.jd.controller;

import cn.qihangerp.api.jd.bo.PullRequest;
import cn.qihangerp.common.constant.HttpStatus;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.open.jd.OrderApiHelper;
import cn.qihangerp.open.jd.common.ApiResultVo;
import cn.qihangerp.open.jd.model.OrderInfo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

@RequestMapping("/jd-api/order")
@RestController
@AllArgsConstructor
public class JdOrderApiController {
//    private final ApiCommon apiCommon;
////    private final RedisCache redisCache;
//    private final MqUtils mqUtils;
//    private final JdOrderService orderService;
//    private final SysShopPullLasttimeService pullLasttimeService;
//    private final SysShopPullLogsService pullLogsService;

    @RequestMapping(value = "/pull_order_jd", method = RequestMethod.POST)
    public AjaxResult pullList(@RequestBody PullRequest params) throws Exception {
        if (params.getShopId() == null || params.getShopId() <= 0) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
        }

        Date currDateTime = new Date();
        long beginTime = System.currentTimeMillis();

//        var checkResult = apiCommon.checkBefore(params.getShopId());
//        if (checkResult.getCode() != HttpStatus.SUCCESS) {
//            return AjaxResult.error(checkResult.getCode(), checkResult.getMsg(),checkResult.getData());
//        }
//        String accessToken = checkResult.getData().getAccessToken();
//        String serverUrl = checkResult.getData().getServerUrl();
//        String appKey = checkResult.getData().getAppKey();
//        String appSecret = checkResult.getData().getAppSecret();
//
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
        String appKey = "";
        String appSecret ="";
        String accessToken = "";
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(1);
        //第一次获取
        ApiResultVo<OrderInfo> upResult = OrderApiHelper.pullOrder(startTime,endTime,appKey,appSecret,accessToken);
        int insertSuccess = 0;//新增成功的订单
        int totalError = 0;
        int hasExistOrder = 0;//已存在的订单数
        //循环插入订单数据到数据库
//        for (var order : upResult.getList()) {
//            //插入订单数据
//            var result = orderService.saveOrder(params.getShopId(), order);
//            if (result.getCode() == ResultVoEnum.DataExist.getIndex()) {
//                //已经存在
//                hasExistOrder++;
//                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.JD,MqType.ORDER_MESSAGE,order.getOrderId()));
//            } else if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
//                insertSuccess++;
//                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.JD,MqType.ORDER_MESSAGE,order.getOrderId()));
//            } else {
//                totalError++;
//            }
//        }
//        if(lasttime == null){
//            // 新增
//            SysShopPullLasttime insertLasttime = new SysShopPullLasttime();
//            insertLasttime.setShopId(params.getShopId());
//            insertLasttime.setCreateTime(new Date());
//            insertLasttime.setLasttime(endTime);
//            insertLasttime.setPullType("ORDER");
//            pullLasttimeService.save(insertLasttime);
//
//        }else {
//            // 修改
//            SysShopPullLasttime updateLasttime = new SysShopPullLasttime();
//            updateLasttime.setId(lasttime.getId());
//            updateLasttime.setUpdateTime(new Date());
//            updateLasttime.setLasttime(endTime);
//            pullLasttimeService.updateById(updateLasttime);
//        }
//        String resultStr ="{insertSuccess:"+insertSuccess+",hasExistOrder:"+hasExistOrder+",totalError:"+totalError+"}";
//        SysShopPullLogs logs = new SysShopPullLogs();
//        logs.setShopType(EnumShopType.JD.getIndex());
//        logs.setShopId(params.getShopId());
//        logs.setPullType("ORDER");
//        logs.setPullWay("主动拉取");
//        logs.setPullParams("{startTime:"+startTime+",endTime:"+endTime+"}");
//        logs.setPullResult(resultStr);
//        logs.setPullTime(currDateTime);
//        logs.setDuration(System.currentTimeMillis() - beginTime);
//        pullLogsService.save(logs);
        return AjaxResult.success();
    }

//    /**
//     * 拉取详情
//     * @param params
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = "/pull_order_detail", method = RequestMethod.POST)
//    public AjaxResult pullDetail(@RequestBody PullRequest params) throws Exception {
//        if (params.getShopId() == null || params.getShopId() <= 0) {
//            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
//        }
//        if (StringUtils.isEmpty(params.getOrderId())) {
//            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，缺少orderId");
//        }
//        Date currDateTime = new Date();
//        long beginTime = System.currentTimeMillis();
//
//        var checkResult = apiCommon.checkBefore(params.getShopId());
//        if (checkResult.getCode() != HttpStatus.SUCCESS) {
//            return AjaxResult.error(checkResult.getCode(), checkResult.getMsg(),checkResult.getData());
//        }
//        String accessToken = checkResult.getData().getAccessToken();
//        String serverUrl = checkResult.getData().getServerUrl();
//        String appKey = checkResult.getData().getAppKey();
//        String appSecret = checkResult.getData().getAppSecret();
//        ResultVo<JdOrder> upResult = OrderApiHelper.pullOrderDetail(Long.parseLong(params.getOrderId()),serverUrl,appKey,appSecret,accessToken);
//        if(upResult.getCode() == ResultVoEnum.SUCCESS.getIndex()){
//            // 更新Order
//            var result = orderService.saveOrder(params.getShopId(), upResult.getData());
//            if (result.getCode() == ResultVoEnum.DataExist.getIndex()) {
//                //已经存在
//                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.JD,MqType.ORDER_MESSAGE,upResult.getData().getOrderId()));
//            } else if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
//                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.JD,MqType.ORDER_MESSAGE,upResult.getData().getOrderId()));
//            }
//            return AjaxResult.success();
//        }else{
//            return AjaxResult.error();
//        }
//    }
}


