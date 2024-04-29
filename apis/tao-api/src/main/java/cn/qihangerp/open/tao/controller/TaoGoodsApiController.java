package cn.qihangerp.open.tao.controller;


import cn.qihangerp.common.ApiResult;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.open.tao.GoodsApiHelper;
import cn.qihangerp.open.tao.bo.TaoRequest;
import cn.qihangerp.open.tao.common.ApiResultVo;
import cn.qihangerp.open.tao.common.ApiResultVoEnum;
import cn.qihangerp.open.tao.domain.OmsTaoGoods;
import cn.qihangerp.open.tao.model.GoodsItem;
import cn.qihangerp.open.tao.service.GoodsApiService;
import cn.qihangerp.open.tao.service.OmsTaoGoodsService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import cn.qihangerp.common.constant.HttpStatus;
import java.util.Date;

@Log
@RequestMapping("/taoapi")
@RestController
@AllArgsConstructor
public class TaoGoodsApiController extends BaseController {
    private final OmsTaoGoodsService goodsService;
    private final ApiPullHelper apiPullHelper;

/**
        * @api {post} /api/v1/pull_goods 更新店铺商品列表
     * @apiVersion 1.0.0
            * @apiName pullGoods
     * @apiGroup taoGood
     * @apiParam {String}  startTime 开始时间
     * @apiParam {String}  endTime 结束时间
     * @apiParam {Number}  shopId 店铺id(东方符号：7)
     * @apiSuccessExample {json} Success-Response:
            * HTTP/1.1 200 OK{
        "code": "0成功其他失败",
                "msg": "成功或失败信息"
    }
     */
    @RequestMapping(value = "/goods/pull_goods", method = RequestMethod.POST)
    public AjaxResult pullGoodsList(@RequestBody TaoRequest req) throws Exception {
        if (req.getShopId() == null || req.getShopId() <= 0) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
//            return ApiResult.build(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
        }
        var checkResult = apiPullHelper.checkBefore(req.getShopId());
        if (checkResult.getCode() != ResultVoEnum.SUCCESS.getIndex()) {
            return AjaxResult.error(checkResult.getCode(), checkResult.getMsg(),checkResult.getData());
        }
        String sessionKey = checkResult.getData().getAccessToken();
        String url = checkResult.getData().getApiRequestUrl();
        String appKey = checkResult.getData().getAppKey();
        String appSecret = checkResult.getData().getAppSecret();

        int insertSuccess = 0;//新增成功的订单
        int totalError = 0;
        int hasExistOrder = 0;//已存在的订单数
        ApiResultVo<GoodsItem> goodsItemApiResultVo = GoodsApiHelper.pullGoodsList(appKey, appSecret, sessionKey);
        if(goodsItemApiResultVo.getCode() == ApiResultVoEnum.SUCCESS.getIndex()){
            //成功
            if(goodsItemApiResultVo.getList()!=null){
                for (var g:goodsItemApiResultVo.getList()) {
                    OmsTaoGoods goods = new OmsTaoGoods();
                    // TODO:转换goods
                    goods.setNumIid(g.getNum_iid());
                    goods.setTitle(g.getTitle());
                    goods.setType(g.getType());
                    goods.setCid(g.getCid());
                    goods.setPicUrl(g.getPic_url());
                    goods.setNum(g.getNum());
                    goods.setValidThru(g.getValid_thru());
                    goods.setHasDiscount(g.isHas_discount()+"");
                    goods.setHasInvoice(g.isHas_invoice()+"");


                    int result = goodsService.saveAndUpdateGoods(req.getShopId(), goods);
                    if (result == ResultVoEnum.DataExist.getIndex()) {
                        //已经存在
                        hasExistOrder++;
                    } else if (result == ResultVoEnum.SUCCESS.getIndex()) {
                        insertSuccess++;
                    }else {
                        totalError++;
                    }
                }
            }
            String msg = "成功，总共找到：" + goodsItemApiResultVo.getTotalRecords() + "条商品数据，新增：" + insertSuccess + "条，添加错误：" + totalError + "条，更新：" + hasExistOrder + "条";
            logger.info(msg);
//        return new ApiResult<>(EnumResultVo.SUCCESS.getIndex(), msg);
            return AjaxResult.success(msg);
        }else{
            return AjaxResult.error(goodsItemApiResultVo.getCode(),goodsItemApiResultVo.getMsg());
        }




    }

}
