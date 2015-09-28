package Utils;

import java.util.Date;

/**
 * Created by Администратор on 27.09.2015.
 */
public class ConnectionLogUnit {

    private String IP;
    private String URI;
    private Date timeStamp;
    private int sentBytes;
    private int receivedBytes;
    private long speed;

    public ConnectionLogUnit(String IP, String URI, int sentBytes, int receivedBytes, long speed ) {
        this.IP = IP;
        this.URI = URI;
        this.timeStamp = new Date();
        this.sentBytes = sentBytes;
        this.receivedBytes = receivedBytes;
        this.speed = speed;
    }


    public String getIP() {
        return IP;
    }

    public String getURI() {
        return URI;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public int getSentBytes() {
        return sentBytes;
    }

    public int getReceivedBytes() {
        return receivedBytes;
    }

    public long getSpeed() {
        return speed;
    }

    @Override
    public String toString() {
        return "ConnectionLogUnit{" +
                "IP='" + IP + '\'' +
                ", URI='" + URI + '\'' +
                ", timeStamp=" + timeStamp +
                ", sentBytes=" + sentBytes +
                ", receivedBytes=" + receivedBytes +
                ", speed=" + speed +
                '}';
    }

}




