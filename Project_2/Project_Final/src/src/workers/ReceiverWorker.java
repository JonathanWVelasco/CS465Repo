package src.workers;

import src.ChatNode;

import src.NodeInfo;
import src.Utils;
import src.messages.NoteMessage;
import src.messages.JoinMessage;
import src.messages.Message;

import java.io.*;
import java.net.Socket;
//Class for listening for connections
public class ReceiverWorker implements Runnable {
    //Init Variables
    final Socket chatSocket;
    final ObjectOutputStream outputStream;
    final ObjectInputStream inputStream;
    
    //Assign variables
    public ReceiverWorker(Socket inputSocket) throws IOException {
        this.chatSocket = inputSocket;
        outputStream = new ObjectOutputStream(chatSocket.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(chatSocket.getInputStream());
    }

    @Override
    //function to handle our messages 
    public void run() {
        try (
                chatSocket;
                outputStream;
                inputStream
        ) {
            //Notify the user of a new connection
            synchronized (System.out) {
            }
            //Get info from connection
            Object fromClient = inputStream.readObject();
            //set variables
            Message clientMessage = (Message) fromClient;
            checkType(clientMessage);
        } catch (Exception e) {
        }
    }
    
    public void checkType(Message message) {
        switch (message.messageType) {
            case JOIN -> handleJoin((JoinMessage) message);
            case LEAVE -> handleLeave(message);
            case CHAT -> handleChat((NoteMessage) message);
            default -> System.out.print("INVALID MESSAGE TYPE");
        }
    }

    //function for handling joining node
    public void handleJoin(JoinMessage message) {
        try {
            //set variables
            boolean isIn = false;
            String socketIP = chatSocket.getInetAddress().getHostAddress();
            //Integer socketPort = chatSocket.getPort();
            if (message.source.ip.equals(socketIP)) {
                synchronized (ChatNode.lock) {
                    outputStream.writeObject(ChatNode.chatParticipants);
                }
                outputStream.flush();
                Utils.sendToAll(message);
            }
            synchronized (ChatNode.lock) {
                //Check if node is in chatters
                for (NodeInfo node : ChatNode.chatParticipants.keySet()){
                    if (node.equals(message.source)) {
                        isIn = true;
                        break;
                    }
                }
            }
            //Insert current node into chatters
            synchronized (ChatNode.lock) {
                if(!isIn) {
                    ChatNode.chatParticipants.put(message.source, true);
                }
            }

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
    
    public void handleLeave(Message message) {
        //set variables
        NodeInfo toRemove = message.source;
        NodeInfo foundNode = null;
        synchronized (ChatNode.lock) {
        //Iterate through the participants
        for (NodeInfo node : ChatNode.chatParticipants.keySet()){
                //If th node is in the map set it to foundNode
                if (node.equals(toRemove)) {
                    foundNode = node;
                }
        }
            
        //remove the found Node
        ChatNode.chatParticipants.remove(foundNode);
        }

    }
    
    public void handleChat(NoteMessage message) {
        System.out.println(message.toString());

    }
}
