package cn.qihangerp.api.pdd.controller;

import cn.qihangerp.api.pdd.bo.LinkErpGoodsSkuBo;
import cn.qihangerp.api.pdd.domain.OmsPddGoods;
import cn.qihangerp.api.pdd.domain.OmsPddGoodsSku;
import cn.qihangerp.api.pdd.service.OmsPddGoodsService;
import cn.qihangerp.api.pdd.service.OmsPddGoodsSkuService;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.core.page.TableDataInfo;
import cn.qihangerp.domain.AjaxResult;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/pdd-api/goods")
@RestController
@AllArgsConstructor
public class PddGoodsController extends BaseController {
    private final OmsPddGoodsService goodsService;
    private final OmsPddGoodsSkuService skuService;
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableDataInfo goodsList(OmsPddGoods bo, PageQuery pageQuery) {
        PageResult<OmsPddGoods> result = goodsService.queryPageList(bo, pageQuery);

        return getDataTable(result);
    }

    @RequestMapping(value = "/skuList", method = RequestMethod.GET)
    public TableDataInfo skuList(OmsPddGoodsSku bo, PageQuery pageQuery) {
        PageResult<OmsPddGoodsSku> result = skuService.queryPageList(bo, pageQuery);

        return getDataTable(result);
    }

    /**
     * 获取店铺订单详细信息
     */
    @GetMapping(value = "/sku/{id}")
    public AjaxResult getSkuInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(skuService.getById(id));
    }
    @PostMapping(value = "/sku/linkErp")
    public AjaxResult linkErp(@RequestBody LinkErpGoodsSkuBo bo)
    {
        OmsPddGoodsSku sku = new OmsPddGoodsSku();
        sku.setId(bo.getId());
        sku.setErpGoodsSkuId(Long.parseLong(bo.getErpGoodsSkuId()));
        skuService.updateById(sku);
        return success();
    }

}
