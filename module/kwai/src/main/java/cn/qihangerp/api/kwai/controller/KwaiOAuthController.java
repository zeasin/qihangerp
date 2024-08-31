package cn.qihangerp.api.kwai.controller;

import cn.qihangerp.common.utils.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * 描述：
 * 阿里授权Controller
 *
 * @author qlp
 * @date 2019-09-12 14:13
 */
@RequestMapping("/kwai_api")
@Controller
public class KwaiOAuthController {
//    @Autowired
//    private SysThirdSettingService thirdSettingService;
    private static  String clientId="ks701717119425407331";
    private static  String clientSecret="0qBrIYOPrqGb8SM9ouVx9w";
    private static Logger log = LoggerFactory.getLogger(KwaiOAuthController.class);


    /**
     * 调取授权页面
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @RequestMapping("/callback")
    public String aliOAuth(HttpServletRequest req) throws IOException, InterruptedException {
        Integer shopId =13;
        String grantCode = req.getParameter("code");
        System.out.println(grantCode);
        String url = "https://openapi.kwaixiaodian.com/oauth2/access_token";
        String appKey="ks700872692254768517";
        String appSecret = "7Bmb4KSuo3SB9sX7JNUETQ";

        Map<String, String> params = new HashMap<>();
        params.put("appkey", appKey);
        params.put("app_secret", appSecret);
        params.put("grant_type", "code");
        params.put("code", grantCode);

        // 组合url参数
        StringJoiner joiner = new StringJoiner("&");
        params.forEach((key, value) -> joiner.add(key + "=" + value));
        String urlP = joiner.toString();
        url = url + "?" + urlP;
        // 调用接口
        TokenApiService remoting = RemoteUtil.Remoting(url, TokenApiService.class);
        String resultString = remoting.getToken();
        JSONObject result = JSONObject.parseObject(resultString);

//        OauthAccessTokenKsClient oauthAccessTokenKsClient = new OauthAccessTokenKsClient(clientId, clientSecret);
//        try {
//            KsAccessTokenResponse response = oauthAccessTokenKsClient.getAccessToken(grantCode);
//            Long expireIn=response.getExpiresIn();
//            thirdSettingService.updateEntity(shopId, response.getAccessToken(), response.getRefreshToken(),expireIn.intValue(),response.getRefreshToken());
//        } catch (KsMerchantApiException e) {
//            e.printStackTrace();
//        }
        return "redirect:/";
    }

    @RequestMapping("/oauth")
    public String callback(HttpServletRequest request) throws IOException, InterruptedException {
//        Integer shopTypeId = EnumShopType.Kwai.getIndex();
//        var entity = thirdSettingService.getEntity(shopTypeId);
        String redirect_uri = "http://localhost:8099/kwai_api/callback";
        String appId = "ks701717119425407331";
        appId="ks700872692254768517";


        String url = "https://s.kwaixiaodian.com/oauth/authorize?response_type=code&app_id="+appId+"&scope=user_info,merchant_item,merchant_order,merchant_refund&redirect_uri="+redirect_uri+"&state=ORDER_LIST";

        return "redirect:" + url;
    }
}
