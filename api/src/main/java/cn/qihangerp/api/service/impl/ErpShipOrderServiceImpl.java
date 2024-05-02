package cn.qihangerp.api.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.domain.ErpOrderItem;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.api.domain.ErpShipOrder;
import cn.qihangerp.api.service.ErpShipOrderService;
import cn.qihangerp.api.mapper.ErpShipOrderMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author qilip
* @description 针对表【erp_ship_order(订单发货表)】的数据库操作Service实现
* @createDate 2024-05-02 10:15:15
*/
@AllArgsConstructor
@Service
public class ErpShipOrderServiceImpl extends ServiceImpl<ErpShipOrderMapper, ErpShipOrder>
    implements ErpShipOrderService{
    private final ErpShipOrderMapper mapper;
    @Override
    public PageResult<ErpShipOrder> queryPageList(ErpShipOrder bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ErpShipOrder> queryWrapper = new LambdaQueryWrapper<ErpShipOrder>()
                .eq(org.springframework.util.StringUtils.hasText(bo.getOrderNum()),ErpShipOrder::getOrderNum,bo.getOrderNum())
                .eq(org.springframework.util.StringUtils.hasText(bo.getSpecNum()),ErpShipOrder::getSpecNum,bo.getSpecNum())
                .eq(ErpShipOrder::getShipType, bo.getShipType())
                .eq(ErpShipOrder::getShipStatus,bo.getShipStatus())
                ;
        Page<ErpShipOrder> pages = mapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(pages);
    }
}




