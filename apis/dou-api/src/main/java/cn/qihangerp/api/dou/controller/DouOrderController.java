package cn.qihangerp.api.dou.controller;

import cn.qihangerp.api.dou.domain.OmsDouOrder;
import cn.qihangerp.api.dou.service.OmsDouOrderService;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.core.page.TableDataInfo;

/**
 * 抖店订单Controller
 * 
 * @author qihang
 * @date 2024-01-02
 */
@AllArgsConstructor
@RestController
@RequestMapping("/dou-api/order")
public class DouOrderController extends BaseController
{
    private final OmsDouOrderService orderService;
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableDataInfo orderList(OmsDouOrder bo, PageQuery pageQuery) {
        PageResult<OmsDouOrder> result = orderService.queryPageList(bo, pageQuery);

        return getDataTable(result);
    }
    
}
