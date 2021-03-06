package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;

public class Server {

    public ServerSocket serverSocket;

    // server constructor
    public Server(ServerSocket serverSocket){

        this.serverSocket = serverSocket;

    }

    public void startServer(){
        System.out.println("Server Started!");
        while(true) {

            try {
                Socket socket = serverSocket.accept();
                //System.out.println("A new client has connected!:");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //if an error occurs then shutdown
    public void closeServerSocket(){
        try{
            if (serverSocket != null) {

                serverSocket.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException{

        InetAddress address = InetAddress.getByName("127.0.0.1");
        ServerSocket serverSocket = new ServerSocket(1234, 100, address);
        System.out.println(serverSocket.getInetAddress().toString());
        Server server = new Server(serverSocket);
        server.startServer();

    }







}

