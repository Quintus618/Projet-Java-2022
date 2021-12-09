package Instant_Messaging;

import java.util.ArrayList;

public class Conversation {
    private String idSender;
    private String idReceiver;
    private int numberMessage;
    private int sport;
    private int dport;
    private ArrayList<Message> MessageList;

    public Conversation(String idSender, String idReceiver, int sport, int dport){
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.sport = sport;
        this.dport = dport;
        MessageList = new ArrayList<Message>();
        this.numberMessage = 0;
    }

    public int getDport() {
        return dport;
    }

    public int getSport() {
        return sport;
    }

    public String getIdSender() {
        return idSender;
    }

    public String getIdReceiver() {
        return idReceiver;
    }

    public ArrayList<Message> getMessageList() {
        return MessageList;
    }
}
