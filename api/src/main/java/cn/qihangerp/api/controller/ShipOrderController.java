package cn.qihangerp.api.controller;

import cn.qihangerp.api.domain.ErpShipOrder;
import cn.qihangerp.api.mapper.ErpShipOrderMapper;
import cn.qihangerp.api.service.ErpShipOrderService;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.enums.ErpOrderStatusEnum;
import cn.qihangerp.core.controller.BaseController;
import cn.qihangerp.core.page.TableDataInfo;
import cn.qihangerp.domain.ErpOrderItem;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/shipping")
public class ShipOrderController extends BaseController {
    private final ErpShipOrderService shipOrderService;
    /**
     * 备货中-仓库发货
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/ship_order")
    public TableDataInfo stockShipList(ErpShipOrder bo, PageQuery pageQuery)
    {
        PageResult<ErpShipOrder> list = shipOrderService.queryPageList(bo, pageQuery);
        return getDataTable(list);
    }
}
