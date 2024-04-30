package cn.qihangerp.open.tao.bo;

import lombok.Data;

@Data
public class TaoOrderConfirmBo {
    private String id;
    /**
     * 收货人的姓名
     */
    private String receiverName;

    /**
     * 收货人的手机号码
     */
    private String receiverMobile;

    /**
     * 收货人的电话号码
     */
    private String receiverAddress;

    /**
     * 数据库更新人
     */
    private String updateBy;

}
