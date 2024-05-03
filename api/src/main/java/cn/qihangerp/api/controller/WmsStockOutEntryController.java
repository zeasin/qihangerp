package cn.qihangerp.api.controller;

import cn.qihangerp.api.domain.WmsStockOutEntryItem;
import cn.qihangerp.api.service.WmsStockOutEntryItemService;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.api.domain.bo.StockOutBo;
import cn.qihangerp.api.service.WmsStockOutEntryService;
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
import cn.qihangerp.api.domain.WmsStockOutEntry;
//import cn.qihangerp.api.service.IWmsStockOutEntryService;
import cn.qihangerp.core.page.TableDataInfo;

/**
 * 出库单Controller
 *
 * @author qihang
 * @date 2024-01-10
 */
@RestController
@RequestMapping("/wms/stockOutEntry")
public class WmsStockOutEntryController extends BaseController
{
    @Autowired
    private WmsStockOutEntryService wmsStockOutEntryService;
    @Autowired
    private WmsStockOutEntryItemService wmsStockOutEntryItemService;

    /**
     * 查询出库单列表
     */
    @PreAuthorize("@ss.hasPermi('wms:stockOutEntry:list')")
    @GetMapping("/list")
    public TableDataInfo list(WmsStockOutEntry bo, PageQuery pageQuery)
    {
        var list = wmsStockOutEntryService.queryPageList(bo, pageQuery);
        return getDataTable(list);
    }

    /**
     * 导出出库单列表
     */
//    @PreAuthorize("@ss.hasPermi('wms:stockOutEntry:export')")
//    @Log(title = "出库单", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, WmsStockOutEntry wmsStockOutEntry)
//    {
//        List<WmsStockOutEntry> list = wmsStockOutEntryService.selectWmsStockOutEntryList(wmsStockOutEntry);
//        ExcelUtil<WmsStockOutEntry> util = new ExcelUtil<WmsStockOutEntry>(WmsStockOutEntry.class);
//        util.exportExcel(response, list, "出库单数据");
//    }

    /**
     * 获取出库单详细信息
     */
    @PreAuthorize("@ss.hasPermi('wms:stockOutEntry:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
//        WmsStockOutEntry entry = wmsStockOutEntryService.selectById(id);

        return success(wmsStockOutEntryService.selectOutEntryItemInventoryDetailsByEntryId(id));
    }
    /**
     * 获取出库明细详情详细信息
     */

    @GetMapping(value = "/item/{id}")
    public AjaxResult getItemInfo(@PathVariable("id") Long id)
    {
        return success(wmsStockOutEntryItemService.getById(id));
    }
    /**
     * 出库操作
     * @param bo
     * @return
     */
    @Log(title = "出库单", businessType = BusinessType.INSERT)
    @PostMapping("/stockOut")
    public AjaxResult stockOut(@RequestBody StockOutBo bo)
    {
        bo.setOperatorId(getUserId());
        bo.setOperatorName(getUsername());
        int result = wmsStockOutEntryService.stockOut(bo);
        if(result == -5) return new AjaxResult(500,"参数错误！请填写出库数量！");
        else if(result == -1) return new AjaxResult(501,"数据错误！");
        else if(result == -2) return new AjaxResult(502,"状态错误！");
        else if(result == -3) return new AjaxResult(503,"已全部出库！");
        else if(result == -4) return new AjaxResult(504,"出库数量超出！");
        else if(result == -11) return new AjaxResult(511,"库存数据不存在！");
        else if(result == -12) return new AjaxResult(512,"仓位库存不足！");
        return toAjax(1);
    }

//    /**
//     * 新增出库单
//     */
//    @PreAuthorize("@ss.hasPermi('wms:stockOutEntry:add')")
//    @Log(title = "出库单", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody WmsStockOutEntry wmsStockOutEntry)
//    {
//        return toAjax(wmsStockOutEntryService.insertWmsStockOutEntry(wmsStockOutEntry));
//    }

//    /**
//     * 修改出库单
//     */
//    @PreAuthorize("@ss.hasPermi('wms:stockOutEntry:edit')")
//    @Log(title = "出库单", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody WmsStockOutEntry wmsStockOutEntry)
//    {
//        return toAjax(wmsStockOutEntryService.updateWmsStockOutEntry(wmsStockOutEntry));
//    }

//    /**
//     * 删除出库单
//     */
//    @PreAuthorize("@ss.hasPermi('wms:stockOutEntry:remove')")
//    @Log(title = "出库单", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(wmsStockOutEntryService.deleteWmsStockOutEntryByIds(ids));
//    }
}
