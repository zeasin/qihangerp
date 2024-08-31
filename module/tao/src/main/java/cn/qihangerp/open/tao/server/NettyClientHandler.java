//package cn.qihangerp.open.tao.server;
//
//import cn.qihangerp.domain.ErpOrder;
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundHandlerAdapter;
//import org.springframework.stereotype.Component;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.io.Serializable;
//
//@Component
//public class NettyClientHandler extends ChannelInboundHandlerAdapter {
//    private ChannelHandlerContext ctx; // 保存 ChannelHandlerContext 引用
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) {
////        ErpOrder order = new ErpOrder();
////        order.setOrderNum("123456");
////        order.setAddress("Product");
////        order.setAmount(99.99);
////        ctx.writeAndFlush(order);
//        this.ctx = ctx; // 保存 ChannelHandlerContext 引用
////        String message = "Hello from client!";
////        byte[] messageBytes = message.getBytes();
////        ByteBuf buffer = Unpooled.copiedBuffer(messageBytes);
////        ctx.writeAndFlush(buffer);
//    }
//
//    // 其他方法中发送消息到服务器的示例
////    public void sendMessageToServer(String message) {
////        if (ctx != null) {
////            byte[] messageBytes = message.getBytes();
////            ByteBuf buffer = Unpooled.copiedBuffer(messageBytes);
////            ctx.writeAndFlush(buffer);
////        } else {
////            System.out.println("ChannelHandlerContext is not initialized.");
////        }
////    }
//    public void sendEntity(Serializable entity) throws IOException {
//        // 将实体类型编码为字节数组并发送到服务器
////        ByteArrayOutputStream baos = new ByteArrayOutputStream();
////        ObjectOutputStream oos = new ObjectOutputStream(baos);
////        oos.writeObject(entity);
////        oos.flush();
////        byte[] data = baos.toByteArray();
////        ctx.writeAndFlush(ctx.alloc().buffer().writeBytes(data));
////        ErpOrder order = new ErpOrder();
////        order.setOrderNum("123456");
////        order.setAddress("Product");
////        order.setAmount(99.99);
//        ctx.writeAndFlush(entity);
//    }
//
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        System.out.println("Client received: " + msg);
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
//        ctx.close();
//    }
//}
//
//
