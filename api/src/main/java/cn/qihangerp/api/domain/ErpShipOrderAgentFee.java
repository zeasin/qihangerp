package cn.qihangerp.api.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 供应商代发账单
 * @TableName erp_ship_order_agent_fee
 */
@Data
public class ErpShipOrderAgentFee implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 订单号
     */
    private String orderNum;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 供应商id
     */
    private Long supplierId;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 日期
     */
    private Date date;

    /**
     * 物流公司
     */
    private String logisticsCompany;

    /**
     * 物流单号
     */
    private String logisticsCode;

    /**
     * 应付总金额
     */
    private Float totalAmount;

    /**
     * 物流费用
     */
    private Float shipAmount;

    /**
     * 商品金额
     */
    private Float goodsAmount;

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

    private static final long serialVersionUID = 1L;
}