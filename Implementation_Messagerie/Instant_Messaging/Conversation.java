package Instant_Messaging;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.*;

import Controller.TCPcontrollerClient;
import GUI.messagingGUI;

public class Conversation extends JLabel{

    private usertype correspondant;
    private int numberMessage;
    private int sport;
    private int dport;
    private static int maxToArchive=64;//TODO voir combien demande le cahier des charges
    private ArrayList<Message> MessageList;
    private TCPcontrollerClient TCP=null;

    public JPanel messagePanel;
    public JScrollPane scrollPane;


    public Conversation(usertype correspondant){
        this.correspondant = correspondant;
        this.sport = sport;
        this.dport = dport;
        MessageList = new ArrayList<Message>();
        this.numberMessage = MessageList.size();
    }

//TODO TODO TODO
    public void launchTCP(){
        try {
            TCP=new TCPcontrollerClient(InetAddress.getByName(correspondant.getIPaddr()));
        } catch (UnknownHostException e) {
            System.out.println("Echec de création du client IP de la conversation avec "+correspondant.toString());;
        }
    }
    
    public void load(messagingGUI mGUI){
        mGUI.getControlCHAT().getComtoBDD().recupererConv(correspondant);
        this.numberMessage = MessageList.size();
        //TODO afficher messages, ici ou dans mGUI?
    }


    public usertype getCorrespondant() {
        return correspondant;
    }

    public int getDport() {
        return dport;
    }

    public int getSport() {
        return sport;
    }

    //reourne le nombre de messages effacés
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
}
