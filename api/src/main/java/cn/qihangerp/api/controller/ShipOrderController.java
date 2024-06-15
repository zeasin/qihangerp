package cn.qihangerp.api.controller;

import cn.qihangerp.api.domain.ErpShipOrder;
import cn.qihangerp.api.domain.bo.ShipOrderSupplierShipBo;
import cn.qihangerp.api.service.ErpShipOrderService;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.core.page.TableDataInfo;
import cn.qihangerp.domain.AjaxResult;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/shipping")
public class ShipOrderController extends BaseController {
    private final ErpShipOrderService shipOrderService;
    /**
     * 备货中-仓库发货
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/ship_order")
    public TableDataInfo stockShipList(ErpShipOrder bo, PageQuery pageQuery)
    {
        PageResult<ErpShipOrder> list = shipOrderService.queryPageList(bo, pageQuery);
        return getDataTable(list);
    }

    /**
     * 查询同订单itemList
     * @param id
     * @return
     */
    @GetMapping("/ship_order/{id}")
    public TableDataInfo stockShipInfo(@PathVariable("id") Long id)
    {
        List<ErpShipOrder> list = shipOrderService.queryOrderListById(id);
        return getDataTable(list);
    }


    /**
     * 供应商代发订单发货
     * @param bo
     * @return
     */
    @PostMapping("/ship_order/supplier_ship")
    public AjaxResult supplierShip(@RequestBody ShipOrderSupplierShipBo bo)
    {
        int result = shipOrderService.supplierShip(bo);
        if(result == -1) return AjaxResult.error("参数错误：shipOrderId为空");
        else if(result == -2) return AjaxResult.error("参数错误：erpOrderId为空");
        else if(result == -3) return AjaxResult.error("参数错误：erpOrderId找不到数据");
        else if(result == -1001) return AjaxResult.error("存在错误的shipOrderId：找不到数据");
        else if(result == -1002) return AjaxResult.error("存在错误的shipOrder数据：不是供应商发货！");
        else if(result == -1003) return AjaxResult.error("存在错误的shipOrder数据：发货状态不正确！");
        //wmsStockOutEntryService.insertWmsStockOutEntry(wmsStockOutEntry)
        return toAjax(1);
    }

    /**
     * 仓库发货订单发货
     * @param bo
     * @return
     */
    @PostMapping("/ship_order/wms_ship")
    public AjaxResult wmsShip(@RequestBody ShipOrderSupplierShipBo bo)
    {
        int result = shipOrderService.wmsShip(bo);
        if(result == -1) return AjaxResult.error("参数错误：shipOrderId为空");
        else if(result == -2) return AjaxResult.error("参数错误：erpOrderId为空");
        else if(result == -3) return AjaxResult.error("参数错误：erpOrderId找不到数据");
        else if(result == -1001) return AjaxResult.error("存在错误的shipOrderId：找不到数据");
        else if(result == -1002) return AjaxResult.error("存在错误的shipOrder数据：不是供应商发货！");
        else if(result == -1003) return AjaxResult.error("存在错误的shipOrder数据：发货状态不正确！");
        //wmsStockOutEntryService.insertWmsStockOutEntry(wmsStockOutEntry)
        return toAjax(1);
    }



}
