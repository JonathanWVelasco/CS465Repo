package Client;

import Messages.*;
import java.net.*;
import java.io.*;
import java.util.LinkedList;


public class Node
{
    // initialize socket and input output streams 
    private Sender sender = null;
    private Receiver receiver = null;
    private LinkedList<NodeInfo> nodeInfoList = null;
    private NodeInfo selfNode = null;
    public boolean active = true; // Boolean used to tell if the node has stopped

    // Create a node

    public Node(InetAddress address, int port, String name) throws IOException, InterruptedException
    {
        //Use given info to add self to the list of ips and ports
        selfNode = new NodeInfo(address, port, name);
        nodeInfoList = new LinkedList<>();

        //Create a receiver thread and hand it this Client.Node
        receiver = new Receiver(this);

        sender = new Sender(this);
        // End of Initialization for Client.Node threads

    }

    // Start the chat client
    public void startChatClient()
    {
        // Begin sender Thread
        sender.start();

        // start the receiver thread.
        receiver.start();
    }

    // Transform a string into a message.
    public MessageType stringToMessage( final String message )
    {
        final String lowerCaseMsg = message.trim().toLowerCase();

        if( lowerCaseMsg.equals( "leave" ) )
        {
            return new leave( this.selfNode );
        }

        // assume this is a chat message
        // send the original message, we don't want to
        // change what the user is trying to send
        return new ChatMessage( this.selfNode,
                message
        );
    }


    public NodeInfo getSelf()
    {
        return this.selfNode;
    }

    public LinkedList getInfoList()
    {
        return this.nodeInfoList;
    }

    public void addNodeInfo(NodeInfo adding)
    {
        nodeInfoList.add(adding);
    }

    public void removeNodeInfo(NodeInfo removing)
    {
        for( NodeInfo no : nodeInfoList )
        {
            if( no.equals( removing ) )
            {
                nodeInfoList.remove( no );
                break;
            }
        }
    }

    public void setInfoList(LinkedList newList)
    {
        nodeInfoList = newList;
    }

//End of class
}