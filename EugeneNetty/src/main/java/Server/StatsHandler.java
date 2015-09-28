package Server;

import Utils.ConnectionLogUnit;
import Utils.LoggingQueue;
import Utils.RequestsCounter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Администратор on 27.09.2015.
 */
@StatsHandler.Sharable
public class StatsHandler extends ChannelTrafficShapingHandler {


    private static final AtomicInteger totalConnectionsCounter = new AtomicInteger(0);
    private static final AtomicInteger activeConnectionsCounter = new AtomicInteger(0);
    private static final ConcurrentHashMap<String,RequestsCounter> requestsCounter = new ConcurrentHashMap<String, RequestsCounter>();
    private static final ConcurrentHashMap<String,Integer> redirectionPerURL = new ConcurrentHashMap<String, Integer>();
    private static final LoggingQueue<ConnectionLogUnit> log = new LoggingQueue<ConnectionLogUnit>();

    public StatsHandler(long checkInterval) {
        super(checkInterval);
    }




    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        totalConnectionsCounter.getAndIncrement();
        activeConnectionsCounter.getAndIncrement();
        super.channelRead(ctx, msg);
    }


    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        activeConnectionsCounter.getAndDecrement();
        super.handlerRemoved(ctx);
    }

    public static void addLogUnit(ConnectionLogUnit unit) {
        log.add(unit);
    }

    public static void addURLRedirection(String url) {
        synchronized (redirectionPerURL) {
            if (!redirectionPerURL.containsKey(url)) {
                redirectionPerURL.put(url, 1);
            } else {
                redirectionPerURL.put(url, redirectionPerURL.get(url) + 1);
            }
        }
    }

    public static void addRequestsCounter(String requestIP, String URI) {
        RequestsCounter c;
        synchronized (requestsCounter) {
            if (!requestsCounter.containsKey(requestIP)) {
                c = new RequestsCounter(requestIP, URI);
                requestsCounter.put(requestIP, c);
            } else {
                c = requestsCounter.get(requestIP).addRequest(URI);
                requestsCounter.put(requestIP, c);
            }
        }
    }

    public static int getTotalConnectionsCounter() {
        return totalConnectionsCounter.get();
    }

    public static int getActiveConnectionsCounter() {
        return activeConnectionsCounter.get();
    }

    public static ConcurrentHashMap<String, RequestsCounter> getRequestsCounter() {
        return requestsCounter;
    }

    public static ConcurrentHashMap<String, Integer> getRedirectionPerURL() {
        return redirectionPerURL;
    }

    public static LoggingQueue<ConnectionLogUnit> getLog() {
        return log;
    }




}
