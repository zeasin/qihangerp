package cn.qihangerp.api.jd.controller;

import cn.qihangerp.api.jd.bo.JdOrderBo;
import cn.qihangerp.api.jd.domain.OmsJdOrder;
import cn.qihangerp.api.jd.service.OmsJdOrderService;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.core.page.TableDataInfo;
import cn.qihangerp.domain.AjaxResult;
import lombok.AllArgsConstructor;
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
//    @PostMapping("/push_oms")
//    @ResponseBody
//    public AjaxResult pushOms(@RequestBody JdOrderPushBo bo) {
//        // TODO:需要优化消息格式
//        if(bo!=null && bo.getIds()!=null) {
//            for(String id: bo.getIds()) {
//                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.JD, MqType.ORDER_MESSAGE, id));
//            }
//        }
//        return success();
//    }
}
