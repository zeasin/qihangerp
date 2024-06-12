package cn.qihangerp.api.dou.domain.bo;

import lombok.Data;

@Data
public class DouRefundBo {
    private Integer shopId;
    private Integer customerExpect;
    private Long orderId;
    private String serviceId;
}
