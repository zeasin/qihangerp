package cn.qihangerp.api.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 订单发货物流费用
 * @TableName erp_ship_order_fee
 */
@Data
public class ErpShipOrderFee implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 物流公司
     */
    private String logisticsCompany;

    /**
     * 物流公司id
     */
    private String logisticsCompanyId;

    /**
     * 物流单号
     */
    private String logisticsNum;

    /**
     * 订单号
     */
    private String orderNum;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 应付金额
     */
    private Float amount;

    /**
     * 应付日期
     */
    private Date date;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态（0已生成1已结算)
     */
    private Integer status;

    /**
     * 订单创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 长
     */
    private Float length;

    /**
     * 宽
     */
    private Float width;

    /**
     * 高
     */
    private Float height;

    /**
     * 重量
     */
    private Float weight;

    /**
     * 收件人姓名
     */
    private String receiverName;

    /**
     * 收件人手机号
     */
    private String receiverPhone;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String town;

    private static final long serialVersionUID = 1L;
}