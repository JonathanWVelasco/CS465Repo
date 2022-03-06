package Client;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NodeInfo implements Serializable
{
    // IP address
    private final InetAddress ipAddress;

    // Port
    private final int port;

    // user's name
    private final String logicalName;

    // constructor to initialize a clients node info
    public NodeInfo( InetAddress ip,
                     int port,
                     String name
    )
    {
        this.ipAddress = ip;
        this.port = port;
        this.logicalName = name;

    }

    // copy a clients node info
    public NodeInfo( final NodeInfo other ) throws UnknownHostException
    {
        this.ipAddress = InetAddress
                .getByAddress( other.ipAddress.getAddress() );
        this.port = other.port;
        this.logicalName = other.logicalName;
    }
    // methods

    // methods to grab node info
    public InetAddress getIPAddress()
    {
        return this.ipAddress;
    }

    public int getPort()
    {
        return this.port;
    }

    public String getName()
    {
        return	this.logicalName;
    }

    // check if a client's node information is equal to another
    public boolean equals( NodeInfo comp )
    {
        return this.getIPAddress().equals( comp.getIPAddress())
                && this.getPort() == comp.getPort()
                && this.getName().equals( comp.getName() );
    }

}