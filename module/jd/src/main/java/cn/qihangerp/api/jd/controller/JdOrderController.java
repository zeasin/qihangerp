package cn.qihangerp.api.jd.controller;

import cn.qihangerp.api.jd.bo.JdOrderBo;
import cn.qihangerp.api.jd.bo.JdOrderConfirmBo;
import cn.qihangerp.api.jd.domain.OmsJdOrder;
import cn.qihangerp.api.jd.service.OmsJdOrderService;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.core.page.TableDataInfo;
import cn.qihangerp.domain.AjaxResult;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/jd-api/order")
public class JdOrderController extends BaseController {
    private final OmsJdOrderService orderService;
//    private final MqUtils mqUtils;
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableDataInfo orderList(JdOrderBo bo, PageQuery pageQuery) {
        PageResult<OmsJdOrder> result = orderService.queryPageList(bo, pageQuery);
        return getDataTable(result);
    }


    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(orderService.queryDetailById(id));
    }

    @PostMapping("/confirmOrder")
    public AjaxResult confirmOrder(@RequestBody JdOrderConfirmBo bo) throws InterruptedException {
        bo.setUpdateBy(getUsername());
        int result = orderService.confirmOrder(bo);
        if(result == -1) return new AjaxResult(501,"已确认过了！请勿重复确认！");
        else if(result == -2) return new AjaxResult(502,"订单已存在！请勿重复确认！");
        else if(result == -3) return new AjaxResult(503,"请指定发货方式！");
        else if(result == -4) return new AjaxResult(504,"发货方式不支持！");
        else if(result == -11) return new AjaxResult(511,"商品SKU编码不存在！");
        else if(result == -12) return new AjaxResult(512,"商品信息不存在！");


        return toAjax(result);
    }
}
