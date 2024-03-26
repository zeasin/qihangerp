package com.qihang.interfaces.goods.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName erp_goods_category
 */
@Data
public class ErpGoodsCategory implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 分类编码
     */
    private String number;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 
     */
    private String remark;

    /**
     * 上架分类id
     */
    private Integer parentId;

    /**
     * 分类路径
     */
    private String path;

    /**
     * 排序值
     */
    private Integer sort;

    /**
     * 图片
     */
    private String image;

    /**
     * 0正常  1删除
     */
    private Integer isdelete;

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