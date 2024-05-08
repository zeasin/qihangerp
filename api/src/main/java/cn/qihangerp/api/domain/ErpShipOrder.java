package cn.qihangerp.api.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 订单发货表
 * @TableName erp_ship_order
 */
@Data
public class ErpShipOrder implements Serializable {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 店铺平台
     */
    private Integer shopType;

    /**
     * erp系统商品id
     */
    private Long supplierId;

    /**
     * 订单编号
     */
    private String orderNum;

    /**
     * erp订单ID
     */
    private Long erpOrderId;

    /**
     * erp子订单ID
     */
    private String erpOrderItemId;

    /**
     * 订单日期
     */
    private String orderTime;

    /**
     * erp系统商品id
     */
    private Long goodsId;

    /**
     * erp系统商品规格id
     */
    private Long specId;

    /**
     * 商品规格编码
     */
    private String specNum;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 说明
     */
    private String remark;

    /**
     * 物流公司
     */
    private String logisticsCompany;

    /**
     * 物流单号
     */
    private String logisticsCode;

    /**
     * 运费
     */
    private Float logisticsFee;

    /**
     * 发货时间
     */
    private String shipTime;

    /**
     * 发货人
     */
    private String shipMan;

    /**
     * 出库人
     */
    private String outOperator;

    /**
     * 出库仓位
     */
    private String outPosition;

    /**
     * 出库时间
     */
    private Date outTime;

    /**
     * 发货类型（0仓库发货；1供应商代发）
     */
    private Integer shipType;

    /**
     * 状态（1出库中2已出库3已发货）
     */
    private Integer shipStatus;

    /**
     * 包裹重量
     */
    private Double packageWeight;

    /**
     * 包裹长度
     */
    private Double packageLength;

    /**
     * 包裹宽度
     */
    private Double packageWidth;

    /**
     * 包裹高度
     */
    private Double packageHeight;

    /**
     * 打包操作人
     */
    private String packsgeOperator;

    /**
     * 打包时间
     */
    private Date packsgeTime;

    /**
     * 包裹内容JSON
     */
    private String packages;

    /**
     * 收件人姓名
     */
    private String receiverName;

    /**
     * 收件人手机号
     */
    private String receiverPhone;

    /**
     * 收件人地址
     */
    private String address;

    /**
     * 国家/地区
     */
    private String country;

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

    private static final long serialVersionUID = 1L;
}