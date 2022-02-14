package Messages;

import Client.NodeInfo;

import java.io.Serializable;
import java.net.UnknownHostException;

// chat message with node info
public class ChatMessage implements MessageType, Serializable
{
    // The chat message sent by a client
    final String messagedata;

    // Message senders node info
    final NodeInfo sendingNode;

    public ChatMessage( final NodeInfo sender,
                        final String message
    ) throws IllegalArgumentException
    {

        try
        {
            this.sendingNode = new NodeInfo( sender );
        }
        catch( UnknownHostException e )
        {
            throw new
                    IllegalArgumentException( "Unable to copy Client.NodeInfo");
        }

        this.messagedata = message;

    }

    // returns message type note
    @Override
    public MessageType.MessageTypes getType()
    {
        return MessageTypes.NOTE;
    }

    //gets note data
    public String getMessage()
    {
        return this.messagedata;
    }

    // gets node info
    public NodeInfo getInfo()
    {
        return this.sendingNode;
    }
}