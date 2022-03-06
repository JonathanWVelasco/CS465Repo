package Client;

import Messages.*;
import java.net.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class Sender extends Thread
{
    // initialize socket and input output streams 

    private LinkedList<NodeInfo> nodeInfoList = null;
    private NodeInfo senderNode = null;
    public boolean active = true; // Boolean used to tell if the node has stopped
    private final Node userNode;


    public Sender(Node chatNode)
    {
        // store parameters for later use
        userNode = chatNode;
    }

    // Creates the Client.Sender thread
    @Override
    public void run()
    {
        //Get the user of the node that created this thread to open sockets
        nodeInfoList = userNode.getInfoList();
        senderNode = userNode.getSelf();

        try
        {

            Scanner inputScan = new Scanner(System.in);

            System.out.println("To join the chat type: \n"
                    + "join ip_address  port \n");


            while (true)
            {
                //Update the node info list just in case it changed since last time
                nodeInfoList = userNode.getInfoList();


                //Get input from chat user
                //This should be some from of string input  
                String input = inputScan.nextLine();

                String lowerIn = input.toLowerCase();

                // split the input into segments
                String[] inputArr = input.split(" ");

                //join 127.0.0.1 2080

                if(inputArr[ 0 ].equals("join"))
                {
                    //If join message
                    //parse out the ip and port that the user is trying to join
                    int joinPort = 0;

                    InetAddress joinIP = null;


                    try
                    {
                        // get the address part
                        joinIP = InetAddress.getByName(inputArr[1]);

                        //get the port part
                        joinPort = Integer.parseInt(inputArr[2]);

                    }

                    catch(UnknownHostException i)
                    {
                        System.out.println(i);

                        System.out.println(i + "ERROR LOCATION 3");
                    }

                    catch(ArrayIndexOutOfBoundsException e)
                    {
                        System.out.println("Please provide an IP and port to join");

                        //   System.out.println("Made it here! 3");

                        //Go back to asking for input
                        continue;
                    }
                    //create a socket to connect
                    Socket otherNode = null;
                    try
                    {
                        otherNode = new Socket(joinIP, joinPort);
                    }
                    catch(ConnectException e)
                    {
                        System.out.println("That IP and Port is not on the network");
                        continue;
                    }

                    //add an input and output stream
                    ObjectOutputStream outputMessage = new ObjectOutputStream(otherNode.getOutputStream());
                    ObjectInputStream inputMessage = new ObjectInputStream(otherNode.getInputStream());



                    //create a new join request message object
                    join newJoin = new join(senderNode);

                    //send the join request message through the input stream
                    //as a string using .toString()
                    outputMessage.writeObject(newJoin);

                    //  System.out.println("Made it here! 4");


                    //wait and read a reply through the output stream
                    joined response = null;
                    try
                    {
                        response = (joined) inputMessage.readObject();
                    }
                    catch(Throwable e)
                    {
                        System.out.println("Failed to convert response");
                    }

                    if(response != null)
                    {
                        //update own list
                        userNode.setInfoList(response.getList());
                        userNode.addNodeInfo(response.getInfo());


                        //Iterate through new lst and notify all
                        //Make notify message
                        update newNotf = new update(senderNode);

                        //Update the node info list of this thread
                        nodeInfoList = userNode.getInfoList();

                        int index = 0;
                        while(index < nodeInfoList.size())
                        {
                            //create a socket to connect
                            NodeInfo indexNodeInfo = (NodeInfo) nodeInfoList.get(index);

                            //Get IP and Port from node at index I
                            InetAddress indexIP = indexNodeInfo.getIPAddress();

                            int indexPort = indexNodeInfo.getPort();

                            //Create a new socket to the node
                            Socket indexSock = new Socket(indexIP, indexPort);

                            //add an output stream
                            ObjectOutputStream notifyMessage = new ObjectOutputStream(indexSock.getOutputStream());

                            //send the Notify message through the input stream
                            //as a string using .toString()
                            notifyMessage.writeObject(newNotf);

                            //Move to next node in list
                            index++;
                        }
                    }//End of notify

                    //Inform user htey joined
                    System.out.println("Join Succeeded!");

                }

                else
                {
                    //If leave message
                    if(lowerIn.equals("leave"))
                    {

                        //Create a loop through the node list of ip and ports
                        int index = 0;
                        while(index < nodeInfoList.size())
                        {
                            //create a socket to connect
                            NodeInfo indexNodeInfo = (NodeInfo) nodeInfoList.get(index);

                            //Get IP and Port from node at index I
                            InetAddress indexIP = indexNodeInfo.getIPAddress();

                            int indexPort = indexNodeInfo.getPort();

                            //Create a new socket to the node
                            Socket indexSock = new Socket(indexIP, indexPort);

                            //add an output stream
                            ObjectOutputStream outputMessage = new ObjectOutputStream(indexSock.getOutputStream());

                            //send the leave message through the socket
                            leave leaveMsg = new leave(senderNode);

                            //send the Leave message through the input stream
                            //as a string using .toString()
                            outputMessage.writeObject(leaveMsg);
                            outputMessage.flush();

                            //Move to next node in list
                            index++;
                        }
                        nodeInfoList = new LinkedList<>();
                        userNode.setInfoList( new LinkedList<NodeInfo>() );

                        active = false;
                        //Close down this node and all threads
                    }

                    else //Not a properly Flagged Message == Regular chat
                    {
                        //Create a loop through the node list of ip and ports
                        int index = 0;

                        //iterate though the list of ip's
                        while(index < nodeInfoList.size())
                        {
                            //create a socket to connect
                            NodeInfo indexNodeInfo = (NodeInfo) nodeInfoList.get(index);

                            //Get IP and Port from node at index I
                            InetAddress indexIP = indexNodeInfo.getIPAddress();
                            int indexPort = indexNodeInfo.getPort();

                            //Create a new socket to the node
                            Socket indexSock = new Socket(indexIP, indexPort);

                            //add an output stream
                            ObjectOutputStream outputMessage = new ObjectOutputStream(indexSock.getOutputStream());

                            //send the chat message through the socket
                            ChatMessage note = new ChatMessage(senderNode, input);

                            //send the chat message through the input stream
                            //as a string using .toString()
                            outputMessage.writeObject(note);


                            //Move to next node in list
                            index++;
                        }

                    }
                }//Done handling message

                //go back to waiting for a user message
            }

        }

        catch(UnknownHostException i)
        {
            System.out.println(i + "ERROR LOCATION 1");
        }

        catch(IOException ioe)
        {
            System.out.println(ioe);
        }

    }

}