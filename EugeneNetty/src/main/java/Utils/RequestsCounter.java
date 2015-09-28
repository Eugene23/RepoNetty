package Utils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Администратор on 27.09.2015.
 */
public class RequestsCounter {

    private String IP;
    private Set<String> requests = new HashSet<String>();
    private int connectionCounter;
    private Date lastConnection;


    public RequestsCounter(String IP , String request){
        this.IP=IP;
        requests.add(request);
        this.connectionCounter = 1;
        this.lastConnection = new Date();

    }

    public synchronized RequestsCounter addRequest(String request){
        requests.add(request);
        this.connectionCounter++;
        lastConnection = new Date();
        return this;
    }

    public String getIP(){
        return IP;
    }

    public int getCountOfUniqueRequests(){
        return requests.size();
    }

    public int getConnectionCounter(){
        return connectionCounter;
    }

    public Date getLastConnectionDate(){
        return lastConnection;
    }
}
