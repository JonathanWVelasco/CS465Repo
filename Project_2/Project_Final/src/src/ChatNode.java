package src;

import src.messages.NoteMessage;
import src.messages.Message;
import src.messages.MessageType;
import src.messages.JoinMessage;
import src.workers.Receiver;
import src.workers.Sender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
This is the core class that houses the receiver and sender classes in their threads. This represents one node in the
mesh topology of the distributed chat system.
 */
public class ChatNode {
    //Store participants list in a map
    public static Chatters chatParticipants = new Chatters();
    //Create the current node and its information
    public static NodeInfo thisNode;
    public static final Object lock = new Object();

    public static void main(String[] args) {
        System.out.println("New User Created!!");
        if (args.length != 2) {
            System.err.println("Parameter Format: <PORT NUMBER> <LOGICAL NAME>");
            System.exit(1);
        }
        //Get our port and logical name for the command line
        int port = Integer.parseInt(args[0]);
        String logicalName = args[1];
        thisNode = new NodeInfo(port, logicalName);

        //Create receiver thread, passing it the server socket we create.
        try {
            Thread listenerThread = new Thread(new Receiver(port));
            listenerThread.start();
            synchronized (ChatNode.lock){
                ChatNode.lock.wait();
            }
            System.out.println("Your user info: " + ChatNode.thisNode.toString());
            printHelpMessage();
            //Add ourselves to our participants map once we initialize the IP.
            chatParticipants.put(thisNode, true);
            //This will wait for user input and spawn sender when needed.
            handleUser();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static void handleUser() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        //init variables
        String input;
        String[] inputParts;
        String joiningIp;
        int joiningPort;
        Sender sender;
        boolean isNote;
        try {
            //Execute our loop
            while (true) {
                //assign variables
                sender = null;
                isNote = false;
                input = reader.readLine();
                inputParts = input.split(" ");
                switch (inputParts[0]) {
                    //Handle user calling JOIN  
                    case "JOIN" -> {
                        //Get the joining IP from command line
                        joiningIp = inputParts[1];
                        if (inputParts.length == 3 && Utils.isValidIpAddr(joiningIp) && Utils.isInt(inputParts[2])) {
                            //Assign the port from command line
                            joiningPort = Integer.parseInt(inputParts[2]);
                            //Create a sender of com.company.message type JOIN
                            sender = new Sender(
                                    new JoinMessage(MessageType.JOIN, ChatNode.thisNode, joiningIp, joiningPort));

                            System.out.println("You have joined the group chat!");
                        } else {
                            isNote = true;
                        }
                    }
                    //Handle user calling LEAVE  
                    case "LEAVE" -> {
                        if (chatParticipants.size() > 1) {
                            sender = new Sender(new Message(MessageType.LEAVE, ChatNode.thisNode));
                        } else {
                            //If user is alone it cannot leave
                            System.out.println("Size of chatter map is " + chatParticipants.size());
                            System.out.println("Cannot leave yourself alone! Call QUIT to shut down the session.");
                        }
                    }

                    //Handle user calling map
                    case "MAP" -> {
                        //show list of current chatters
                        synchronized (ChatNode.lock){
                            System.out.println(ChatNode.chatParticipants.toString());
                        }
                    }
                       
                    //Handle user calling SHUTDOWN
                    case "SHUTDOWN" -> System.exit(0);

                    //If command is from none of the above, it is a chat message
                    default-> {
                        NoteMessage chatMessage = new NoteMessage(MessageType.CHAT, ChatNode.thisNode,
                                input);
                        //Only create a new sender if the chatters map has more than one node
                        if (chatParticipants.size() > 1) {
                            sender = new Sender(chatMessage);
                        }
                        // check to see if it is your message
                        if(chatMessage.source.name != thisNode.name)
                        {
                            System.out.println(chatMessage.toString());
                        }

                    }


                }
                if (sender != null) {
                    new Thread(sender).start();
                }

            }
        } catch (IOException e) {
            System.err.println("There was trouble reading your input.");
            System.err.println(e.getLocalizedMessage());
            System.exit(1);
        }
    }
    
    //class that shows the valid commands 
    private static void printHelpMessage() {
        System.out.println(
                "JOIN <IP Address> <Port Number>\n" +
                "LEAVE\n" +
                "SHUTDOWN\n");

        System.out.println("If you are doing 'multiple instances' to test this,\n" +
                "then use different port number and name for client,\n" +
                "then join the user you want with the IP and THEIR port");
    }
}
