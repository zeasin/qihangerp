package com.qihang.erp.api.controller.openApi.jd;

import lombok.Data;

import java.io.Serializable;

@Data
public class JdOrderBo implements Serializable {
    /**
     * 商品标题
     */
    private String orderId;
    private Integer shopId;
    private String orderState;
}
