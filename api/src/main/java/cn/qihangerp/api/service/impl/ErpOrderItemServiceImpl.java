package cn.qihangerp.api.service.impl;

import cn.qihangerp.api.domain.GoodsSpec;
import cn.qihangerp.api.domain.bo.OrderItemSpecIdUpdateBo;
import cn.qihangerp.api.mapper.GoodsSpecMapper;
import cn.qihangerp.common.enums.ErpOrderStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.domain.ErpOrderItem;
import cn.qihangerp.api.mapper.ErpOrderMapper;
import cn.qihangerp.api.service.ErpOrderItemService;
import cn.qihangerp.api.mapper.ErpOrderItemMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author TW
* @description 针对表【erp_order_item(订单明细表)】的数据库操作Service实现
* @createDate 2024-04-15 13:35:02
*/
@AllArgsConstructor
@Service
public class ErpOrderItemServiceImpl extends ServiceImpl<ErpOrderItemMapper, ErpOrderItem>
    implements ErpOrderItemService{
    private final ErpOrderItemMapper mapper;
    private final ErpOrderMapper orderMapper;
    private final GoodsSpecMapper specMapper;
    @Override
    public PageResult<ErpOrderItem> queryPageList(ErpOrderStatusEnum status,Integer shipStatus, ErpOrderItem bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ErpOrderItem> queryWrapper = new LambdaQueryWrapper<ErpOrderItem>()
                .eq(org.springframework.util.StringUtils.hasText(bo.getOrderNum()),ErpOrderItem::getOrderNum,bo.getOrderNum())
                .eq(org.springframework.util.StringUtils.hasText(bo.getSpecNum()),ErpOrderItem::getSpecNum,bo.getSpecNum())
                .eq(ErpOrderItem::getOrderStatus, status.WAIT_SELLER_SEND_GOODS.getIndex())
                .eq(ErpOrderItem::getShipStatus,shipStatus)
                ;
        Page<ErpOrderItem> pages = mapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(pages);
    }

    @Override
    public int orderItemSpecIdUpdate(OrderItemSpecIdUpdateBo bo) {
        ErpOrderItem erpOrderItem = mapper.selectById(bo.getOrderItemId());
        if(erpOrderItem == null )return -1;
        GoodsSpec goodsSpec = specMapper.selectGoodsSpecById(bo.getErpGoodsSpecId());
        if(goodsSpec== null) return -2;

        ErpOrderItem update = new ErpOrderItem();
        update.setId(bo.getOrderItemId().toString());
        update.setGoodsId(goodsSpec.getGoodsId());
        update.setSpecId(goodsSpec.getId());
        update.setSpecNum(goodsSpec.getSpecNum());
        update.setUpdateBy("手动修改SkuId");
        update.setUpdateTime(new Date());
        mapper.updateById(update);
        return 0;
    }
}




