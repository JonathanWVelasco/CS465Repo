package Messages;

import Client.NodeInfo;

// message types
public interface MessageType
{
    // enum of message types
    public enum MessageTypes
    {
        JOIN, // join message
        JOINED, // joined chat
        UPDATE, // Updates other node types
        NOTE, // Message data sent
        LEAVE // leave message
    }

    MessageTypes getType();

    NodeInfo getInfo();
}