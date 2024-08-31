package cn.qihangerp.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 订单明细表
 * @TableName erp_sale_order_item
 */
@TableName("erp_sale_order_item")
@Data
public class ErpSaleOrderItem implements Serializable {
    /**
     * id，自增
     */
    private String id;

    /**
     * 订单ID
     */
    private Long orderId;
    private Long shopId;

    /**
     * erp系统商品id
     */
    private Long goodsId;

    /**
     * erp系统商品规格id
     */
    private Long specId;

    /**
     * 商品标题
     */
    private String goodsTitle;

    /**
     * 商品图片
     */
    private String goodsImg;

    /**
     * 商品编码
     */
    private String goodsNum;

    /**
     * 商品规格
     */
    private String goodsSpec;

    /**
     * 商品规格编码
     */
    private String specNum;

    /**
     * 商品单价
     */
    private Double goodsPrice;

    /**
     * 子订单金额
     */
    private Double itemAmount;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 备注
     */
    private String remark;

    /**
     * 子订单编号(来源订单)
     */
    private String originalOrderItemId;
    private String originalOrderId;
    private String originalSkuId;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 是否赠品0否1是
     */
    private Integer isGift;

    /**
     * 已退货数量
     */
    private Integer refundCount;

    /**
     * 售后状态 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功 
     */
    private Integer refundStatus;

    /**
     * 创建时间
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

    private Integer shipType;
    private Integer shipStatus;
    private String shipTime;
    private String logisticsCode;
    private String logisticsCompany;
    private String shipMan;
    private Integer orderStatus;

    private static final long serialVersionUID = 1L;
}