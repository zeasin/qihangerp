package cn.qihangerp.open.tao.controller;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.annotation.Log;
import cn.qihangerp.common.enums.BusinessType;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.core.page.TableDataInfo;
import cn.qihangerp.open.tao.bo.TaoOrderBo;
import cn.qihangerp.open.tao.bo.TaoOrderConfirmBo;
import cn.qihangerp.open.tao.domain.OmsTaoOrder;
import cn.qihangerp.open.tao.server.SimpleClientHandler;
import cn.qihangerp.open.tao.service.ITaoOrderService;
import cn.qihangerp.open.tao.service.OmsTaoOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 淘宝订单Controller
 * 
 * @author qihang
 * @date 2024-01-03
 */
@RestController
@RequestMapping("/tao-api/order")
public class TaoOrderController extends BaseController
{
    @Autowired
    private OmsTaoOrderService orderService;
    @Autowired
    private ITaoOrderService taoOrderService;

    @Autowired
    private SimpleClientHandler simpleClientHandler;

    /**
     * 查询淘宝订单列表
     */
    @PreAuthorize("@ss.hasPermi('tao:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(TaoOrderBo bo, PageQuery pageQuery) throws IOException, InterruptedException {
//        ErpOrder erpOrder = new ErpOrder();
//        erpOrder.setAddress("aaaaaaaaaaaaa");
////        nettyClientHandler.sendEntity(erpOrder);
//        ApiRequest<ErpOrder> req = new ApiRequest<>();
//        req.setType(102);
//        req.setData(erpOrder);
//
//        ApiResult s = simpleClientHandler.sendRequestAndWaitForResponse(req);


//        ErpOrderReturned erpOrderReturned = new ErpOrderReturned();
//        erpOrderReturned.setAddress("bbbbbbbbbbbbb");
//        nettyClientHandler.sendEntity(erpOrderReturned);
//        nettyClientHandler.sendMessageToServer("你好我是TAO订单");
//        startPage();
//        List<TaoOrder> list = taoOrderService.selectTaoOrderList(taoOrder);

//        return getDataTable(list);
        PageResult<OmsTaoOrder> result = orderService.queryPageList(bo, pageQuery);

        return getDataTable(result);
    }


    /**
     * 获取淘宝订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('tao:order:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(orderService.queryDetailById(id));
//        return success(taoOrderService.selectTaoOrderById(id+""));
    }

    @Log(title = "淘宝订单", businessType = BusinessType.UPDATE)
    @PostMapping("/confirmOrder")
    public AjaxResult confirmOrder(@RequestBody TaoOrderConfirmBo bo) throws InterruptedException {
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
