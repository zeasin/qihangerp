package cn.qihangerp.open.tao.service.impl;

import cn.qihangerp.common.ResultVo;
import cn.qihangerp.common.utils.DateUtils;
import cn.qihangerp.common.utils.StringUtils;
//import com.qihang.erp.api.common.EnumShopType;
//import com.qihang.erp.api.common.ResultVo;
//import com.qihang.erp.api.controller.tao.OrderImportItem;
//import com.qihang.erp.api.service.ITaoOrderService;
import cn.qihangerp.domain.Shop;
import cn.qihangerp.domain.ShopSetting;
import cn.qihangerp.open.tao.domain.TaoOrder;
import cn.qihangerp.open.tao.domain.TaoOrderAddress;
import cn.qihangerp.open.tao.domain.TaoOrderItem;
import cn.qihangerp.open.tao.domain.bo.OrderImportItem;
import cn.qihangerp.open.tao.mapper.TaoOrderAddressMapper;
import cn.qihangerp.open.tao.mapper.TaoOrderMapper;
import cn.qihangerp.open.tao.service.ITaoOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 淘宝订单Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-03
 */
@Service
public class TaoOrderServiceImpl implements ITaoOrderService
{
    @Autowired
    private TaoOrderMapper taoOrderMapper;
    @Autowired
    private TaoOrderAddressMapper addressMapper;
//    @Autowired
//    private ErpOrderMapper erpOrderMapper;
//
//    @Autowired
//    private GoodsMapper goodsMapper;
//    @Autowired
//    private GoodsSpecMapper goodsSpecMapper;

    /**
     * 查询淘宝订单
     * 
     * @param id 淘宝订单主键
     * @return 淘宝订单
     */
    @Override
    public TaoOrder selectTaoOrderById(String id)
    {
        TaoOrder taoOrder = taoOrderMapper.selectTaoOrderById(id);
        TaoOrderAddress addr = addressMapper.selectTaoOrderAddressByOrderId(taoOrder.getId());
        taoOrder.setReceiver(addr.getContactPerson());
        taoOrder.setPhone(addr.getMobile());
        taoOrder.setProvince(addr.getProvince());
        taoOrder.setCity(addr.getCity());
        taoOrder.setDistrict(addr.getTown());
        taoOrder.setAddress(addr.getAddress());
        return taoOrder;
    }

    /**
     * 查询淘宝订单列表
     * 
     * @param taoOrder 淘宝订单
     * @return 淘宝订单
     */
    @Override
    public List<TaoOrder> selectTaoOrderList(TaoOrder taoOrder)
    {
        var orderList = taoOrderMapper.selectTaoOrderList(taoOrder);
        for (var o:orderList) {
            List<TaoOrderItem> items = taoOrderMapper.selectOrderItemByOrderId(o.getId());
            o.setTaoOrderItemList(items);
        }
        return orderList;
    }

    /**
     * 新增淘宝订单
     * 
     * @param taoOrder 淘宝订单
     * @return 结果
     */
    @Transactional
    @Override
    public int insertTaoOrder(TaoOrder taoOrder)
    {
        if (StringUtils.isNotNull(taoOrder.getTaoOrderItemList())) return -2;
        TaoOrder order = taoOrderMapper.selectTaoOrderById(taoOrder.getId());
        if (order != null) return -1;

        if(StringUtils.isNull(taoOrder.getShippingFee())) taoOrder.setShippingFee(BigDecimal.ZERO);
        taoOrder.setTotalAmount(taoOrder.getTotalAmount().add(taoOrder.getShippingFee()));
        taoOrder.setPayAmount(taoOrder.getTotalAmount());
        taoOrder.setStatus(2L);
        taoOrder.setStatusStr("等待发货");
        taoOrder.setRefundStatus("0");
        taoOrder.setAuditStatus(0L);
        taoOrder.setSendStatus(0L);
        taoOrder.setIsComment(0);

        taoOrder.setCreateTime(DateUtils.getNowDate());
        int rows = taoOrderMapper.insertTaoOrder(taoOrder);
        insertTaoOrderItem(taoOrder);
        // 添加地址
        TaoOrderAddress address = new TaoOrderAddress();
        address.setOrderId(taoOrder.getId());
        address.setContactPerson(taoOrder.getReceiver());
        address.setMobile(taoOrder.getPhone());
        address.setProvince(taoOrder.getProvince());
        address.setCity(taoOrder.getCity());
        address.setArea(taoOrder.getDistrict());
        address.setAddress(taoOrder.getAddress());
        addressMapper.insertTaoOrderAddress(address);
        return rows;
    }

