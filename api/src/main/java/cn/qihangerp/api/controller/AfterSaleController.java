package cn.qihangerp.api.controller;

import cn.qihangerp.api.domain.ErpSaleAfterInfo;
import cn.qihangerp.api.service.ErpSaleAfterInfoService;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.core.page.TableDataInfo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@AllArgsConstructor
@RestController
@RequestMapping("/afterSale")
public class AfterSaleController extends BaseController {
    private final ErpSaleAfterInfoService afterInfoService;
    /**
     * 查询列表
     */
    @GetMapping("/ship_again_list")
    public TableDataInfo ship_again_list(ErpSaleAfterInfo bo, PageQuery pageQuery)
    {
        bo.setType(80);
        PageResult<ErpSaleAfterInfo> result = afterInfoService.queryPageList(bo, pageQuery);
        return getDataTable(result);
    }

    @PostMapping("/shipAgain")
    public AjaxResult shipAgainAdd(@RequestBody ErpSaleAfterInfo addBo)
    {
        addBo.setType(80);
        addBo.setCreateTime(new Date());
        addBo.setCreateBy("手动添加");
        addBo.setStatus(1);
        var result = afterInfoService.save(addBo);
        return toAjax(result);
    }

    @PutMapping("/shipAgain/complete/{id}")
    public AjaxResult completeShipAgain(@PathVariable Long id)
    {
        ErpSaleAfterInfo complete = new ErpSaleAfterInfo();
        complete.setId(id.toString());
        complete.setStatus(2);
        complete.setUpdateTime(new Date());
        complete.setUpdateBy("手动完成");
        afterInfoService.updateById(complete);
        return toAjax(1);
    }


    @GetMapping("/returned_list")
    public TableDataInfo returned_list(ErpSaleAfterInfo bo, PageQuery pageQuery)
    {
        bo.setType(10);
        PageResult<ErpSaleAfterInfo> result = afterInfoService.queryPageList(bo, pageQuery);
        return getDataTable(result);
    }

    @GetMapping("/exchange_list")
    public TableDataInfo exchange_list(ErpSaleAfterInfo bo, PageQuery pageQuery)
    {
        bo.setType(20);
        PageResult<ErpSaleAfterInfo> result = afterInfoService.queryPageList(bo, pageQuery);
        return getDataTable(result);
    }

    @PostMapping("/exchange")
    public AjaxResult exchangeAdd(@RequestBody ErpSaleAfterInfo addBo)
    {
        addBo.setType(20);
        addBo.setCreateTime(new Date());
        addBo.setCreateBy("手动添加");
        addBo.setStatus(1);
        var result = afterInfoService.save(addBo);
        return toAjax(result);
    }

    @GetMapping("/intercept_list")
    public TableDataInfo intercept_list(ErpSaleAfterInfo bo, PageQuery pageQuery)
    {
        bo.setType(99);
        PageResult<ErpSaleAfterInfo> result = afterInfoService.queryPageList(bo, pageQuery);
        return getDataTable(result);
    }


}
