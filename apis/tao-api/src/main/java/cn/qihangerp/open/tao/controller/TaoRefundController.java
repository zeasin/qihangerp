package cn.qihangerp.open.tao.controller;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.annotation.Log;
import cn.qihangerp.common.enums.BusinessType;
import cn.qihangerp.common.utils.poi.ExcelUtil;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.core.page.TableDataInfo;
import cn.qihangerp.open.tao.domain.OmsTaoGoods;
import cn.qihangerp.open.tao.domain.OmsTaoRefund;
import cn.qihangerp.open.tao.domain.TaoOrderRefund;
import cn.qihangerp.open.tao.domain.bo.TaoRefundBo;
import cn.qihangerp.open.tao.service.ITaoOrderRefundService;
import cn.qihangerp.open.tao.service.OmsTaoRefundService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 淘宝退款订单Controller
 * 
 * @author qihang
 * @date 2024-01-13
 */
@RestController
@RequestMapping("/tao-api/refund")
public class TaoRefundController extends BaseController
{
    @Autowired
    private OmsTaoRefundService refundService;

    /**
     * 查询淘宝退款订单列表
     */
    @PreAuthorize("@ss.hasPermi('tao:taoRefund:list')")
    @GetMapping("/list")
    public TableDataInfo list(TaoRefundBo bo, PageQuery pageQuery)
    {
        PageResult<OmsTaoRefund> pageResult = refundService.queryPageList(bo, pageQuery);
        return getDataTable(pageResult);
    }


    /**
     * 获取淘宝退款订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('tao:taoRefund:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(refundService.getById(id));
    }


}
