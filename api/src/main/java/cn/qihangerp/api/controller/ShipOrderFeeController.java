package cn.qihangerp.api.controller;

import java.util.List;

import cn.qihangerp.api.domain.ErpShipOrderFee;
import cn.qihangerp.api.service.ErpShipOrderFeeService;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.core.page.TableDataInfo;

/**
 * 财务管理-应付款-物流费用Controller
 * 
 * @author qihang
 * @date 2024-01-28
 */
@RestController
@RequestMapping("/fms/shipFee")
public class ShipOrderFeeController extends BaseController
{
    @Autowired
    private ErpShipOrderFeeService feeService;

    /**
     * 查询财务管理-应付款-物流费用列表
     */
    @PreAuthorize("@ss.hasPermi('fms:shipFee:list')")
    @GetMapping("/list")
    public TableDataInfo list(ErpShipOrderFee bo, PageQuery pageQuery)
    {
        PageResult<ErpShipOrderFee> pageResult = feeService.queryPageList(bo, pageQuery);
        return getDataTable(pageResult);
    }



    /**
     * 获取财务管理-应付款-物流费用详细信息
     */
    @PreAuthorize("@ss.hasPermi('fms:shipFee:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(feeService.getById(id));
    }


}
