package cn.qihangerp.open.tao.domain.vo;

import lombok.Data;

@Data
public class ErpGoodsSpecVo {
    private Long id;
    private Long goodsId;
    private Long supplierId;
    private String specNum;
    private String colorValue;
    private String colorImage;
    private String sizeValue;
    private String styleValue;

}
