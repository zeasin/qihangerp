package cn.qihangerp.api.domain.bo;

import lombok.Data;

@Data
public class RefundBo {
    private Long shopId;
    private String originalOrderId;
    private Integer status;
}
