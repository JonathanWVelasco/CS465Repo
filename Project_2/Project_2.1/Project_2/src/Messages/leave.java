package Messages;


import Client.NodeInfo;

import java.io.Serializable;
import java.net.UnknownHostException;

// Leave message
// Tells other users to remove them from node list
public class leave implements MessageType, Serializable
{
    private final NodeInfo leavingNodeInfo;

    // constructor
    public leave(final NodeInfo leavingNode )
            throws IllegalArgumentException
    {
        try
        {
            this.leavingNodeInfo = new NodeInfo( leavingNode );
        }
        catch( UnknownHostException e )
        {
            throw new
                    IllegalArgumentException( "Unable to copy Client.NodeInfo" );
        }
    }

    // returns message type leave
    @Override
    public MessageType.MessageTypes getType()
    {
        return MessageTypes.LEAVE;
    }

    // gets node info
    public NodeInfo getInfo()
    {
        return this.leavingNodeInfo;
    }
}
