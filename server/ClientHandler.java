package server;

import utils.Message;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientArray = new ArrayList<>();

    public Socket socket;
    public ObjectInputStream objectReader;
    public ObjectOutputStream bufferedWriter;
    public String clientUsername;

    public ClientHandler(Socket socket) {

        try{
            this.socket = socket;
            this.bufferedWriter = new ObjectOutputStream(socket.getOutputStream());
            this.objectReader = new ObjectInputStream(socket.getInputStream());
            Message objectFromChat;
            objectFromChat = (Message) objectReader.readObject();

            // assign client username for array from incoming object
            this.clientUsername = objectFromChat.node.username;

            //add client to array list
            clientArray.add(this);

            broadcastMessage("SERVER: " + clientUsername +" has entered the chat!");
        }catch(IOException e){
            closeEverything(socket, objectReader, bufferedWriter);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
        Message msgObj;
        String note;

        while(socket.isConnected()){
            try{
                msgObj = (Message) objectReader.readObject();
                note = msgObj.content;
                broadcastMessage(note);

            }catch(IOException e){
                closeEverything(socket, objectReader, bufferedWriter);
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public void broadcastMessage(String message){
        for(ClientHandler clientHandler: clientArray){
            try{
                if(!clientHandler.clientUsername.equals(clientUsername))
                {
                    clientHandler.bufferedWriter.writeObject(message);
                    clientHandler.bufferedWriter.flush();
                }

            }catch (IOException e){
                closeEverything(socket, objectReader,bufferedWriter);
            }
        }
    }

    public void removeClient(){
        clientArray.remove(this);
        broadcastMessage("SERVER: " + clientUsername + "has left the chat!");
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
