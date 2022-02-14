package Messages;


import java.io.Serializable;
import Client.NodeInfo;


// Message requesting to join chat
public class join implements MessageType, Serializable
{

    // The joining nodes info
    private final NodeInfo joiningNodeInfo;

    // Get message type
    @Override
    public MessageType.MessageTypes getType()
    {
        return MessageType.MessageTypes.JOIN;
    }

    // Initialize message.
    public join(NodeInfo info )
    {
        this.joiningNodeInfo = info;
    }

    // get the node information
    public NodeInfo getInfo()
    {
        return this.joiningNodeInfo;
    }
}