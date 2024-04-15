package com.qihang.erp.api.controller.openApi.wei;

import com.qihang.common.PageQuery;
import com.qihang.common.PageResult;
import com.qihang.core.controller.BaseController;
import com.qihang.core.domain.AjaxResult;
import com.qihang.core.page.TableDataInfo;
import com.qihang.erp.api.common.ResultVo;
import com.qihang.erp.api.domain.WeiOrder;
import com.qihang.erp.api.service.WeiOrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/wei/order")
public class WeiOrderController extends BaseController {
    private final WeiOrderService orderService;
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableDataInfo goodsList(WeiOrder bo, PageQuery pageQuery) {
        PageResult<WeiOrder> result = orderService.queryPageList(bo, pageQuery);

        return getDataTable(result);
    }

    @PostMapping("/confirm")
    @ResponseBody
    public AjaxResult orderConfirm(@RequestBody ShopOrderConfirmBo bo) {
        if(bo!=null && bo.getIds()!=null) {
            ResultVo<Integer> resultVo = orderService.orderConfirm(bo.getIds());

            return success(resultVo.getData());
        }else{
            return AjaxResult.error("没有选择任何订单！");
        }
    }
}
