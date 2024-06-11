package cn.qihangerp.api.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.qihangerp.domain.Shop;
import cn.qihangerp.api.domain.SShopPlatform;
import cn.qihangerp.api.service.SShopPlatformService;
import cn.qihangerp.api.service.SShopService;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import cn.qihangerp.core.page.TableDataInfo;

/**
 * 店铺Controller
 * 
 * @author qihang
 * @date 2023-12-31
 */
@RestController
@RequestMapping("/shop/shop")
public class ShopController extends BaseController
{
    @Autowired
    private SShopService shopService;
    @Autowired
    private SShopPlatformService platformService;

    /**
     * 查询店铺列表
     */
    @PreAuthorize("@ss.hasPermi('shop:shop:list')")
    @GetMapping("/list")
    public TableDataInfo list(Shop shop)
    {
//        startPage();
//        List<Shop> list = shopService.list();
//        return getDataTable(list);
        LambdaQueryWrapper<Shop> qw = new LambdaQueryWrapper<Shop>().eq(shop.getPlatform()!=null,Shop::getPlatform,shop.getPlatform());
        List<Shop> list = shopService.list(qw);
        return getDataTable(list);
    }


    /**
     * 获取店铺详细信息
     */
    @PreAuthorize("@ss.hasPermi('shop:shop:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(shopService.getById(id));
    }

    /**
     * 新增店铺
     */
    @PreAuthorize("@ss.hasPermi('shop:shop:add')")
    @Log(title = "店铺", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Shop shop)
    {
        shop.setCreateTime(new Date());
//        shop.setUpdateTime(new Date());
        return toAjax(shopService.save(shop));
    }

    /**
     * 修改店铺
     */
    @PreAuthorize("@ss.hasPermi('shop:shop:edit')")
    @Log(title = "店铺", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Shop shop)
    {
        shop.setUpdateTime(new Date());
        return toAjax(shopService.updateById(shop));
    }

    /**
     * 删除店铺
     */
    @PreAuthorize("@ss.hasPermi('shop:shop:remove')")
    @Log(title = "店铺", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(shopService.removeByIds(Arrays.stream(ids).toList()));
    }


    @GetMapping("/platformList")
    public TableDataInfo platformList(SShopPlatform bo)
    {
        List<SShopPlatform> list = platformService.list();
        return getDataTable(list);
    }

    @GetMapping(value = "/platform/{id}")
    public AjaxResult getPlatform(@PathVariable("id") Long id)
    {
        return success(platformService.getById(id));
    }

    @PutMapping("/platform")
    public AjaxResult edit(@RequestBody SShopPlatform bo)
    {
        bo.setUpdateTime(new Date());
        return toAjax(platformService.updateById(bo));
    }
}
