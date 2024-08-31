package cn.qihangerp.api.wei.controller;

import cn.qihangerp.api.wei.bo.LinkErpGoodsSkuBo;
import cn.qihangerp.api.wei.domain.OmsWeiGoods;
import cn.qihangerp.api.wei.domain.OmsWeiGoodsSku;
import cn.qihangerp.api.wei.service.OmsWeiGoodsSkuService;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.core.page.TableDataInfo;
import cn.qihangerp.domain.AjaxResult;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/wei-api/goods")
@RestController
@AllArgsConstructor
public class WeiGoodsController extends BaseController {
    private final OmsWeiGoodsSkuService skuService;

    @RequestMapping(value = "/skuList", method = RequestMethod.GET)
    public TableDataInfo skuList(OmsWeiGoodsSku bo, PageQuery pageQuery) {
        PageResult<OmsWeiGoodsSku> result = skuService.queryPageList(bo, pageQuery);

        return getDataTable(result);
    }

    /**
     *
     */
    @GetMapping(value = "/sku/{id}")
    public AjaxResult getSkuInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(skuService.getById(id));
    }
    @PostMapping(value = "/sku/linkErp")
    public AjaxResult linkErp(@RequestBody LinkErpGoodsSkuBo bo)
    {
        OmsWeiGoodsSku sku = new OmsWeiGoodsSku();
        sku.setId(bo.getId());
        sku.setErpGoodsSkuId(Long.parseLong(bo.getErpGoodsSkuId()));
        skuService.updateById(sku);
        return success();
    }
}
