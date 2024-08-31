package cn.qihangerp.api.dou.controller;

import cn.qihangerp.api.dou.*;
import cn.qihangerp.api.dou.domain.OmsDouOrder;
import cn.qihangerp.api.dou.domain.OmsDouOrderItem;
import cn.qihangerp.api.dou.service.OmsDouOrderService;
import cn.qihangerp.common.ApiResult;
import cn.qihangerp.common.constant.HttpStatus;
import cn.qihangerp.common.utils.DateUtil;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.open.dou.DouOrderApiHelper;
import cn.qihangerp.open.dou.DouTokenApiHelper;
import cn.qihangerp.open.dou.common.ApiResultVo;
import cn.qihangerp.open.dou.model.Token;
import cn.qihangerp.open.dou.model.order.Order;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import cn.qihangerp.common.ResultVoEnum;
import jakarta.servlet.http.HttpServletRequest;

import java.net.http.HttpResponse;
import java.util.*;

@AllArgsConstructor
@RequestMapping("/dou-api/order")
@RestController
public class DouOrderApiController {
    private static Logger logger = LoggerFactory.getLogger(DouOrderApiController.class);
    private final DouApiHelper douApiHelper;
    private final OmsDouOrderService orderService;

    @RequestMapping(value = "/pull_order", method = RequestMethod.POST)
    public AjaxResult getOrderList(@RequestBody DouRequest req) throws Exception {
        if (req.getShopId() == null || req.getShopId() <= 0) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
        }
        var checkResult = douApiHelper.checkBefore(req.getShopId());
        if (checkResult.getResult() != ResultVoEnum.SUCCESS.getIndex()) {
            return AjaxResult.error(checkResult.getResult(), checkResult.getMsg(), checkResult.getData());
        }
        String accessToken = checkResult.getData().getAccessToken();
        String url = checkResult.getData().getServerUrl();
        String appKey = checkResult.getData().getAppKey();
        String appSercet = checkResult.getData().getAppSecret();

        ApiResultVo<Token> token = DouTokenApiHelper.getToken(appKey, appSercet,checkResult.getData().getSellerShopId());
        if(token.getCode()==0) {
            accessToken = token.getData().getAccessToken();
        }else{
            return AjaxResult.error(token.getMsg());
        }

        Long endTime = System.currentTimeMillis() / 1000;//订单更新结束时间
        Long startTime = endTime-(60 * 60 * 24 * 7);//订单更新开始时间

        int insertSuccess = 0;//新增成功的订单
        int totalError = 0;
        int hasExistOrder = 0;//已存在的订单数
        ApiResultVo<Order> apiResultVo = DouOrderApiHelper.pullOrderList(startTime, endTime, 0, 50, appKey, appSercet, accessToken);
        if (apiResultVo.getCode() != 0) return AjaxResult.error(apiResultVo.getCode(), apiResultVo.getMsg());

        Integer total  = apiResultVo.getTotalRecords();
        //成功
        if (apiResultVo.getList() != null) {
            for (var gitem:apiResultVo.getList()) {
                OmsDouOrder douOrder = new OmsDouOrder();
                BeanUtils.copyProperties(gitem, douOrder);
                douOrder.setOrderPhaseList(JSONObject.toJSONString(gitem.getOrderPhaseList()));
                douOrder.setEncryptPostAddress(gitem.getPostAddr().getEncryptDetail());
                douOrder.setProvinceName(gitem.getPostAddr().getProvince().getName());
                douOrder.setProvinceId(gitem.getPostAddr().getProvince().getId());
                douOrder.setCityName(gitem.getPostAddr().getCity().getName());
                douOrder.setCityId(gitem.getPostAddr().getCity().getId());
                douOrder.setTownName(gitem.getPostAddr().getTown().getName());
                douOrder.setTownId(gitem.getPostAddr().getTown().getId());
                douOrder.setStreetName(gitem.getPostAddr().getStreet().getName());
                douOrder.setStreetId(gitem.getPostAddr().getStreet().getId());
                douOrder.setMaskPostAddress(gitem.getMaskPostAddr().getDetail());
                douOrder.setLogisticsInfo(JSONObject.toJSONString(gitem.getLogisticsInfo()));
                List<OmsDouOrderItem> items = new ArrayList<>();
                if (gitem.getSkuOrderList() != null) {
                    for (var i : gitem.getSkuOrderList()) {
                        OmsDouOrderItem item = new OmsDouOrderItem();
                        BeanUtils.copyProperties(i, item);
                        item.setAfterSaleStatus(i.getAfterSaleInfo().getAfterSaleStatus());
                        item.setAfterSaleType(i.getAfterSaleInfo().getAfterSaleType());
                        item.setRefundStatus(i.getAfterSaleInfo().getRefundStatus());
                        item.setSpec(JSONObject.toJSONString(i.getSpec()));
                        items.add(item);
                    }
                    douOrder.setItems(items);
                }
                //插入订单数据
                var result = orderService.saveOrder(req.getShopId(), douOrder);
                if (result.getCode() == ResultVoEnum.DataExist.getIndex()) {
                    //已经存在
                    logger.info("/**************主动更新pdd订单：开始更新数据库：" + douOrder.getId() + "存在、更新****************/");

                    hasExistOrder++;
                } else if (result.getCode() == ResultVoEnum.SUCCESS.getIndex()) {
                    logger.info("/**************主动更新pdd订单：开始更新数据库：" + douOrder.getId() + "不存在、新增****************/");

                    insertSuccess++;
                } else {
                    logger.info("/**************主动更新pdd订单：开始更新数据库：" + douOrder.getId() + "报错****************/");
                    totalError++;
                }
            }
        }
        String msg = "成功，总共找到：" + apiResultVo.getList().size() + "条商品数据，新增：" + insertSuccess + "条，添加错误：" + totalError + "条，更新：" + hasExistOrder + "条";
        logger.info(msg);
        return AjaxResult.success(msg);
    }

}
