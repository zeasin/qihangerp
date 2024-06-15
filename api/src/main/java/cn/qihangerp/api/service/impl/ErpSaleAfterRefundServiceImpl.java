package cn.qihangerp.api.service.impl;

import cn.qihangerp.api.domain.bo.RefundBo;
import cn.qihangerp.api.mapper.ErpOrderItemMapper;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.domain.ErpSaleOrderItem;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.domain.ErpSaleAfterRefund;
import cn.qihangerp.api.service.ErpSaleAfterRefundService;
import cn.qihangerp.api.mapper.ErpSaleAfterRefundMapper;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
* @author qilip
* @description 针对表【erp_sale_after_refund(售后退款表)】的数据库操作Service实现
* @createDate 2024-05-04 19:53:00
*/
@Log
@AllArgsConstructor
@Service
public class ErpSaleAfterRefundServiceImpl extends ServiceImpl<ErpSaleAfterRefundMapper, ErpSaleAfterRefund>
    implements ErpSaleAfterRefundService{
    private final ErpSaleAfterRefundMapper refundMapper;
    private final ErpOrderItemMapper orderItemMapper;
    @Override
    public ResultVo<Integer> taoRefundMessage(ErpSaleAfterRefund refund) {
        log.info("TAO退款消息处理" + refund.getRefundNum());
        // 处理数据
        // 查询ERP订单
        ErpSaleOrderItem orderItem = null;
        List<ErpSaleOrderItem> oOrderItems = orderItemMapper.selectList(new LambdaQueryWrapper<ErpSaleOrderItem>()
                .eq(ErpSaleOrderItem::getOriginalOrderId, refund.getOriginalOrderId())
                .eq(ErpSaleOrderItem::getOriginalOrderItemId, refund.getOriginalOrderItemId()));
        if (oOrderItems != null && oOrderItems.size() > 0) {
            orderItem = oOrderItems.get(0);
        }

        refund.setErpGoodsId(orderItem != null ? orderItem.getGoodsId() : 0L);
        refund.setErpGoodsSkuId(orderItem != null ? orderItem.getSpecId() : 0L);


        List<ErpSaleAfterRefund> refundList = refundMapper.selectList(new LambdaQueryWrapper<ErpSaleAfterRefund>().eq(ErpSaleAfterRefund::getRefundNum, refund.getRefundNum()));
        if (refundList == null || refundList.size() == 0) {
            // 不存在，新增
            refund.setCreateTime(new Date());
            refund.setCreateBy("REFUND_MESSAGE");
            refundMapper.insert(refund);
            return ResultVo.success("新增成功");
        } else {
            // 存在，修改
            ErpSaleAfterRefund update = new ErpSaleAfterRefund();
            update.setId(refundList.get(0).getId());
            update.setHasGoodReturn(refund.getHasGoodReturn());
            update.setStatus(refund.getStatus());
            update.setUpdateTime(new Date());
            update.setUpdateBy("REFUND_MESSAGE");
            refundMapper.updateById(update);
            return ResultVo.success("修改成功");
        }

    }

    @Override
    public PageResult<ErpSaleAfterRefund> queryPageList(RefundBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ErpSaleAfterRefund> queryWrapper = new LambdaQueryWrapper<ErpSaleAfterRefund>()
                .eq(bo.getShopId()!=null,ErpSaleAfterRefund::getShopId,bo.getShopId())
                .eq(StringUtils.hasText(bo.getOriginalOrderId()),ErpSaleAfterRefund::getOriginalOrderId,bo.getOriginalOrderId())
                .eq(bo.getStatus()!=null,ErpSaleAfterRefund::getStatus,bo.getStatus())
                ;

        Page<ErpSaleAfterRefund> taoGoodsPage = refundMapper.selectPage(pageQuery.build(), queryWrapper);
        return PageResult.build(taoGoodsPage);
    }
}




