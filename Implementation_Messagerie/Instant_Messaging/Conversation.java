package Instant_Messaging;

import java.util.ArrayList;
import javax.swing.*;

import Instant_Messaging.Message;
import Controller.*;
import java.awt.event.*;
import java.io.IOException;
import java.awt.*;
import java.lang.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;
import GUI.messagingGUI;

public class Conversation extends JLabel{

    private usertype correspondant;
    private int numberMessage;
    private int sport;
    private int dport;
    private static int maxToArchive=64;//TODO voir combien demande le cahier des charges
    private ArrayList<Message> MessageList;

    public JPanel messagePanel;
    public JScrollPane scrollPane;

    public Conversation(usertype correspondant, int sport, int dport, messagingGUI mGUI){
        this.correspondant = correspondant;
        this.sport = sport;
        this.dport = dport;
        MessageList = mGUI.getComtoBDD().recupererConv(correspondant);
        this.numberMessage = MessageList.size();
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
