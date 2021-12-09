package Instant_Messaging;
import java.time.LocalTime;

import javax.swing.JLabel;

public class Message extends JLabel{
    private boolean isSender;
    private String textMessage;
    private LocalTime horodata;

    public Message(String textMessage, boolean isSender){
        super(textMessage);
        this.textMessage = textMessage;
        this.isSender = isSender;
        this.horodata = java.time.LocalTime.now();
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public LocalTime getHorodata() {
        return horodata;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public boolean getIsSender() {
        return isSender;
    }
}
