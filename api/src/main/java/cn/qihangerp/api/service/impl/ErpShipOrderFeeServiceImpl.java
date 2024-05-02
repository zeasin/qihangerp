package cn.qihangerp.api.service.impl;

import cn.qihangerp.api.domain.ErpShipOrder;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.api.domain.ErpShipOrderFee;
import cn.qihangerp.api.service.ErpShipOrderFeeService;
import cn.qihangerp.api.mapper.ErpShipOrderFeeMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author qilip
* @description 针对表【erp_ship_order_fee(订单发货物流费用)】的数据库操作Service实现
* @createDate 2024-05-02 13:35:20
*/
@AllArgsConstructor
@Service
public class ErpShipOrderFeeServiceImpl extends ServiceImpl<ErpShipOrderFeeMapper, ErpShipOrderFee>
    implements ErpShipOrderFeeService{
    private final ErpShipOrderFeeMapper mapper;

    @Override
    public PageResult<ErpShipOrderFee> queryPageList(ErpShipOrderFee bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ErpShipOrderFee> queryWrapper = new LambdaQueryWrapper<ErpShipOrderFee>()

                ;
        Page<ErpShipOrderFee> pages = mapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(pages);
    }
}




