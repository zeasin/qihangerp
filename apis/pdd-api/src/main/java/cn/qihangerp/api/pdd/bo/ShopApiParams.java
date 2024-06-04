package cn.qihangerp.api.pdd.bo;

import lombok.Data;

@Data
public class ShopApiParams {
    private String appKey;
    private String appSecret;
    private String accessToken;
    private String serverUrl;
    private String redirectUrl;

}
