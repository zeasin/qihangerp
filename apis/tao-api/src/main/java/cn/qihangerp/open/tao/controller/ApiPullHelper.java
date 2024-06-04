package cn.qihangerp.open.tao.controller;

import cn.qihangerp.common.ApiResult;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.core.config.ServerConfig;
import cn.qihangerp.open.tao.service.ITaoOrderService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Log
@Component
public class ApiPullHelper {
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private ITaoOrderService tmallOrderService;


    public ApiResult<ShopApiParams> checkBefore(Long shopId) {
        log.info("/**************主动更新tao 参数检查****************/");
        var shop = tmallOrderService.selectShopById(shopId);

        if (shop == null) return new ApiResult<>(ResultVoEnum.ParamsError.getIndex(), "参数错误，没有找到店铺");

        if (shop.getPlatform().intValue() != EnumShopType.TAO.getIndex())
            return new ApiResult<>(ResultVoEnum.ParamsError.getIndex(), "参数错误，店铺不是淘系店铺");

        if(shop.getSellerShopId()==null || shop.getSellerShopId()<=0) return new ApiResult<>(ResultVoEnum.ParamsError.getIndex(), "配置错误，请在店铺信息中配置seller_shop_id");

        var thirdConfig = tmallOrderService.selectShopSettingById(shop.getPlatform());
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
        params.setRedirectUrl(thirdConfig.getRedirectUrl());
        params.setServerUrl(thirdConfig.getServerUrl());

        if (!StringUtils.hasText(shop.getAccessToken()))
            return new ApiResult<>(ResultVoEnum.TokenFail.getIndex(), "Token已过期，请重新授权",params);

        return new ApiResult<>(ResultVoEnum.SUCCESS.getIndex(), "", params);
    }
}
