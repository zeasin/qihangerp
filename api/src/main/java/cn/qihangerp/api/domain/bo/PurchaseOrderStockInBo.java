package cn.qihangerp.api.domain.bo;

import cn.qihangerp.domain.BaseEntity;

import java.util.List;

/**
 * 采购订单对象 scm_purchase_order
 * 
 * @author qihang
 * @date 2023-12-29
 */
public class PurchaseOrderStockInBo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;//采购单id

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private List<PurchaseOrderStockInItemBo> goodsList;

    public List<PurchaseOrderStockInItemBo> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<PurchaseOrderStockInItemBo> goodsList) {
        this.goodsList = goodsList;
    }

}
