package cn.qihangerp.open.tao.controller;

import cn.qihangerp.common.ApiResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.open.tao.RefundApiHelper;
import cn.qihangerp.open.tao.bo.TaoRequest;
import cn.qihangerp.open.tao.common.ApiResultVo;
import cn.qihangerp.open.tao.domain.OmsTaoRefund;
import cn.qihangerp.open.tao.domain.TaoOrderRefund;
import cn.qihangerp.open.tao.model.Refund;
import cn.qihangerp.open.tao.service.ITaoOrderRefundService;
import cn.qihangerp.open.tao.service.OmsTaoOrderService;
import cn.qihangerp.open.tao.service.OmsTaoRefundService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/tao-api")
public class TaoRefundApiController {
    private static Logger log = LoggerFactory.getLogger(TaoRefundApiController.class);
    @Autowired
    private ApiPullHelper apiPullHelper;
    @Autowired
    private OmsTaoRefundService refundService;
//    @Autowired
//    private ITaoOrderRefundService tmallOrderReturnService;
    /**
     * 更新退货订单
     *
     * @param
     * @param
     * @return
     * @throws
     */
    @RequestMapping("/refund/pull_refund")
    @ResponseBody
    public ApiResult<String> refundOrderPull(@RequestBody TaoRequest taoRequest) throws InterruptedException {
        log.info("/**************主动更新tao退货订单****************/");
        if (taoRequest.getShopId() == null || taoRequest.getShopId() <= 0) {
            return new ApiResult<>(ResultVoEnum.ParamsError.getIndex(), "参数错误，没有店铺Id");
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


        Long pageSize = 50l;
        Long pageIndex = 1l;
        LocalDateTime entTime = LocalDateTime.now();
        LocalDateTime startTime = entTime.minusDays(1);
        int insertSuccess = 0;//新增成功的订单
        int totalError = 0;
        int hasExistOrder = 0;//已存在的订单数
        ApiResultVo<Refund> refundApiResultVo = RefundApiHelper.pullRefund(startTime, entTime, appKey, appSecret, sessionKey);
        if(refundApiResultVo.getCode()==0){

            for (var refund : refundApiResultVo.getList()) {
                OmsTaoRefund taoRefund = new OmsTaoRefund();
                BeanUtils.copyProperties(refund,taoRefund);
                taoRefund.setShopId(taoRequest.getShopId());
                taoRefund.setDesc1(refund.getDesc());
                ResultVo<Integer> resultVo = refundService.saveAndUpdateRefund(taoRequest.getShopId(), taoRefund);

                //插入订单数据
//                var result = tmallOrderReturnService.updOrderRefund(shopId, order);
                if (resultVo.getMsg().equals("更新成功")) {
                    //已经存在
                    log.info("/**************主动更新tao退货订单：开始更新数据库：" + taoRefund.getRefundId() + "存在、更新****************/");
                    hasExistOrder++;
                } else if (resultVo.getMsg().equals("新增成功")) {
                    log.info("/**************主动更新tao退货订单：开始更新数据库：" + taoRefund.getRefundId() + "不存在、新增****************/");
                    insertSuccess++;
                } else {
                    log.info("/**************主动更新tao退货订单：开始更新数据库：" + taoRefund.getRefundId() + "报错****************/");
                    totalError++;
                }
            }
        }else{
            log.info("/**************主动更新tao退货订单：获取结果失败：" + refundApiResultVo.getMsg() + "****************/");
            return new ApiResult<>(ResultVoEnum.SystemException.getIndex(), refundApiResultVo.getMsg());
        }
        String msg = "成功，总共找到：" + refundApiResultVo.getTotalRecords() + "条订单，新增：" + insertSuccess + "条，添加错误：" + totalError + "条，更新：" + hasExistOrder + "条";
//        log.info("/**************主动更新tao订单：END：" + msg + "****************/");
        return new ApiResult<>(ResultVoEnum.SUCCESS.getIndex(), msg);
    }

    /**
     * 更新单条退货单
     *
     * @param taoRequest
     * @param
     * @param
     * @return
     * @throws
     */
    @RequestMapping("/refund/pull_refund_detail")
    @ResponseBody
    public ApiResult<String> refundOrderPullByNum(@RequestBody TaoRequest taoRequest) throws InterruptedException {
        log.info("/**************主动更新tao退货订单by number****************/");
        if (taoRequest.getShopId() == null || taoRequest.getShopId() <= 0) {
            return new ApiResult<>(ResultVoEnum.ParamsError.getIndex(), "参数错误，没有店铺Id");
        }
        if (taoRequest.getRefundId() == null || taoRequest.getRefundId() <= 0) {
            return new ApiResult<>(ResultVoEnum.ParamsError.getIndex(), "参数错误，缺少退款单号");
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
        ApiResultVo<Refund> refundApiResultVo = RefundApiHelper.pullRefundDetail(taoRequest.getRefundId(), appKey, appSecret, sessionKey);
        if(refundApiResultVo.getCode()==0) {
            OmsTaoRefund taoRefund = new OmsTaoRefund();
            BeanUtils.copyProperties(refundApiResultVo.getData(), taoRefund);
            ResultVo<Integer> resultVo = refundService.saveAndUpdateRefund(taoRequest.getShopId(), taoRefund);

            return ApiResult.success();
        }else{
            return new ApiResult<>(ResultVoEnum.SystemException.getIndex(), refundApiResultVo.getMsg());
        }
    }


}
