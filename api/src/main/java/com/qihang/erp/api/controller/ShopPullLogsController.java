package com.qihang.erp.api.controller;


import com.qihang.common.PageQuery;
import com.qihang.core.controller.BaseController;
import com.qihang.core.page.TableDataInfo;
import com.qihang.erp.api.domain.SShopPullLogs;
import com.qihang.erp.api.service.SShopPullLogsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/shop")
public class ShopPullLogsController extends BaseController {
    private final SShopPullLogsService pullLogsService;

    @GetMapping("/pull_logs_list")
    public TableDataInfo list(SShopPullLogs logs, PageQuery pageQuery)
    {
        var pageList = pullLogsService.queryPageList(logs,pageQuery);
        return getDataTable(pageList);
    }
}
