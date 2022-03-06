package utils;

import java.io.Serializable;

public class Message implements MessageTypes {

    public MessageTypes type;
    public String content;
    public NodeInfo node;

    public Message(MessageTypes type, String content, NodeInfo node)
    {
        this.type = type;
        this.content = content;
        this.node = node;
    }

}
