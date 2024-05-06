package cn.qihangerp.api.wei.controller;

import cn.qihangerp.api.wei.domain.OmsWeiGoods;
import cn.qihangerp.api.wei.domain.OmsWeiGoodsSku;
import cn.qihangerp.api.wei.service.OmsWeiGoodsSkuService;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.core.page.TableDataInfo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
