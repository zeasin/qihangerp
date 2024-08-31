package cn.qihangerp.api.wei.domain;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName oms_wei_goods_sku
 */
@Data
public class OmsWeiGoodsSku implements Serializable {
    /**
     * 
     */
    private String id;

    /**
     *
     */
    private String productId;
    private Long shopId;

    /**
     * skuID
     */
    private String skuId;

    /**
     * 商家自定义skuID。如果添加时没录入，回包可能不包含该字段
     */
    private String outSkuId;

    /**
     * sku小图
     */
    private String thumbImg;
    private String title;

    /**
     * 售卖价格，以分为单位
     */
    private Integer salePrice;

    /**
     * sku库存
     */
    private Integer stockNum;

    /**
     * sku编码
     */
    private String skuCode;

    /**
     * sku状态
     */
    private Integer status;

    /**
     * sku_attrs
     */
    private String skuAttrs;
    private String skuAttr;

    /**
     * sku_deliver_info
     */
    private String skuDeliverInfo;

    /**
     * erp系统商品id
     */
    private Long erpGoodsId;

    /**
     * erp系统商品skuid
     */
    private Long erpGoodsSkuId;

    private static final long serialVersionUID = 1L;
}