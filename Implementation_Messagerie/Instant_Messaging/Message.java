package Instant_Messaging;
import java.time.LocalTime;

import javax.swing.JLabel;

public class Message extends JLabel{

    private String idfrom=null;
    private String idto=null;

    private boolean isSender;
    private String textMessage;
    private LocalTime horodata;

    //cet initialisateur sera à supprimer dès lors que l'on aura réglé le stockage de l'id
    public Message(String textMessage, boolean isSender){
        super(textMessage);
        this.textMessage = textMessage;
        this.isSender = isSender;
        this.horodata = java.time.LocalTime.now();
    }

    //pas encore pleinement fonctionnel
    public Message(String textMessage, String idsender, String iddest){
        super(textMessage);
        this.textMessage = textMessage;
        this.idfrom=idsender;
        this.idto=iddest;
        this.isSender = true;//TODO
        //NON!!!!!!! Devra être idfrom==MY_ID
        this.horodata = java.time.LocalTime.now();
    }  

    //utilisée pour le désarchivage
    public Message(String textMessage, String idsender, String iddest, LocalTime temps){
        super(textMessage);
        this.textMessage = textMessage;
        this.idfrom=idsender;
        this.idto=iddest;
        this.isSender = true;//TODO
        //NON!!!!!!! Devra être idfrom==MY_ID
        this.horodata = temps;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public LocalTime getHorodata() {
        return horodata;
    }

    public String getSender() {
        return idfrom;
    }

    public String getDest() {
        return idto;
    }
      
    public String getTextMessage() {
        return textMessage;
    }

    public boolean getIsSender() {
        return isSender;
    }
}
