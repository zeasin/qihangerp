package cn.qihangerp.api.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 入库单明细
 * @TableName wms_stock_in_entry_item
 */
@Data
public class WmsStockInEntryItem implements Serializable {
    /**
     * 
     */
    private String id;

    /**
     * 入库单id
     */
    private String entryId;

    /**
     * 来源类型（1采购订单2退货订单）
     */
    private Integer sourceType;

    /**
     * 来源单id
     */
    private Long sourceId;

    /**
     * 来源单itemId
     */
    private Long sourceItemId;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品编码
     */
    private String goodsNum;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品规格id
     */
    private Long specId;

    /**
     * 商品规格编码
     */
    private String specNum;

    /**
     * 颜色
     */
    private String colorValue;

    /**
     * 图片
     */
    private String colorImage;

    /**
     * 尺码
     */
    private String sizeValue;

    /**
     * 款式
     */
    private String styleValue;

    /**
     * 原始数量
     */
    private Long originalQuantity;

    /**
     * 入库数量
     */
    private Long inQuantity;
    @TableField(exist = false)
    private Long quantity;

    /**
     * 备注
     */
    private String remark;

    /**
     * 入库仓位
     */
    private Long locationId;

    /**
     * 入库仓位编码
     */
    private String locationNum;

    /**
     * 状态（0待入库1部分入库2全部入库）
     */
    private Integer status;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}