package server;

import utils.Message;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import utils.MessageTypes;
import utils.NodeInfo;

import javax.swing.*;

public class ClientHandler implements Runnable, MessageTypes{

    public static ArrayList<NodeInfo> clientArray = new ArrayList<>();

    public Socket socket;
    public ObjectInputStream objectReader;
    public ObjectOutputStream bufferedWriter;
    public String clientUsername;

    public ClientHandler(Socket socket) {
        try
        {
            this.socket = socket;
            this.objectReader = new ObjectInputStream(this.socket.getInputStream());
            this.bufferedWriter = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

            /*
             */


    }

    @Override
    public void run()
    {
        Message objectFromChat;
        System.out.println("It ran");
        try
        {
            objectFromChat = (Message) this.objectReader.readObject();
            int messageType = objectFromChat.getNode().getType();
            if ( messageType == JOIN)
            {
                // assign client username for array from incoming object
                this.clientUsername = objectFromChat.getNode().getUsername();

                //add client to array list
                clientArray.add(objectFromChat.getNode());

                broadcastMessage("SERVER: " + clientUsername +" has entered the chat");
                //System.out.println("SERVER: " + clientUsername + " has entered the Chat");
            }
            else if (messageType == LEAVE)
            {
                clientArray.remove(objectFromChat.getNode());
                broadcastMessage(objectFromChat.getNode().getUsername() + " has left the chat");
            }
            else
            {
                if (clientArray.contains(objectFromChat.getNode()))
                {
                    broadcastMessage(objectFromChat.getNode().getUsername() + ": " + objectFromChat.getContent());
                }
                else
                {
                System.out.println("a dumb was did");
                }
            }
            closeEverything(this.socket, this.objectReader,this.bufferedWriter);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    public void broadcastMessage(String message){
        //System.out.println("Im trying to broadcast now");
        Message msg;
        for(NodeInfo client : clientArray){
            try{
                //System.out.println("Im trying to broadcast in try");
                // create a socket to the client
                System.out.println("Trying to make socket at" + client.getIP() +" ::" + client.getPort());
                Socket outgoingSocket = new Socket(client.getIP(), client.getPort());
                //System.out.println("Socket MADE");
                //ObjectInputStream localObjectReader = new ObjectInputStream(outgoingSocket.getInputStream());
                ObjectOutputStream localObjectWriter = new ObjectOutputStream(outgoingSocket.getOutputStream());
                //System.out.println("Writer MADE");
                // create a message object to send
                 msg = new Message(message, null);
                //if(!client.getUsername().equals(clientUsername))
               // {
                localObjectWriter.writeObject(msg);
                localObjectWriter.flush();
               // }

            }catch (IOException e){
                System.out.println("heres the error message:" + e.getMessage());
                closeEverything(socket, objectReader,bufferedWriter);
            }
        }
    }


    public void removeClient(){
        clientArray.remove(this);

    }


    public void closeEverything(Socket socket, ObjectInputStream bufferedReader, ObjectOutputStream bufferedWriter){
        removeClient();
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedReader.close();
            }
            if(socket!=null){
                socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }


}
