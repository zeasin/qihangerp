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
import cn.qihangerp.api.domain.ShopGoods;
import cn.qihangerp.api.service.IShopGoodsService;
import cn.qihangerp.common.utils.poi.ExcelUtil;
import cn.qihangerp.core.page.TableDataInfo;

/**
 * 店铺商品Controller
 * 
 * @author qihang
 * @date 2023-12-31
 */
@RestController
@RequestMapping("/shop/goods")
public class ShopGoodsController extends BaseController
{
    @Autowired
    private IShopGoodsService shopGoodsService;

    /**
     * 查询店铺商品列表
     */
    @PreAuthorize("@ss.hasPermi('shop:goods:list')")
    @GetMapping("/list")
    public TableDataInfo list(ShopGoods shopGoods)
    {
        startPage();
        List<ShopGoods> list = shopGoodsService.selectShopGoodsList(shopGoods);
        return getDataTable(list);
    }

    /**
     * 导出店铺商品列表
     */
    @PreAuthorize("@ss.hasPermi('shop:goods:export')")
    @Log(title = "店铺商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ShopGoods shopGoods)
    {
        List<ShopGoods> list = shopGoodsService.selectShopGoodsList(shopGoods);
        ExcelUtil<ShopGoods> util = new ExcelUtil<ShopGoods>(ShopGoods.class);
        util.exportExcel(response, list, "店铺商品数据");
    }

    /**
     * 获取店铺商品详细信息
     */
    @PreAuthorize("@ss.hasPermi('shop:goods:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(shopGoodsService.selectShopGoodsById(id));
    }

    /**
     * 新增店铺商品
     */
    @PreAuthorize("@ss.hasPermi('shop:goods:add')")
    @Log(title = "店铺商品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ShopGoods shopGoods)
    {
        int result = shopGoodsService.insertShopGoods(shopGoods);
        if(result==10020) return AjaxResult.error("缺少参数：ShopId");
        else if(result==10021) return AjaxResult.error("找不到店铺");
        return toAjax(result);
    }

    /**
     * 修改店铺商品
     */
    @PreAuthorize("@ss.hasPermi('shop:goods:edit')")
    @Log(title = "店铺商品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ShopGoods shopGoods)
    {
        return toAjax(shopGoodsService.updateShopGoods(shopGoods));
    }

    /**
     * 删除店铺商品
     */
    @PreAuthorize("@ss.hasPermi('shop:goods:remove')")
    @Log(title = "店铺商品", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(shopGoodsService.deleteShopGoodsByIds(ids));
    }
}
