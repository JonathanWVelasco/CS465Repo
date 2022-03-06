package Client;

import java.net.*;
import java.io.*;

public class ClientThread extends Thread
{
    private DataInputStream  inputMessage  =  null;
    private DataOutputStream outputMessage = null;
    private Node node;
    private Socket socket;
    public String username;
    private Boolean flag;

    public ClientThread(Node node, Socket socket, String logicalName)
    {

        this.node = node;
        this.socket = socket;
        this.username = logicalName;
        this.flag = true;

        System.out.println("Starting Thread");

    }

}