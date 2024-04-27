package cn.qihangerp.api.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.qihangerp.common.annotation.Log;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.common.enums.BusinessType;
import cn.qihangerp.api.domain.ScmPurchaseOrderCost;
import cn.qihangerp.api.service.IScmPurchaseOrderCostService;
import cn.qihangerp.common.utils.poi.ExcelUtil;
import cn.qihangerp.core.page.TableDataInfo;

/**
 * 采购订单费用确认Controller
 * 
 * @author qihang
 * @date 2023-12-30
 */
@RestController
@RequestMapping("/purchase/purchaseOrderCost")
public class ScmPurchaseOrderCostController extends BaseController
{
    @Autowired
    private IScmPurchaseOrderCostService scmPurchaseOrderCostService;

    /**
     * 查询采购订单费用确认列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:purchaseOrderCost:list')")
    @GetMapping("/list")
    public TableDataInfo list(ScmPurchaseOrderCost scmPurchaseOrderCost)
    {
        startPage();
        List<ScmPurchaseOrderCost> list = scmPurchaseOrderCostService.selectScmPurchaseOrderCostList(scmPurchaseOrderCost);
        return getDataTable(list);
    }

    /**
     * 导出采购订单费用确认列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:purchaseOrderCost:export')")
    @Log(title = "采购订单费用确认", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ScmPurchaseOrderCost scmPurchaseOrderCost)
    {
        List<ScmPurchaseOrderCost> list = scmPurchaseOrderCostService.selectScmPurchaseOrderCostList(scmPurchaseOrderCost);
        ExcelUtil<ScmPurchaseOrderCost> util = new ExcelUtil<ScmPurchaseOrderCost>(ScmPurchaseOrderCost.class);
        util.exportExcel(response, list, "采购订单费用确认数据");
    }

    /**
     * 获取采购订单费用确认详细信息
     */
    @PreAuthorize("@ss.hasPermi('purchase:purchaseOrderCost:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(scmPurchaseOrderCostService.selectScmPurchaseOrderCostById(id));
    }



    /**
     * 修改采购订单费用确认
     */
    @PreAuthorize("@ss.hasPermi('purchase:purchaseOrderCost:edit')")
    @Log(title = "采购订单费用确认", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ScmPurchaseOrderCost scmPurchaseOrderCost)
    {
        scmPurchaseOrderCost.setUpdateBy(getUsername());
        return toAjax(scmPurchaseOrderCostService.updateScmPurchaseOrderCost(scmPurchaseOrderCost));
    }


}
