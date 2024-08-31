package cn.qihangerp.api.jd.controller;

import cn.qihangerp.api.jd.bo.PullRequest;
import cn.qihangerp.api.jd.common.ApiCommon;
import cn.qihangerp.api.jd.domain.OmsJdGoodsSku;
import cn.qihangerp.api.jd.service.OmsJdGoodsSkuService;
import cn.qihangerp.common.ApiResultEnum;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.constant.HttpStatus;
import cn.qihangerp.common.utils.DateUtil;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.core.page.TableDataInfo;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.open.jd.GoodsApiHelper;
import cn.qihangerp.open.jd.common.ApiResultVo;
import cn.qihangerp.open.jd.model.GoodsSku;
import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@AllArgsConstructor
@RestController
@RequestMapping("/jd-api/goods")
public class JdGoodsApiController extends BaseController {
    private final ApiCommon apiCommon;
    private final OmsJdGoodsSkuService goodsSkuService;

    @RequestMapping(value = "/pull_goods", method = RequestMethod.POST)
    public AjaxResult pullList(@RequestBody PullRequest params) throws Exception {
        if (params.getShopId() == null || params.getShopId() <= 0) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
        }
        var checkResult = apiCommon.checkBefore(params.getShopId());
        if (checkResult.getResult() != ApiResultEnum.SUCCESS.getIndex()) {
            return AjaxResult.error(checkResult.getResult(), checkResult.getMsg(),checkResult.getData());
        }
        String accessToken = checkResult.getData().getAccessToken();
        String appKey = checkResult.getData().getAppKey();
        String appSecret = checkResult.getData().getAppSecret();

        //获取接口
        ApiResultVo<GoodsSku> upResult = GoodsApiHelper.pullGoods(appKey,appSecret,accessToken);
        if(upResult.getCode() == 19) return AjaxResult.error(ApiResultEnum.TokenFail.getIndex(),"Token已过期");
        if(upResult.getCode()!=0) return AjaxResult.error(upResult.getCode(),upResult.getMsg());

        int successTotal = 0;//新增成功的订单
        int totalError = 0;
        int hasExistOrder = 0;//已存在的订单数
        for (GoodsSku sku: upResult.getList()) {
            OmsJdGoodsSku goodsSku = new OmsJdGoodsSku();
            BeanUtils.copyProperties(sku, goodsSku);
            goodsSku.setJdPrice(BigDecimal.valueOf(sku.getJdPrice()/100));
            goodsSku.setCreated( DateUtil.unixTimeToDate(sku.getCreated()/1000));
            goodsSku.setModified(DateUtil.unixTimeToDate(sku.getModified()/1000));
            goodsSku.setShopId(params.getShopId());
            if(sku.getSaleAttrs()!=null){
                goodsSku.setSaleAttrs(JSON.toJSONString(sku.getSaleAttrs()));
                String attrs = "";
                for(GoodsSku.SaleAttrsDTO dto:sku.getSaleAttrs()){
                    for(String s: dto.getAttrValueAlias()) {
                        attrs += s +";";
                    }
                }
                goodsSku.setAttrs(attrs);
            }

            goodsSkuService.saveGoodsSku(params.getShopId(), goodsSku);
            successTotal++;
            logger.info("添加商品："+successTotal);

        }
        return AjaxResult.success();
    }
}
