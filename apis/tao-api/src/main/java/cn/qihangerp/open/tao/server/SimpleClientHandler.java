package cn.qihangerp.open.tao.server;
import cn.qihangerp.domain.ErpSaleAfterRefund;
import cn.qihangerp.domain.ErpSaleOrder;
import cn.qihangerp.mq.MQRequest;
import cn.qihangerp.common.ApiResult;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class SimpleClientHandler extends SimpleChannelInboundHandler<ApiResult> {
    private final BlockingQueue<ApiResult> responseQueue = new LinkedBlockingQueue<>();
    private ChannelHandlerContext ctx; // 保存 ChannelHandlerContext 引用
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
//        ErpOrder order = new ErpOrder();
//        order.setOrderNum("123456");
//        order.setAddress("Product");
//        order.setAmount(99.99);
//        ctx.writeAndFlush(order);
        this.ctx = ctx; // 保存 ChannelHandlerContext 引用
//        String message = "Hello from client!";
//        byte[] messageBytes = message.getBytes();
//        ByteBuf buffer = Unpooled.copiedBuffer(messageBytes);
//        ctx.writeAndFlush(buffer);
    }
    // 发送请求并阻塞等待响应
    public ApiResult sendRequestAndWaitForResponse(MQRequest<ErpSaleOrder> apiRequest) throws InterruptedException {
        // 构造请求消息
//        String req = buildRequestFromErpOrder(apiRequest);

        // 发送请求并将当前线程阻塞，直到收到响应或超时
        ChannelFuture future = ctx.writeAndFlush(apiRequest).sync();

        // 注册监听器，处理异步响应
        future.addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                // 请求成功发送，等待响应
            } else {
                // 请求发送失败
                Throwable cause = channelFuture.cause();
                cause.printStackTrace();
            }
        });

        // 阻塞等待响应
        ApiResult response = responseQueue.poll(30, TimeUnit.SECONDS); // 设置超时时间为30秒
        if (response == null) {
            // 超时未收到响应
            throw new RuntimeException("Timeout waiting for response");
        }

        return response;
    }

    public ApiResult sendRefund(MQRequest<ErpSaleAfterRefund> apiRequest) throws InterruptedException {
        // 构造请求消息
//        String req = buildRequestFromErpOrder(apiRequest);

        // 发送请求并将当前线程阻塞，直到收到响应或超时
        ChannelFuture future = ctx.writeAndFlush(apiRequest).sync();

        // 注册监听器，处理异步响应
        future.addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                // 请求成功发送，等待响应
            } else {
                // 请求发送失败
                Throwable cause = channelFuture.cause();
                cause.printStackTrace();
            }
        });

        // 阻塞等待响应
        ApiResult response = responseQueue.poll(30, TimeUnit.SECONDS); // 设置超时时间为30秒
        if (response == null) {
            // 超时未收到响应
            throw new RuntimeException("Timeout waiting for response");
        }

        return response;
    }

//    private ApiRequest buildRequestFromErpOrder(ApiRequest request) {
//        // 在这里根据 ErpOrder 构造请求消息字符串
//        // 这里假设简单地将 ErpOrder 转换为 JSON 字符串
//        return request;
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ApiResult response) {
        // 将收到的响应放入队列中
        responseQueue.offer(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

