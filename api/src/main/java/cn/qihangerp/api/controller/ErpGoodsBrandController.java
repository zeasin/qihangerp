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
import cn.qihangerp.api.domain.ErpGoodsBrand;
import cn.qihangerp.api.service.IErpGoodsBrandService;
import cn.qihangerp.common.utils.poi.ExcelUtil;
import cn.qihangerp.core.page.TableDataInfo;

/**
 * 商品品牌Controller
 * 
 * @author qihang
 * @date 2023-12-29
 */
@RestController
@RequestMapping("/goods/brand")
public class ErpGoodsBrandController extends BaseController
{
    @Autowired
    private IErpGoodsBrandService erpGoodsBrandService;

    /**
     * 查询商品品牌列表
     */
    @PreAuthorize("@ss.hasPermi('goods:brand:list')")
    @GetMapping("/list")
    public TableDataInfo list(ErpGoodsBrand erpGoodsBrand)
    {
        startPage();
        List<ErpGoodsBrand> list = erpGoodsBrandService.selectErpGoodsBrandList(erpGoodsBrand);
        return getDataTable(list);
    }

    /**
     * 导出商品品牌列表
     */
    @PreAuthorize("@ss.hasPermi('goods:brand:export')")
    @Log(title = "商品品牌", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ErpGoodsBrand erpGoodsBrand)
    {
        List<ErpGoodsBrand> list = erpGoodsBrandService.selectErpGoodsBrandList(erpGoodsBrand);
        ExcelUtil<ErpGoodsBrand> util = new ExcelUtil<ErpGoodsBrand>(ErpGoodsBrand.class);
        util.exportExcel(response, list, "商品品牌数据");
    }

    /**
     * 获取商品品牌详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:brand:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(erpGoodsBrandService.selectErpGoodsBrandById(id));
    }

    /**
     * 新增商品品牌
     */
    @PreAuthorize("@ss.hasPermi('goods:brand:add')")
    @Log(title = "商品品牌", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ErpGoodsBrand erpGoodsBrand)
    {
        return toAjax(erpGoodsBrandService.insertErpGoodsBrand(erpGoodsBrand));
    }

    /**
     * 修改商品品牌
     */
    @PreAuthorize("@ss.hasPermi('goods:brand:edit')")
    @Log(title = "商品品牌", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ErpGoodsBrand erpGoodsBrand)
    {
        return toAjax(erpGoodsBrandService.updateErpGoodsBrand(erpGoodsBrand));
    }

    /**
     * 删除商品品牌
     */
    @PreAuthorize("@ss.hasPermi('goods:brand:remove')")
    @Log(title = "商品品牌", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(erpGoodsBrandService.deleteErpGoodsBrandByIds(ids));
    }
}
