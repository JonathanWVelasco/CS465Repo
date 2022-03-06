package Client;

import Messages.ChatMessage;
import Messages.formatter;
import Messages.update;
import Messages.join;
import Messages.joined;
import Messages.leave;
import Messages.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class Receiver extends Thread
{
    private Node userNode;

    public Receiver(Node chatNode)
    {
        // store parameters for later use
        userNode = chatNode;

    }

    public void run()
    {
        //Get the user of the node that created this thread to open sockets
        LinkedList nodeInfoList = userNode.getInfoList();
        NodeInfo selfNode = userNode.getSelf();
        int port = selfNode.getPort();

        try
        {
            // begin server given port that is passed in.
            ServerSocket serverSocket = new ServerSocket(port);

            while (true)
            {

                //Wait for a user to connect
                Socket socketTalker = serverSocket.accept();

                //Update Client.Node info from user Client.Node for this thread
                nodeInfoList = userNode.getInfoList();

                //Create an input and output stream to handle this connection
                ObjectOutputStream outputMessage = new ObjectOutputStream(socketTalker.getOutputStream());
                ObjectInputStream inputMessage = new ObjectInputStream(socketTalker.getInputStream());
                //System.out.println("Made input and output");

                MessageType messageRec = null;

                try
                {
                    //System.out.println("getting object");
                    messageRec = (MessageType) inputMessage.readObject();
                    //System.out.println("got object");
                }
                catch(Throwable e)
                {
                    e.printStackTrace();
                    continue;
                }

                //Check the received message
                //IF NULL == Bad Message
                if(messageRec == null)
                {
                    //go back to the start of the loop
                    System.out.println("Message was null");
                    continue;//handle the error by going to the top of the loop
                }

                //Record the input as a String
                String input = messageRec.toString();


                //IF the message is a Join message
                if(messageRec.getType() == MessageType.MessageTypes.JOIN)
                {
                    //Transfrom the message into the join request type
                    join joinReq = (join) messageRec;

                    //Send the ip/port list of this node back
                    joined responseMsg = new joined(nodeInfoList, selfNode);
                    outputMessage.writeObject(responseMsg);
                }
                //IF the message is a Join NOTIFY message
                else if(messageRec.getType() == MessageType.MessageTypes.UPDATE)
                {
                    //Transform the message into the join notification type
                    update joinNotf = (update) messageRec;


                    //Append given ip/port of the message to list of this node
                    userNode.addNodeInfo(joinNotf.getInfo());
                    //Update the node info list of this thread
                    nodeInfoList = userNode.getInfoList();

                    NodeInfo notifingNode = (NodeInfo) joinNotf.getInfo();
                    System.out.println("New Client Joined: " + notifingNode.getName() );
                    //Reshow chat input test
                }
                //IF the message is a normal message
                else if(messageRec.getType() == MessageType.MessageTypes.NOTE)
                {
                    //Transform the message into the chat message type
                    ChatMessage note = (ChatMessage) messageRec;

                    NodeInfo talkerInfo = note.getInfo();
                    formatter fmt = new formatter();

                    //Display the message to the user
                    System.out.print( fmt.formatMessage( note ) + "\n");
                }
                //IF the message is a Leave message
                else if (messageRec.getType() == MessageType.MessageTypes.LEAVE)
                {
                    //Transform the message into the leave message type
                    leave leaveMsg = (leave) messageRec;
                    System.out.println( leaveMsg.getInfo().getName() + " has left the chat.");

                    //Remove the ip/port of the leaver from this nodes list
                    userNode.removeNodeInfo(leaveMsg.getInfo());
                }

            }

        }

        catch(UnknownHostException i)
        {
            System.out.println(i);
        }

        catch(IOException ioe)
        {
            System.out.println(ioe);
        }
    }
}