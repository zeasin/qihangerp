package com.qihang.erp.api.controller;

import com.qihang.common.PageQuery;
import com.qihang.common.PageResult;
import com.qihang.core.controller.BaseController;
import com.qihang.core.page.TableDataInfo;
import com.qihang.erp.api.domain.ErpOrderItem;
import com.qihang.erp.api.service.ErpOrderItemService;
import com.qihang.erp.api.service.IErpOrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/shipping")
public class ShippingController extends BaseController {
    private final ErpOrderItemService erpOrderItemService;
    /**
     * 备货中-供应商代发
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/supplier_ship_list")
    public TableDataInfo supplierShipList(ErpOrderItem bo, PageQuery pageQuery)
    {
        PageResult<ErpOrderItem> list = erpOrderItemService.queryPageList(1,1,bo, pageQuery);
        return getDataTable(list);
    }

    /**
     * 备货中-仓库发货
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/stock_ship_list")
    public TableDataInfo stockShipList(ErpOrderItem bo, PageQuery pageQuery)
    {
        PageResult<ErpOrderItem> list = erpOrderItemService.queryPageList(0,1,bo, pageQuery);
        return getDataTable(list);
    }
}
