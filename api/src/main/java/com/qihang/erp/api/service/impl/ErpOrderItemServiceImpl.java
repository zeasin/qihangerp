package com.qihang.erp.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qihang.common.PageQuery;
import com.qihang.common.PageResult;
import com.qihang.erp.api.domain.ErpOrder;
import com.qihang.erp.api.domain.ErpOrderItem;
import com.qihang.erp.api.mapper.ErpOrderMapper;
import com.qihang.erp.api.service.ErpOrderItemService;
import com.qihang.erp.api.mapper.ErpOrderItemMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author TW
* @description 针对表【erp_order_item(订单明细表)】的数据库操作Service实现
* @createDate 2024-04-15 13:35:02
*/
@AllArgsConstructor
@Service
public class ErpOrderItemServiceImpl extends ServiceImpl<ErpOrderItemMapper, ErpOrderItem>
    implements ErpOrderItemService{
    private final ErpOrderItemMapper mapper;
    private final ErpOrderMapper orderMapper;
    @Override
    public PageResult<ErpOrderItem> queryPageList(Integer shipType, Integer status, ErpOrderItem bo, PageQuery pageQuery) {
//        List<Long> ids = null;
//        if(shipType!=null|| status!= null){
////            LambdaQueryWrapper<ErpOrder> qw = new LambdaQueryWrapper<ErpOrder>()
////                    .eq(shipType!=null,ErpOrder::getShipType,shipType)
////                    .eq(status!=null,ErpOrder::getOrderStatus,status);
////            List<ErpOrder> erpOrders = orderMapper.selectList(qw);
//            ErpOrder qw = new ErpOrder();
//            qw.setShipType(shipType);
//            qw.setOrderStatus(status);
//            List<ErpOrder> erpOrders = orderMapper.selectErpOrderList(qw);
//            if(erpOrders!=null&&erpOrders.size()>0) {
//                ids = erpOrders.stream().map(m -> m.getId()).collect(Collectors.toList());
//            }
//        }
//        if(ids == null) return PageResult.build();

        LambdaQueryWrapper<ErpOrderItem> queryWrapper = new LambdaQueryWrapper<ErpOrderItem>()
                .eq(org.springframework.util.StringUtils.hasText(bo.getOrderNum()),ErpOrderItem::getOrderNum,bo.getOrderNum())
                .eq(org.springframework.util.StringUtils.hasText(bo.getSpecNum()),ErpOrderItem::getSpecNum,bo.getSpecNum())
                .eq(ErpOrderItem::getShipType,shipType)
                .eq(ErpOrderItem::getShipStatus,status)
                ;
        Page<ErpOrderItem> pages = mapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(pages);
    }
}




