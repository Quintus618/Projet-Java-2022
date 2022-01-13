package com.insatact.Instant_Messaging;
import java.time.LocalDateTime;

import com.insatact.Controller.controllerInstantMessaging;

public class Message {

    private String idfrom=null;
    private String idto=null;

    private boolean isSender;
    private String textMessage;
    private LocalDateTime horodata;

    //cet initialisateur sera à supprimer dès lors que l'on aura réglé le stockage de l'id
    public Message(String textMessage, String idcorresp, boolean amSender){
        this.textMessage = textMessage;
        this.isSender = amSender;
        if(isSender){
            this.idfrom=controllerInstantMessaging.getmyID();
            this.idto=idcorresp;
            }
        else{
            this.idto=controllerInstantMessaging.getmyID();
            this.idfrom=idcorresp;
        }
        this.horodata = java.time.LocalDateTime.now();
    }

 
    //utilisée pour le désarchivage
    public Message(String textMessage, String idsender, String iddest, LocalDateTime temps){
        this.textMessage = textMessage;
        this.idfrom=idsender;
        this.idto=iddest;
        this.isSender = idfrom.equals(controllerInstantMessaging.getmyID());
        this.horodata = temps;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public LocalDateTime getHorodata() {
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
