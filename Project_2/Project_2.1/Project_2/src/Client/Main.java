package Client;

import java.io.*;
import java.net.*;
import java.lang.Integer;

public class Main
{
    private static int arguments = 3;
    public static void main(String[] args) throws IOException, InterruptedException
    {
        if( args.length != arguments )
        {
            System.out.println("These are not the args we're looking for.\n"
                    + "Edit run configuration arguments to look like:"
                    + " 127.0.0.1 1234 <username>.\n"
            );
            return;
        }


        InetAddress address = InetAddress.getByName( args[ 0 ] );
        int port = Integer.parseInt( args[ 1 ] );
        String username = args[ 2 ];

        Node node = new Node( address , port, username);
        node.startChatClient();
    }


}