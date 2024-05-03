package cn.qihangerp.api.service;

import cn.qihangerp.api.domain.ErpShipOrder;
import cn.qihangerp.api.domain.WmsStockOutEntry;
import cn.qihangerp.api.domain.bo.ShipOrderSupplierShipBo;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author qilip
* @description 针对表【erp_ship_order(订单发货表)】的数据库操作Service
* @createDate 2024-05-02 10:15:15
*/
public interface ErpShipOrderService extends IService<ErpShipOrder> {
    PageResult<ErpShipOrder> queryPageList(ErpShipOrder bo, PageQuery pageQuery);
    List<ErpShipOrder> queryOrderListById(Long id);

    /**
     * 供应商发货
     * @param bo
     * @return
     */
    int supplierShip(ShipOrderSupplierShipBo bo);
    int wmsShip(ShipOrderSupplierShipBo bo);
}
