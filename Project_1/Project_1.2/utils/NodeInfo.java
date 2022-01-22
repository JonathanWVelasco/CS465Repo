package utils;

import java.io.Serializable;
import utils.MessageTypes;

public class NodeInfo implements Serializable {

    public String IP;
    public int port;
    public String username;
    public int type;

    public NodeInfo(String IP, int port, String username, int type)
    {
        this.IP = IP;
        this.port = port;
        this.username = username;
        this.type = type;

    }


    // getters
    public String getIP()
    {
        return IP;
    }
    public void setIP(String ip)
    {
        this.IP = ip;
    }

    public int getPort()
    {
        return port;
    }
    public void setPort(int port)
    {
        this.port = port;
    }

    public String getUsername()
    {
        return username;
    }
    public void setUsername(String username)
    {
        this.username = username;
    }

    public int getType()
    {
        return type;
    }
    public void setType(int type)
    {
        this.type = type;
    }
    @Override
    public boolean equals(Object o)
    {
        try{
            NodeInfo obj = (NodeInfo) o;
            if (( obj.getIP().equals(this.getIP())) && ( obj.getPort() == this.getPort()))
            {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
