package Server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import javax.xml.ws.spi.http.HttpExchange;
import javax.xml.ws.spi.http.HttpHandler;
import java.io.IOException;

/**
 * Created by Администратор on 27.09.2015.
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

   private final StatsHandler statsHandler = new StatsHandler(0);

    private final Server.HttpHandler httpHandler = new Server.HttpHandler();

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();

        channelPipeline.addLast("codec" , new HttpServerCodec())
                .addLast("aggregator",new HttpObjectAggregator(512*1024))
                .addLast("statsHandler",statsHandler)
                .addLast("handler" ,httpHandler );
    }
}
