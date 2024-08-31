package cn.qihangerp.domain;

import lombok.Data;

@Data
public class ShopSetting {
    private Long id;
    private String name;
    private String appKey;
    private String appSecret;
    private String remark;
    private String serverUrl;
    private String redirectUrl;
}
