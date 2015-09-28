package Server;

import UriHandler.UriHandler;
import Utils.ConnectionLogUnit;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.net.InetSocketAddress;

/**
 * Created by EugeneKPI on 28.09.2015.
 */
@HttpHandler.Sharable
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest httpRequest) throws Exception {


        long time = System.nanoTime();

        String requestIP = (((InetSocketAddress) ctx.channel().remoteAddress()).getHostString());
        String URI = httpRequest.getUri();
        UriHandler uriHandler = new UriHandler();

        FullHttpResponse response = uriHandler.responseToUri(URI);
        //close the connection immediately because no more requests can be sent from the browser
        ctx.write(response).addListener(ChannelFutureListener.CLOSE);

        // do some statistics
        ByteBuf buffer = Unpooled.copiedBuffer(httpRequest.toString().getBytes());
        int receivedBytes = buffer.readableBytes() + httpRequest.content().readableBytes();
        int sentBytes = response.content().writerIndex();
        long time0 = System.nanoTime() - time;
        double time1 = time0 / (double) 1000000000;
        long speed = Math.round((sentBytes + receivedBytes) / time1);

        ConnectionLogUnit logUnit = new ConnectionLogUnit(requestIP, URI, sentBytes, receivedBytes, speed);

        StatsHandler.addRequestsCounter(requestIP, URI);
        StatsHandler.addLogUnit(logUnit);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
