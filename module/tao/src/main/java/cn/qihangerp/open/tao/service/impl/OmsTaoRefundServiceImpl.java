package cn.qihangerp.open.tao.service.impl;

import cn.qihangerp.common.*;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.domain.ErpSaleAfterRefund;
import cn.qihangerp.mq.MQRequest;
import cn.qihangerp.mq.MQRequestType;
import cn.qihangerp.open.tao.domain.OmsTaoOrderItem;
import cn.qihangerp.open.tao.domain.bo.TaoRefundBo;
import cn.qihangerp.open.tao.mapper.OmsTaoOrderItemMapper;
import cn.qihangerp.open.tao.server.SimpleClientHandler;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.open.tao.domain.OmsTaoRefund;
import cn.qihangerp.open.tao.service.OmsTaoRefundService;
import cn.qihangerp.open.tao.mapper.OmsTaoRefundMapper;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
* @author TW
* @description 针对表【oms_tao_refund(淘宝退款表)】的数据库操作Service实现
* @createDate 2024-04-30 13:52:20
*/
@Log
@AllArgsConstructor
@Service
public class OmsTaoRefundServiceImpl extends ServiceImpl<OmsTaoRefundMapper, OmsTaoRefund>
    implements OmsTaoRefundService{
    private final OmsTaoRefundMapper mapper;
    private final OmsTaoOrderItemMapper orderItemMapper;
    private final SimpleClientHandler simpleClientHandler;

    @Override
    public PageResult<OmsTaoRefund> queryPageList(TaoRefundBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OmsTaoRefund> queryWrapper = new LambdaQueryWrapper<OmsTaoRefund>()
                .eq(bo.getShopId()!=null,OmsTaoRefund::getShopId,bo.getShopId())
                .eq(StringUtils.hasText(bo.getRefundId()),OmsTaoRefund::getRefundId,bo.getRefundId())
                .eq(StringUtils.hasText(bo.getTid()),OmsTaoRefund::getTid,bo.getTid())
                ;

        Page<OmsTaoRefund> page = mapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(page);
    }

    @Transactional
    @Override
    public ResultVo<Integer>  saveAndUpdateRefund(Long shopId, OmsTaoRefund refund) throws InterruptedException {
        List<OmsTaoRefund> taoRefunds = mapper.selectList(new LambdaQueryWrapper<OmsTaoRefund>().eq(OmsTaoRefund::getRefundId, refund.getRefundId()));
        OmsTaoRefund newRefund = new OmsTaoRefund();
        if (taoRefunds != null && taoRefunds.size() > 0) {
            newRefund = taoRefunds.get(0);
            // 存在，修改
            OmsTaoRefund update = new OmsTaoRefund();
            update.setId(taoRefunds.get(0).getId());
//            update.setTid(refund.getTid());
//            update.setOid(refund.getOid());
            update.setNumIid(refund.getNumIid());
            update.setOuterId(refund.getOuterId());
            update.setModified(refund.getModified());
            update.setStatus(refund.getStatus());
            update.setOrderStatus(refund.getOrderStatus());
            update.setEndTime(refund.getEndTime());
            update.setUpdateTime(new Date());
            mapper.updateById(update);
            newRefund.setNumIid(refund.getNumIid());
            newRefund.setOuterId(refund.getOuterId());
            newRefund.setModified(refund.getModified());
            newRefund.setStatus(refund.getStatus());
            newRefund.setOrderStatus(refund.getOrderStatus());
            newRefund.setEndTime(refund.getEndTime());


//            return ResultVo.success("更新成功");
        } else {
            refund.setShopId(shopId);
            refund.setCreateTime(new Date());
            mapper.insert(refund);
            newRefund = refund;
//            return  ResultVo.success("新增成功");
        }

        log.info("TAO退款消息处理" + refund.getRefundId());
        // 组合售后退款表数据
        ErpSaleAfterRefund erpRefund = new ErpSaleAfterRefund();
        erpRefund.setRefundNum(newRefund.getRefundId());
        // (1-售前退款 10-退货 20-换货 30-维修 40-大家电安装 50-大家电移机 60-大家电增值服务 70-上门维修 90-优鲜赔 80-补发商品 100-试用收回 11-仅退款)
        Integer refundType = null;
        // REFUND(仅退款),REFUND_AND_RETURN(退货退款),TMALL_EXCHANGE(天猫换货),TAOBAO_EXCHANGE(淘宝换货),REPAIR(维修),RESHIPPING(补寄),OTHERS(其他)
        if (newRefund.getDisputeType().equals("REFUND")) {
            refundType = 11;
        } else if (newRefund.getDisputeType().equals("REFUND_AND_RETURN")) {
            refundType = 10;
        } else if (newRefund.getDisputeType().equals("TMALL_EXCHANGE") || newRefund.getDisputeType().equals("TAOBAO_EXCHANGE")) {
            refundType = 20;
        } else if (newRefund.getDisputeType().equals("REPAIR")) {
            refundType = 30;
        } else if (newRefund.getDisputeType().equals("RESHIPPING")) {
            refundType = 80;
        } else if (newRefund.getDisputeType().equals("OTHERS")) {
            refundType = 0;
        }
        erpRefund.setRefundType(refundType);
        erpRefund.setHasGoodReturn(newRefund.getHasGoodReturn() ? 1 : 0);
        erpRefund.setShopId(newRefund.getShopId());
        erpRefund.setShopType(EnumShopType.TAO.getIndex());
        erpRefund.setOriginalOrderId(newRefund.getTid());
        erpRefund.setOriginalOrderItemId(newRefund.getOid());
        List<OmsTaoOrderItem> omsTaoOrderItems = orderItemMapper.selectList(new LambdaQueryWrapper<OmsTaoOrderItem>().eq(OmsTaoOrderItem::getOid, newRefund.getOid()));
        erpRefund.setOriginalSkuId(!omsTaoOrderItems.isEmpty()?omsTaoOrderItems.get(0).getSkuId():"");
//        erpRefund.setErpGoodsId(orderItem.getErpGoodsId());
//        erpRefund.setErpSkuId(orderItem.getErpSkuId());
        erpRefund.setSpecNum(newRefund.getOuterId());
        erpRefund.setGoodsName(newRefund.getTitle());
        erpRefund.setGoodsSku(newRefund.getSku());
//        erpRefund.setGoodsImage(orderItem.getGoodsImg());
        erpRefund.setQuantity(newRefund.getNum());
        Integer status = null;
        //退款状态。WAIT_SELLER_AGREE(买家已经申请退款，等待卖家同意) WAIT_BUYER_RETURN_GOODS(卖家已经同意退款，等待买家退货) WAIT_SELLER_CONFIRM_GOODS(买家已经退货，等待卖家确认收货)
        // SELLER_REFUSE_BUYER(卖家拒绝退款) CLOSED(退款关闭) SUCCESS(退款成功)
        if (newRefund.getStatus().equals("WAIT_SELLER_AGREE")) {
            // 10001-待审核
            status = 10001;
        } else if (newRefund.getStatus().equals("WAIT_BUYER_RETURN_GOODS")) {
            // 10002-等待买家退货(待客户反馈)
            status = 10002;
        } else if (newRefund.getStatus().equals("WAIT_SELLER_CONFIRM_GOODS")) {
            // 10005-等待卖家确认收货(待收货)
            status = 10005;
        } else if (newRefund.getStatus().equals("SELLER_REFUSE_BUYER")) {
            // 14000-卖家拒绝退款
            status = 14000;
        } else if (newRefund.getStatus().equals("CLOSED")) {
            // 10011-退款关闭（取消）
            status = 10011;
        } else if (newRefund.getStatus().equals("SUCCESS")) {
            // 10010-退款成功（完成）
            status = 10010;
        }
        erpRefund.setStatus(status);
        try {
            erpRefund.setRefundFee(Double.parseDouble(newRefund.getRefundFee()));
        } catch (Exception e) {
        }
        erpRefund.setRefundReason(newRefund.getReason());
        erpRefund.setRemark(newRefund.getDesc1());
        erpRefund.setCreateTime(new Date());
        erpRefund.setCreateBy("REFUND_MESSAGE");


        // 远程服务调用
        MQRequest<ErpSaleAfterRefund> req = new MQRequest<>();
        req.setMqRequestType(MQRequestType.REFUND_PUSH);
        req.setData(erpRefund);
        ApiResult s = simpleClientHandler.sendRefund(req);
        if(s.getResult()==ApiResultEnum.SUCCESS.getIndex()) {
            //更新自己
            OmsTaoRefund update = new OmsTaoRefund();
            update.setId(newRefund.getId());
            update.setPullStatus(1);
            update.setPullTime(new Date());
            mapper.updateById(update);
        }else{
            //更新自己
            OmsTaoRefund update = new OmsTaoRefund();
            update.setId(newRefund.getId());
            update.setPullStatus(2);
            update.setPullTime(new Date());
            mapper.updateById(update);
        }

        if (taoRefunds != null && taoRefunds.size() > 0) {
            return ResultVo.success("更新成功");
        }else{
            return  ResultVo.success("新增成功");
        }
    }


}




