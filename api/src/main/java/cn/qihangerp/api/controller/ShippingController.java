package cn.qihangerp.api.controller;

import cn.qihangerp.api.domain.bo.OrderItemSpecIdUpdateBo;
import cn.qihangerp.api.domain.bo.SupplierShipDistBo;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.enums.ErpOrderStatusEnum;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.core.page.TableDataInfo;
import cn.qihangerp.domain.ErpSaleOrderItem;
import cn.qihangerp.api.domain.bo.StockOutEntryGenerateBo;
import cn.qihangerp.api.service.ErpOrderItemService;

import cn.qihangerp.api.service.WmsStockOutEntryItemService;
import cn.qihangerp.api.service.WmsStockOutEntryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/shipping")
public class ShippingController extends BaseController {
    private final ErpOrderItemService erpOrderItemService;
    private final WmsStockOutEntryService stockOutEntryService;
    private final WmsStockOutEntryItemService stockOutEntryItemService;


    /**
     * 备货中-仓库发货
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/stock_ship_list")
    public TableDataInfo stockShipList(ErpSaleOrderItem bo, PageQuery pageQuery)
    {
        PageResult<ErpSaleOrderItem> list = erpOrderItemService.queryPageList(ErpOrderStatusEnum.WAIT_SELLER_SEND_GOODS,0,bo, pageQuery);
        return getDataTable(list);
    }

    /**
     * 新增出库单
     */
    @PostMapping("/generate_stock_out_entry")
    public AjaxResult generateStockOutEntry(@RequestBody StockOutEntryGenerateBo bo)
    {
        int result = stockOutEntryService.generateStockOutEntryForOrderItem(bo);
        if(result == -1) return AjaxResult.error("参数错误：orderItemIds为空");
        if(result == -2) return AjaxResult.error("参数错误：没有要添加的");
        else if(result == -1001) return AjaxResult.error("存在错误的orderItemId：状态不对不能生成出库单");
        else if(result == -1002) return AjaxResult.error("存在错误的订单数据：名单明细中没有skuId请修改！");
        //wmsStockOutEntryService.insertWmsStockOutEntry(wmsStockOutEntry)
        return toAjax(1);
    }

    @PostMapping("/supplier_ship_dist")
    public AjaxResult SupplierShipDist(@RequestBody SupplierShipDistBo bo)
    {
        int result = erpOrderItemService.distributeSupplierShip(bo);
        if(result == -1) return AjaxResult.error("参数错误：orderItemIds为空");
        if(result == -2) return AjaxResult.error("参数错误：存在错误的orderItemId");
        else if(result == -1001) return AjaxResult.error("存在错误的orderItemId：状态不对不能分配发货");
        else if(result == -1002) return AjaxResult.error("存在错误的订单数据：名单明细中没有supplierId请修改！");

        return toAjax(1);
    }

    /**
     * 修改订单明细specId
     * @param bo
     * @return
     */
    @PostMapping("/order_item_spec_id_update")
    public AjaxResult orderItemSpecIdUpdate(@RequestBody OrderItemSpecIdUpdateBo bo)
    {
        if(bo.getOrderItemId()==null || bo.getOrderItemId() ==0) return AjaxResult.error("参数错误：orderItemId为空");
        if(bo.getErpGoodsSpecId()==null || bo.getErpGoodsSpecId() ==0) return AjaxResult.error("参数错误：ErpGoodsSpecId为空");

        int result = erpOrderItemService.orderItemSpecIdUpdate(bo);
        if(result == -1) return AjaxResult.error("参数错误：orderItemId错误");
        else if(result == -2) return AjaxResult.error("参数错误：找不到ErpGoodsSpec");

        return toAjax(1);
    }


}
