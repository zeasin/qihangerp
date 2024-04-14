package com.qihang.erp.api.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.qihang.common.annotation.Log;
import com.qihang.core.controller.BaseController;
import com.qihang.core.domain.AjaxResult;
import com.qihang.common.enums.BusinessType;
import com.qihang.erp.api.domain.GoodsSpec;
import com.qihang.erp.api.service.IGoodsSpecService;
import com.qihang.common.utils.poi.ExcelUtil;
import com.qihang.core.page.TableDataInfo;

/**
 * 商品规格Controller
 * 
 * @author qihang
 * @date 2023-12-29
 */
@RestController
@RequestMapping("/goods/goodsSpec")
public class GoodsSpecController extends BaseController
{
    @Autowired
    private IGoodsSpecService goodsSpecService;

    /**
     * 查询商品规格列表
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsSpec:list')")
    @GetMapping("/list")
    public TableDataInfo list(GoodsSpec goodsSpec)
    {
        startPage();
        List<GoodsSpec> list = goodsSpecService.selectGoodsSpecList(goodsSpec);
        return getDataTable(list);
    }

    /**
     * 导出商品规格列表
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsSpec:export')")
    @Log(title = "商品规格", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GoodsSpec goodsSpec)
    {
        List<GoodsSpec> list = goodsSpecService.selectGoodsSpecList(goodsSpec);
        ExcelUtil<GoodsSpec> util = new ExcelUtil<GoodsSpec>(GoodsSpec.class);
        util.exportExcel(response, list, "商品规格数据");
    }

    /**
     * 获取商品规格详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:goodsSpec:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(goodsSpecService.selectGoodsSpecById(id));
    }

//    /**
//     * 新增商品规格
//     */
//    @PreAuthorize("@ss.hasPermi('goods:goodsSpec:add')")
//    @Log(title = "商品规格", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody GoodsSpec goodsSpec)
//    {
//        return toAjax(goodsSpecService.insertGoodsSpec(goodsSpec));
//    }
//
//    /**
//     * 修改商品规格
//     */
//    @PreAuthorize("@ss.hasPermi('goods:goodsSpec:edit')")
//    @Log(title = "商品规格", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody GoodsSpec goodsSpec)
//    {
//        return toAjax(goodsSpecService.updateGoodsSpec(goodsSpec));
//    }
//
//    /**
//     * 删除商品规格
//     */
//    @PreAuthorize("@ss.hasPermi('goods:goodsSpec:remove')")
//    @Log(title = "商品规格", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(goodsSpecService.deleteGoodsSpecByIds(ids));
//    }
}
