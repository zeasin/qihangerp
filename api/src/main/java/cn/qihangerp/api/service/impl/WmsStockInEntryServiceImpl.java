package cn.qihangerp.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.utils.StringUtils;
import cn.qihangerp.api.domain.*;
import cn.qihangerp.api.mapper.ErpGoodsInventoryMapper;
import cn.qihangerp.api.mapper.WmsStockInEntryItemMapper;
import cn.qihangerp.api.service.WmsStockInEntryService;
import cn.qihangerp.api.mapper.WmsStockInEntryMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author TW
* @description 针对表【wms_stock_in_entry(入库单)】的数据库操作Service实现
* @createDate 2024-04-26 14:54:16
*/
@AllArgsConstructor
@Service
public class WmsStockInEntryServiceImpl extends ServiceImpl<WmsStockInEntryMapper, WmsStockInEntry>
    implements WmsStockInEntryService{
    private final WmsStockInEntryMapper wmsStockInEntryMapper;
    private final WmsStockInEntryItemMapper wmsStockInEntryItemMapper;
    private final ErpGoodsInventoryMapper goodsInventoryMapper;

    @Override
    public PageResult<WmsStockInEntry> queryPageList(WmsStockInEntry bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WmsStockInEntry> qw = new LambdaQueryWrapper<WmsStockInEntry>();
//        qw.eq(WmsStockOutEntry::getStockOutType,bo.getStockOutType());
//        if(bo.getStatus()!=null) {
//            if (bo.getStatus() == 0) {
//                qw.and(q -> q.eq(WmsStockOutEntry::getStatus, 0).or().eq(WmsStockOutEntry::getStatus, 1));
//            } else {
//                qw.eq(WmsStockOutEntry::getStatus, bo.getStatus());
//            }
//        }
        Page<WmsStockInEntry> pages = wmsStockInEntryMapper.selectPage(pageQuery.build(), qw);

        return PageResult.build(pages);
    }

    /**
     * 修改入库单
     *
     * @param wmsStockInEntry 入库单
     * @return 结果
     */
    @Transactional
    @Override
    public int stockIn(WmsStockInEntry wmsStockInEntry)
    {
        // 查询入库单
        WmsStockInEntry origin = wmsStockInEntryMapper.selectById(wmsStockInEntry.getId());
        if(origin == null) return -1;
        else if(origin.getStatus() == 2) return -9;

        // 待入库数据
        List<WmsStockInEntryItem> waitList = new ArrayList<>();
        for (var item:wmsStockInEntry.getWmsStockInEntryItemList()) {
            if(StringUtils.isNotNull(item.getQuantity()) && StringUtils.isNotNull(item.getLocationId())){
                waitList.add(item);
            }
        }
        if(waitList.size() == 0) return -2;

        // 查询
        for (var item:waitList) {
            // 查询 商品库存
            ErpGoodsInventory gi = goodsInventoryMapper.selectErpGoodsInventoryBySpecId(item.getSpecId());
            if(gi == null){
                // 添加
                ErpGoodsInventory add = new ErpGoodsInventory();
                add.setGoodsId(item.getGoodsId());
                add.setGoodsNumber(item.getGoodsNum());
                add.setSpecId(item.getSpecId());
                add.setSpecNumber(item.getSpecNum());
                add.setIsDelete(0);
                add.setCreateBy(wmsStockInEntry.getUpdateBy());
                add.setCreateTime(new Date());
                add.setLockedQty(0L);
                add.setCurrentQty(item.getQuantity());
                goodsInventoryMapper.insertErpGoodsInventory(add);

                // 添加detail
                ErpGoodsInventoryDetail detail = new ErpGoodsInventoryDetail();
                detail.setInventoryId(add.getId());
                detail.setInQty(item.getQuantity());
                detail.setOriginQty(0L);
                detail.setCurrentQty(item.getQuantity());
                detail.setGoodsId(item.getGoodsId());
                detail.setSpecId(item.getSpecId());
                detail.setInLocation(item.getLocationId());
                detail.setEntryId(origin.getId());
                detail.setEntryItemId(item.getId());
                detail.setCreateBy(wmsStockInEntry.getUpdateBy());
                detail.setCreateTime(new Date());
                List<ErpGoodsInventoryDetail> ds = new ArrayList<>();
                ds.add(detail);
                goodsInventoryMapper.batchErpGoodsInventoryDetail(ds);

                // update item
                WmsStockInEntryItem upItem = new WmsStockInEntryItem();
                upItem.setId(item.getId());
                upItem.setInQuantity(item.getInQuantity() + item.getQuantity());
                upItem.setStatus(1);
                upItem.setUpdateTime(new Date());
                upItem.setUpdateBy(wmsStockInEntry.getUpdateBy());
//                wmsStockInEntryMapper.updateWmsStockInEntryItem(upItem);
                wmsStockInEntryItemMapper.updateById(upItem);
            }else{
                // 修改
                ErpGoodsInventory update = new ErpGoodsInventory();
                update.setId(gi.getId());
                update.setCurrentQty(gi.getCurrentQty() + item.getQuantity());
                update.setUpdateBy(wmsStockInEntry.getUpdateBy());
                update.setUpdateTime(new Date());
                goodsInventoryMapper.updateErpGoodsInventory(update);

                // 添加detail
                ErpGoodsInventoryDetail detail = new ErpGoodsInventoryDetail();
                detail.setInventoryId(update.getId());
                detail.setInQty(item.getQuantity());
                detail.setOriginQty(gi.getCurrentQty());
                detail.setCurrentQty(update.getCurrentQty());
                detail.setGoodsId(item.getGoodsId());
                detail.setSpecId(item.getSpecId());
                detail.setInLocation(item.getLocationId());
                detail.setEntryId(origin.getId());
                detail.setEntryItemId(item.getId());
                detail.setCreateBy(wmsStockInEntry.getUpdateBy());
                detail.setCreateTime(new Date());
                List<ErpGoodsInventoryDetail> ds = new ArrayList<>();
                ds.add(detail);
                goodsInventoryMapper.batchErpGoodsInventoryDetail(ds);

                // update item
                WmsStockInEntryItem itemUpdate = new WmsStockInEntryItem();
                itemUpdate.setId(item.getId());
                itemUpdate.setInQuantity(item.getInQuantity() + item.getQuantity());
                itemUpdate.setStatus(1);
                itemUpdate.setUpdateTime(new Date());
                itemUpdate.setUpdateBy(wmsStockInEntry.getUpdateBy());
//                wmsStockInEntryMapper.updateWmsStockInEntryItem(upItem);
                wmsStockInEntryItemMapper.updateById(itemUpdate);
            }

        }

        // 更新自己
        WmsStockInEntry new1 = new WmsStockInEntry();
        new1.setId(wmsStockInEntry.getId());
        new1.setStockInOperatorId(wmsStockInEntry.getStockInOperatorId());
        new1.setStockInOperator(wmsStockInEntry.getUpdateBy());
        new1.setStockInTime(wmsStockInEntry.getStockInTime());
        new1.setUpdateBy(wmsStockInEntry.getUpdateBy());
        new1.setUpdateTime(new Date());
        new1.setStatus(1);
        wmsStockInEntryMapper.updateById(new1);
//        wmsStockInEntry.setUpdateTime(DateUtils.getNowDate());
//        wmsStockInEntryMapper.deleteWmsStockInEntryItemByEntryId(wmsStockInEntry.getId());
//        insertWmsStockInEntryItem(wmsStockInEntry);
//        return wmsStockInEntryMapper.updateWmsStockInEntry(wmsStockInEntry);
        return 1;
    }

    @Transactional
    @Override
    public int complete(Long id,String updateBy) {
        // 更新自己
        WmsStockInEntry new1 = new WmsStockInEntry();
        new1.setId(id.toString());
        new1.setUpdateBy(updateBy);
        new1.setUpdateTime(new Date());
        new1.setStatus(2);
        wmsStockInEntryMapper.updateById(new1);

        List<WmsStockInEntryItem> items = wmsStockInEntryItemMapper.selectList(new LambdaQueryWrapper<WmsStockInEntryItem>().eq(WmsStockInEntryItem::getEntryId,id));
        if(!items.isEmpty()){
            for (var item:items) {
                // update item
                WmsStockInEntryItem upItem = new WmsStockInEntryItem();
                upItem.setId(item.getId());
                upItem.setStatus(2);
                upItem.setUpdateTime(new Date());
                upItem.setUpdateBy(updateBy);
                wmsStockInEntryItemMapper.updateById(upItem);
            }
        }

        return 1;
    }

    @Override
    public WmsStockInEntry getDetailAndItemById(Long entryId) {
        WmsStockInEntry wmsStockInEntry = wmsStockInEntryMapper.selectById(entryId);
        if( wmsStockInEntry != null ){
            wmsStockInEntry.setWmsStockInEntryItemList(wmsStockInEntryItemMapper.selectList(new LambdaQueryWrapper<WmsStockInEntryItem>().eq(WmsStockInEntryItem::getEntryId,wmsStockInEntry.getId())));
            return wmsStockInEntry;
        }else return null;
    }
}




