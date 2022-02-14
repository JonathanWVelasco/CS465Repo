package Messages;

public class formatter
{

    //Format message with username

    public String formatMessage( final ChatMessage toFormat )
    {
        String message = toFormat.getMessage();
        String senderName = toFormat.getInfo().getName();

        return "[" + senderName + "]: " + message;
    }
}
