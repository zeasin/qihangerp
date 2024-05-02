package cn.qihangerp.api.service;

import cn.qihangerp.api.domain.ErpShipOrder;
import cn.qihangerp.api.domain.WmsStockOutEntry;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author qilip
* @description 针对表【erp_ship_order(订单发货表)】的数据库操作Service
* @createDate 2024-05-02 10:15:15
*/
public interface ErpShipOrderService extends IService<ErpShipOrder> {
    PageResult<ErpShipOrder> queryPageList(ErpShipOrder bo, PageQuery pageQuery);

}
