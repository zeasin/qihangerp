package cn.qihangerp.api.jd.common;

import cn.qihangerp.api.jd.service.OmsJdGoodsSkuService;
import cn.qihangerp.common.ApiResult;
import cn.qihangerp.common.ApiResultEnum;
import cn.qihangerp.common.constant.HttpStatus;
import cn.qihangerp.common.enums.EnumShopType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@AllArgsConstructor
@Component
public class ApiCommon {
    private final OmsJdGoodsSkuService goodsSkuService;
    /**
     * 更新前的检查
     *
     * @param shopId
     * @return
     * @throws
     */
    public ApiResult<ShopApiParams> checkBefore(Long shopId) {
        var shop = goodsSkuService.selectShopById(shopId);
        if (shop == null) {
//            return new ApiResult<>(EnumResultVo.ParamsError.getIndex(), "参数错误，没有找到店铺");
            return ApiResult.error(ApiResultEnum.PARAMS_ERROR, "参数错误，没有找到店铺");
        }

        if (shop.getPlatform() != EnumShopType.JD.getIndex()) {
            return ApiResult.error(ApiResultEnum.PARAMS_ERROR, "参数错误，店铺不是JD店铺");
        }
        var platform = goodsSkuService.selectShopSettingById(shop.getPlatform());

        if (!StringUtils.hasText(platform.getAppKey())) {
            return ApiResult.error(ApiResultEnum.PARAMS_ERROR, "平台配置错误，没有找到AppKey");
        }
        if (!StringUtils.hasText(platform.getAppSecret())) {
            return ApiResult.error(ApiResultEnum.PARAMS_ERROR, "第三方平台配置错误，没有找到AppSercet");
        }
//        if (!StringUtils.hasText(platform.getRedirectUrl())) {
//            return ApiResult.error(ApiResultEnum.PARAMS_ERROR, "第三方平台配置错误，没有找到RedirectUri");
//        }
//        if (!StringUtils.hasText(platform.getServerUrl())) {
//            return ApiResult.error(ApiResultEnum.PARAMS_ERROR, "第三方平台配置错误，没有找到ServerUrl");
//        }

//        if(shop.getSellerId() == null || shop.getSellerId() <= 0) {
//            return com.qihang.tao.common.ApiResult.build(HttpStatus.PARAMS_ERROR,  "第三方平台配置错误，没有找到SellerUserId");
//        }

        ShopApiParams params = new ShopApiParams();
        params.setAppKey(platform.getAppKey());
        params.setAppSecret(platform.getAppSecret());
        params.setAccessToken(shop.getAccessToken());
        params.setRedirectUrl(platform.getRedirectUrl());
        params.setServerUrl(platform.getServerUrl());
        params.setSellerId(shop.getSellerShopId().toString());

        if (!StringUtils.hasText(shop.getAccessToken())) {

            return ApiResult.error(ApiResultEnum.TokenFail, "Token已过期，请重新授权", params);
        }


        return ApiResult.success(params);
    }

}
