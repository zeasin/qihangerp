package cn.qihangerp.api.controller;

import cn.qihangerp.api.domain.bo.RefundBo;
import cn.qihangerp.api.service.ErpSaleAfterRefundService;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.core.page.TableDataInfo;
import cn.qihangerp.domain.ErpSaleAfterRefund;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
@AllArgsConstructor
@RestController
@RequestMapping("/api/refund")
public class ErpOrderRefundController extends BaseController {
    private final ErpSaleAfterRefundService refundService;
    @GetMapping("/list")
    public TableDataInfo list(RefundBo bo, PageQuery pageQuery) throws IOException, InterruptedException {
        PageResult<ErpSaleAfterRefund> result = refundService.queryPageList(bo, pageQuery);
        return getDataTable(result);
    }
}
