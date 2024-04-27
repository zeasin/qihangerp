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
import cn.qihangerp.api.domain.ErpOrderReturned;
import cn.qihangerp.api.service.IErpOrderReturnedService;
import cn.qihangerp.common.utils.poi.ExcelUtil;
import cn.qihangerp.core.page.TableDataInfo;

/**
 * 退换货Controller
 * 
 * @author qihang
 * @date 2024-01-13
 */
@RestController
@RequestMapping("/api/returned")
public class ErpOrderReturnedController extends BaseController
{
    @Autowired
    private IErpOrderReturnedService erpOrderReturnedService;

    /**
     * 查询退换货列表
     */
    @PreAuthorize("@ss.hasPermi('api:returned:list')")
    @GetMapping("/list")
    public TableDataInfo list(ErpOrderReturned erpOrderReturned)
    {
        startPage();
        List<ErpOrderReturned> list = erpOrderReturnedService.selectErpOrderReturnedList(erpOrderReturned);
        return getDataTable(list);
    }

    /**
     * 导出退换货列表
     */
    @PreAuthorize("@ss.hasPermi('api:returned:export')")
    @Log(title = "退换货", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ErpOrderReturned erpOrderReturned)
    {
        List<ErpOrderReturned> list = erpOrderReturnedService.selectErpOrderReturnedList(erpOrderReturned);
        ExcelUtil<ErpOrderReturned> util = new ExcelUtil<ErpOrderReturned>(ErpOrderReturned.class);
        util.exportExcel(response, list, "退换货数据");
    }

    /**
     * 获取退换货详细信息
     */
    @PreAuthorize("@ss.hasPermi('api:returned:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(erpOrderReturnedService.selectErpOrderReturnedById(id));
    }

//    /**
//     * 新增退换货
//     */
//    @PreAuthorize("@ss.hasPermi('api:returned:add')")
//    @Log(title = "退换货", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody ErpOrderReturned erpOrderReturned)
//    {
//        return toAjax(erpOrderReturnedService.insertErpOrderReturned(erpOrderReturned));
//    }

    /**
     * 修改退换货
     */
    @PreAuthorize("@ss.hasPermi('api:returned:edit')")
    @Log(title = "退换货", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ErpOrderReturned erpOrderReturned)
    {
        //erpOrderReturnedService.updateErpOrderReturned(erpOrderReturned)
        return toAjax(1);
    }

//    /**
//     * 删除退换货
//     */
//    @PreAuthorize("@ss.hasPermi('api:returned:remove')")
//    @Log(title = "退换货", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(erpOrderReturnedService.deleteErpOrderReturnedByIds(ids));
//    }
}
