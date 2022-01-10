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
import java.util.Timer;

import javax.swing.*;

import GUI.*;
import Instant_Messaging.usertype;


public class UDPcontroller {

    private int nbfoisdemande = 0;

    private Thread receiveBroadcast;
    private Thread connexionPeriodBroadcast;
    private DatagramSocket soc;

    public UDPcontroller(messagingGUI mGUI){
        receiveBroadcast = new Thread(new Runnable(){
            @Override
            public void run(){
                //TimerTask task;
                try {

                    soc = new DatagramSocket(7000, InetAddress.getByName("0.0.0.0"));
                    soc.setBroadcast(true);

                    while(!Thread.currentThread().isInterrupted()||soc.isClosed()){
                        byte[] bufrecep = new byte[10000];
                        DatagramPacket packet = new DatagramPacket(bufrecep, bufrecep.length);

                        try {
                            soc.receive(packet);
                        } catch (IOException e) {
                            System.out.println("Listening UDP socket closed");
                            Thread.currentThread().interrupt();
                        }
                        //TODO ce thread empeche la fermeture
                        String machine = InetAddress.getLocalHost().getHostName();
                        String s = packet.getAddress().getHostName();
                    
                        char[] amachine = machine.toCharArray();
                        char[] as = s.toCharArray();
                        Boolean meme = true;

                        for(int i=0 ; i<machine.length() ; i++){
                            if(amachine[i]!=as[i]){
                                meme = false;
                            }
                        }

                        if(meme==true)
                            continue;
                        
                        
                        String message = new String(packet.getData()).trim();

                        String[] messages = message.split(":");
                        /*
                        String[] messages = message.split(":",2);
                        le 2 permet que ça marche même si un user a un : dans son pseudo ou ID
                        mais ne marche pas ici à cause du message changepseudo qui en a un autre
                         */


                        System.out.println(message);
                        //String pseudoRecu="";

                        switch(messages[0]){
                            case"USERCONNECTED":
                            if (messages[1].equals(mGUI.getControlCHAT().getMyPseudo())){
                                udpbroadcastChangePseudo(packet,messages[1]);
                                //pseudoRecu=messages[1];
                            }
                            else {
                                mGUI.displayConnectedUsers(messages[1]);

                                nbfoisdemande = 0;
                                /*
                                timer.cancel();
                                timer.purge();
                                timer.schedule(TODOdeconnexion, 15000);//vérifier le délai avec le cahier des charges
                                //comment avoir autant de timer que d'users?
                                */
                            }
                            break;
                            case "USERDISCONNECTED"://or if a timer is exceeded?
                            mGUI.removeConnectedUsers(new usertype(messages[1]));
                            /*
                            timer.cancel();
                            timer.purge();
                            */
                            break;
                            case "CHANGEPSEUDO":
                            if(nbfoisdemande==0){
                                new changePseudoPopUp(mGUI, 100, 500);
                                nbfoisdemande++;
                            }
                            break;
                            case "MODIFIEDPSEUDO":

                            /////////////////////////////////////////////
                            System.out.println(messages[1]+ " " + messages[2]);
                            /////////////////////////////////////////////

                            if (messages[1].equals(mGUI.getControlCHAT().getMyPseudo())){
                                udpbroadcastChangePseudo(packet,messages[1]);
                                //pseudoRecu=messages[1];
                            }
                            else {
                                mGUI.updateConnectedList(messages[1], messages[2]);
                                mGUI.updatePseudo(messages[2], messages[1], false);;
                            }
                            break;
                            default: System.out.println("Alert: unexpected message");
                        }
                    }
                } catch (SocketException | UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });

        receiveBroadcast.start();

        connexionPeriodBroadcast = new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    while(!Thread.currentThread().isInterrupted()){
                        try {
                            udpbroadcastco(mGUI.getControlCHAT().getMyIdentity().toString());
                            Thread.sleep(3000);
                        } catch (SocketException e) {
                            e.printStackTrace();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }

                        
                    }
                } catch (InterruptedException e) {
                    System.out.println("Thread broadcastCo interrupted");
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
                    //System.out.println(coPseudo);
                    byte[] sendconnexion = coPseudo.getBytes();
                    DatagramPacket sendpaqconnexion = new DatagramPacket(sendconnexion, sendconnexion.length, broadcast,7000);
                    socket.send(sendpaqconnexion);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
    
            }
        }
    
    }
    
    //broadcastUDP to notify deconnexion
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
                    //NE PAS RAJOUTER D'ESPACES
                    String coPseudo = "USERDISCONNECTED:" + ps;//TODO ?
                    byte[] sendconnexion = coPseudo.getBytes();
                    DatagramPacket sendpaqconnexion = new DatagramPacket(sendconnexion, sendconnexion.length, broadcast,7000);
                    socket.send(sendpaqconnexion);
                }
                catch (Exception e){}
        
            }
        }
        socket.close();
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

        //broadcastUDP to notify a pseudo modification
        public void udpbroadcastPseudoChanged(String newPseudo, String oldPseudo) throws SocketException, UnknownHostException{
            
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
                        String sendmodPseudoString = "MODIFIEDPSEUDO:" + newPseudo + ":" + oldPseudo;
                        byte[] sendmodps = sendmodPseudoString.getBytes();
                        DatagramPacket sendpaqconnexion = new DatagramPacket(sendmodps, sendmodps.length, broadcast,7000);
                        socket.send(sendpaqconnexion);
                    }
                    catch (Exception e){}
                
                }
            }
                
        }

        //kill the threads for deconnexion (maybe put this in udpbroadcastdeco?)
        public void interrupt(){
            soc.close();
            connexionPeriodBroadcast.interrupt();
            receiveBroadcast.interrupt();
            
        }

    }
