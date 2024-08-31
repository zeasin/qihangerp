package cn.qihangerp.api.dou.controller;

import cn.qihangerp.api.dou.domain.OmsDouGoods;
import cn.qihangerp.api.dou.domain.OmsDouGoodsSku;
import cn.qihangerp.api.dou.domain.bo.LinkErpGoodsSkuBo;
import cn.qihangerp.api.dou.mapper.OmsDouGoodsMapper;
import cn.qihangerp.api.dou.service.OmsDouGoodsService;
import cn.qihangerp.api.dou.service.OmsDouGoodsSkuService;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.core.page.TableDataInfo;
import cn.qihangerp.domain.AjaxResult;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/dou-api/goods")
@RestController
@AllArgsConstructor
public class DouGoodsController extends BaseController {
    private final OmsDouGoodsService goodsService;
    private final OmsDouGoodsSkuService skuService;
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableDataInfo goodsList(OmsDouGoods bo, PageQuery pageQuery) {
        PageResult<OmsDouGoods> result = goodsService.queryPageList(bo, pageQuery);

        return getDataTable(result);
    }

    @RequestMapping(value = "/skuList", method = RequestMethod.GET)
    public TableDataInfo skuList(OmsDouGoodsSku bo, PageQuery pageQuery) {
        PageResult<OmsDouGoodsSku> result = skuService.queryPageList(bo, pageQuery);

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
        OmsDouGoodsSku sku = new OmsDouGoodsSku();
        sku.setId(bo.getId());
        sku.setErpGoodsSkuId(Long.parseLong(bo.getErpGoodsSkuId()));
        skuService.updateById(sku);
        return success();
    }

}