    @Transactional
    @Override
    public int confirmOrder(TaoOrder taoOrder) {
        if(StringUtils.isNull(taoOrder.getShipType())){
            return -3;
        }
        if(taoOrder.getShipType() != 0 && taoOrder.getShipType() != 1){
            // 1 供应商发货 0 仓库发货
            return -4;
        }

//        TaoOrder original = taoOrderMapper.selectTaoOrderById(taoOrder.getId());
//        if(original.getAuditStatus() != 0) return -1;
//        ErpOrder erpOrder = erpOrderMapper.selectErpOrderByNum(taoOrder.getId());
//        if(erpOrder!=null) return -2;
//
//        // 新增ErpOrder
//        // 确认订单（操作：插入数据到s_shop_order、s_shop_order_item）
//        ErpOrder so = new ErpOrder();
//        so.setOrderNum(original.getId());
//        so.setShopId(original.getShopId().intValue());
//        so.setShopType(EnumShopType.TAO.getIndex());
//        so.setRemark(original.getRemark());
//        so.setBuyerMemo(original.getBuyerFeedback());
//        so.setTag(original.getTag());
//        so.setRefundStatus(1);
//        so.setOrderStatus(1);
//        so.setShipStatus(0);
//        so.setShipType(taoOrder.getShipType());
//
//        so.setGoodsAmount(original.getTotalAmount().subtract(original.getShippingFee()).doubleValue());
//        so.setDiscountAmount(original.getDiscountAmount());
//        so.setAmount(original.getTotalAmount().doubleValue());
//        so.setPostage(original.getShippingFee());
//
//        so.setPayTime(original.getPayTime());
//        so.setConfirmTime(new Date());
//        so.setCreateTime(new Date());
//        so.setCreateBy(taoOrder.getUpdateBy());
//
//        TaoOrderAddress addr = addressMapper.selectTaoOrderAddressByOrderId(taoOrder.getId());
//        so.setReceiverName(addr.getContactPerson());
//        so.setReceiverPhone(addr.getMobile());
//        so.setAddress(addr.getAddress());
//        so.setCountry("中国");
//        so.setProvince(addr.getProvince());
//        so.setCity(addr.getCity());
//        so.setTown(addr.getArea());
//
//        erpOrderMapper.insertErpOrder(so);
//        // 新增ErpOrderItem
//        List<TaoOrderItem> taoOrderItems = taoOrderMapper.selectOrderItemByOrderId(taoOrder.getId());
//        List<ErpOrderItem> items = new ArrayList<>();
//        for (var i:taoOrderItems) {
//            if(StringUtils.isEmpty(i.getSpecNumber())) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                return -11;
//            }
//            GoodsSpec spec = goodsSpecMapper.selectGoodsSpecBySpecNum(i.getSpecNumber());
//            if (spec == null) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                return -11;
//            }
//            Goods goods = goodsMapper.selectGoodsById(spec.getGoodsId());
//            if(goods == null){
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                return -12;
//            }
//
//            ErpOrderItem item = new ErpOrderItem();
//            item.setShipStatus(0);
//            item.setShipType(taoOrder.getShipType());
//            item.setShopId(original.getShopId().intValue());
//            item.setOrderId(so.getId());
//            item.setOrderNum(original.getId());
//            item.setOrderItemNum(i.getSubItemId());
//            item.setSupplierId(goods.getSupplierId().intValue());
//            item.setGoodsId(spec.getGoodsId());
//            item.setSpecId(spec.getId());
//            item.setGoodsTitle(i.getGoodsTitle());
//            item.setGoodsImg(i.getProductImgUrl());
//            item.setGoodsNum(i.getGoodsNumber());
//            item.setSpecNum(i.getSpecNumber());
//            item.setGoodsSpec(i.getSkuInfo());
//            item.setGoodsPrice(i.getPrice().doubleValue());
////            item.setGoodsPurPrice(spec.getPurPrice());
//            item.setItemAmount(i.getItemAmount().doubleValue());
//            item.setQuantity(i.getQuantity());
//            item.setIsGift(i.getIsGift().intValue());
//            item.setRefundCount(0);
//            item.setRefundStatus(1);
//            item.setCreateBy(taoOrder.getUpdateBy());
//            item.setCreateTime(new Date());
//            items.add(item);
//        }
//        // 添加了赠品
//        if(taoOrder.getTaoOrderItemList()!=null && !taoOrder.getTaoOrderItemList().isEmpty()){
//            for (var g:taoOrder.getTaoOrderItemList()) {
//                if(StringUtils.isEmpty(g.getSpecNumber())) {
//                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                    return -11;
//                }
//                GoodsSpec spec = goodsSpecMapper.selectGoodsSpecBySpecNum(g.getSpecNumber());
//                if (spec == null) {
//                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                    return -11;
//                }
//                Goods goods = goodsMapper.selectGoodsById(spec.getGoodsId());
//                if(goods == null) {
//                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                    return -12;
//                }
//
//                ErpOrderItem item = new ErpOrderItem();
//                item.setShipStatus(0);
//                item.setShipType(taoOrder.getShipType());
//                item.setShopId(original.getShopId().intValue());
//                item.setOrderId(so.getId());
//                item.setOrderNum(original.getId());
//                item.setOrderItemNum(original.getId()+"_");
//                item.setSupplierId(goods.getSupplierId().intValue());
//                item.setGoodsId(spec.getGoodsId());
//                item.setSpecId(spec.getId());
//                item.setGoodsTitle(g.getGoodsTitle());
//                item.setGoodsImg(g.getProductImgUrl());
//                item.setGoodsNum(g.getGoodsNumber());
//                item.setSpecNum(g.getSpecNumber());
//                item.setGoodsSpec(g.getSkuInfo());
//                item.setGoodsPrice(g.getPrice().doubleValue());
////                item.setGoodsPurPrice(spec.getPurPrice());
//                item.setItemAmount(g.getItemAmount().doubleValue());
//                item.setQuantity(g.getQuantity());
//                item.setIsGift(1);
//                item.setRefundCount(0);
//                item.setRefundStatus(1);
//                item.setCreateBy(taoOrder.getUpdateBy());
//                item.setCreateTime(new Date());
//                items.add(item);
//            }
//        }
//        erpOrderMapper.batchErpOrderItem(items);
//
//        //更新自己
//        TaoOrder update = new TaoOrder();
//        update.setId(original.getId());
//        update.setAuditStatus(1L);
//        update.setAuditTime(new Date());
//        update.setUpdateBy(taoOrder.getUpdateBy());
//        update.setUpdateTime(new Date());
//        taoOrderMapper.updateTaoOrder(update);
        return 1;
    }


