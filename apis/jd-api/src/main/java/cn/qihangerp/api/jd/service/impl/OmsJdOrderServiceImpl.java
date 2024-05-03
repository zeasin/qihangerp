package cn.qihangerp.api.jd.service.impl;

import cn.qihangerp.api.jd.bo.JdOrderBo;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.api.jd.domain.OmsJdOrder;
import cn.qihangerp.api.jd.service.OmsJdOrderService;
import cn.qihangerp.api.jd.mapper.OmsJdOrderMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
* @author qilip
* @description 针对表【oms_jd_order(京东订单表)】的数据库操作Service实现
* @createDate 2024-05-03 12:21:08
*/
@AllArgsConstructor
@Service
public class OmsJdOrderServiceImpl extends ServiceImpl<OmsJdOrderMapper, OmsJdOrder>
    implements OmsJdOrderService{
    private final OmsJdOrderMapper mapper;
    @Override
    public PageResult<OmsJdOrder> queryPageList(JdOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OmsJdOrder> queryWrapper = new LambdaQueryWrapper<OmsJdOrder>()
                .eq(bo.getShopId()!=null,OmsJdOrder::getShopId,bo.getShopId())
                .eq(StringUtils.hasText(bo.getOrderId()),OmsJdOrder::getOrderId,bo.getOrderId())
                .eq(StringUtils.hasText(bo.getOrderState()),OmsJdOrder::getOrderState,bo.getOrderState())
                ;

        Page<OmsJdOrder> page = mapper.selectPage(pageQuery.build(), queryWrapper);
//        if(page.getRecords()!=null){
//            for (var order:taoGoodsPage.getRecords()) {
//                order.setItems(itemMapper.selectList(new LambdaQueryWrapper<OmsTaoOrderItem>().eq(OmsTaoOrderItem::getTid,order.getTid())));
//            }
//        }
        return PageResult.build(page);
    }
}




