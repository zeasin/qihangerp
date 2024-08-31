package cn.qihangerp.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.qihangerp.common.annotation.Excel;
import lombok.Data;

/**
 * 订单对象 erp_order
 * 
 * @author qihang
 * @date 2024-01-05
 */
@Data
public class ErpSaleOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单id，自增 */
    private Long id;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderNum;

    /** 店铺类型 */
    @Excel(name = "店铺类型")
    private Integer shopType;

    /** 店铺ID */
    @Excel(name = "店铺ID")
    private Long shopId;

    /** 买家留言信息 */
    @Excel(name = "买家留言信息")
    private String buyerMemo;
    private String sellerMemo;

    /** 标签 */
    @Excel(name = "标签")
    private String tag;

    /** 售后状态 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功 5：全部 */
    @Excel(name = "售后状态 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功 5：全部")
    private Integer refundStatus;

    /** 订单状态1：待发货，2：已发货待签收，3：已签收 */
    @Excel(name = "订单状态1：待发货，2：已发货待签收，3：已签收")
    private Integer orderStatus;

    /** 商品金额 */
    @Excel(name = "商品金额")
    private Double goodsAmount;

    /** 卖家优惠金额 */
    @Excel(name = "卖家优惠金额")
    private Double sellerDiscount;
    
    /** 卖家优惠金额 */
    @Excel(name = "平台优惠金额")
    private Double platformDiscount;

    /** 运费 */
    @Excel(name = "运费")
    private Double postage;

    /** 订单金额，单位：元 */
    @Excel(name = "订单金额，单位：元")
    private Double orderAmount;

    /** 支付金额，单位：元，支付金额=商品金额-折扣金额+邮费 */
    @Excel(name = "支付金额，单位：元，支付金额=商品金额-折扣金额+邮费")
    private Double payAmount;

    /** 收件人姓名 */
    @Excel(name = "收件人姓名")
    private String receiverName;

    /** 收件人手机号 */
    @Excel(name = "收件人手机号")
    private String receiverPhone;

    /** 收件人地址 */
    @Excel(name = "收件人地址")
    private String address;

    /** 国家/地区 */
    @Excel(name = "国家/地区")
    private String country;

    /** 省 */
    @Excel(name = "省")
    private String province;

    /** 市 */
    @Excel(name = "市")
    private String city;

    /** 区 */
    @Excel(name = "区")
    private String town;

    /** 支付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd")
    private String payTime;
    private String orderTime;

    /** 订单确认时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "订单确认时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date confirmTime;

    private Integer shipType;
    private Integer shipStatus;


    /** 发货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date shippingTime;

    /** 快递单号 */
    @Excel(name = "快递单号")
    private String shippingNumber;

    /** 物流公司 */
    @Excel(name = "物流公司")
    private String shippingCompany;
    private Float shippingCost;
    private String shippingMan;
    private Float length;
    private Float width;
    private Float height;
    private Float weight;

    /** 订单明细信息 */
    private List<ErpSaleOrderItem> itemList;
    private List<ErpSaleOrderItem> erpSaleOrderItemList;


}
