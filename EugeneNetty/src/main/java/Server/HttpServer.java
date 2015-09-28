package Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by Администратор on 27.09.2015.
 */
public class HttpServer {
    private final int port;
    public HttpServer(int port){
        this.port=port;
    }

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public void start() throws InterruptedException {

        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(4);

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerInitializer());
            Channel channel = serverBootstrap.bind(port).sync().channel();
            channel.closeFuture().sync();



        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }

    }


    int a =2;

}
