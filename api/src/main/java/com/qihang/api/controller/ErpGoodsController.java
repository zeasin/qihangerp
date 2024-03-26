package com.qihang.api.controller;

import com.qihang.api.common.BaseController;
import com.qihang.api.common.TableDataInfo;
import com.qihang.common.PageQuery;
import com.qihang.common.PageResult;
import com.qihang.interfaces.goods.domain.ErpGoods;
import com.qihang.interfaces.goods.service.ErpGoodsService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/goods/goods")
@RestController
public class ErpGoodsController extends BaseController {
    @DubboReference
    private ErpGoodsService goodsService;
    @GetMapping("/list")
    public TableDataInfo list(ErpGoods goods, PageQuery pageQuery)
    {
        PageResult<ErpGoods> pageResult = goodsService.queryPageList(goods, pageQuery);
        return getDataTable(pageResult);
    }
}
