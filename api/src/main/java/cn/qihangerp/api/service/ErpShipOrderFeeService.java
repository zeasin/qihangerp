package cn.qihangerp.api.service;

import cn.qihangerp.api.domain.ErpShipOrder;
import cn.qihangerp.api.domain.ErpShipOrderFee;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author qilip
* @description 针对表【erp_ship_order_fee(订单发货物流费用)】的数据库操作Service
* @createDate 2024-05-02 13:35:20
*/
public interface ErpShipOrderFeeService extends IService<ErpShipOrderFee> {
    PageResult<ErpShipOrderFee> queryPageList(ErpShipOrderFee bo, PageQuery pageQuery);
}
