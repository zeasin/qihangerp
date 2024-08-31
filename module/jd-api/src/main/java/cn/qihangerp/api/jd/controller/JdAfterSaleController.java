package cn.qihangerp.api.jd.controller;

import cn.qihangerp.api.jd.domain.OmsJdAfterSale;
import cn.qihangerp.api.jd.domain.bo.JdAfterSaleBo;
import cn.qihangerp.api.jd.domain.bo.JdOrderPushBo;
import cn.qihangerp.api.jd.service.OmsJdAfterSaleService;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.core.page.TableDataInfo;
import cn.qihangerp.domain.AjaxResult;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/jd-api/afterSale")
public class JdAfterSaleController extends BaseController {
    private final OmsJdAfterSaleService afterSaleService;
//    private final MqUtils mqUtils;
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableDataInfo goodsList(JdAfterSaleBo bo, PageQuery pageQuery) {
        PageResult<OmsJdAfterSale> result = afterSaleService.queryPageList(bo, pageQuery);

        return getDataTable(result);
    }

    @PostMapping("/push_oms")
    @ResponseBody
    public AjaxResult pushOms(@RequestBody JdOrderPushBo bo) {
        // TODO:需要优化消息格式
//        if(bo!=null && bo.getIds()!=null) {
//            for(String id: bo.getIds()) {
//                mqUtils.sendApiMessage(MqMessage.build(EnumShopType.JD, MqType.REFUND_MESSAGE, id));
//            }
//        }
        return success();
    }
}
