package Messages;

import Client.NodeInfo;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.LinkedList;

//message type join deals with sending linked list of other clients
public class joined implements MessageType, Serializable
{

     // List of the other nodes
    final LinkedList<NodeInfo> nodesInNetwork;

    // The node that is sending the JoinResponse.
    final NodeInfo sendingNode;

    // get message type
    @Override
    public MessageType.MessageTypes getType()
    {
        return MessageType.MessageTypes.JOINED;
    }

    // initializes joined with the list of nodes to send
    public joined(LinkedList<NodeInfo> nodes,
                  NodeInfo sendingNodeInfo
    ) throws IllegalArgumentException
    {
        this.nodesInNetwork = new LinkedList();

        try
        {
            int counter = 0;
            while( counter <  nodes.size())
            {
                NodeInfo info = (NodeInfo) nodes.get(counter);
                this.nodesInNetwork.add( new NodeInfo( info ));
                counter++;
            }

            this.sendingNode = new NodeInfo( sendingNodeInfo );
        }
        catch( UnknownHostException e )
        {
            throw new
                    IllegalArgumentException( "Input Client.NodeInfo threw "
                    + "an UnknownHostException"
            );
        }
    }


    // gets node info
    public NodeInfo getInfo()
    {
        return this.sendingNode;
    }


    // get node list
    public LinkedList<NodeInfo> getList()
    {
        return this.nodesInNetwork;
    }


}
