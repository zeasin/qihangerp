//package cn.qihangerp.api.server;
//
//import cn.qihangerp.api.mapper.ErpOrderMapper;
//import cn.qihangerp.common.utils.bean.BeanUtils;
//import cn.qihangerp.common.utils.spring.SpringUtils;
//import cn.qihangerp.domain.ErpOrder;
//import cn.qihangerp.domain.ErpOrderReturned;
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundHandlerAdapter;
//import lombok.extern.java.Log;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Log
//@Component
//public class NettyServerHandler extends ChannelInboundHandlerAdapter {
//    @Autowired
//    private ErpOrderMapper orderMapper;
//
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        log.info("=======Netty服务器接收消息");
////        ErpOrder erpOrder = (ErpOrder) msg;
////        System.out.println("Received order: " + erpOrder.getOrderNum());
//
//        // 这里实现将ErpOrder保存到数据库的逻辑
//        // 示例代码省略
//
////        ByteBuf buf = (ByteBuf) msg;
////        try {
////            while (buf.isReadable()) {
////                System.out.print((char) buf.readByte());
////                System.out.flush();
////            }
////        } finally {
////            buf.release();
////        }
//
//
//         //判断消息类型并进行相应的处理
//        if (msg instanceof ErpOrder) {
//            // 处理实体类型A的消息
//            ErpOrder entityA = (ErpOrder) msg;
//            ErpOrderMapper bean = SpringUtils.getBean(ErpOrderMapper.class);
////            bean.insertErpOrder(entityA);
////            orderMapper.insertErpOrder(entityA);
//            System.out.println("Received EntityA: " + entityA.getAddress());
//            // 在这里添加处理实体类型A的业务逻辑
//        }else if (msg instanceof ErpOrderReturned) {
//            // 处理实体类型A的消息
//            ErpOrderReturned returned = (ErpOrderReturned) msg;
//            System.out.println("Received ErpOrderReturned: " + returned.getAddress());
//            // 在这里添加处理实体类型A的业务逻辑
//        } else {
//            // 未知的消息类型
//            System.err.println("Unknown message type: " + msg.getClass().getName());
//        }
//        // 实时响应客户端信息
//        ctx.writeAndFlush("{code:500,msg:'order_num不能为空'}");
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
//        ctx.close();
//    }
//}
