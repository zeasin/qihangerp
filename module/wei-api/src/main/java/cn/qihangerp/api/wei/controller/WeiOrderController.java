package cn.qihangerp.api.wei.controller;


import cn.qihangerp.api.wei.bo.WeiOrderConfirmBo;
import cn.qihangerp.api.wei.domain.OmsWeiOrder;
import cn.qihangerp.api.wei.service.OmsWeiOrderService;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.core.page.TableDataInfo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/wei-api/order")
public class WeiOrderController extends BaseController {
    private final OmsWeiOrderService orderService;
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableDataInfo orderList(OmsWeiOrder bo, PageQuery pageQuery) {
        PageResult<OmsWeiOrder> result = orderService.queryPageList(bo, pageQuery);

        return getDataTable(result);
    }

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(orderService.queryDetailById(id));
    }

//    @PostMapping("/confirm")
//    @ResponseBody
//    public AjaxResult orderConfirm(@RequestBody ShopOrderConfirmBo bo) {
//        if(bo!=null && bo.getIds()!=null) {
//            ResultVo<Integer> resultVo = orderService.orderConfirm(bo.getIds());
//
//            return success(resultVo.getData());
//        }else{
//            return AjaxResult.error("没有选择任何订单！");
//        }
//    }

    @PostMapping("/confirmOrder")
    public AjaxResult confirmOrder(@RequestBody WeiOrderConfirmBo bo) throws InterruptedException {
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
