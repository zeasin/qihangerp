package cn.qihangerp.api.service;

import cn.qihangerp.api.domain.ErpShipOrderAgentFee;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author qilip
* @description 针对表【erp_ship_order_agent_fee(供应商代发账单)】的数据库操作Service
* @createDate 2024-05-02 14:58:16
*/
public interface ErpShipOrderAgentFeeService extends IService<ErpShipOrderAgentFee> {
    PageResult<ErpShipOrderAgentFee> queryPageList(ErpShipOrderAgentFee bo, PageQuery pageQuery);
}
