package cn.qihangerp.api.pdd.controller;

import cn.qihangerp.api.pdd.bo.PddAfterSaleBo;
import cn.qihangerp.api.pdd.domain.OmsPddRefund;
import cn.qihangerp.api.pdd.service.OmsPddRefundService;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.core.page.TableDataInfo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 拼多多订单Controller
 * 
 * @author qihang
 * @date 2024-01-02
 */
@AllArgsConstructor
@RestController
@RequestMapping("/pdd-api/refund")
public class PddRefundController extends BaseController
{

    private final OmsPddRefundService refundService;
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableDataInfo orderList(PddAfterSaleBo bo, PageQuery pageQuery) {
        PageResult<OmsPddRefund> result = refundService.queryPageList(bo, pageQuery);

        return getDataTable(result);
    }


}
