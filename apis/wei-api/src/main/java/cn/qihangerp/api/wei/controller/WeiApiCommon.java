package cn.qihangerp.api.wei.controller;



import cn.qihangerp.api.wei.common.ShopApiParams;
import cn.qihangerp.api.wei.service.OmsWeiGoodsSkuService;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.common.constant.HttpStatus;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.open.wei.TokenApiHelper;
import cn.qihangerp.open.wei.common.ApiResultVo;
import cn.qihangerp.open.wei.vo.Token;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@AllArgsConstructor
@Component
public class WeiApiCommon {
    private final OmsWeiGoodsSkuService skuService;
    /**
     * 更新前的检查
     *
     * @param shopId
     * @return
     * @throws
     */
    public ResultVo<ShopApiParams> checkBefore(Long shopId) {
        var shop = skuService.selectShopById(shopId);
        if (shop == null) {
            return ResultVo.error(HttpStatus.PARAMS_ERROR,"参数错误，没有找到店铺");
        }
        if (shop.getType() != EnumShopType.WEI.getIndex()) {
            return ResultVo.error(HttpStatus.PARAMS_ERROR, "参数错误，店铺不是JD店铺");
        }
        if(!StringUtils.hasText(shop.getAppkey())) {
            return ResultVo.error(HttpStatus.PARAMS_ERROR, "平台配置错误，没有找到AppKey");
        }
        if(!StringUtils.hasText(shop.getAppSercet())) {
            return ResultVo.error(HttpStatus.PARAMS_ERROR, "第三方平台配置错误，没有找到AppSercet");
        }
//        var platform =skuService.selectShopSettingById(EnumShopType.WEI.getIndex());
//        if(!StringUtils.hasText(platform.getse())) {
//            return ResultVo.error(HttpStatus.PARAMS_ERROR, "第三方平台配置错误，没有找到ServerUrl");
//        }

        ShopApiParams params = new ShopApiParams();
        params.setAppKey(shop.getAppkey());
        params.setAppSecret(shop.getAppSercet());
        params.setAccessToken(shop.getSessionKey());
        params.setApiRequestUrl(shop.getApiRequestUrl());
//        params.setServerUrl(platform.getServerUrl());
        params.setSellerId(shop.getSellerUserId().toString());
        if (!StringUtils.hasText(params.getAccessToken())) {
            ApiResultVo<Token> token1 = TokenApiHelper.getToken(params.getAppKey(), params.getAppSecret());
            if(token1.getCode()==0){
                params.setAccessToken(token1.getData().getAccess_token());
                skuService.updateShopSessionByShopId(shopId, params.getAccessToken());
                return ResultVo.success(params);
            }else{
                return ResultVo.error(HttpStatus.PARAMS_ERROR, token1.getMsg());
            }
        }else {
            // 调用 店铺基本信息接口 验证Token
            ApiResultVo<Token> tokenApiResultVo = TokenApiHelper.checkToken(params.getAppKey(), params.getAppSecret(), params.getAccessToken());
            if(tokenApiResultVo.getCode()==0){
//                params.setAccessToken(tokenApiResultVo.getData().getAccess_token());
//                skuService.updateShopSessionByShopId(shopId, params.getAccessToken());
                return ResultVo.success(params);
            }else {
                String appId = "wx62136b9e9b09930b";
                String secret = "8021ff6362f6173766ef46370e9a833b";
                params.setAppKey(appId);
                params.setAppSecret(secret);
                ApiResultVo<Token> token2 = TokenApiHelper.getToken(params.getAppKey(), params.getAppSecret());
                if (token2.getCode() == 0) {
                    params.setAccessToken(token2.getData().getAccess_token());
                    skuService.updateShopSessionByShopId(shopId, params.getAccessToken());
                    return ResultVo.success(params);
                } else {
                    return ResultVo.error(HttpStatus.PARAMS_ERROR, token2.getMsg());
                }
            }
        }
    }

}
