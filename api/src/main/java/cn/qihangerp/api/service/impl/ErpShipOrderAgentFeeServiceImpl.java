package cn.qihangerp.api.service.impl;

import cn.qihangerp.api.domain.ErpShipOrderFee;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.api.domain.ErpShipOrderAgentFee;
import cn.qihangerp.api.service.ErpShipOrderAgentFeeService;
import cn.qihangerp.api.mapper.ErpShipOrderAgentFeeMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author qilip
* @description 针对表【erp_ship_order_agent_fee(供应商代发账单)】的数据库操作Service实现
* @createDate 2024-05-02 14:58:16
*/
@AllArgsConstructor
@Service
public class ErpShipOrderAgentFeeServiceImpl extends ServiceImpl<ErpShipOrderAgentFeeMapper, ErpShipOrderAgentFee>
    implements ErpShipOrderAgentFeeService{
    private final ErpShipOrderAgentFeeMapper mapper;

    @Override
    public PageResult<ErpShipOrderAgentFee> queryPageList(ErpShipOrderAgentFee bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ErpShipOrderAgentFee> queryWrapper = new LambdaQueryWrapper<ErpShipOrderAgentFee>()

                ;
        Page<ErpShipOrderAgentFee> pages = mapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(pages);
    }
}




