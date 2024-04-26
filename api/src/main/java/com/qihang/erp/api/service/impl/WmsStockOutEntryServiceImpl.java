package com.qihang.erp.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qihang.common.PageQuery;
import com.qihang.common.PageResult;
import com.qihang.erp.api.domain.ErpOrderItem;
import com.qihang.erp.api.domain.WmsStockOutEntry;
import com.qihang.erp.api.domain.WmsStockOutEntryItem;
import com.qihang.erp.api.domain.bo.StockOutEntryGenerateBo;
import com.qihang.erp.api.mapper.ErpOrderItemMapper;
import com.qihang.erp.api.mapper.WmsStockOutEntryItemMapper;
import com.qihang.erp.api.service.WmsStockOutEntryItemService;
import com.qihang.erp.api.service.WmsStockOutEntryService;
import com.qihang.erp.api.mapper.WmsStockOutEntryMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author TW
* @description 针对表【wms_stock_out_entry(出库单)】的数据库操作Service实现
* @createDate 2024-04-26 11:31:15
*/
@AllArgsConstructor
@Service
public class WmsStockOutEntryServiceImpl extends ServiceImpl<WmsStockOutEntryMapper, WmsStockOutEntry>
    implements WmsStockOutEntryService{
    private final ErpOrderItemMapper orderItemMapper;
    private final WmsStockOutEntryMapper entryMapper;
    private final WmsStockOutEntryItemMapper entryItemMapper;
    private final WmsStockOutEntryItemService entryItemService;

    @Transactional
    @Override
    public int generateStockOutEntryForOrderItem(StockOutEntryGenerateBo bo) {
        if(bo.getOrderItemIds() == null || bo.getOrderItemIds().length == 0) return -1;

        List<WmsStockOutEntryItem> items = new ArrayList<>();
        int total=0;
        // 循环判断状态
        for (var id:bo.getOrderItemIds()) {
            ErpOrderItem erpOrderItem = orderItemMapper.selectById(id);
            if(erpOrderItem!=null){
                if(erpOrderItem.getRefundCount() ==0 && erpOrderItem.getShipStatus() == 0){
                    WmsStockOutEntryItem item = new WmsStockOutEntryItem();
                    item.setStockOutType(1);
                    item.setSourceOrderId(erpOrderItem.getOrderId());
                    item.setSourceOrderNum(erpOrderItem.getOrderNum());
                    item.setSourceOrderItemId(Long.parseLong(erpOrderItem.getId()));
                    item.setGoodsId(erpOrderItem.getGoodsId());
                    item.setSpecId(erpOrderItem.getSpecId());
                    item.setSpecNum(erpOrderItem.getSpecNum());
                    item.setOriginalQuantity(erpOrderItem.getQuantity());
                    item.setOutQuantity(0);
                    item.setStatus(0);
                    item.setCreateTime(new Date());
                    items.add(item);

                    total += erpOrderItem.getQuantity();
                }else{
                    // 状态不对不能生成出库单
//                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                    return -1001;
                }
            }
        }

        if(items.size()==0) return -2;//没有要更新的
        // 添加主表
        Map<Long, List<WmsStockOutEntryItem>> goodsGroup = items.stream().collect(Collectors.groupingBy(x -> x.getGoodsId()));
        Map<Long, List<WmsStockOutEntryItem>> specGroup = items.stream().collect(Collectors.groupingBy(x -> x.getSpecId()));
        WmsStockOutEntry entry = new WmsStockOutEntry();
        entry.setStockOutNum(bo.getStockOutNum());
        entry.setStockOutType(1);
        entry.setGoodsUnit(goodsGroup.size());
        entry.setSpecUnit(specGroup.size());
        entry.setSpecUnitTotal(total);
        entry.setStatus(0);
        entry.setPrintStatus(0);
        entry.setCreateTime(new Date());
        entry.setCreateBy("生成拣货单");
        entryMapper.insert(entry);
        // 添加子表
        items.forEach(e->e.setEntryId(entry.getId()));
        entryItemService.saveBatch(items);
        for (var item:items) {
            ErpOrderItem orderItem = new ErpOrderItem();
            orderItem.setId(item.getSourceOrderItemId().toString());
            orderItem.setShipStatus(1);
            orderItem.setUpdateTime(new Date());
            orderItem.setUpdateBy("生成拣货单");
            orderItemMapper.updateById(orderItem);
        }
        return 0;
    }

    @Override
    public PageResult<WmsStockOutEntry> queryPageList(WmsStockOutEntry bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WmsStockOutEntry> qw = new LambdaQueryWrapper<WmsStockOutEntry>();
        qw.eq(WmsStockOutEntry::getStockOutType,bo.getStockOutType());
        if(bo.getStatus()!=null) {
            if (bo.getStatus() == 0) {
                qw.and(q -> q.eq(WmsStockOutEntry::getStatus, 0).or().eq(WmsStockOutEntry::getStatus, 1));
            } else {
                qw.eq(WmsStockOutEntry::getStatus, bo.getStatus());
            }
        }
        Page<WmsStockOutEntry> pages = entryMapper.selectPage(pageQuery.build(), qw);

        return PageResult.build(pages);
    }
}




