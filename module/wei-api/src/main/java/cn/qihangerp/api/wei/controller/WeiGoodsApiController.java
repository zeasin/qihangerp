package cn.qihangerp.api.wei.controller;


import cn.qihangerp.api.wei.bo.PullRequest;
import cn.qihangerp.api.wei.domain.OmsWeiGoods;
import cn.qihangerp.api.wei.domain.OmsWeiGoodsSku;
import cn.qihangerp.api.wei.service.OmsWeiGoodsService;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.constant.HttpStatus;
import cn.qihangerp.common.utils.bean.BeanUtils;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.open.wei.GoodsApiHelper;
import cn.qihangerp.open.wei.common.ApiResultVo;
import cn.qihangerp.open.wei.model.Product;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@RequestMapping("/wei-api/goods")
@RestController
@AllArgsConstructor
public class WeiGoodsApiController extends BaseController {
    private final WeiApiCommon apiCommon;
    private final OmsWeiGoodsService weiGoodsService;

    @RequestMapping(value = "/pull_goods_list", method = RequestMethod.POST)
    public AjaxResult pullList(@RequestBody PullRequest params) throws Exception {
        if (params.getShopId() == null || params.getShopId() <= 0) {
//            return ApiResul new ApiResult(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
        }

        var checkResult = apiCommon.checkBefore(params.getShopId());
        if (checkResult.getCode() != ResultVoEnum.SUCCESS.getIndex()) {
            return AjaxResult.error(checkResult.getCode(), checkResult.getMsg(), checkResult.getData());
        }

        String accessToken = checkResult.getData().getAccessToken();
//        String appKey = checkResult.getData().getAppKey();
//        String appSecret = checkResult.getData().getAppSecret();
        ApiResultVo<Product> productApiResultVo = GoodsApiHelper.pullGoodsList(accessToken);
        if(productApiResultVo.getCode() == 0){
            if(productApiResultVo.getList()!=null) {
                // 拉取到了数据 拉取详情
                for (var product : productApiResultVo.getList()) {
                        // 保存到数据库
                        OmsWeiGoods goods = new OmsWeiGoods();
                        goods.setProductId(product.getProduct_id());
                        goods.setShopId(params.getShopId());
                        goods.setOutProductId(product.getOut_product_id());
                        goods.setTitle(product.getTitle());
                        goods.setSubTitle(product.getSub_title());
                        goods.setHeadImg(product.getHead_imgs().getString(0));
                        goods.setHeadImgs(JSONObject.toJSONString(product.getHead_imgs()));
                        goods.setDescInfo(JSONObject.toJSONString(product.getDesc_info()));
                        goods.setAttrs(JSONObject.toJSONString(product.getAttrs()));
                        goods.setStatus(product.getStatus());
                        goods.setEditStatus(product.getEdit_status());
                        goods.setMinPrice(product.getMin_price());
                        goods.setSpuCode(product.getSpu_code());
                        goods.setProductType(product.getProduct_type());
                        try {
                            goods.setEditTime(Integer.parseInt(product.getEdit_time()));
                        }catch (Exception e){

                        }
                        List<OmsWeiGoodsSku> skuList = new ArrayList<>();
                        for (var sku:product.getSkus()) {
                            OmsWeiGoodsSku goodsSku = new OmsWeiGoodsSku();
                            goodsSku.setProductId(goods.getProductId());
                            goodsSku.setSkuId(sku.getSku_id());
                            goodsSku.setOutSkuId(sku.getOut_sku_id());
                            goodsSku.setSkuCode(sku.getSku_code());
                            goodsSku.setSalePrice(sku.getSale_price());
                            goodsSku.setThumbImg(sku.getThumb_img());
                            String skuAttr="";
                            if(sku.getSku_attrs()!=null){

                                for (Object attr:sku.getSku_attrs()) {
                                    skuAttr +=  ((LinkedHashMap)attr).getOrDefault("attr_value","")+" ; ";
//                                    skuAttr += ((JSONObject)attr).getString("attr_value")+" ; ";
                                }
                            }
                            goodsSku.setSkuAttr(skuAttr);
                            goodsSku.setSkuAttrs(JSONObject.toJSONString(sku.getSku_attrs()));
                            goodsSku.setSkuDeliverInfo(JSONObject.toJSONString(sku.getSku_deliver_info()));
                            goodsSku.setStatus(sku.getStatus());
                            goodsSku.setStockNum(sku.getStock_num());
                            skuList.add(goodsSku);
                        }
                        goods.setSkuList(skuList);
                        weiGoodsService.saveAndUpdateGoods(params.getShopId(),goods);

                }
            }
        }

        return AjaxResult.success();
    }
}
