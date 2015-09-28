package Server;

import java.net.BindException;

/**
 * Created by Администратор on 27.09.2015.
 */
public class Main extends Thread {

    private static int port;
    private static HttpServer httpServer;


    public static void main(String[] args)  {


        port = 8080;

        new Main().start();
        System.err.println("Server choose this port - "+port);

        try{
            httpServer = new HttpServer(port);
            httpServer.start();
        }catch (InterruptedException e){
            System.err.println("Hi! Server trying to start on this port - "+port+" , but it is already in use.");
            System.exit(1);
        }


    }
}
