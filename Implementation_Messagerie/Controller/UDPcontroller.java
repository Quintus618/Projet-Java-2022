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
import GUI.messagingGUI;


public class UDPcontroller {

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
                        if(messages[0].equals("USERCONNECTED")){
                            if (messages[1].equals(mGUI.getPseudo())){
                                udpbroadcastChangePseudo(packet);
                            }
                            else {
                                mGUI.displayConnectedUsers(messages[1]);
                            }
                        }
                        else if (messages[0].equals("USERDISCONNECTED")){
                            mGUI.removeConnectedUsers("Milou");
                        }
                        else if (messages[0].equals("CHANGEPSEUDO")){
                            //System.out.println("Tata");
                        }
                    }
                } catch (SocketException | UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        receiveBroadcast.start();
    }


    //broadcastUDP to notify connexion
    public void udpbroadcastco() throws SocketException, UnknownHostException{
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
    
        // Broadcast the message over all the network interfaces
        Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
    
        while (interfaces.hasMoreElements()) {
    
            NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
        
    
            if (/*networkInterface.isLoopback() ||*/ !networkInterface.isUp()) {
                continue; 
            }
    
            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast == null) {
                    continue;
                }
                    
                try {
                    //Send a message to show that we are connected
                    String coPseudo = "USERCONNECTED" + ":Tintin";
                    byte[] sendconnexion = coPseudo.getBytes();
                    DatagramPacket sendpaqconnexion = new DatagramPacket(sendconnexion, sendconnexion.length, broadcast,7000);
                    socket.send(sendpaqconnexion);
                }
                catch (Exception e){}
    
            }
        }
    
    }
    
    //broadcastUDP to notify connexion
    public void udpbroadcastdeco() throws SocketException, UnknownHostException{
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
        
        // Broadcast the message over all the network interfaces
        Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
        
        while (interfaces.hasMoreElements()) {
        
            NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
            
        
            if (/*networkInterface.isLoopback() ||*/ !networkInterface.isUp()) {
                continue; 
            }
        
            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast == null) {
                    continue;
                }
                        
                try {
                    //Send a message to show that we are connected
                    String coPseudo = "USERDISCONNECTED" + ":Tintin";
                    byte[] sendconnexion = coPseudo.getBytes();
                    DatagramPacket sendpaqconnexion = new DatagramPacket(sendconnexion, sendconnexion.length, broadcast,7000);
                    socket.send(sendpaqconnexion);
                }
                catch (Exception e){}
        
            }
        }
        
    }
    
    //broadcastUDP to notify connexion
    public void udpbroadcastChangePseudo(DatagramPacket packet) throws SocketException, UnknownHostException{
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
            
        // Broadcast the message over all the network interfaces
        Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            
        while (interfaces.hasMoreElements()) {
            
            NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
                
            
            if (/*networkInterface.isLoopback() ||*/ !networkInterface.isUp()) {
                continue; 
            }
            
            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast == null) {
                    continue;
                }
                            
                try {
                    //Send a message to show that we are connected
                    byte[] sendurg = "CHANGEPSEUDO".getBytes();
                    DatagramPacket sendpaqconnexion = new DatagramPacket(sendurg, sendurg.length, packet.getAddress(),7000);
                    socket.send(sendpaqconnexion);
                }
                catch (Exception e){}
            
            }
        }
            
    }
}
