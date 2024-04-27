package cn.qihangerp.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.qihangerp.api.domain.ScmPurchaseOrder;
import cn.qihangerp.api.domain.WmsStockInEntry;
import cn.qihangerp.api.domain.WmsStockInEntryItem;
import cn.qihangerp.api.domain.bo.PurchaseOrderStockInBo;
import cn.qihangerp.api.mapper.ScmPurchaseOrderMapper;
import cn.qihangerp.api.mapper.WmsStockInEntryMapper;
import cn.qihangerp.common.utils.DateUtils;
import cn.qihangerp.api.service.WmsStockInEntryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.qihangerp.api.mapper.ScmPurchaseOrderShipMapper;
import cn.qihangerp.api.domain.ScmPurchaseOrderShip;
import cn.qihangerp.api.service.IScmPurchaseOrderShipService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 采购订单物流Service业务层处理
 * 
 * @author qihang
 * @date 2023-12-30
 */
@Service
public class ScmPurchaseOrderShipServiceImpl implements IScmPurchaseOrderShipService 
{
    @Autowired
    private ScmPurchaseOrderShipMapper scmPurchaseOrderShipMapper;
    @Autowired
    private ScmPurchaseOrderMapper purchaseOrderMapper;
    @Autowired
    private WmsStockInEntryMapper stockInEntryMapper;
    @Autowired
    private WmsStockInEntryItemService stockInEntryItemService;

    /**
     * 查询采购订单物流
     * 
     * @param id 采购订单物流主键
     * @return 采购订单物流
     */
    @Override
    public ScmPurchaseOrderShip selectScmPurchaseOrderShipById(Long id)
    {
        return scmPurchaseOrderShipMapper.selectScmPurchaseOrderShipById(id);
    }

    /**
     * 查询采购订单物流列表
     * 
     * @param scmPurchaseOrderShip 采购订单物流
     * @return 采购订单物流
     */
    @Override
    public List<ScmPurchaseOrderShip> selectScmPurchaseOrderShipList(ScmPurchaseOrderShip scmPurchaseOrderShip)
    {
        return scmPurchaseOrderShipMapper.selectScmPurchaseOrderShipList(scmPurchaseOrderShip);
    }

    /**
     * 新增采购订单物流
     * 
     * @param scmPurchaseOrderShip 采购订单物流
     * @return 结果
     */
    @Override
    public int insertScmPurchaseOrderShip(ScmPurchaseOrderShip scmPurchaseOrderShip)
    {
        scmPurchaseOrderShip.setCreateTime(DateUtils.getNowDate());
        return scmPurchaseOrderShipMapper.insertScmPurchaseOrderShip(scmPurchaseOrderShip);
    }

    /**
     * 修改采购订单物流
     * 
     * @param scmPurchaseOrderShip 采购订单物流
     * @return 结果
     */
    @Transactional
    @Override
    public int updateScmPurchaseOrderShip(ScmPurchaseOrderShip scmPurchaseOrderShip)
    {
        ScmPurchaseOrderShip ship = scmPurchaseOrderShipMapper.selectScmPurchaseOrderShipById(scmPurchaseOrderShip.getId());
        if(ship== null) return -1;
        else if(ship.getStatus()!=0)return -2;
        // 更新采购单状态
        ScmPurchaseOrder order = new ScmPurchaseOrder();
        order.setId(scmPurchaseOrderShip.getId());
        order.setStatus(2);
        order.setReceivedTime(scmPurchaseOrderShip.getReceiptTime());
        order.setUpdateTime(DateUtils.getNowDate());
        order.setUpdateBy(scmPurchaseOrderShip.getUpdateBy());
        purchaseOrderMapper.updateScmPurchaseOrder(order);
        //更新
        ScmPurchaseOrderShip update = new ScmPurchaseOrderShip();
        update.setUpdateTime(DateUtils.getNowDate());
        update.setUpdateBy(scmPurchaseOrderShip.getUpdateBy());
        update.setStatus(1L);
        update.setRemark(scmPurchaseOrderShip.getRemark());
        update.setReceiptTime(scmPurchaseOrderShip.getReceiptTime());
//        update.setReceiptTime(DateUtils.getNowDate());
        update.setId(scmPurchaseOrderShip.getId());
        return scmPurchaseOrderShipMapper.updateScmPurchaseOrderShip(update);
    }

