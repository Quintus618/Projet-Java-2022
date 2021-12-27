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
    private String idSender;
    private String idReceiver;
    private int numberMessage;
    private int sport;
    private int dport;
    private ArrayList<Message> MessageList;

    public JPanel messagePanel;
    public JScrollPane scrollPane;

    public Conversation(String idSender, String idReceiver, int sport, int dport, messagingGUI mGUI){
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
