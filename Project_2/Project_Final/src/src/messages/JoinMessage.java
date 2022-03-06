package src.messages;

import src.NodeInfo;

public class JoinMessage extends Message {
    //init variables
    final public String knownUserIp;
    final public int knownUserPort;

    public JoinMessage(MessageType messageType, NodeInfo source, String destinationIp, int destinationPort) {
        //set variables
        super(messageType, source);
        this.knownUserIp = destinationIp;
        this.knownUserPort = destinationPort;
    }
}
