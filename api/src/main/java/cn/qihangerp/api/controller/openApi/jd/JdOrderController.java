package cn.qihangerp.api.controller.openApi.jd;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.domain.AjaxResult;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/jd")
public class JdOrderController extends BaseController {
//    private final JdOrderService orderService;
//    private final MqUtils mqUtils;
    @RequestMapping(value = "/order/list", method = RequestMethod.GET)
    public AjaxResult orderList(JdOrderBo bo, PageQuery pageQuery) {
//        PageResult<JdOrder> result = orderService.queryPageList(bo, pageQuery);
//
//        return getDataTable(result);
        return AjaxResult.error("开源版本暂未实现JD相关接口");
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
