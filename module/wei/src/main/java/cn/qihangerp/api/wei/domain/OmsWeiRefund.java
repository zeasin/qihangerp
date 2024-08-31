package cn.qihangerp.api.wei.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 视频号小店退款
 * @TableName oms_wei_refund
 */
@Data
public class OmsWeiRefund implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 售后单号
     */
    private String afterSaleOrderId;

    /**
     * 售后单当前状态，参考：AfterSaleStatus
     */
    private String status;

    /**
     * 买家身份标识
     */
    private String openid;

    /**
     * 买家在开放平台的唯一标识符，若当前视频号小店已绑定到微信开放平台账号下会返回
     */
    private String unionid;

    /**
     * 商品spuid
     */
    private String productId;

    /**
     * 商品skuid
     */
    private String skuId;

    /**
     * 售后数量
     */
    private Integer count;

    /**
     * 是否极速退款RefundInfo
     */
    private String fastRefund;

    /**
     * 标明售后单退款直接原因, 枚举值参考 RefundReason
     */
    private Integer refundReason;

    /**
     * 退款金额（分）
     */
    private Integer refundAmount;

    /**
     * 快递单号
     */
    private String returnWaybillId;

    /**
     * 物流公司id
     */
    private String returnDeliveryId;

    /**
     * 物流公司名称
     */
    private String returnDeliveryName;

    /**
     * 售后相关商品信息JSON
     */
    private String merchantUploadInfo;

    /**
     * 售后单创建时间戳
     */
    private Integer createTime;

    /**
     * 售后单更新时间戳
     */
    private Integer updateTime;

    /**
     * 退款原因
     */
    private String reason;

    /**
     * 退款原因解释
     */
    private String reasonText;

    /**
     * 售后类型。REFUND:退款；RETURN:退货退款。
     */
    private String type;

    /**
     * 订单号，该字段可用于获取订单
     */
    private String orderId;

    /**
     * detail json
     */
    private String details;

    /**
     * 纠纷id，该字段可用于获取纠纷信息
     */
    private String complaintId;

    /**
     * 微信支付退款的响应
     */
    private String refundResp;

    /**
     * 推送状态（0未推送；1推送成功；2推送失败）
     */
    private Integer pullStatus;

    /**
     * 确认时间
     */
    private Date pullTime;

    private static final long serialVersionUID = 1L;
}