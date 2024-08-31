package cn.qihangerp.api.jd.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 京东商品SKU表
 * @TableName oms_jd_goods_sku
 */
@Data
public class OmsJdGoodsSku implements Serializable {
    /**
     * 
     */
    private String id;

    /**
     * 
     */
    private Long wareId;

    /**
     * 
     */
    private Long skuId;

    /**
     * 
     */
    private Integer status;

    /**
     * 
     */
    private BigDecimal jdPrice;

    /**
     * 
     */
    private String outerId;

    /**
     * 
     */
    private String barCode;

    /**
     * 
     */
    private String logo;

    /**
     * 
     */
    private String saleAttrs;

    /**
     * sku属性名称
     */
    private String attrs;

    /**
     * 
     */
    private String skuName;

    /**
     * 
     */
    private Integer stockNum;

    /**
     * 
     */
    private Date modified;

    /**
     * 
     */
    private Date created;

    /**
     * 
     */
    private String currencySpuId;

    /**
     * erp商品id
     */
    private Long erpGoodsId;

    /**
     * erp商品sku id
     */
    private Long erpGoodsSkuId;
    private Long shopId;


    private static final long serialVersionUID = 1L;
}