package cn.qihangerp.api.jd.domain.bo;

import lombok.Data;

@Data
public class JdAfterSaleBo {
    private Integer shopId;
    private Integer customerExpect;
    private Long orderId;
    private String serviceId;
}
