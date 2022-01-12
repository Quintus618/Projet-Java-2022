package com.insatact.Instant_Messaging;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.*;

import com.insatact.Controller.TCPcontrollerClient;
import com.insatact.GUI.messagingGUI;

public class Conversation {

    private usertype correspondant;
    private int numberMessage;
    private static int maxToArchive=64;//TODO voir combien demande le cahier des charges
    private ArrayList<Message> MessageList;
    private TCPcontrollerClient TCP=null;
    private boolean hasTCP=false;
    private boolean hasunread= false;

    public JPanel messagePanel;
    public JScrollPane scrollPane;


    public Conversation(usertype correspondant){
        this.correspondant = correspondant;
        MessageList = new ArrayList<Message>();
        this.numberMessage = MessageList.size();
        this.hasTCP=false;
        this.hasunread=false;
    }

    public boolean isStarted(){
        return numberMessage!=0;
    }

    public boolean hasunreadsms() {
        return hasunread;
    }

    public void setHasunread(boolean hasunread) {
        this.hasunread = hasunread;
    }


    public void launchTCP(){
        try {
            System.out.println("Adresse: " + InetAddress.getByName(correspondant.getIPaddr()) + "Port: " + correspondant.getPort());
            TCP=new TCPcontrollerClient(InetAddress.getByName(correspondant.getIPaddr()), correspondant.getPort());
            hasTCP=true;
        } catch (UnknownHostException e) {
            System.out.println("Echec de création du client TCP de la conversation avec "+correspondant.toString());
        } catch (IOException e) {
            System.out.println("Echec de création du client TCP de la conversation avec "+correspondant.toString());
        }
    }

    public void killTCP(){
        if (hasTCP){
            TCP.end();
            System.out.println("Client TCP de la conversation avec "+correspondant.getPseudo()+" fermé avec succès.");
        }else{

            }
    }
    
    public void load(messagingGUI mGUI){
        MessageList.addAll(mGUI.getControlCHAT().getComtoBDD().recupererConv(correspondant));
        this.numberMessage = MessageList.size();
    }


    public usertype getCorrespondant() {
        return correspondant;
    }
    
    public void setCorrespondant(usertype neousr){
        this.correspondant=neousr;
    }

    public TCPcontrollerClient getTCP() {
        return TCP;
    }

    public boolean gethasTCP() {
        return hasTCP;
    }

    //retourne le nombre de messages effacés
    public int addMessage(Message sms){
        int nbdeleted=0;
        while(numberMessage>=maxToArchive){
            //la méthode remove est censée décaler l'index des éléments restants
            MessageList.remove(0);
            numberMessage--;
            nbdeleted++;
        }
        MessageList.add(sms);
        numberMessage++;
        return nbdeleted;
    }


    public ArrayList<Message> getMessageList() {
        return MessageList;
    }

    public String toString(){
        return "Conversation avec "+this.correspondant.toString()+".";
    }

}
