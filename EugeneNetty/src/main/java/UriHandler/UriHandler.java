package UriHandler;

import Server.StatsHandler;
import Utils.ConnectionLogUnit;
import Utils.RequestsCounter;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by EugeneKPI on 28.09.2015.
 */
public class UriHandler {

    private static final int TIMEOUT = 10000;
    private static final String HELLO_WORLD = "<!DOCTYPE html><html><body><h1>Hello World!!!</h1></body></html>";

    private static final String ANSWER_NOT_FOUND = "<!DOCTYPE html><html><body><h1>404 NOT FOUND!</h1></body></html>";

public FullHttpResponse responseToUri(String uri){
    FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);


//

    switch (uri.toLowerCase()){

        case "/hello":
            try {
                Thread.sleep(TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
             response = new DefaultFullHttpResponse(
                    HTTP_1_1, OK, Unpooled.copiedBuffer(HELLO_WORLD, CharsetUtil.UTF_8)
            );
            response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");


        break;

        case "/status":
            final StringBuilder buff = new StringBuilder();

            buff.append("<!DOCTYPE html><html><head>");
            buff.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />\n");
            buff.append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css\">\n");
            buff.append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css\">\n");
            buff.append("</head>");

            buff.append("<body>");
            buff.append("<div style=\"margin: 0 0 0 25px  \">");
            buff.append("<h2>SERVER STATISTICS</h2>");

            // Total connections
            buff.append("<h4>Total connections: ").append(StatsHandler.getTotalConnectionsCounter()).append("</h4>");
            // Active connections
            buff.append("<h4>Active connections: ").append(StatsHandler.getActiveConnectionsCounter()).append("</h4>");

            // Unique Requests per one IP table
            buff.append("<h4>Unique requests per IP :</h4>");
            buff.append("<table class=\"table table-hover table-bordered table-striped\" style=\"width: 30%;\"><tbody>");
            buff.append("<tr><th>").append(" IP ").append("</th><th>").append("requests").append("</th></tr>");
            for (Map.Entry<String, RequestsCounter> pair : StatsHandler.getRequestsCounter().entrySet()) {
                buff.append("<tr><td>");
                buff.append(pair.getKey());
                buff.append("</td><td>");
                buff.append(pair.getValue().getCountOfUniqueRequests());
                buff.append("</td></tr>");
            }
            buff.append("</tbody></table>");

            //Counter of the requests per 1 IP
            buff.append("<h4>IP requests counter :</h4>");
            buff.append("<table class=\"table table-hover table-bordered table-striped\" style=\"width: 30%;\"><tbody>");
            buff.append("<tr><th>").append(" IP ").append("</th><th>").append("requests")
                    .append("</th><th>").append(" last request ").append("</th></tr>");
            for (Map.Entry<String, RequestsCounter> pair : StatsHandler.getRequestsCounter().entrySet()) {
                buff.append("<tr><td>");
                buff.append(pair.getKey());
                buff.append("</td><td>");
                buff.append(pair.getValue().getConnectionCounter());
                buff.append("</td><td>");
                buff.append(pair.getValue().getLastConnectionDate());
                buff.append("</td></tr>");
            }
            buff.append("</tbody></table>");

            //Counter of the redirection per URL
            buff.append("<h4>URL redirection counter :</h4>");
            buff.append("<table class=\"table table-hover table-bordered table-striped\" style=\"width: 40%;\"><tbody>");
            buff.append("<tr><th class=\"col-md-4\"> URL ")
                    .append("</th><th class=\"col-md-1\">").append(" count ").append("</th></tr>");
            for (Map.Entry<String, Integer> pair : StatsHandler.getRedirectionPerURL().entrySet()) {
                buff.append("<tr><td>");
                buff.append(pair.getKey());
                buff.append("</td><td>");
                buff.append(pair.getValue());
                buff.append("</td></tr>");
            }
            buff.append("</tbody></table>");

            //Connections log
            buff.append("<h4>Connections log :</h4>");
            buff.append("<table class=\"table table-hover table-bordered table-striped\" style=\"width: 70%;\"><tbody>");
            buff.append("<tr><th class=\"col-md-1\"> IP ")
                    .append("</th><th class=\"col-md-3\">").append("URI ")
                    .append("</th><th class=\"col-md-3\">").append("timestamp ")
                    .append("</th><th class=\"col-md-1\">").append("sent bytes ")
                    .append("</th><th class=\"col-md-1\">").append("received bytes ")
                    .append("</th><th class=\"col-md-1\">").append("speed (Bytes/Sec)")
                    .append("</th></tr>");
            buff.append("</tbody>");
            for (ConnectionLogUnit c : StatsHandler.getLog()) {
                buff.append("<tr><td>");
                buff.append(c.getIP()).append("</td><td>");
                buff.append(c.getURI()).append("</td><td>");
                buff.append(c.getTimeStamp()).append("</td><td>");
                buff.append(c.getSentBytes()).append("</td><td>");
                buff.append(c.getReceivedBytes()).append("</td><td>");
                buff.append(c.getSpeed()).append("</td></tr>");
            }
            buff.append("</tbody></table></div></body></html>");


             response = new DefaultFullHttpResponse(
                    HTTP_1_1,
                    OK,
                    Unpooled.copiedBuffer(buff.toString(), CharsetUtil.UTF_8)
            );
            response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");


        break;





        default:
            String url = uri.substring(uri.indexOf("=") + 1, uri.length());

            StatsHandler.addURLRedirection(url);

            if (!url.matches("http://\\S*")) {
                url = "http://" + url;
            }

            response = new DefaultFullHttpResponse(
                    HTTP_1_1, FOUND);
            response.headers().set(HttpHeaders.Names.LOCATION, url);
            response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");



       break;
    }


    return response;
}



}