    /**
     * 创建入库单
     * @param bo
     * @return
     */
    @Transactional
    @Override
    public int createStockInEntry(PurchaseOrderStockInBo bo) {
        ScmPurchaseOrderShip ship = scmPurchaseOrderShipMapper.selectScmPurchaseOrderShipById(bo.getId());
        if(ship == null) return -1;//数据不存在
        else if(ship.getStatus().intValue() == 0) return -2;//未确认收货不允许操作
        else if(ship.getStatus().intValue() == 2) return -3;//已入库请勿重复操作
        else if (ship.getStatus().intValue() == 1) {
            WmsStockInEntry entry = new WmsStockInEntry();
            entry.setStockInNum(DateUtils.parseDateToStr("yyyyMMddHHmmss",new Date()));
            entry.setSourceId(ship.getId());
            entry.setSourceNo(ship.getOrderNo());
            entry.setSourceSpecUnit(ship.getOrderSpecUnit());
            entry.setSourceGoodsUnit(ship.getOrderGoodsUnit());
            entry.setSourceSpecUnitTotal(ship.getOrderSpecUnitTotal());
            entry.setSourceType(1);
            entry.setStatus(0);
            entry.setCreateBy(bo.getCreateBy());
            entry.setCreateTime(new Date());
            stockInEntryMapper.insert(entry);

            // 子表

            if(bo.getGoodsList()!=null && bo.getGoodsList().size()>0){
                List<WmsStockInEntryItem> items = new ArrayList<>();
                for (var item:bo.getGoodsList()) {
                    WmsStockInEntryItem entryItem = new WmsStockInEntryItem();
                    entryItem.setEntryId(entry.getId());
                    entryItem.setSourceType(1);
                    entryItem.setSourceId(ship.getId());
                    entryItem.setSourceItemId(item.getId());
                    entryItem.setGoodsId(item.getGoodsId());
                    entryItem.setGoodsNum(item.getGoodsNum());
                    entryItem.setGoodsName(item.getGoodsName());
                    entryItem.setSpecId(item.getSpecId());
                    entryItem.setSpecNum(item.getSpecNum());
                    entryItem.setColorValue(item.getColorValue());
                    entryItem.setColorImage(item.getColorImage());
                    entryItem.setSizeValue(item.getSizeValue());
                    entryItem.setStyleValue(item.getStyleValue());
                    entryItem.setOriginalQuantity(item.getQuantity());
                    entryItem.setInQuantity(0L);
                    entryItem.setCreateBy(bo.getCreateBy());
                    entryItem.setCreateTime(new Date());
                    entryItem.setRemark("");
//                    entryItem.setLocationId(0L);
                    entryItem.setStatus(0);
                    items.add(entryItem);
                }
//                stockInEntryMapper.batchWmsStockInEntryItem(items);
                stockInEntryItemService.saveBatch(items);
            }

            // 更新表状态
            ScmPurchaseOrderShip update = new ScmPurchaseOrderShip();
            update.setUpdateTime(DateUtils.getNowDate());
            update.setStockInTime(DateUtils.getNowDate());
            update.setUpdateBy(bo.getCreateBy());
            update.setStatus(2L);
            update.setId(ship.getId());
            scmPurchaseOrderShipMapper.updateScmPurchaseOrderShip(update);

            //更新 采购订单
            // 更新采购单状态
            ScmPurchaseOrder order = new ScmPurchaseOrder();
            order.setId(bo.getId());
            order.setStatus(3);
            order.setStockInTime(DateUtils.getNowDate());
            order.setUpdateTime(DateUtils.getNowDate());
            order.setUpdateBy(bo.getUpdateBy());
            purchaseOrderMapper.updateScmPurchaseOrder(order);

            return 1;
        }
        else return -4;
    }

    /**
     * 批量删除采购订单物流
     * 
     * @param ids 需要删除的采购订单物流主键
     * @return 结果
     */
    @Override
    public int deleteScmPurchaseOrderShipByIds(Long[] ids)
    {
        return scmPurchaseOrderShipMapper.deleteScmPurchaseOrderShipByIds(ids);
    }

    /**
     * 删除采购订单物流信息
     * 
     * @param id 采购订单物流主键
     * @return 结果
     */
    @Override
    public int deleteScmPurchaseOrderShipById(Long id)
    {
        return scmPurchaseOrderShipMapper.deleteScmPurchaseOrderShipById(id);
    }
}
