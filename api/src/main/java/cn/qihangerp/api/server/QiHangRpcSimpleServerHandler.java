package cn.qihangerp.api.server;

import cn.qihangerp.api.service.ErpSaleAfterRefundService;
import cn.qihangerp.api.service.IErpOrderService;
import cn.qihangerp.domain.ErpSaleAfterRefund;
import cn.qihangerp.mq.MQRequest;
import cn.qihangerp.common.ApiResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.utils.spring.SpringUtils;
import cn.qihangerp.domain.ErpSaleOrder;
import cn.qihangerp.mq.MQRequestType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class QiHangRpcSimpleServerHandler extends SimpleChannelInboundHandler<MQRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MQRequest request) {
        // 处理客户端请求

        //判断消息类型并进行相应的处理
        if (request.getMqRequestType() == MQRequestType.ORDER_CONFIRM) {
            // 确认订单
            IErpOrderService orderService =  SpringUtils.getBean(IErpOrderService.class);
            ResultVo<Integer> integerResultVo = orderService.saveOrderMessage((ErpSaleOrder) request.getData());
            if(integerResultVo.getCode() != ResultVoEnum.SUCCESS.getIndex()){
                ApiResult response = ApiResult.error(integerResultVo.getMsg());
                ctx.writeAndFlush(response);
                return;
            }

        }else if (request.getMqRequestType() == MQRequestType.REFUND_PUSH) {
            // 退款推送
            ErpSaleAfterRefundService refundService =  SpringUtils.getBean(ErpSaleAfterRefundService.class);
            ResultVo<Integer> integerResultVo = refundService.taoRefundMessage((ErpSaleAfterRefund) request.getData());
            if(integerResultVo.getCode() != ResultVoEnum.SUCCESS.getIndex()){
                ApiResult response = ApiResult.error(integerResultVo.getMsg());
                ctx.writeAndFlush(response);
                return;
            }
        } else {
            // 未知的消息类型
            System.err.println("Unknown type");
        }

//        String responseMessage = "Hello, " + request;
        ApiResult response = ApiResult.success();
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
