package cn.qihangerp.api.service;

import cn.qihangerp.api.domain.bo.OrderItemSpecIdUpdateBo;
import cn.qihangerp.api.domain.bo.SupplierShipDistBo;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.enums.ErpOrderStatusEnum;
import cn.qihangerp.domain.ErpSaleOrderItem;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TW
* @description 针对表【erp_order_item(订单明细表)】的数据库操作Service
* @createDate 2024-04-15 13:35:02
*/
public interface ErpOrderItemService extends IService<ErpSaleOrderItem> {
    PageResult<ErpSaleOrderItem> queryPageList(ErpOrderStatusEnum status, Integer shipStatus, ErpSaleOrderItem bo, PageQuery pageQuery);

    int orderItemSpecIdUpdate(OrderItemSpecIdUpdateBo bo);

    /**
     * 分配给供应商发货
     * @param bo
     * @return
     */
    int distributeSupplierShip(SupplierShipDistBo bo);
}
