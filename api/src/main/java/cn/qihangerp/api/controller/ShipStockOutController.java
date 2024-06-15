package cn.qihangerp.api.controller;

import cn.qihangerp.api.domain.WmsStockOutEntry;
import cn.qihangerp.api.domain.WmsStockOutEntryItem;
import cn.qihangerp.api.service.ErpOrderItemService;
import cn.qihangerp.api.service.WmsStockOutEntryItemService;
import cn.qihangerp.api.service.WmsStockOutEntryService;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.core.page.TableDataInfo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/shipping")
public class ShipStockOutController extends BaseController {
    private final ErpOrderItemService erpOrderItemService;
    private final WmsStockOutEntryService stockOutEntryService;
    private final WmsStockOutEntryItemService stockOutEntryItemService;


    /**
     * 订单待出库出库单list
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/order_stock_out_entry_list")
    public TableDataInfo stockOutEntryList(WmsStockOutEntry bo, PageQuery pageQuery)
    {
        bo.setStockOutType(1);
        bo.setStatus(0);
        PageResult<WmsStockOutEntry> list = stockOutEntryService.queryPageList(bo, pageQuery);
        return getDataTable(list);
    }

    /**
     * 订单待出库明细list
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/order_stock_out_entry_item_list")
    public TableDataInfo stockOutEntryItemList(WmsStockOutEntryItem bo, PageQuery pageQuery)
    {
        bo.setStockOutType(1);
        bo.setStatus(0);
        PageResult<WmsStockOutEntryItem> list = stockOutEntryItemService.queryPageList(bo, pageQuery);
        return getDataTable(list);
    }
}
