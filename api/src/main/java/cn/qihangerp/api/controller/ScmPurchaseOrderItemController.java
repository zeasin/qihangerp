package cn.qihangerp.api.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.api.domain.ScmPurchaseOrderItem;
import cn.qihangerp.api.service.IScmPurchaseOrderItemService;
import cn.qihangerp.core.page.TableDataInfo;

/**
 * 采购订单明细Controller
 * 
 * @author qihang
 * @date 2023-12-29
 */
@RestController
@RequestMapping("/purchase/purchaseOrderItem")
public class ScmPurchaseOrderItemController extends BaseController
{
    @Autowired
    private IScmPurchaseOrderItemService scmPurchaseOrderItemService;

    /**
     * 查询采购订单明细列表
     */
    @PreAuthorize("@ss.hasPermi('purchase:purchaseOrderItem:list')")
    @GetMapping("/list")
    public TableDataInfo list(ScmPurchaseOrderItem scmPurchaseOrderItem)
    {
        startPage();
        List<ScmPurchaseOrderItem> list = scmPurchaseOrderItemService.selectScmPurchaseOrderItemList(scmPurchaseOrderItem);
        return getDataTable(list);
    }


}
