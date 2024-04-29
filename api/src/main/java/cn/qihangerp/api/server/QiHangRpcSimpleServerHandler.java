package cn.qihangerp.api.server;

import cn.qihangerp.api.mapper.ErpOrderMapper;
import cn.qihangerp.common.ApiRequest;
import cn.qihangerp.common.ApiResult;
import cn.qihangerp.common.utils.spring.SpringUtils;
import cn.qihangerp.domain.ErpOrder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class QiHangRpcSimpleServerHandler extends SimpleChannelInboundHandler<ApiRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ApiRequest request) {
        // 处理客户端请求

        //判断消息类型并进行相应的处理
        if (request.getType() == 101) {
            // 订单保存
            ErpOrder entityA = (ErpOrder) request.getData();
            ErpOrderMapper bean = SpringUtils.getBean(ErpOrderMapper.class);
//            bean.insertErpOrder(entityA);
//            orderMapper.insertErpOrder(entityA);
            System.out.println("Received EntityA: " + entityA.getAddress());

        }else if (request.getType() == 102) {
            // 确认订单

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
