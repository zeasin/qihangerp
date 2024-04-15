package com.qihang.erp.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qihang.common.PageQuery;
import com.qihang.common.PageResult;
import com.qihang.erp.api.common.EnumShopType;
import com.qihang.erp.api.common.ResultVo;
import com.qihang.erp.api.common.ResultVoEnum;
import com.qihang.erp.api.domain.ErpOrder;
import com.qihang.erp.api.domain.ErpOrderItem;
import com.qihang.erp.api.domain.WeiOrder;
import com.qihang.erp.api.domain.WeiOrderItem;
import com.qihang.erp.api.mapper.ErpOrderItemMapper;
import com.qihang.erp.api.mapper.ErpOrderMapper;
import com.qihang.erp.api.mapper.WeiOrderItemMapper;
import com.qihang.erp.api.service.WeiOrderService;
import com.qihang.erp.api.mapper.WeiOrderMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
* @author TW
* @description 针对表【wei_order】的数据库操作Service实现
* @createDate 2024-04-15 17:28:27
*/
@AllArgsConstructor
@Service
public class WeiOrderServiceImpl extends ServiceImpl<WeiOrderMapper, WeiOrder>
    implements WeiOrderService{
    private final WeiOrderMapper mapper;
    private final WeiOrderItemMapper itemMapper;
    private final ErpOrderMapper erpOrderMapper;
    private final ErpOrderItemMapper erpOrderItemMapper;

    @Override
    public PageResult<WeiOrder> queryPageList(WeiOrder bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WeiOrder> queryWrapper = new LambdaQueryWrapper<WeiOrder>()
                .eq(bo.getShopId()!=null,WeiOrder::getShopId,bo.getShopId())
                .eq(StringUtils.hasText(bo.getOrderId()),WeiOrder::getOrderId,bo.getOrderId())
                ;

        Page<WeiOrder> taoGoodsPage = mapper.selectPage(pageQuery.build(), queryWrapper);
        if(taoGoodsPage.getRecords()!=null){
            for (var order:taoGoodsPage.getRecords()) {
                order.setItems(itemMapper.selectList(new LambdaQueryWrapper<WeiOrderItem>().eq(WeiOrderItem::getWeiOrderId,order.getId())));
            }
        }
        return PageResult.build(taoGoodsPage);
    }

    @Transactional
    @Override
    public ResultVo<Integer> saveOrder(Integer shopId, WeiOrder order) {
        try {
            List<WeiOrder> orders = mapper.selectList(new LambdaQueryWrapper<WeiOrder>().eq(WeiOrder::getOrderId, order.getOrderId()));
            if (orders != null && orders.size() > 0) {
                // 存在，修改
                WeiOrder update = new WeiOrder();
                update.setId(orders.get(0).getId());
                update.setStatus(order.getStatus());
                update.setUpdateTime(order.getUpdateTime());
                update.setPayInfo(order.getPayInfo());
                update.setAftersaleDetail(order.getAftersaleDetail());
                update.setDeliveryProductInfo(order.getDeliveryProductInfo());

                mapper.updateById(update);
                // 更新item
                for (var item : order.getItems()) {
                    List<WeiOrderItem> taoOrderItems = itemMapper.selectList(new LambdaQueryWrapper<WeiOrderItem>().eq(WeiOrderItem::getSkuId, item.getSkuId()));
                    if (taoOrderItems != null && taoOrderItems.size() > 0) {
                        // 不处理
                    } else {
                        // 新增
                        item.setWeiOrderId(update.getId());
                        itemMapper.insert(item);
                    }
                }
                return ResultVo.error(ResultVoEnum.DataExist, "订单已经存在，更新成功");
            } else {
                // 不存在，新增
                order.setShopId(shopId);
                mapper.insert(order);
                // 添加item
                for (var item : order.getItems()) {
                    item.setWeiOrderId(order.getId());
                    itemMapper.insert(item);
                }
                return ResultVo.success();
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultVo.error(ResultVoEnum.SystemException, "系统异常：" + e.getMessage());
        }
    }

    @Transactional
    @Override
    public ResultVo<Integer> orderConfirm(String[] ids) {
        if(ids!=null && ids.length >0) {
            Integer success =0;
            for(var id : ids){
                if(StringUtils.hasText(id)) {
                    // 查询订单
                    WeiOrder weiOrder = mapper.selectById(id);
                    if(weiOrder!=null){
                        // 查询是否确认过
                        if(weiOrder.getConfirmStatus() == null || weiOrder.getConfirmStatus() == 0){
                            // 确认状态是null或者0
//                            List<ErpOrder> erpOrders = erpOrderMapper.selectList(new LambdaQueryWrapper<ErpOrder>().eq(ErpOrder::getOrderNum, weiOrder.getOrderId()).eq(ErpOrder::getShopId, weiOrder.getShopId()));
//                            new LambdaQueryWrapper<ErpOrder>().eq(ErpOrder::getOrderNum, weiOrder.getOrderId()).eq(ErpOrder::getShopId, weiOrder.getShopId())
                            ErpOrder qw = new ErpOrder();
                            qw.setOrderNum(weiOrder.getOrderId());
                            qw.setShopId(weiOrder.getShopId());
                            List<ErpOrder> erpOrders = erpOrderMapper.selectErpOrderList(qw);


                            if(erpOrders==null||erpOrders.size()==0){
                                // 没有数据，开始插入订单数据
                                ErpOrder insert = new ErpOrder();
                                insert.setOrderNum(weiOrder.getOrderId());
                                insert.setShopType(EnumShopType.WEI.getIndex());
                                insert.setShopId(weiOrder.getShopId());
                                insert.setRefundStatus(1);
                                insert.setOrderStatus(0);
                                insert.setGoodsAmount(weiOrder.getProductPrice().doubleValue()/100);
                                insert.setAmount(weiOrder.getOrderPrice().doubleValue()/100);
                                insert.setReceiverName(weiOrder.getUserName());
                                insert.setReceiverPhone(weiOrder.getTelNumber());
                                insert.setProvince(weiOrder.getProvinceName());
                                insert.setCity(weiOrder.getCityName());
                                insert.setTown(weiOrder.getCountyName());
                                insert.setAddress(weiOrder.getDetailInfo());
                                insert.setShipType(-1);
                                insert.setOrderTime(new Date(weiOrder.getCreateTime() *1000));
                                insert.setCreateTime(new Date());
                                insert.setCreateBy("手动确认");
                                erpOrderMapper.insertErpOrder(insert);

                                // 插入order_item
                                List<WeiOrderItem> weiOrderItems = itemMapper.selectList(new LambdaQueryWrapper<WeiOrderItem>().eq(WeiOrderItem::getWeiOrderId, weiOrder.getId()));
                                for(var item :weiOrderItems) {
                                    ErpOrderItem itemInsert = new ErpOrderItem();
                                    itemInsert.setOrderId(insert.getId());
                                    itemInsert.setOrderNum(weiOrder.getOrderId());
                                    itemInsert.setOrderItemNum(item.getId()+"");
//                                    itemInsert.setSkuId(item.getSkuId());
                                    // TODO:skuId需要查询erp系统的
//                                    itemInsert.setErpGoodsId(0L);
//                                    itemInsert.setErpSkuId(0L);
                                    itemInsert.setGoodsTitle(item.getTitle());
                                    itemInsert.setGoodsImg(item.getThumbImg());
                                    itemInsert.setGoodsNum("");
//                                    itemInsert.setSkuNum("");
                                    itemInsert.setGoodsSpec(item.getSkuAttrs());
                                    if(item.getMarketPrice()!=null) {
                                        itemInsert.setGoodsPrice(item.getMarketPrice().doubleValue() / 100);
                                    }
                                    if(item.getRealPrice()!=null){
                                        itemInsert.setItemAmount(item.getRealPrice().doubleValue()/100);
                                    }
                                    itemInsert.setQuantity(item.getSkuCnt());
                                    itemInsert.setRefundCount(0);
                                    itemInsert.setRefundStatus(1);
                                    erpOrderItemMapper.insert(itemInsert);
                                }
                                // 更新wei_order确认状态
                                WeiOrder update = new WeiOrder();
                                update.setId(weiOrder.getId());
                                update.setConfirmStatus(1);
                                update.setConfirmTime(new Date());
                                mapper.updateById(update);
                                success++;
                            }
                        }
                    }
                }
            }
            return ResultVo.success(success);
        }else {
            return ResultVo.error(ResultVoEnum.ParamsError, "没有订单ID：");
        }
    }
}




