package cn.qihangerp.api.wei.service.impl;

import cn.qihangerp.api.wei.domain.OmsWeiOrderItem;
import cn.qihangerp.api.wei.mapper.OmsWeiOrderItemMapper;
import cn.qihangerp.common.*;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.domain.ErpSaleAfterRefund;
import cn.qihangerp.mq.MQRequest;
import cn.qihangerp.mq.MQRequestType;
import cn.qihangerp.mq.client.MQClientService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.api.wei.domain.OmsWeiRefund;
import cn.qihangerp.api.wei.service.OmsWeiRefundService;
import cn.qihangerp.api.wei.mapper.OmsWeiRefundMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
* @author TW
* @description 针对表【oms_wei_refund(视频号小店退款)】的数据库操作Service实现
* @createDate 2024-05-07 14:31:54
*/
@AllArgsConstructor
@Service
public class OmsWeiRefundServiceImpl extends ServiceImpl<OmsWeiRefundMapper, OmsWeiRefund>
    implements OmsWeiRefundService{
    private final OmsWeiRefundMapper mapper;
    private final OmsWeiOrderItemMapper orderItemMapper;
    private final MQClientService mqClientService;

    @Override
    public PageResult<OmsWeiRefund> queryPageList(OmsWeiRefund bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OmsWeiRefund> queryWrapper = new LambdaQueryWrapper<OmsWeiRefund>()
                .eq(bo.getShopId()!=null,OmsWeiRefund::getShopId,bo.getShopId())
                .eq(StringUtils.hasText(bo.getOrderId()),OmsWeiRefund::getOrderId,bo.getOrderId())
                ;

        Page<OmsWeiRefund> page = mapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(page);
    }

    @Transactional
    @Override
    public ResultVo<Integer> saveRefund(Long shopId, OmsWeiRefund refund) {
        try {
            List<OmsWeiRefund> refunds = mapper.selectList(new LambdaQueryWrapper<OmsWeiRefund>().eq(OmsWeiRefund::getAfterSaleOrderId, refund.getAfterSaleOrderId()));
            OmsWeiRefund newRefund = new OmsWeiRefund();
            if (refunds != null && refunds.size() > 0) {
                newRefund = refunds.get(0);
                // 存在，修改
                OmsWeiRefund update = new OmsWeiRefund();
                update.setId(refunds.get(0).getId());
                update.setOrderId(refund.getOrderId());
                update.setStatus(refund.getStatus());
                update.setUpdateTime(refund.getUpdateTime());
                update.setReturnWaybillId(refund.getReturnWaybillId());
                update.setReturnDeliveryName(refund.getReturnDeliveryName());
                update.setReturnDeliveryId(refund.getReturnDeliveryId());
                update.setComplaintId(refund.getComplaintId());
                mapper.updateById(update);

//                return ResultVo.error(ResultVoEnum.DataExist, "退款已经存在，更新成功");
            } else {
                newRefund = refund;
                // 不存在，新增
                refund.setShopId(shopId);
                mapper.insert(refund);
//                return ResultVo.success();
            }

//            log.info("TAO退款消息处理" + refund.getRefundId());
            // 组合售后退款表数据
            ErpSaleAfterRefund erpRefund = new ErpSaleAfterRefund();
            erpRefund.setRefundNum(newRefund.getAfterSaleOrderId());
            Integer refundType = null;
            //售后类型。REFUND:退款；RETURN:退货退款。
            //类型(10-退货 20-换货 30-维修 40-大家电安装 50-大家电移机 60-大家电增值服务 70-上门维修 90-优鲜赔 80-补发商品 100-试用收回 11-仅退款)
            if("REFUND".equals(newRefund.getType())){
                refundType = 11;
                erpRefund.setHasGoodReturn(0);
            }else if("RETURN".equals(newRefund.getType())){
                refundType = 10;
                erpRefund.setHasGoodReturn(1);
            }
            erpRefund.setRefundType(refundType);

            erpRefund.setShopId(newRefund.getShopId());
            erpRefund.setShopType(EnumShopType.WEI.getIndex());
            erpRefund.setOriginalOrderId(newRefund.getOrderId());
            try {
                List<OmsWeiOrderItem> omsWeiOrderItems = orderItemMapper.selectList(new LambdaQueryWrapper<OmsWeiOrderItem>().eq(OmsWeiOrderItem::getOrderId, newRefund.getOrderId()).eq(OmsWeiOrderItem::getSkuId, newRefund.getSkuId()));
                erpRefund.setOriginalOrderItemId(omsWeiOrderItems.get(0).getId().toString());
                erpRefund.setGoodsName(omsWeiOrderItems.get(0).getTitle());
                erpRefund.setSpecNum(omsWeiOrderItems.get(0).getSkuCode());
                erpRefund.setGoodsImage(omsWeiOrderItems.get(0).getThumbImg());
                erpRefund.setGoodsSku(omsWeiOrderItems.get(0).getSkuAttrs());
            }catch (Exception e){
                erpRefund.setOriginalOrderItemId("0");
            }
            erpRefund.setOriginalSkuId(newRefund.getSkuId());

//        erpRefund.setErpGoodsId(orderItem.getErpGoodsId());
//        erpRefund.setErpSkuId(orderItem.getErpSkuId());



//        erpRefund.setGoodsImage(orderItem.getGoodsImg());
            erpRefund.setQuantity(newRefund.getCount());
            Integer status = null;
            //退款状态。AfterSaleStatus
            // 售后单当前状态，参考：
            //USER_CANCELD	用户取消申请
            //MERCHANT_PROCESSING	商家受理中
            //MERCHANT_REJECT_REFUND	商家拒绝退款
            //MERCHANT_REJECT_RETURN	商家拒绝退货退款
            //USER_WAIT_RETURN	待买家退货
            //RETURN_CLOSED	退货退款关闭
            //MERCHANT_WAIT_RECEIPT	待商家收货
            //MERCHANT_OVERDUE_REFUND	商家逾期未退款
            //MERCHANT_REFUND_SUCCESS	退款完成
            //MERCHANT_RETURN_SUCCESS	退货退款完成
            //PLATFORM_REFUNDING	平台退款中
            //PLATFORM_REFUND_FAIL	平台退款失败
            //USER_WAIT_CONFIRM	待用户确认
            //MERCHANT_REFUND_RETRY_FAIL	商家打款失败，客服关闭售后
            //MERCHANT_FAIL	售后关闭
            //USER_WAIT_CONFIRM_UPDATE	待用户处理商家协商
            //USER_WAIT_HANDLE_MERCHANT_AFTER_SALE	待用户处理商家代发起的售后申请

            if (newRefund.getStatus().equals("MERCHANT_PROCESSING")) {
                // 10001-待审核
                status = 10001;
            } else if (newRefund.getStatus().equals("USER_WAIT_RETURN")) {
                // 10002-等待买家退货(待客户反馈)
                status = 10002;
            } else if (newRefund.getStatus().equals("MERCHANT_WAIT_RECEIPT")) {
                // 10005-等待卖家确认收货(待收货)
                status = 10005;
            } else if (newRefund.getStatus().equals("MERCHANT_REJECT_REFUND")|| newRefund.getStatus().equals("MERCHANT_REJECT_RETURN")) {
                // 14000-卖家拒绝退款
                status = 14000;
            } else if (newRefund.getStatus().equals("USER_CANCELD")||newRefund.getStatus().equals("RETURN_CLOSED")||newRefund.getStatus().equals("MERCHANT_FAIL")) {
                // 10011-退款关闭（取消）
                status = 10011;
            } else if (newRefund.getStatus().equals("MERCHANT_REFUND_SUCCESS")||newRefund.getStatus().equals("MERCHANT_RETURN_SUCCESS")) {
                // 10010-退款成功（完成）
                status = 10010;
            }
            erpRefund.setStatus(status);
            try {
                erpRefund.setRefundFee(newRefund.getRefundAmount().doubleValue()/100);
            } catch (Exception e) {
            }
            erpRefund.setRefundReason(newRefund.getReasonText());
            erpRefund.setRemark("");
            erpRefund.setCreateTime(new Date());
            erpRefund.setCreateBy("REFUND_MESSAGE");


            // 远程服务调用
            MQRequest<ErpSaleAfterRefund> req = new MQRequest<>();
            req.setMqRequestType(MQRequestType.REFUND_PUSH);
            req.setData(erpRefund);
            ApiResult s = mqClientService.sendRefund(req);
            if(s.getResult()==ApiResultEnum.SUCCESS.getIndex()) {
                //更新自己
                OmsWeiRefund update = new OmsWeiRefund();
                update.setId(newRefund.getId());
                update.setPullStatus(1);
                update.setPullTime(new Date());
                mapper.updateById(update);
            }else{
                //更新自己
                OmsWeiRefund update = new OmsWeiRefund();
                update.setId(newRefund.getId());
                update.setPullStatus(2);
                update.setPullTime(new Date());
                mapper.updateById(update);
            }

            if (refunds != null && refunds.size() > 0) {
                return ResultVo.success("更新成功");
            }else{
                return  ResultVo.success("新增成功");
            }


        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultVo.error(ResultVoEnum.SystemException, "系统异常：" + e.getMessage());
        }
    }
}




