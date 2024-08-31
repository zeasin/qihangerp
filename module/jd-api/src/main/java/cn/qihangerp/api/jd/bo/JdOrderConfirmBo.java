package cn.qihangerp.api.jd.bo;

import lombok.Data;

@Data
public class JdOrderConfirmBo {
    private String id;
    /**
     * 收货人的姓名
     */
    private String fullname;

    /**
     * 收货人的手机号码
     */
    private String mobile;

    /**
     * 收货人的电话号码
     */
    private String fullAddress;

    /**
     * 数据库更新人
     */
    private String updateBy;

}
