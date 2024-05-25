package cn.qihangerp.api.domain;

import cn.qihangerp.common.annotation.Excel;
import cn.qihangerp.domain.BaseEntity;

/**
 * 商品库存明细对象 erp_goods_inventory_detail
 * 
 * @author qihang
 * @date 2024-01-09
 */
public class ErpGoodsInventoryDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 商品库存id */
    @Excel(name = "商品库存id")
    private Long inventoryId;

    /** 入库数量 */
    @Excel(name = "入库数量")
    private Long inQty;

    /** 入库前数量 */
    @Excel(name = "入库前数量")
    private Long originQty;

    /** 当前库存数量 */
    @Excel(name = "当前库存数量")
    private Long currentQty;

    /** 采购价 */
    @Excel(name = "采购价")
    private Long purPrice;

    /** 入库单id */
    @Excel(name = "入库单id")
    private String entryId;

    /** 入库单itemId */
    @Excel(name = "入库单itemId")
    private String entryItemId;

    /** 规格id */
    @Excel(name = "规格id")
    private Long specId;

    /** 商品id */
    @Excel(name = "商品id")
    private Long goodsId;

    /** 入库仓位id */
    @Excel(name = "入库仓位id")
    private Long inLocation;
    private String locationNum;

    public String getLocationNum() {
        return locationNum;
    }

    public void setLocationNum(String locationNum) {
        this.locationNum = locationNum;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setInventoryId(Long inventoryId) 
    {
        this.inventoryId = inventoryId;
    }

    public Long getInventoryId() 
    {
        return inventoryId;
    }
    public void setInQty(Long inQty) 
    {
        this.inQty = inQty;
    }

    public Long getInQty() 
    {
        return inQty;
    }
    public void setOriginQty(Long originQty) 
    {
        this.originQty = originQty;
    }

    public Long getOriginQty() 
    {
        return originQty;
    }
    public void setCurrentQty(Long currentQty) 
    {
        this.currentQty = currentQty;
    }

    public Long getCurrentQty() 
    {
        return currentQty;
    }
    public void setPurPrice(Long purPrice) 
    {
        this.purPrice = purPrice;
    }

    public Long getPurPrice() 
    {
        return purPrice;
    }
    public void setEntryId(String entryId)
    {
        this.entryId = entryId;
    }

    public String getEntryId()
    {
        return entryId;
    }
    public void setEntryItemId(String entryItemId)
    {
        this.entryItemId = entryItemId;
    }

    public String getEntryItemId()
    {
        return entryItemId;
    }
    public void setSpecId(Long specId) 
    {
        this.specId = specId;
    }

    public Long getSpecId() 
    {
        return specId;
    }
    public void setGoodsId(Long goodsId) 
    {
        this.goodsId = goodsId;
    }

    public Long getGoodsId() 
    {
        return goodsId;
    }
    public void setInLocation(Long inLocation) 
    {
        this.inLocation = inLocation;
    }

    public Long getInLocation() 
    {
        return inLocation;
    }


}
