package cn.qihangerp.api.controller;

import java.util.List;

import cn.qihangerp.domain.ErpSaleOrder;
import jakarta.servlet.http.HttpServletResponse;

import cn.qihangerp.api.service.IErpOrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.qihangerp.common.annotation.Log;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.common.enums.BusinessType;
import cn.qihangerp.common.utils.poi.ExcelUtil;
import cn.qihangerp.core.page.TableDataInfo;

/**
 * 店铺订单Controller
 *
 * @author qihang
 * @date 2023-12-31
 */
@RestController
@RequestMapping("/api/order")
public class ErpOrderController extends BaseController
{
    @Autowired
    private IErpOrderService orderService;

    /**
     * 查询店铺订单列表
     */
    @PreAuthorize("@ss.hasPermi('shop:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(ErpSaleOrder order)
    {
        startPage();
        List<ErpSaleOrder> list = orderService.selectErpOrderList(order);
        return getDataTable(list);
    }

    /**
     * 导出店铺订单列表
     */
    @PreAuthorize("@ss.hasPermi('shop:order:export')")
    @Log(title = "店铺订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ErpSaleOrder order)
    {
        List<ErpSaleOrder> list = orderService.selectErpOrderList(order);
        ExcelUtil<ErpSaleOrder> util = new ExcelUtil<ErpSaleOrder>(ErpSaleOrder.class);
        util.exportExcel(response, list, "店铺订单数据");
    }

    /**
     * 获取店铺订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('shop:order:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(orderService.selectErpOrderById(id));
    }

    /**
     * 新增店铺订单
     */
    @PreAuthorize("@ss.hasPermi('shop:order:add')")
    @Log(title = "店铺订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ErpSaleOrder order)
    {
        if(order.getGoodsAmount()==null)return new AjaxResult(1503,"请填写商品价格！");
        order.setCreateBy(getUsername());
        int result = orderService.insertErpOrder(order);
        if(result == -1) return new AjaxResult(501,"订单号已存在！");
        if(result == -2) return new AjaxResult(502,"请添加订单商品！");
        if(result == -3) return new AjaxResult(503,"请选择订单商品规格！");
        if(result == -4) return new AjaxResult(504,"请选择店铺！");
        return toAjax(result);
    }

    /**
     * 订单发货(发货操作移到erpShipOrder中)
     * @param order
     * @return
     */
//    @Log(title = "店铺订单", businessType = BusinessType.UPDATE)
//    @PostMapping("/ship")
//    public AjaxResult ship(@RequestBody ErpOrder order)
//    {
//        order.setUpdateBy(getUsername());
//        int result = orderService.shipErpOrder(order);
//        if(result == -1) return new AjaxResult(501,"订单不存在！");
//        else if(result == -2) return new AjaxResult(502,"订单状态不对！无法发货！");
//        else if(result == -3) return new AjaxResult(502,"订单发货状态不对！无法发货！");
//        return toAjax(result);
//    }

}
