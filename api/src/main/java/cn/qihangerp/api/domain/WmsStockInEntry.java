package cn.qihangerp.api.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 入库单
 * @TableName wms_stock_in_entry
 */
@Data
public class WmsStockInEntry implements Serializable {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 入库单据编号
     */
    private String stockInNum;

    /**
     * 来源单号
     */
    private String sourceNo;

    /**
     * 来源单id
     */
    private Long sourceId;

    /**
     * 来源类型（1采购订单2退货订单）
     */
    private Integer sourceType;

    /**
     * 采购订单商品数
     */
    private Integer sourceGoodsUnit;

    /**
     * 采购订单总件数
     */
    private Long sourceSpecUnitTotal;

    /**
     * 采购订单商品规格数
     */
    private Integer sourceSpecUnit;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作入库人id
     */
    private Long stockInOperatorId;

    /**
     * 操作入库人
     */
    private String stockInOperator;

    /**
     * 入库时间
     */
    private String stockInTime;

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

    @TableField(exist = false)
    private List<WmsStockInEntryItem> wmsStockInEntryItemList;

    private static final long serialVersionUID = 1L;
}