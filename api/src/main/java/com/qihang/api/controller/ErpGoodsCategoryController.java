package com.qihang.api.controller;

import com.qihang.api.common.BaseController;
import com.qihang.api.common.TableDataInfo;
import com.qihang.interfaces.goods.domain.ErpGoodsCategory;
import com.qihang.interfaces.goods.service.ErpGoodsCategoryService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/goods/category")
public class ErpGoodsCategoryController extends BaseController {
    @DubboReference
    private ErpGoodsCategoryService categoryService;

    @GetMapping("/list")
    public TableDataInfo list(ErpGoodsCategory erpGoodsCategory)
    {
//        startPage();
        List<ErpGoodsCategory> list = categoryService.list();
        return getDataTable(list);
    }
}
