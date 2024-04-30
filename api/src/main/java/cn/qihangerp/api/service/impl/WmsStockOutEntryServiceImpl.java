package cn.qihangerp.api.service.impl;

import cn.qihangerp.domain.ErpOrderItem;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.api.domain.*;
import cn.qihangerp.api.domain.bo.StockOutBo;
import cn.qihangerp.api.domain.bo.StockOutEntryGenerateBo;
import cn.qihangerp.api.mapper.*;
import cn.qihangerp.api.service.WmsStockOutEntryItemService;
import cn.qihangerp.api.service.WmsStockOutEntryService;
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
    private final WmsStockOutEntryItemDetailMapper stockOutEntryItemDetailMapper;
    private final ErpGoodsInventoryMapper erpGoodsInventoryMapper;

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

    /**
     * 出库
     * @param bo
     * @return
     */
    @Transactional
    @Override
    public int stockOut(StockOutBo bo) {
//        WmsStockOutEntryItem item = wmsStockOutEntryMapper.selectWmsStockOutEntryItemById(bo.getEntryItemId());
        WmsStockOutEntryItem item = entryItemMapper.selectById(bo.getEntryItemId());
        if(item==null) return -1;
        else if(item.getStatus().intValue() ==2) return -2;
        else if(item.getOriginalQuantity() == item.getOutQuantity()) return -3;
        else if(item.getOriginalQuantity() - item.getOutQuantity() < bo.getOutQty()) return -4;
        // 库存判断

        ErpGoodsInventoryDetail detail = erpGoodsInventoryMapper.selectErpGoodsInventoryDetailById(bo.getInventoryDetailId());
        if(detail == null) return -11;
        else if(detail.getCurrentQty() < bo.getOutQty()) return -12;
        ErpGoodsInventory inventory = erpGoodsInventoryMapper.selectErpGoodsInventoryById(detail.getInventoryId());

        // 开始出库
        // 第一步：新增 wms_stock_out_entry_item_detail
        WmsStockOutEntryItemDetail itemDetail = new WmsStockOutEntryItemDetail();
        itemDetail.setEntryId(Long.parseLong(item.getEntryId()));
        itemDetail.setEntryItemId(item.getId());
        itemDetail.setGoodsInventoryId(detail.getInventoryId());
        itemDetail.setGoodsInventoryDetailId(detail.getId());
        itemDetail.setQuantity(bo.getOutQty());
        itemDetail.setLocationId(detail.getInLocation().intValue());
        itemDetail.setOperatorId(bo.getOperatorId());
        itemDetail.setOperatorName(bo.getOperatorName());
        itemDetail.setOutTime(new Date());
//        stockOutEntryItemDetailMapper.insertWmsStockOutEntryItemDetail(itemDetail);
        stockOutEntryItemDetailMapper.insert(itemDetail);
        // 第二步：更新 wms_stock_out_entry_item
        WmsStockOutEntryItem entryItem = new WmsStockOutEntryItem();
        entryItem.setId(item.getId());
        entryItem.setOutQuantity(item.getOutQuantity()+bo.getOutQty());
        if(item.getOutQuantity()==0){
            // 证明是刚开始出库
            entryItem.setPickedTime(new Date());
        }
        // 更新状态
        if(item.getOutQuantity()+ bo.getOutQty() == item.getOriginalQuantity()){
            // 出库完成了
            entryItem.setCompleteTime(new Date());
            entryItem.setStatus(2);
        }else{
            entryItem.setStatus(1);
        }
//        wmsStockOutEntryMapper.updateWmsStockOutEntryItem(entryItem);
        entryItemMapper.updateById(entryItem);

        // 第三步：减库存
        ErpGoodsInventoryDetail inventoryDetailUpdate = new ErpGoodsInventoryDetail();
        inventoryDetailUpdate.setId(detail.getId());
        inventoryDetailUpdate.setCurrentQty(detail.getCurrentQty() - bo.getOutQty());
        erpGoodsInventoryMapper.updateErpGoodsInventoryDetail(inventoryDetailUpdate);

        ErpGoodsInventory inventoryUpdate = new ErpGoodsInventory();
        inventoryUpdate.setId(detail.getInventoryId());
        inventoryUpdate.setCurrentQty(inventory.getCurrentQty() - bo.getOutQty());
        inventoryUpdate.setUpdateBy(bo.getOperatorName());
        inventoryUpdate.setUpdateTime(new Date());
        erpGoodsInventoryMapper.updateErpGoodsInventory(inventoryUpdate);

        // 第四步：更新wms_order_shipping状态
//        if(entryItem.getStatus()!=null && entryItem.getStatus() ==2) {
//            WmsOrderShipping shippingUpdate = new WmsOrderShipping();
//            shippingUpdate.setErpOrderItemId(item.getSourceOrderItemId());
//            shippingUpdate.setStatus(2L);
//            shippingUpdate.setOutOperator(bo.getOperatorName());
//            shippingUpdate.setOutPosition(detail.getInLocation().toString());
//            shippingUpdate.setOutTime(new Date());
//            shippingUpdate.setUpdateBy(bo.getOperatorName());
//            shippingUpdate.setUpdateTime(new Date());
//
//            orderShippingMapper.updateWmsOrderShippingStatusByErpOrderItemId(shippingUpdate);
//            WmsOrderShipping select = new WmsOrderShipping();
//            select.setErpOrderId(item.getSourceOrderId());
//            // 查询 同orderId所有数据状态
//            List<WmsOrderShipping> shipList = orderShippingMapper.selectWmsOrderShippingList(select);
//            // 循环
//            if(shipList!=null){
//                int allTotal = shipList.size();
//                int outTotal = 0;
//                for (WmsOrderShipping s:shipList) {
//                    if(s.getStatus().intValue() == 2){
//                        outTotal +=1;
//                    }
//                }
//                //是否全部出库
//                if(allTotal == outTotal){
//                    // 订单下面的所有子订单已出库 更新订单状态
//                    ErpOrder erpOrder = new ErpOrder();
//                    erpOrder.setId(item.getSourceOrderId());
//                    erpOrder.setOrderStatus(2);
//                    erpOrder.setUpdateBy(bo.getOperatorName());
//                    erpOrder.setUpdateTime(new Date());
//                    erpOrderMapper.updateErpOrder(erpOrder);
//                }
//            }
//        }
        return 1;
    }
}




