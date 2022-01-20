package client;

import java.io.*;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

import utils.Message;
import utils.PropertyHandler;



public class Client {

    private Socket socket;
    private ObjectInputStream objectReader;
    private ObjectOutputStream objectWriter;
    private String username;
    private Message msg;
    private static String serverIP = null;
    private static int serverPort = 0;


    public Client(Socket socket, String username){

         try{
             this.socket = socket;
             this.objectReader = new ObjectInputStream(socket.getInputStream());
             this.objectWriter = new ObjectOutputStream((socket.getOutputStream()));
             this.username = username;
         }catch(IOException e){
             closeEverything(socket, objectReader, objectWriter);
         }
    }

    public void sendMessage(){
        try{


            objectWriter.writeObject(username);
            System.out.println();
            objectWriter.flush();

            Scanner sc = new Scanner(System.in);
            while(socket.isConnected()){
                String message = sc.nextLine();
                // assign object message to string
                msg.content = message;
                objectWriter.writeObject(username + ": " + msg.content);
                System.out.println();
                objectWriter.flush();
            }
        }catch(IOException e){
            closeEverything(socket, objectReader, objectWriter);
        }
    }

    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message objectFromChat;
                while (socket.isConnected()){
                    try{
                        objectFromChat = (Message) objectReader.readObject();
                        // print out content from object
                        System.out.println(objectFromChat.content);
                    }catch(ClassNotFoundException e){
                        closeEverything(socket, objectReader, objectWriter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    public void closeEverything(Socket socket, ObjectInputStream bufferedReader, ObjectOutputStream bufferedWriter){
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


    public static void main(String[] args) throws IOException {

        //initialize methods/variables
        String propertiesFile = null;
        Scanner sc = new Scanner(System.in);

        // Read properties file
        propertiesFile = "src/client/Server.properties";
        //
        // get the properties of the server
        //
        Properties properties = null;
        // open properties
        try
        {
            properties = new PropertyHandler(propertiesFile);
        }
        catch (IOException ex)
        {
            System.exit(1);
        }
        // get server IP
        try
        {
            serverIP = properties.getProperty("SERVER_IP");
        }
        catch (Exception ex)
        {
            System.exit(1);
        }

        // get server port
        try
        {
            serverPort = Integer.parseInt(properties.getProperty("SERVER_PORT"));
        }
        catch (NumberFormatException ex)
        {
            System.exit(1);
        }
        //

        System.out.println("Enter your name for the chat!: ");
        String username = sc.nextLine();
        Socket socket = new Socket(serverIP, serverPort);
        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage();

    }




}
