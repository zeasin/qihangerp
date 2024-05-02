package cn.qihangerp.api.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.qihangerp.common.annotation.Log;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.common.enums.BusinessType;
import cn.qihangerp.api.domain.ErpShipLogistics;
import cn.qihangerp.api.service.ErpShipLogisticsService;
import cn.qihangerp.common.utils.poi.ExcelUtil;
import cn.qihangerp.core.page.TableDataInfo;

/**
 * 物流公司Controller
 *
 * @author qihang
 * @date 2024-01-12
 */
@RestController
@RequestMapping("/api/logistics")
public class ErpShipLogisticsController extends BaseController
{
    @Autowired
    private ErpShipLogisticsService bLogisticsCompanyService;

    /**
     * 查询物流公司列表
     */
    @PreAuthorize("@ss.hasPermi('api:logistics:list')")
    @GetMapping("/list")
    public TableDataInfo list(ErpShipLogistics bLogisticsCompany)
    {
        startPage();
        List<ErpShipLogistics> list = bLogisticsCompanyService.selectBLogisticsCompanyList(bLogisticsCompany);
        return getDataTable(list);
    }

    /**
     * 导出物流公司列表
     */
    @PreAuthorize("@ss.hasPermi('api:logistics:export')")
    @Log(title = "物流公司", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ErpShipLogistics bLogisticsCompany)
    {
        List<ErpShipLogistics> list = bLogisticsCompanyService.selectBLogisticsCompanyList(bLogisticsCompany);
        ExcelUtil<ErpShipLogistics> util = new ExcelUtil<ErpShipLogistics>(ErpShipLogistics.class);
        util.exportExcel(response, list, "物流公司数据");
    }

    /**
     * 获取物流公司详细信息
     */
    @PreAuthorize("@ss.hasPermi('api:logistics:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bLogisticsCompanyService.selectBLogisticsCompanyById(id));
    }

    /**
     * 新增物流公司
     */
    @PreAuthorize("@ss.hasPermi('api:logistics:add')")
    @Log(title = "物流公司", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ErpShipLogistics bLogisticsCompany)
    {
        return toAjax(bLogisticsCompanyService.insertBLogisticsCompany(bLogisticsCompany));
    }

    /**
     * 修改物流公司
     */
    @PreAuthorize("@ss.hasPermi('api:logistics:edit')")
    @Log(title = "物流公司", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ErpShipLogistics bLogisticsCompany)
    {
        return toAjax(bLogisticsCompanyService.updateBLogisticsCompany(bLogisticsCompany));
    }

    /**
     * 删除物流公司
     */
    @PreAuthorize("@ss.hasPermi('api:logistics:remove')")
    @Log(title = "物流公司", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bLogisticsCompanyService.deleteBLogisticsCompanyByIds(ids));
    }
}
