package cn.qihangerp.open.tao.controller;


import cn.qihangerp.common.ApiResult;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.open.tao.OrderApiHelper;
import cn.qihangerp.open.tao.bo.TaoRequest;
import cn.qihangerp.open.tao.common.ApiResultVo;
import cn.qihangerp.open.tao.domain.OmsTaoOrder;
import cn.qihangerp.open.tao.model.TradeDetail1;
import cn.qihangerp.open.tao.model.TradeList;
import cn.qihangerp.open.tao.service.OmsTaoOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 淘系订单更新
 */
@RestController
@RequestMapping("/tao-api")
public class TaoOrderApiController {
    private static Logger log = LoggerFactory.getLogger(TaoOrderApiController.class);
//    @Autowired
//    private IShopSettingService shopSettingService;
//    @Autowired
//    private ITaoOrderService tmallOrderService;

//    @Autowired
//    private IShopService shopService;
    @Autowired
    private ApiPullHelper apiPullHelper;
    @Autowired
    private OmsTaoOrderService orderService;



    /**
     * 拉取天猫订单
     *
     * @param req
     * @return
     * @throws
     */
    @GetMapping("/order/pull_order")
    @ResponseBody
    public ApiResult<Object> orderPull(TaoRequest req) throws IOException {
        log.info("/**************主动更新tao订单****************/");
        if (req.getShopId() == null || req.getShopId() <= 0) {
            return new ApiResult<>(ResultVoEnum.ParamsError.getIndex(), "参数错误，没有店铺Id");
        }
        var checkResult = apiPullHelper.checkBefore(req.getShopId());
        if (checkResult.getResult() != ResultVoEnum.SUCCESS.getIndex()) {
            return new ApiResult<>(checkResult.getResult(), checkResult.getMsg(),checkResult.getData());
        }
        String sessionKey = checkResult.getData().getAccessToken();
        String url = checkResult.getData().getServerUrl();
        String appKey = checkResult.getData().getAppKey();
        String appSecret = checkResult.getData().getAppSecret();

        log.info("/**************主动更新tao订单，条件判断完成，开始更新。。。。。。****************/");
        LocalDateTime entTime = LocalDateTime.now();
        LocalDateTime startTime = entTime.minusDays(1);

        // 从接口获取订单数据
        ApiResultVo<TradeList> tradeBeanApiResultVo = OrderApiHelper.pullTradeList(startTime,entTime,appKey, appSecret, sessionKey);
        if (tradeBeanApiResultVo.getCode()== ResultVoEnum.SUCCESS.getIndex()){
            log.info("/**************主动更新tao订单：第一次获取结果：总记录数" + tradeBeanApiResultVo.getTotalRecords() + "****************/");
            int insertSuccess = 0;//新增成功的订单
            int totalError = 0;
            int hasExistOrder = 0;//已存在的订单数

            //循环插入订单数据到数据库
            for (var trade : tradeBeanApiResultVo.getList()) {
                OmsTaoOrder order = OrderAssembleHelper.assembleOrder(trade);
                //插入订单数据
                var result = orderService.saveOrder(req.getShopId(), order);
                if (result.getCode() == ResultVoEnum.DataExist.getIndex()) {
                    //已经存在
                    log.info("/**************主动更新tao订单：开始更新数据库：" + order.getId() + "存在、更新****************/");

                    hasExistOrder++;
                } else if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
                    log.info("/**************主动更新tao订单：开始更新数据库：" + order.getId() + "不存在、新增****************/");

                    insertSuccess++;
                } else {
                    log.info("/**************主动更新tao订单：开始更新数据库：" + order.getId() + "报错****************/");
                    totalError++;
                }
            }

            String msg = "成功，总共找到：" + tradeBeanApiResultVo.getTotalRecords() + "条订单，新增：" + insertSuccess + "条，添加错误：" + totalError + "条，更新：" + hasExistOrder + "条";
            log.info("/**************主动更新tao订单：END：" + msg + "****************/");
            return new ApiResult<>(ResultVoEnum.SUCCESS.getIndex(), msg);

        }else{
            return new ApiResult<>(ResultVoEnum.SystemException.getIndex(), tradeBeanApiResultVo.getMsg());
        }
    }

    /**
     * 更新单个订单
     *
     * @param taoRequest
     * @param
     * @param
     * @return
     * @throws
     */
    @RequestMapping("/order/pull_order_detail")
    @ResponseBody
    public ApiResult<Object> getOrderPullByNum(@RequestBody TaoRequest taoRequest)  {
        log.info("/**************主动更新tao订单by number****************/");
        if (taoRequest.getShopId() == null || taoRequest.getShopId() <= 0) {
            return new ApiResult<>(ResultVoEnum.ParamsError.getIndex(), "参数错误，没有店铺Id");
        }
        if (taoRequest.getOrderId() == null || taoRequest.getOrderId() <= 0) {
            return new ApiResult<>(ResultVoEnum.ParamsError.getIndex(), "参数错误，缺少orderId");
        }

        Long shopId = taoRequest.getShopId();
        var checkResult = apiPullHelper.checkBefore(shopId);

        if (checkResult.getResult() != ResultVoEnum.SUCCESS.getIndex()) {
            return new ApiResult<>(checkResult.getResult(), checkResult.getMsg());
        }

        String sessionKey = checkResult.getData().getAccessToken();
        String url = checkResult.getData().getServerUrl();
        String appKey = checkResult.getData().getAppKey();
        String appSecret = checkResult.getData().getAppSecret();

        ApiResultVo<TradeDetail1> apiResultVo = OrderApiHelper.pullOrderDetail(taoRequest.getOrderId(), appKey, appSecret, sessionKey);

        if(apiResultVo.getCode()!=0) return ApiResult.error(apiResultVo.getMsg());
        else {
            OmsTaoOrder order = new OmsTaoOrder();
//            OmsTaoOrder order = OrderAssembleHelper.assembleOrder(trade);
            // TODO:这里将获取到的TAO订单详情转换成数据库实体，未经过测试
            BeanUtils.copyProperties(apiResultVo.getData(),order);
            var result = orderService.updateOrderStatus(order);
            if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
                log.info("/**************主动更新tao订单：更新成功****************/");
                return ApiResult.success( );
            }  else {
                log.info("/**************主动更新tao订单：开始更新数据库：" + order.getId() + "报错****************/");
                return ApiResult.error(result.getMsg() );
            }
        }
    }

}
