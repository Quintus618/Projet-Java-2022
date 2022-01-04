package Controller;

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
import javax.swing.*;

import GUI.changePseudoPopUp;
import GUI.messagingGUI;


public class UDPcontroller {

    private int nbfoisdemande = 0;

    public UDPcontroller(messagingGUI mGUI){
        Thread receiveBroadcast = new Thread(new Runnable(){
            @Override
            public void run(){
                DatagramSocket soc;
                try {

                    soc = new DatagramSocket(7000, InetAddress.getByName("0.0.0.0"));
                    soc.setBroadcast(true);

                    while(true){
                        byte[] bufrecep = new byte[10000];
                        DatagramPacket packet = new DatagramPacket(bufrecep, bufrecep.length);

                        try {
                            soc.receive(packet);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
    
                        String message = new String(packet.getData()).trim();

                        String[] messagesplit = message.split(":");
                        String[] messages = new String[2];
                        int index = 0;
                        for (String m : messagesplit){
                            messages[index] = m;
                            index++;
                        }

                        System.out.println(message);
                        //String pseudoRecu="";

                        if(messages[0].equals("USERCONNECTED")){
                            if (messages[1].equals(mGUI.getPseudo())){
                                udpbroadcastChangePseudo(packet,messages[1]);
                                //pseudoRecu=messages[1];
                            }
                            else {
                                mGUI.displayConnectedUsers(messages[1]);
                                nbfoisdemande = 0;
                                System.out.println("totto");
                            }
                        }
                        else if (messages[0].equals("USERDISCONNECTED")){
                            mGUI.removeConnectedUsers(messages[1]);
                        }
                        else if (messages[0].equals("CHANGEPSEUDO")){
                            if(nbfoisdemande==0){
                                changePseudoPopUp cPseudo = new changePseudoPopUp(100, 500);
                                nbfoisdemande++;
                            }
                        }
                    }
                } catch (SocketException | UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        receiveBroadcast.start();

        Thread connexionPeriodBroadcast = new Thread(new Runnable(){
            @Override
            public void run(){
                while(true){
                    try {
                        udpbroadcastco(mGUI.getPseudo());
                    } catch (SocketException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (UnknownHostException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });   
        
        connexionPeriodBroadcast.start();
    }


    //broadcastUDP to notify connexion
    public void udpbroadcastco(String ps) throws SocketException, UnknownHostException{
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
    
        // Broadcast the message over all the network interfaces
        Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
    
        while (interfaces.hasMoreElements()) {
    
            NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
        
    
            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue; 
            }
    
            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast == null) {
                    continue;
                }
                    
                try {
                    //Send a message to show that we are connected
                    String coPseudo = "USERCONNECTED:" + ps;
                    System.out.println(coPseudo);
                    byte[] sendconnexion = coPseudo.getBytes();
                    DatagramPacket sendpaqconnexion = new DatagramPacket(sendconnexion, sendconnexion.length, broadcast,7000);
                    socket.send(sendpaqconnexion);
                }
                catch (Exception e){}
    
            }
        }
    
    }
    
    //broadcastUDP to notify connexion
    public void udpbroadcastdeco(String ps) throws SocketException, UnknownHostException{
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
        
        // Broadcast the message over all the network interfaces
        Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
        
        while (interfaces.hasMoreElements()) {
        
            NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
            
        
            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue; 
            }
        
            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast == null) {
                    continue;
                }
                        
                try {
                    //Send a message to show that we are connected
                    String coPseudo = "USERDISCONNECTED:" + ps;
                    byte[] sendconnexion = coPseudo.getBytes();
                    DatagramPacket sendpaqconnexion = new DatagramPacket(sendconnexion, sendconnexion.length, broadcast,7000);
                    socket.send(sendpaqconnexion);
                }
                catch (Exception e){}
        
            }
        }
        
    }
    
    //broadcastUDP to notify connexion
    public void udpbroadcastChangePseudo(DatagramPacket packet, String ps) throws SocketException, UnknownHostException{
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
            
        // Broadcast the message over all the network interfaces
        Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            
        while (interfaces.hasMoreElements()) {
            
            NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
                
            
            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue; 
            }
            
            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast == null) {
                    continue;
                }
                            
                try {
                    //Send a message to show that we are connected
                    String sendurgString = "CHANGEPSEUDO:" + ps;
                    byte[] sendurg = sendurgString.getBytes();
                    DatagramPacket sendpaqconnexion = new DatagramPacket(sendurg, sendurg.length, packet.getAddress(),7000);
                    socket.send(sendpaqconnexion);
                }
                catch (Exception e){}
            
            }
        }
            
    }
}
