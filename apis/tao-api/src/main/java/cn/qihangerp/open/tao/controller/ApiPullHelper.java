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

        if (shop.getType().intValue() != EnumShopType.TAO.getIndex())
            return new ApiResult<>(ResultVoEnum.ParamsError.getIndex(), "参数错误，店铺不是淘系店铺");

        if(!StringUtils.hasText(shop.getAppkey())) return new ApiResult<>(ResultVoEnum.ParamsError.getIndex(), "第三方平台配置错误，没有找到AppKey");
        if(!StringUtils.hasText(shop.getAppSercet())) return new ApiResult<>(ResultVoEnum.ParamsError.getIndex(), "第三方平台配置错误，没有找到AppSercet");
        if(!StringUtils.hasText(shop.getApiRequestUrl())) return new ApiResult<>(ResultVoEnum.ParamsError.getIndex(), "第三方平台配置错误，没有找到ApiRequestUrl");
        if(shop.getSellerUserId()==null || shop.getSellerUserId()<=0) return new ApiResult<>(ResultVoEnum.ParamsError.getIndex(), "第三方平台配置错误，没有找到SellerUserId");

        ShopApiParams params = new ShopApiParams();
        params.setAppKey(shop.getAppkey());
        params.setAppSecret(shop.getAppSercet());
        params.setAccessToken(shop.getSessionKey());
        params.setTokenRequestUrl(serverConfig.getUrl()+"/taoapi2/tao_oauth");
        params.setApiRequestUrl(shop.getApiRequestUrl());
        if (!StringUtils.hasText(shop.getSessionKey()))
            return new ApiResult<>(ResultVoEnum.TokenFail.getIndex(), "Token已过期，请重新授权",params);

        String sessionKey = shop.getSessionKey();

        var thirdConfig = tmallOrderService.selectShopSettingById(shop.getType());
        if (thirdConfig == null) return new ApiResult<>(ResultVoEnum.SystemException.getIndex(), "系统错误，没有找到第三方平台的配置信息");
        else if (StringUtils.isEmpty(thirdConfig.getAppKey()))
            return new ApiResult<>(ResultVoEnum.SystemException.getIndex(), "系统错误，第三方平台配置信息不完整，缺少appkey");
        else if (StringUtils.isEmpty(thirdConfig.getAppSecret()))
            return new ApiResult<>(ResultVoEnum.SystemException.getIndex(), "系统错误，第三方平台配置信息不完整，缺少appSecret");
        else if (StringUtils.isEmpty(thirdConfig.getRequestUrl()))
            return new ApiResult<>(ResultVoEnum.SystemException.getIndex(), "系统错误，第三方平台配置信息不完整，缺少request_url");

//        thirdConfig.setAccessToken(sessionKey);

//        String url = thirdConfig.getRequestUrl();
//        String appkey = thirdConfig.getAppKey();
//        String secret = thirdConfig.getAppSecret();
        String url = shop.getApiRequestUrl();
        String appkey = shop.getAppkey();
        String secret = shop.getAppSercet();

        /****************先查询卖家对不对***************/
//        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
//        UserSellerGetRequest reqSeller = new UserSellerGetRequest();
//        reqSeller.setFields("nick,user_id");
//        UserSellerGetResponse rsp = client.execute(reqSeller, sessionKey);
//        if(StringUtils.hasText(rsp.getErrorCode())){
//            if(rsp.getErrorCode().equals("27")){
//                return new ApiResult<>(EnumResultVo.TokenFail.getIndex(), "Token已过期，请重新授权",params);
//            }
//            else if(rsp.getErrorCode().equals("11")){
//                if(rsp.getSubCode().equals("isv.permission-api-package-limit"))
//                    return new ApiResult<>(EnumResultVo.ParamsError.getIndex(), "请检查淘宝用户API：taobao.user.seller.get是否具有访问权限",params);
//                return new ApiResult<>(EnumResultVo.ParamsError.getIndex(), rsp.getSubCode(),params);
//            }
//            else if(rsp.getErrorCode().equals("25")){
//                return new ApiResult<>(EnumResultVo.ParamsError.getIndex(), "无效签名！请检查SessionKey、appKey、appSecret是否匹配",params);
//            } else
//                return new ApiResult<>(EnumResultVo.ParamsError.getIndex(), "参数错误！"+(StringUtils.hasText(rsp.getSubMsg()) ? rsp.getSubMsg(): rsp.getMsg()));
//        }
//        if(rsp.getUser() == null || rsp.getUser().getUserId() == null){
//            return new ApiResult<>(EnumResultVo.ParamsError.getIndex(), "参数错误！请设置店铺SellerUserId值！",params);
//        }
//        else if (shop.getSellerUserId().longValue() != rsp.getUser().getUserId().longValue()) {
//            return new ApiResult<>(EnumResultVo.TokenFail.getIndex(), "当前用户是：" + rsp.getUser().getNick() + "，请重新授权",params);
//        }

        return new ApiResult<>(ResultVoEnum.SUCCESS.getIndex(), "", params);
    }
}
