package com.qihang.erp.api.controller.tao;

import java.math.BigDecimal;

/**
 * 描述：天猫订单excel（适用于淘宝开放平台服务市场订单导出应用）
 *
 * @author qlp
 * @date 2019-10-23 09:13
 */
public class OrderImportItem {

    private String orderNum;
    private String subOrderNum;
    private String goodsTitle;//宝贝名称
    private String goodsNumber;//商家编码
    private String goodsImg;//主图productImgUrl
    private Integer goodsId;//商品数字ID,天猫店铺的商品id
    private Integer specId;//SKUID,天猫店铺的skuid
    private String specNumber;//属性商家编码
    private String skuInfo;//商品规格属性信息
    private String color;
    private String size;
    private BigDecimal price;//单价
    private BigDecimal payAmount;//实际支付金额
    private Long quantity;//数量

    
    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getSubOrderNum() {
        return subOrderNum;
    }

    public void setSubOrderNum(String subOrderNum) {
        this.subOrderNum = subOrderNum;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(String goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getSpecId() {
        return specId;
    }

    public void setSpecId(Integer specId) {
        this.specId = specId;
    }

    public String getSpecNumber() {
        return specNumber;
    }

    public void setSpecNumber(String specNumber) {
        this.specNumber = specNumber;
    }

    public String getSkuInfo() {
        return skuInfo;
    }

    public void setSkuInfo(String skuInfo) {
        this.skuInfo = skuInfo;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }
}
