package utils;

import java.io.Serializable;

public class NodeInfo implements Serializable {

    public String IP;
    public String port;
    public String username;
    public MessageTypes type;

    public NodeInfo(String IP, String port, String username, MessageTypes type)
    {
        this.IP = IP;
        this.port = port;
        this.username = username;
        this.type = type;

    }


    // getters
    public String getIP() {
        return IP;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public MessageTypes getType() {
        return type;
    }
}
