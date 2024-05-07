package cn.qihangerp.api.wei.controller;


import cn.qihangerp.api.wei.domain.OmsWeiRefund;
import cn.qihangerp.api.wei.service.OmsWeiRefundService;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.core.page.TableDataInfo;
import cn.qihangerp.domain.AjaxResult;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/wei-api/refund")
public class WeiRefundController extends BaseController {
    private final OmsWeiRefundService refundService;
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableDataInfo goodsList(OmsWeiRefund bo, PageQuery pageQuery) {
        PageResult<OmsWeiRefund> result = refundService.queryPageList(bo, pageQuery);

        return getDataTable(result);
    }


    @PutMapping("/returnedConfirm/{id}")
    public AjaxResult returnedConfirm(@PathVariable Long id)
    {
//        refundService.returnedConfirm(id);

        return toAjax(1);
    }
    @PutMapping("/orderIntercept/{id}")
    public AjaxResult orderIntercept(@PathVariable Long id)
    {
//        refundService.orderIntercept(id);

        return toAjax(1);
    }

}
