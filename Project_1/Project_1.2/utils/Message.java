package utils;

import java.io.Serializable;
import utils.MessageTypes;

public class Message implements Serializable {

    private String content;
    private NodeInfo node;

    public Message(String content, NodeInfo node)
    {
        this.content = content;
        this.node = node;
    }
    public String getContent()
    {
        return this.content;
    }
    public void setContent(String info)
    {
        this.content = info;
    }
    public NodeInfo getNode()
    {
        return this.node;
    }
    public void setNode(NodeInfo node)
    {
        this.node = node;
    }



}
