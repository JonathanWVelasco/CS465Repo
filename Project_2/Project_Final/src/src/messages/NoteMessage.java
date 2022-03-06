package src.messages;

import src.NodeInfo;

//Message of Chat Type
public class NoteMessage extends Message {
    public final String payload;
    //init ChatMessage
    public NoteMessage(MessageType messageType, NodeInfo source, String payload) {
        super(messageType, source);
        this.payload = payload;
    }

    @Override
    public String toString() {
        //return payload
        return source.name + ": " + payload;
    }
}
