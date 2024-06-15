package cn.qihangerp.api.service.impl;

import cn.qihangerp.common.utils.DateUtils;
import cn.qihangerp.domain.ErpSaleOrder;
import cn.qihangerp.domain.ErpSaleOrderItem;
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
    private final ErpOrderMapper orderMapper;
    private final ErpShipOrderMapper shipOrderMapper;
    private final WmsStockOutEntryMapper entryMapper;
    private final WmsStockOutEntryItemMapper entryItemMapper;
    private final WmsStockOutEntryItemService entryItemService;
    private final WmsStockOutEntryItemDetailMapper stockOutEntryItemDetailMapper;
    private final ErpGoodsInventoryMapper erpGoodsInventoryMapper;


    /**
     * 生成拣货单
     * @param bo
     * @return
     */
    @Transactional
    @Override
    public int generateStockOutEntryForOrderItem(StockOutEntryGenerateBo bo) {
        if(bo.getOrderItemIds() == null || bo.getOrderItemIds().length == 0) return -1;

        List<WmsStockOutEntryItem> items = new ArrayList<>();
        int total=0;
        // 循环判断状态
        for (var id:bo.getOrderItemIds()) {
            ErpSaleOrderItem erpSaleOrderItem = orderItemMapper.selectById(id);
            if(erpSaleOrderItem !=null){
                if(erpSaleOrderItem.getRefundCount() ==0 && erpSaleOrderItem.getShipStatus() == 0){
                    if(erpSaleOrderItem.getSpecId()== null || erpSaleOrderItem.getSpecId()==0) return -1002;
                    WmsStockOutEntryItem item = new WmsStockOutEntryItem();
                    item.setStockOutType(1);
                    item.setSourceOrderId(erpSaleOrderItem.getOrderId());
                    item.setSourceOrderNum(erpSaleOrderItem.getOriginalOrderId());
                    item.setSourceOrderItemId(Long.parseLong(erpSaleOrderItem.getId()));
                    item.setGoodsId(erpSaleOrderItem.getGoodsId());
                    item.setSpecId(erpSaleOrderItem.getSpecId());
                    item.setSpecNum(erpSaleOrderItem.getSpecNum());
                    item.setOriginalQuantity(erpSaleOrderItem.getQuantity());
                    item.setOutQuantity(0);
                    item.setStatus(0);
                    item.setCreateTime(new Date());
                    item.setSupplierId(erpSaleOrderItem.getSupplierId());
                    items.add(item);

                    total += erpSaleOrderItem.getQuantity();
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
        // 拣货单主表
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
        // 添加子表（拣货单子表）
        items.forEach(e->e.setEntryId(entry.getId()));
        entryItemService.saveBatch(items);

        // 更新订单子表及主表出库状态
        for (var item:items) {
            ErpSaleOrderItem orderItem = new ErpSaleOrderItem();
            orderItem.setId(item.getSourceOrderItemId().toString());
            orderItem.setShipType(0);
            orderItem.setShipStatus(1);
            orderItem.setUpdateTime(new Date());
            orderItem.setUpdateBy("生成拣货单");
            orderItemMapper.updateById(orderItem);
            // 更新订单主表

            // 查询是否全部出库中
            List<ErpSaleOrderItem> erpSaleOrderItems = orderMapper.selectOrderItemByOrderId(item.getSourceOrderId());
            List<ErpSaleOrderItem> waitShipList = erpSaleOrderItems.stream().filter(x -> (x.getShipStatus()==null||x.getShipStatus() == 0)&& Long.parseLong(x.getId())!=item.getSourceOrderItemId()).collect(Collectors.toList());
            if(waitShipList==null || waitShipList.size()==0) {
                // 全部不是待备货状态，更新主表状态
                ErpSaleOrder orderUpdate = new ErpSaleOrder();
                orderUpdate.setId(item.getSourceOrderId());
                orderUpdate.setShipStatus(1);
                orderUpdate.setShipType(0);
                orderUpdate.setUpdateTime(new Date());
                orderUpdate.setUpdateBy("生成拣货单");
                orderMapper.updateErpOrder(orderUpdate);
            }
            ErpSaleOrder erpSaleOrder = orderMapper.selectErpOrderById(item.getSourceOrderId());
            // 添加到发货记录表
            ErpShipOrder shipOrder = new ErpShipOrder();
            shipOrder.setShopId(erpSaleOrder.getShopId());
            shipOrder.setShopType(erpSaleOrder.getShopType());
            shipOrder.setSupplierId(item.getSupplierId()==null?0:item.getSupplierId());
            shipOrder.setOrderNum(erpSaleOrder.getOrderNum());
            shipOrder.setOrderTime(erpSaleOrder.getOrderTime()==null? DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", erpSaleOrder.getCreateTime()) : erpSaleOrder.getOrderTime());
            shipOrder.setErpOrderId(item.getSourceOrderId());
            shipOrder.setErpOrderItemId(item.getSourceOrderItemId().toString());
            shipOrder.setGoodsId(item.getGoodsId());
            shipOrder.setSpecId(item.getSpecId());
            shipOrder.setSpecNum(item.getSpecNum());
            shipOrder.setQuantity(item.getOriginalQuantity());
            shipOrder.setShipType(0);
            shipOrder.setShipStatus(1);
            shipOrder.setReceiverName(erpSaleOrder.getReceiverName());
            shipOrder.setReceiverPhone(erpSaleOrder.getReceiverPhone());
            shipOrder.setCountry(erpSaleOrder.getCountry());
            shipOrder.setProvince(erpSaleOrder.getProvince());
            shipOrder.setCity(erpSaleOrder.getCity());
            shipOrder.setTown(erpSaleOrder.getTown());
            shipOrder.setAddress(erpSaleOrder.getAddress());
            shipOrder.setCreateBy("生成拣货单");
            shipOrder.setCreateTime(new Date());
            shipOrderMapper.insert(shipOrder);
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
        if(bo.getOutQty()== null || bo.getOutQty()==0) return -5;
//        WmsStockOutEntryItem item = wmsStockOutEntryMapper.selectWmsStockOutEntryItemById(bo.getEntryItemId());
        WmsStockOutEntryItem originalItem = entryItemMapper.selectById(bo.getEntryItemId());
        if(originalItem==null) return -1;
        else if(originalItem.getStatus().intValue() ==2) return -2;
        else if(originalItem.getOriginalQuantity() == originalItem.getOutQuantity()) return -3;
        else if(originalItem.getOriginalQuantity() - originalItem.getOutQuantity() < bo.getOutQty()) return -4;

        // 库存判断
        ErpGoodsInventoryDetail detail = erpGoodsInventoryMapper.selectErpGoodsInventoryDetailById(bo.getInventoryDetailId());
        if(detail == null) return -11;
        else if(detail.getCurrentQty() < bo.getOutQty()) return -12;
        ErpGoodsInventory inventory = erpGoodsInventoryMapper.selectErpGoodsInventoryById(detail.getInventoryId());

        // 开始出库
        // 第一步：新增 wms_stock_out_entry_item_detail
        WmsStockOutEntryItemDetail itemDetail = new WmsStockOutEntryItemDetail();
        itemDetail.setEntryId(Long.parseLong(originalItem.getEntryId()));
        itemDetail.setEntryItemId(Long.parseLong(originalItem.getId()));
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
        entryItem.setId(originalItem.getId());
        entryItem.setOutQuantity(originalItem.getOutQuantity()+bo.getOutQty());
        if(originalItem.getOutQuantity()==0){
            // 证明是刚开始出库
            entryItem.setPickedTime(new Date());
        }
        // 更新状态
        if(originalItem.getOutQuantity()+ bo.getOutQty() == originalItem.getOriginalQuantity()){
            // 出库完成了
            entryItem.setCompleteTime(new Date());
            entryItem.setStatus(2);
        }else{
            entryItem.setStatus(1);
        }
//        wmsStockOutEntryMapper.updateWmsStockOutEntryItem(entryItem);
        entryItemMapper.updateById(entryItem);

        /***更新出库单状态***/
        //状态：0待出库1部分出库2全部出库
        WmsStockOutEntry wmsStockOutEntry = entryMapper.selectById(originalItem.getEntryId());
        if(wmsStockOutEntry!=null){
            int outTotal = wmsStockOutEntry.getOutTotal();
            outTotal+= bo.getOutQty();
            int status = 1;
            if(outTotal == wmsStockOutEntry.getSpecUnitTotal()){
                status = 2;
            }
            WmsStockOutEntry entryUpdate = new WmsStockOutEntry();
            entryUpdate.setId(wmsStockOutEntry.getId());
            entryUpdate.setOutTotal(outTotal);
            entryUpdate.setStatus(status);
            entryUpdate.setOutTime(new Date());
            if(status==2){
                entryUpdate.setCompleteTime(new Date());
            }
            entryUpdate.setOperatorId(bo.getOperatorId());
            entryUpdate.setOperatorName(bo.getOperatorName());
            entryUpdate.setUpdateBy("出库");
            entryUpdate.setUpdateTime(new Date());
            entryMapper.updateById(entryUpdate);
        }


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

        // 第四步：更新erp_ship_order状态 （更新为出库中）
        if(entryItem.getStatus()!=null && entryItem.getStatus() ==2) {
            // 查出erp_ship_order数据（根据 wms_stock_in_entry_item 的 source_order_item_id）
            List<ErpShipOrder> erpShipOrders = shipOrderMapper.selectList(new LambdaQueryWrapper<ErpShipOrder>().eq(ErpShipOrder::getErpOrderItemId, originalItem.getSourceOrderItemId()));
            if(erpShipOrders!=null&& erpShipOrders.size()>0){
                // 更新erp_ship_order状态为出库中
                ErpShipOrder shippingUpdate = new ErpShipOrder();
                shippingUpdate.setId(erpShipOrders.get(0).getId());
                shippingUpdate.setShipStatus(2);//已出库
                shippingUpdate.setOutOperator(bo.getOperatorName());
                shippingUpdate.setOutPosition(detail.getInLocation().toString());
                shippingUpdate.setOutTime(new Date());
                shippingUpdate.setUpdateBy(bo.getOperatorName());
                shippingUpdate.setUpdateTime(new Date());

                shipOrderMapper.updateById(shippingUpdate);
                // 更新erp_order表出库状态（2.0版本已经放弃这个偶尔）
//                WmsOrderShipping select = new WmsOrderShipping();
//                select.setErpOrderId(item.getSourceOrderId());
//                // 查询 同orderId所有数据状态
//                List<WmsOrderShipping> shipList = orderShippingMapper.selectWmsOrderShippingList(select);
//                // 循环
//                if(shipList!=null){
//                    int allTotal = shipList.size();
//                    int outTotal = 0;
//                    for (WmsOrderShipping s:shipList) {
//                        if(s.getStatus().intValue() == 2){
//                            outTotal +=1;
//                        }
//                    }
//                    //是否全部出库
//                    if(allTotal == outTotal){
//                        // 订单下面的所有子订单已出库 更新订单状态
//                        ErpOrder erpOrder = new ErpOrder();
//                        erpOrder.setId(item.getSourceOrderId());
//                        erpOrder.setOrderStatus(2);
//                        erpOrder.setUpdateBy(bo.getOperatorName());
//                        erpOrder.setUpdateTime(new Date());
//                        erpOrderMapper.updateErpOrder(erpOrder);
//                    }
//                }


            }


        }
        return 1;
    }

    @Override
    public WmsStockOutEntry selectById(Long id) {
        WmsStockOutEntry wmsStockOutEntry = entryMapper.selectById(id);
        if(wmsStockOutEntry!=null) {
            wmsStockOutEntry.setItems(entryItemMapper.selectList(new LambdaQueryWrapper<WmsStockOutEntryItem>().eq(WmsStockOutEntryItem::getEntryId,wmsStockOutEntry.getId())));
        }
        return wmsStockOutEntry;
    }

    /**
     * 获取出库单详情（包括出库单明细及明细库存详情）
     * @param entryId
     * @return
     */
    @Override
    public WmsStockOutEntry selectOutEntryItemInventoryDetailsByEntryId(Long entryId) {
        WmsStockOutEntry wmsStockOutEntry = entryMapper.selectById(entryId);
        if(wmsStockOutEntry!=null) {
            List<WmsStockOutEntryItem> entryItems = entryItemMapper.selectList(new LambdaQueryWrapper<WmsStockOutEntryItem>().eq(WmsStockOutEntryItem::getEntryId, wmsStockOutEntry.getId()));
            for (WmsStockOutEntryItem item : entryItems){
                List<ErpGoodsInventoryDetail> erpGoodsInventoryDetails = erpGoodsInventoryMapper.selectErpGoodsInventoryDetailBySpecId(item.getSpecId());
                item.setInventoryDetails(erpGoodsInventoryDetails);
            }
            wmsStockOutEntry.setItems(entryItems);
        }
        return wmsStockOutEntry;
    }
}




