package cn.qihangerp.api.pdd.bo;

import lombok.Data;

@Data
public class PddAfterSaleBo {
    private Integer shopId;
    private Integer customerExpect;
    private Long orderId;
    private String serviceId;
}
