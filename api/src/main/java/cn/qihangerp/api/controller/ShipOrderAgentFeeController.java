package cn.qihangerp.api.controller;

import java.util.List;

import cn.qihangerp.api.domain.ErpShipOrderAgentFee;
import cn.qihangerp.api.service.ErpShipOrderAgentFeeService;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.qihangerp.common.annotation.Log;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.common.enums.BusinessType;
import cn.qihangerp.common.utils.poi.ExcelUtil;
import cn.qihangerp.core.page.TableDataInfo;

/**
 * 财务管理-应付款-代发账单Controller
 * 
 * @author qihang
 * @date 2024-01-28
 */
@RestController
@RequestMapping("/fms/agentShip")
public class ShipOrderAgentFeeController extends BaseController
{
    @Autowired
    private ErpShipOrderAgentFeeService agentFeeService;

    /**
     * 查询财务管理-应付款-代发账单列表
     */
    @PreAuthorize("@ss.hasPermi('fms:agentShip:list')")
    @GetMapping("/list")
    public TableDataInfo list(ErpShipOrderAgentFee bo, PageQuery pageQuery)
    {
        PageResult<ErpShipOrderAgentFee> pageResult = agentFeeService.queryPageList(bo, pageQuery);
        return getDataTable(pageResult);
    }



    /**
     * 获取财务管理-应付款-代发账单详细信息
     */
    @PreAuthorize("@ss.hasPermi('fms:agentShip:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(agentFeeService.getById(id));
    }


}
