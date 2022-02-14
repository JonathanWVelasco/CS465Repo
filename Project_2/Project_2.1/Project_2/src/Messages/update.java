package Messages;

import Client.NodeInfo;

import java.io.Serializable;
import java.net.UnknownHostException;

// updates other nodes  with the joining nodes info
public class update implements MessageType, Serializable
{
    //joining node info
    final NodeInfo joiningNodeInfo;

    // update constructor creates joining node info
    public update(final NodeInfo joiningNode )
            throws IllegalArgumentException
    {
        try
        {
            joiningNodeInfo = new NodeInfo( joiningNode );
        }
        catch( UnknownHostException e )
        {
            throw new
                    IllegalArgumentException( "Unable to copy Client.NodeInfo" );
        }
    }


    // Get message type
    @Override
    public MessageType.MessageTypes getType()
    {
        return MessageType.MessageTypes.UPDATE;
    }

    // gets node info
    public NodeInfo getInfo()
    {
        return this.joiningNodeInfo;
    }

}
