package cn.qihangerp.open.tao.controller;

import cn.qihangerp.common.ApiRequest;
import cn.qihangerp.common.ApiResult;
import cn.qihangerp.common.annotation.Log;
import cn.qihangerp.common.enums.BusinessType;
import cn.qihangerp.common.utils.poi.ExcelUtil;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.domain.AjaxResult;
import cn.qihangerp.core.page.TableDataInfo;
import cn.qihangerp.domain.ErpOrder;
import cn.qihangerp.open.tao.domain.TaoOrder;
import cn.qihangerp.open.tao.server.SimpleClientHandler;
import cn.qihangerp.open.tao.service.ITaoOrderService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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
    private ITaoOrderService taoOrderService;

    @Autowired
    private SimpleClientHandler simpleClientHandler;

    /**
     * 查询淘宝订单列表
     */
    @PreAuthorize("@ss.hasPermi('tao:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(TaoOrder taoOrder) throws IOException, InterruptedException {
        ErpOrder erpOrder = new ErpOrder();
        erpOrder.setAddress("aaaaaaaaaaaaa");
//        nettyClientHandler.sendEntity(erpOrder);
        ApiRequest<ErpOrder> req = new ApiRequest<>();
        req.setType(102);
        req.setData(erpOrder);

        ApiResult s = simpleClientHandler.sendRequestAndWaitForResponse(req);


//        ErpOrderReturned erpOrderReturned = new ErpOrderReturned();
//        erpOrderReturned.setAddress("bbbbbbbbbbbbb");
//        nettyClientHandler.sendEntity(erpOrderReturned);
//        nettyClientHandler.sendMessageToServer("你好我是TAO订单");
        startPage();
        List<TaoOrder> list = taoOrderService.selectTaoOrderList(taoOrder);

        return getDataTable(list);
    }

    /**
     * 导出淘宝订单列表
     */
    @PreAuthorize("@ss.hasPermi('tao:order:export')")
    @Log(title = "淘宝订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TaoOrder taoOrder)
    {
        List<TaoOrder> list = taoOrderService.selectTaoOrderList(taoOrder);
        ExcelUtil<TaoOrder> util = new ExcelUtil<TaoOrder>(TaoOrder.class);
        util.exportExcel(response, list, "淘宝订单数据");
    }

    /**
     * 获取淘宝订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('tao:order:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(taoOrderService.selectTaoOrderById(id+""));
    }

    /**
     * 新增淘宝订单
     */
    @PreAuthorize("@ss.hasPermi('tao:order:add')")
    @Log(title = "淘宝订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TaoOrder taoOrder)
    {
        taoOrder.setCreateBy(getUsername());
        int result = taoOrderService.insertTaoOrder(taoOrder);
        if(result == -1) return new AjaxResult(505,"订单号已存在");
        else if(result == -2) return new AjaxResult(506,"请添加商品");
        else if(result == -3) return new AjaxResult(507,"商品数据错误");
        return toAjax(result);
    }
    @Log(title = "淘宝订单", businessType = BusinessType.UPDATE)
    @PostMapping("/confirmOrder")
    public AjaxResult confirmOrder(@RequestBody TaoOrder taoOrder)
    {
        taoOrder.setUpdateBy(getUsername());
        int result = taoOrderService.confirmOrder(taoOrder);
        if(result == -1) return new AjaxResult(501,"已确认过了！请勿重复确认！");
        else if(result == -2) return new AjaxResult(502,"订单已存在！请勿重复确认！");
        else if(result == -3) return new AjaxResult(503,"请指定发货方式！");
        else if(result == -4) return new AjaxResult(504,"发货方式不支持！");
        else if(result == -11) return new AjaxResult(511,"商品SKU编码不存在！");
        else if(result == -12) return new AjaxResult(512,"商品信息不存在！");


        return toAjax(result);
    }


//    /**
//     * 修改淘宝订单
//     */
//    @PreAuthorize("@ss.hasPermi('tao:order:edit')")
//    @Log(title = "淘宝订单", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody TaoOrder taoOrder)
//    {
//        return toAjax(taoOrderService.updateTaoOrder(taoOrder));
//    }
//
//    /**
//     * 删除淘宝订单
//     */
//    @PreAuthorize("@ss.hasPermi('tao:order:remove')")
//    @Log(title = "淘宝订单", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(taoOrderService.deleteTaoOrderByIds(ids));
//    }
}
