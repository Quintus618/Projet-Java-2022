package com.insatact.Instant_Messaging;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.*;

import com.insatact.Controller.TCPcontrollerClient;
import com.insatact.Controller.controllerInstantMessaging;
import com.insatact.GUI.messagingGUI;

public class Conversation {

    private usertype correspondant;
    private ArrayList<Message> MessageList;
    private TCPcontrollerClient TCP=null;
    private boolean hasTCP=false;
    private boolean hasunread= false;

    private int fromArchives=0;

    public JPanel messagePanel;
    public JScrollPane scrollPane;


    public Conversation(usertype correspondant){
        this.correspondant = correspondant;
        MessageList = new ArrayList<Message>();
        this.hasTCP=false;
        this.hasunread=false;
    }

    public boolean isStarted(){
        return MessageList.size()!=0;
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
            System.out.println("Fermeture: pas de client TCP avec "+correspondant.getPseudo()+".");
        }
    }
    
    public void load(messagingGUI mGUI){
        if(this.fromArchives==0){
            ArrayList<Message> smsenabscence=this.MessageList;
            MessageList=new ArrayList<Message>();
            MessageList.addAll(mGUI.getControlCHAT().getComtoBDD().recupererConv(correspondant));
            MessageList.addAll(smsenabscence);
            this.fromArchives=MessageList.size();
            System.out.println(Integer.toString(fromArchives)+" messages d'archives avec "+correspondant.getId()+" récupérés");
        }else{
            System.out.println("Inattendu: conversation avec "+correspondant.getId()+" déjà load");
        }
    }


    public usertype getCorrespondant() {
        return correspondant;
    }

    public int cmbFromArchives() {
        return fromArchives;
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
        while(MessageList.size()>=controllerInstantMessaging.getMaxmessToArchive()){
            //la méthode remove est censée décaler l'index des éléments restants
            MessageList.remove(0);
            nbdeleted++;
            if(fromArchives>0){
                fromArchives--;
            }
        }
        MessageList.add(sms);
        return nbdeleted;
    }


    public ArrayList<Message> getMessageList() {
        return MessageList;
    }

    public int nbMess(){
        return this.MessageList.size();
    }

    public String toString(){
        return "Conversation avec "+this.correspondant.toString()+".";
    }

}
