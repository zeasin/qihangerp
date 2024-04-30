package cn.qihangerp.open.tao.controller;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.core.page.TableDataInfo;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.open.tao.domain.OmsTaoGoods;
import cn.qihangerp.open.tao.domain.OmsTaoGoodsSku;
import cn.qihangerp.open.tao.domain.bo.LinkErpGoodsSkuBo;
import cn.qihangerp.open.tao.domain.vo.TaoGoodsSkuListVo;
import cn.qihangerp.open.tao.service.OmsTaoGoodsService;
import cn.qihangerp.open.tao.service.OmsTaoGoodsSkuService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/tao-api/goods")
@RestController
@AllArgsConstructor
public class TaoGoodsController extends BaseController {
    private final OmsTaoGoodsService goodsService;
    private final OmsTaoGoodsSkuService skuService;
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableDataInfo goodsList(OmsTaoGoods bo, PageQuery pageQuery) {
        PageResult<OmsTaoGoods> result = goodsService.queryPageList(bo, pageQuery);

        return getDataTable(result);
    }

    @RequestMapping(value = "/skuList", method = RequestMethod.GET)
    public TableDataInfo skuList(OmsTaoGoods bo, PageQuery pageQuery) {
        PageResult<TaoGoodsSkuListVo> result = skuService.queryPageList(bo, pageQuery);

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
        OmsTaoGoodsSku sku = new OmsTaoGoodsSku();
        sku.setId(bo.getId());
        sku.setErpGoodsSkuId(Long.parseLong(bo.getErpGoodsSkuId()));
        skuService.updateById(sku);
        return success();
    }

}
