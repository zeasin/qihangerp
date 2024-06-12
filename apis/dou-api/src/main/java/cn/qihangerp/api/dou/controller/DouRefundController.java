package cn.qihangerp.api.dou.controller;

import cn.qihangerp.api.dou.domain.OmsDouRefund;
import cn.qihangerp.api.dou.domain.bo.DouRefundBo;
import cn.qihangerp.api.dou.service.OmsDouRefundService;
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
@RequestMapping("/dou-api/refund")
public class DouRefundController extends BaseController
{

    private final OmsDouRefundService refundService;
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public TableDataInfo orderList(DouRefundBo bo, PageQuery pageQuery) {
        PageResult<OmsDouRefund> result = refundService.queryPageList(bo, pageQuery);

        return getDataTable(result);
    }


}
