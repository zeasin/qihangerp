package cn.qihangerp.api.dou.controller;


import cn.qihangerp.api.dou.domain.vo.ShopApiParams;
import cn.qihangerp.api.dou.service.OmsDouGoodsService;
import cn.qihangerp.common.ApiResult;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.enums.EnumShopType;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Log
@Component
public class DouApiHelper {
    @Autowired
    private OmsDouGoodsService omsPddGoodsService;


    public ApiResult<ShopApiParams> checkBefore(Long shopId) {
        log.info("/**************主动更新dou 参数检查****************/");
        var shop = omsPddGoodsService.selectShopById(shopId);

        if (shop == null) return new ApiResult<>(ResultVoEnum.ParamsError.getIndex(), "参数错误，没有找到店铺");

        if (shop.getType().intValue() != EnumShopType.DouYin.getIndex())
            return new ApiResult<>(ResultVoEnum.ParamsError.getIndex(), "参数错误，店铺不是dou店铺");

       if(shop.getSellerUserId()==null || shop.getSellerUserId()<=0) return new ApiResult<>(ResultVoEnum.ParamsError.getIndex(), "第三方平台配置错误，没有找到SellerUserId");

        var thirdConfig = omsPddGoodsService.selectShopSettingById(shop.getType());
        if (thirdConfig == null) return new ApiResult<>(ResultVoEnum.SystemException.getIndex(), "系统错误，没有找到第三方平台的配置信息");
        else if (StringUtils.isEmpty(thirdConfig.getAppKey()))
            return new ApiResult<>(ResultVoEnum.SystemException.getIndex(), "系统错误，第三方平台配置信息不完整，缺少appkey");
        else if (StringUtils.isEmpty(thirdConfig.getAppSecret()))
            return new ApiResult<>(ResultVoEnum.SystemException.getIndex(), "系统错误，第三方平台配置信息不完整，缺少appSecret");
        else if (StringUtils.isEmpty(thirdConfig.getRequestUrl()))
            return new ApiResult<>(ResultVoEnum.SystemException.getIndex(), "系统错误，第三方平台配置信息不完整，缺少request_url");

        ShopApiParams params = new ShopApiParams();
        params.setAppKey(thirdConfig.getAppKey());
        params.setAppSecret(thirdConfig.getAppSecret());
        params.setAccessToken(shop.getSessionKey());
        params.setTokenRequestUrl(thirdConfig.getRequestUrl());
        params.setApiRequestUrl(shop.getApiRequestUrl());
        params.setSellerUserId(shop.getSellerUserId());
        if (!StringUtils.hasText(shop.getSessionKey()))
            return new ApiResult<>(ResultVoEnum.TokenFail.getIndex(), "Token已过期，请重新授权",params);

        return new ApiResult<>(ResultVoEnum.SUCCESS.getIndex(), "", params);
    }
}
