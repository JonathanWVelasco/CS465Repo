package src.workers;

import src.ChatNode;
import src.Chatters;
import src.Utils;
import src.messages.Message;
import src.messages.JoinMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
public class Sender implements Runnable {
    //init variables
    final Message message;
    //assign variables
    public Sender(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        try {
            switch (message.messageType) {
                case JOIN -> {
                    JoinMessage joinMessage = (JoinMessage) message;
                    try (
                            //create new socket
                            Socket socket = new Socket(joinMessage.knownUserIp, joinMessage.knownUserPort);
                            //get stream for sending
                            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                            //get stream for receiving
                            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
                    ) {
                        out.flush();
                        out.writeObject(message);
                        out.flush();
                        try {
                            //reads the chatter list sent back
                            Chatters othersList = (Chatters) in.readObject();
                            ChatNode.chatParticipants.putAll(othersList);
                        } catch (ClassNotFoundException e) {
                            System.err.println("Invalid application message received!");
                        }
                    } catch (IOException e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
                   
                case CHAT -> Utils.sendToAll(message);

                case LEAVE -> {
                    try {
                        while (!Utils.sendToAll(message)) {
                            Utils.sendToAll(message);
                        }
                        synchronized (ChatNode.lock) {
                            //clear the current nodes chatter list
                            ChatNode.chatParticipants.clear();
                            //put the current node back into its own list
                            ChatNode.chatParticipants.put(ChatNode.thisNode, true);
                        }
                    } catch (Exception e){
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        } catch (IOException e) {
        }
    }
}
