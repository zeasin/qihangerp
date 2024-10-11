package cn.qihangerp.api.pdd.controller;

import cn.qihangerp.api.pdd.bo.ShopApiParams;
import cn.qihangerp.api.pdd.service.OmsPddGoodsService;
import cn.qihangerp.common.ApiResult;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.enums.EnumShopType;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Log
@Component
public class PddApiHelper {
    @Autowired
    private OmsPddGoodsService omsPddGoodsService;


    public ApiResult<ShopApiParams> checkBefore(Long shopId) {
        log.info("/**************主动更新pdd 参数检查****************/");
        var shop = omsPddGoodsService.selectShopById(shopId);

        if (shop == null) return new ApiResult<>(ResultVoEnum.ParamsError.getIndex(), "参数错误，没有找到店铺");

        if (shop.getPlatform() != EnumShopType.Pdd.getIndex())
            return new ApiResult<>(ResultVoEnum.ParamsError.getIndex(), "参数错误，店铺不是pdd店铺");

       if(shop.getSellerShopId()==null || shop.getSellerShopId()<=0) return new ApiResult<>(ResultVoEnum.ParamsError.getIndex(), "店铺设置错误，请配置店铺的莆田shop_id");



        var thirdConfig = omsPddGoodsService.selectShopSettingById(shop.getPlatform());
        if (thirdConfig == null) return new ApiResult<>(ResultVoEnum.SystemException.getIndex(), "系统错误，没有找到第三方平台的配置信息");
        else if (StringUtils.isEmpty(thirdConfig.getAppKey()))
            return new ApiResult<>(ResultVoEnum.SystemException.getIndex(), "系统错误，第三方平台配置信息不完整，缺少appkey");
        else if (StringUtils.isEmpty(thirdConfig.getAppSecret()))
            return new ApiResult<>(ResultVoEnum.SystemException.getIndex(), "系统错误，第三方平台配置信息不完整，缺少appSecret");
        else if (StringUtils.isEmpty(thirdConfig.getServerUrl()))
            return new ApiResult<>(ResultVoEnum.SystemException.getIndex(), "系统错误，第三方平台配置信息不完整，缺少server_url");

        ShopApiParams params = new ShopApiParams();
        params.setAppKey(thirdConfig.getAppKey());
        params.setAppSecret(thirdConfig.getAppSecret());
        params.setAccessToken(shop.getAccessToken());
        params.setServerUrl(thirdConfig.getServerUrl());
        params.setRedirectUrl(thirdConfig.getRedirectUrl());

        if (!StringUtils.hasText(shop.getAccessToken()))
            return new ApiResult<>(ResultVoEnum.TokenFail.getIndex(), "Token已过期，请重新授权",params);

        return new ApiResult<>(ResultVoEnum.SUCCESS.getIndex(), "", params);
    }
}
