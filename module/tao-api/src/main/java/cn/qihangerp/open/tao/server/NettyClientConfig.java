package cn.qihangerp.open.tao.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Log
@Component
public class NettyClientConfig {

//    @Autowired
//    private NettyClientHandler nettyClientHandler;
    @Autowired
    private SimpleClientHandler simpleClientHandler;
    @Value("${server.address-ip}")
    private String serverIp;
    @Value("${server.address-port}")
    private Integer serverPort;

    @PostConstruct
    public void startClient() {
        log.info("======Netty客户端初始化1");
        new Thread(() -> {
            log.info("======Netty客户端初始化2");
            // 初始化 Netty 客户端
            EventLoopGroup group = new NioEventLoopGroup();

            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline p = ch.pipeline();
                                p.addLast(
                                        new ObjectEncoder(),
                                        new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                        simpleClientHandler);
                            }
                        })
                        .option(ChannelOption.SO_KEEPALIVE, true);

                ChannelFuture future = bootstrap.connect(serverIp,serverPort).sync();
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                group.shutdownGracefully();
            }
        }).start();
    }
}

