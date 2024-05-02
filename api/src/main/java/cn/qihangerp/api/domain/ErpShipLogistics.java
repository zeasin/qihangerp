package cn.qihangerp.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import cn.qihangerp.common.annotation.Excel;
import cn.qihangerp.domain.BaseEntity;

/**
 * 物流公司对象 b_logistics_company
 * 
 * @author qihang
 * @date 2024-01-12
 */
@TableName("erp_ship_logistics")
public class ErpShipLogistics extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 快递公司 */
    @Excel(name = "快递公司")
    private String name;

    /** 快递公司编码 */
    @Excel(name = "快递公司编码")
    private String number;
    private String remark;

    private Integer status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setNumber(String number) 
    {
        this.number = number;
    }

    public String getNumber() 
    {
        return number;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