    /**
     * 批量删除淘宝订单
     * 
     * @param ids 需要删除的淘宝订单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteTaoOrderByIds(Long[] ids)
    {
        taoOrderMapper.deleteTaoOrderItemByOrderIds(ids);
        return taoOrderMapper.deleteTaoOrderByIds(ids);
    }

    /**
     * 删除淘宝订单信息
     * 
     * @param id 淘宝订单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteTaoOrderById(Long id)
    {
        taoOrderMapper.deleteTaoOrderItemByOrderId(id);
        return taoOrderMapper.deleteTaoOrderById(id);
    }

    @Transactional
    @Override
    public ResultVo<Integer> updateTmallOrderForOpenTaobao(Long shopId, TaoOrder order) {
        //查询订单是否存在
        TaoOrder taoOrder = taoOrderMapper.selectTaoOrderById(order.getId());
        if(taoOrder == null){
            // 不存在，新增
            order.setCreateTime(new Date());
            order.setAuditStatus(0L);
            taoOrderMapper.insertTaoOrder(order);
            // 添加收货地址
            TaoOrderAddress  address = new TaoOrderAddress();
            address.setContactPerson(order.getReceiver());
            address.setProvince(order.getProvince());
            address.setCity(order.getCity());
            address.setTown(order.getDistrict());
            address.setAddress(order.getAddress());
            address.setMobile(order.getPhone());
            address.setOrderId(order.getId());
            addressMapper.insertTaoOrderAddress(address);
            // 添加订单item
//            for (TaoOrderItem orderItem:order.getTaoOrderItemList()) {
//                orderItem.setNewSpecId(0L);
//                orderItem.setIsGift(0);
//                orderItem.setIsSwap(0);
//            }
            taoOrderMapper.batchTaoOrderItem(order.getTaoOrderItemList());

        }else{
            // 存在更新

        }

//        var oList = jdbcTemplate.query("SELECT * FROM " + Tables.DcTmallOrder + " WHERE id=? ", new BeanPropertyRowMapper<>(DcTmallOrderEntity.class), Long.parseLong(order.getId()));
//        if (oList != null && oList.size() > 0) {
//            //存在，更新
//            /**********1、更新订单状态**********/
//            String updSQL = "UPDATE " + Tables.DcTmallOrder + " SET totalAmount=?,shippingFee=?,payAmount=?" +
//                    ",createTime=?,modifyTime=?,payTime=?,sellerMemo=?,buyerFeedback=?,statusStr=?,status=?,is_comment=? WHERE id=?";
//
//            jdbcTemplate.update(updSQL, order.getTotalAmount(), order.getShippingFee(), order.getPayAmount(),
//                    order.getCreateTime(), order.getModifyTime(), order.getPayTime(),
//                    order.getSellerMemo(), order.getBuyerFeedback(), order.getStatusStr(), order.getStatus(),order.getIsComment(),order.getId());
//
//            /**********2、更新订单items**********/
//
//
//            //重新添加dc_tmall_order_item
//            String itemSQL1 = "UPDATE " + Tables.DcTmallOrderItem + " SET itemAmount=?" +
//                    ",goodsTitle=?,goodsNumber=?,productImgUrl=?,productUrl=?,specNumber=?,skuInfo=?," +
//                    "price=?,quantity=?,status=?,statusStr=?,refundStatus=?,refundStatusStr=?," +
//                    "discount_fee=?,adjust_fee=?,productId=? WHERE subItemId=? AND orderId=? ";
//
////            Integer totalQuantity=0;//商品总数
//            for (var item : order.getItems()) {
//                /*******************2.2、添加tmall_order_item**************************/
////                double itemAmount = item.getPrice().doubleValue() * item.getQuantity();
//                jdbcTemplate.update(itemSQL1, item.getItemAmount(),
//                        item.getGoodsTitle(), item.getGoodsNumber(), item.getProductImgUrl(), item.getProductUrl(), item.getSpecNumber(), item.getSkuInfo(),
//                        item.getPrice(), item.getQuantity(), item.getStatus(), item.getStatusStr(), item.getRefundStatus(), item.getRefundStatusStr(),
//                        item.getDiscountFee(), item.getAdjustFee(),item.getProductId(),
//                        item.getSubItemId(), order.getId());
//                //totalQuantity += item.getQuantity().intValue();
//            }
//
//            /**********3、更新订单收货地址（暂时不做更新）**********/
//
//            return new ResultVo<>(EnumResultVo.DataExist, "订单已经存在，并且更新成功");
//        } else {
//            try {
//                /**************1、新增tmall_order数据**********************/
//                //不存在，新增订单
//                String insertSQL = "INSERT INTO " + Tables.DcTmallOrder + " (id,buyerName,totalAmount,shippingFee,discountAmount,payAmount,discountRemark" +
//                        ",createTime,modifyTime,payTime" +
//                        ",sellerMemo,buyerFeedback,statusStr,status" +
//                        ",auditStatus,orderSource,createOn,shopId,is_comment) " +
//                        " VALUE (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//
//                jdbcTemplate.update(insertSQL, order.getId(), order.getBuyerName(), order.getTotalAmount(), order.getShippingFee(), 0.00, order.getPayAmount(), "",
//                        order.getCreateTime(), order.getModifyTime(), order.getPayTime(),
//                        order.getSellerMemo(), order.getBuyerFeedback(), order.getStatusStr(), order.getStatus(),
//                        0, 0, System.currentTimeMillis() / 1000, shopId,order.getIsComment());
//
//                //添加订单收货地址ngc
//                String addressSQL = "INSERT INTO " + Tables.DcTmallOrderAddress + " (orderId,contactPerson,mobile,province,city,area,address) VALUE (?,?,?,?,?,?,?)";
//                jdbcTemplate.update(addressSQL, order.getId(),order.getContactPerson() ,order.getMobile(), order.getProvince(), order.getCity(), order.getArea(),order.getAddress());
//
////                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                /*******************3、添加tmall_order_item**************************/
//                //添加订单明细
//                String itemSQL = "INSERT INTO " + Tables.DcTmallOrderItem + " (orderId,subItemId,itemAmount" +
//                        ",goodsTitle,goodsNumber,productImgUrl,productUrl,productId,skuId,specNumber,skuInfo,price,quantity," +
//                        "status,statusStr,refundStatus,refundStatusStr,discount_fee,adjust_fee,erpGoodsId,erpGoodsSpecId) VALUE (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//
//                Integer totalQuantity = 0;//商品总数
//                double goodsTotalAmount = 0l;//商品总价
//                for (var item : order.getItems()) {
//
//                    /*******************3、添加tmall_order_item**************************/
//
//                    /****查询sku*****/
//                    Integer goodsId = 0;
//                    Integer goodsSpecId = 0;
//                    //查询erp商品规格信息
//                    if(org.springframework.util.StringUtils.isEmpty(item.getSpecNumber())==false) {
//                        try {
//                            ErpGoodsSpecEntity erpGoodsSpec = jdbcTemplate.queryForObject("SELECT * FROM " + Tables.ErpGoodsSpec + " WHERE specNumber=?", new BeanPropertyRowMapper<>(ErpGoodsSpecEntity.class), item.getSpecNumber());
//                            goodsId = erpGoodsSpec.getGoodsId();
//                            goodsSpecId = erpGoodsSpec.getId();
//                        } catch (Exception E) {
//                        }
//                    }
//
//                    jdbcTemplate.update(itemSQL, order.getId(), item.getSubItemId(), item.getItemAmount(),
//                            item.getGoodsTitle(), item.getGoodsNumber(), item.getProductImgUrl(), item.getProductUrl(), Long.valueOf(item.getProductId()), 0, item.getSpecNumber(), item.getSkuInfo(),
//                            item.getPrice(), item.getQuantity(), item.getStatus(), item.getStatusStr(), item.getRefundStatus(), item.getRefundStatusStr(), item.getDiscountFee(), item.getAdjustFee()
//                            ,goodsId,goodsSpecId);
//
//                    totalQuantity += item.getQuantity().intValue();
//                    goodsTotalAmount += item.getPrice().doubleValue() * item.getQuantity();
//                }
//
//
//            } catch (Exception e) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                return new ResultVo<>(EnumResultVo.SystemException, "系统异常：" + e.getMessage());
//            }
//        }
        return ResultVo.success();
//        return new ResultVo<>(EnumResultVo.SUCCESS, "SUCCESS");
    }

    /**
     * Excel 导入 子订单列表
     * @param orderItemList
     * @return
     */
    @Transactional
    @Override
    public ResultVo<Integer> excelImportForSubOrder(List<OrderImportItem> orderItemList) {
        // 分组
        Map<String, List<OrderImportItem>> map = orderItemList.stream().collect(Collectors.groupingBy(t -> t.getOrderNum()));
        for (Map.Entry<String, List<OrderImportItem>> m : map.entrySet()) {
            // 判断订单是否存在
            TaoOrder taoOrder = taoOrderMapper.selectTaoOrderById(m.getKey());
            if(taoOrder == null){
                // 不存在，添加order
                List<TaoOrderItem> taoOrderItemList = new ArrayList<>();
                TaoOrder insert = new TaoOrder();
                insert.setId(m.getKey());
                List<OrderImportItem> items = m.getValue();
                insert.setShopId(items.get(0).getShopId().longValue());
                BigDecimal totalAmount = BigDecimal.ZERO;
                BigDecimal payAmount = BigDecimal.ZERO;
                for (OrderImportItem item:items) {
                    totalAmount.add(item.getAmount());
                    payAmount.add(item.getPayAmount());
                    // 添加子订单
                    TaoOrderItem orderItem = new TaoOrderItem();
                    orderItem.setOrderId(m.getKey());
                    orderItem.setSubItemId(item.getSubOrderNum());
                    orderItem.setItemAmount(item.getAmount());
                    orderItem.setGoodsTitle(item.getGoodsTitle());
                    orderItem.setGoodsNumber(item.getGoodsNumber());
                    orderItem.setProductId(Long.parseLong(item.getNumIid()));
                    orderItem.setSkuId(0L);
                    orderItem.setSkuInfo(item.getSkuInfo());
                    orderItem.setPrice(item.getPrice());
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setStatusStr(items.get(0).getStatus());
                    if("买家已付款，等待卖家发货".equals(items.get(0).getStatus())){
                        orderItem.setStatus("2");
                    }else if("等待买家确认收货".equals(items.get(0).getStatus())){
                        orderItem.setStatus("3");
                    }else{
                        orderItem.setStatus("0");
                    }
                    orderItem.setRefundStatusStr(items.get(0).getRefundStatus());
                    orderItem.setRefundStatus(0L);
//                    try {
//                        if(!StringUtils.isEmpty(items.get(0).getRefundAmount()) && !"无退款申请".equals(items.get(0).getRefundAmount())){
//                            orderItem.setRefundAmount(new BigDecimal(items.get(0).getRefundAmount()));
//                        }
//                    }catch (Exception e){}
                    orderItem.setIsGift(0);
                    orderItem.setIsSwap(0);
                    orderItem.setNewSpecId(0L);

                    taoOrderItemList.add(orderItem);
                }
                insert.setTotalAmount(totalAmount);
                insert.setPayAmount(payAmount);
                insert.setOrderCreateTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss",items.get(0).getOrderCreated()));
                insert.setPayTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss",items.get(0).getOrderPayTime()));
                insert.setSellerMemo(items.get(0).getSellerMemo());
                insert.setBuyerFeedback(items.get(0).getBuyerMemo());
                insert.setStatusStr(items.get(0).getStatus());
                if("买家已付款，等待卖家发货".equals(items.get(0).getStatus())){
                    insert.setStatus(2L);
                }else if("等待买家确认收货".equals(items.get(0).getStatus())){
                    insert.setStatus(3L);
                }else{
                    insert.setStatus(0L);
                }
                insert.setLogisticsCode(items.get(0).getLogisticsCode());
                insert.setLogisticsCompany(items.get(0).getLogisticsCom());
                try {
                    if (!StringUtils.isEmpty(items.get(0).getSendTime())) {
                        insert.setDeliveredTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", items.get(0).getSendTime()));
                    }
                    insert.setRefundStatus(items.get(0).getRefundStatus());
                    if(!StringUtils.isEmpty(items.get(0).getRefundAmount()) && !"无退款申请".equals(items.get(0).getRefundAmount())){
                        insert.setRefundAmount(new BigDecimal(items.get(0).getRefundAmount()));
                    }
                }catch (Exception e){}
                insert.setAuditStatus(0L);
                insert.setSendStatus(0L);
                insert.setIsComment(0);
                taoOrderMapper.insertTaoOrder(insert);
                if (taoOrderItemList.size() > 0)
                {
                    taoOrderMapper.batchTaoOrderItem(taoOrderItemList);
                }
            }
//            System.out.println("key:" + m.getKey() + " value:" + m.getValue());
        }


        return ResultVo.success();
    }

    @Override
    public Shop selectShopById(Long id) {
        return taoOrderMapper.selectShopById(id);
    }

    @Override
    public ShopSetting selectShopSettingById(Integer id) {
        return taoOrderMapper.selectShopSettingById(id);
    }

    @Override
    public void updateShopSessionByShopId(Long shopId, String sessionKey) {
        taoOrderMapper.updateShopSessionByShopId(shopId,sessionKey);
    }

    /**
     * 新增淘宝订单明细信息
     * 
     * @param taoOrder 淘宝订单对象
     */
    public void insertTaoOrderItem(TaoOrder taoOrder)
    {
        List<TaoOrderItem> taoOrderItemList = taoOrder.getTaoOrderItemList();
        String id = taoOrder.getId();
        if (StringUtils.isNotNull(taoOrderItemList))
        {
            List<TaoOrderItem> list = new ArrayList<TaoOrderItem>();
            for (TaoOrderItem taoOrderItem : taoOrderItemList)
            {
                taoOrderItem.setSubItemId(taoOrder.getId());
                taoOrderItem.setOrderId(id);
                taoOrderItem.setDiscountFee(BigDecimal.ZERO);
                taoOrderItem.setAdjustFee(BigDecimal.ZERO);
                taoOrderItem.setStatus("2");
                taoOrderItem.setRefundStatus(0L);
                taoOrderItem.setLogisticsStatus(0L);
                taoOrderItem.setNewSpecId(0L);
                taoOrderItem.setAfterSaleState(0L);
                taoOrderItem.setIsGift(0);
                taoOrderItem.setIsSwap(0);
                list.add(taoOrderItem);
            }
            if (list.size() > 0)
            {
                taoOrderMapper.batchTaoOrderItem(list);
            }
        }
    }
}
