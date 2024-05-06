package cn.qihangerp.api.wei.bo;

import lombok.Data;

@Data
public class PullRequest {
    private Long shopId;//店铺Id
    private String orderId;//订单编号
}
