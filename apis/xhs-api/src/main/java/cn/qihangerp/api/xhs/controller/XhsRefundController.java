package cn.qihangerp.api.xhs.controller;

import java.util.List;

import cn.qihangerp.api.xhs.domain.XhsRefund;
import cn.qihangerp.api.xhs.service.IXhsRefundService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.qihangerp.common.annotation.Log;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.common.enums.BusinessType;
import cn.qihangerp.common.utils.poi.ExcelUtil;
import cn.qihangerp.core.page.TableDataInfo;

/**
 * 小红书订单退款Controller
 * 
 * @author qihang
 * @date 2024-01-13
 */
@RestController
@RequestMapping("/xhs/xhsRefund")
public class XhsRefundController extends BaseController
{
    @Autowired
    private IXhsRefundService xhsRefundService;

    /**
     * 查询小红书订单退款列表
     */
    @PreAuthorize("@ss.hasPermi('xhs:xhsRefund:list')")
    @GetMapping("/list")
    public TableDataInfo list(XhsRefund xhsRefund)
    {
        startPage();
        List<XhsRefund> list = xhsRefundService.selectXhsRefundList(xhsRefund);
        return getDataTable(list);
    }

    /**
     * 导出小红书订单退款列表
     */
    @PreAuthorize("@ss.hasPermi('xhs:xhsRefund:export')")
    @Log(title = "小红书订单退款", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, XhsRefund xhsRefund)
    {
        List<XhsRefund> list = xhsRefundService.selectXhsRefundList(xhsRefund);
        ExcelUtil<XhsRefund> util = new ExcelUtil<XhsRefund>(XhsRefund.class);
        util.exportExcel(response, list, "小红书订单退款数据");
    }

    /**
     * 获取小红书订单退款详细信息
     */
    @PreAuthorize("@ss.hasPermi('xhs:xhsRefund:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(xhsRefundService.selectXhsRefundById(id));
    }

//    /**
//     * 新增小红书订单退款
//     */
//    @PreAuthorize("@ss.hasPermi('xhs:xhsRefund:add')")
//    @Log(title = "小红书订单退款", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody XhsRefund xhsRefund)
//    {
//        return toAjax(xhsRefundService.insertXhsRefund(xhsRefund));
//    }
//
//    /**
//     * 修改小红书订单退款
//     */
//    @PreAuthorize("@ss.hasPermi('xhs:xhsRefund:edit')")
//    @Log(title = "小红书订单退款", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody XhsRefund xhsRefund)
//    {
//        return toAjax(xhsRefundService.updateXhsRefund(xhsRefund));
//    }
//
//    /**
//     * 删除小红书订单退款
//     */
//    @PreAuthorize("@ss.hasPermi('xhs:xhsRefund:remove')")
//    @Log(title = "小红书订单退款", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(xhsRefundService.deleteXhsRefundByIds(ids));
//    }
}
