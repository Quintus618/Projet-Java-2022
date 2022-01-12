package com.insatact.Controller;

import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.ArrayList;

import com.insatact.GUI.messagingGUI;
import com.insatact.Instant_Messaging.Message;
import com.insatact.Instant_Messaging.usertype;

public class TCPcontrollerServer {

    //Creation of the server socket
    private ServerSocket socServer;
    private ArrayList<Thread> threadList = new ArrayList<Thread>();
    private ArrayList<Socket> socketList = new ArrayList<Socket>();

    public TCPcontrollerServer(usertype myself){//pas plutôt inetAdress?
        //dans messagingGUI c'est le pseudo qui est passé! ça doit être l'erreur
//usertype correspondant?
        String MyIP=myself.getIPaddr();
        try {
            //Verification if the input IP address is valid
            if (MyIP != null && !MyIP.isEmpty()){
                this.socServer = new ServerSocket(0,1, InetAddress.getByName(MyIP));
            }
            else {
                this.socServer = new ServerSocket(0,1,InetAddress.getLocalHost());
            }
        }
        catch(Exception e){
            System.out.println("\n\n\nEchec de la création du serveur TCP!\n\n");
        }
        System.out.println("Succès de la création du serveur TCP!");
        myself.setPort(socServer.getLocalPort());
    }

    //Listen to receive data from the client
    public void dataReception(messagingGUI mGUI){
        
        Socket socClient;

        try {
            System.out.println("En attente de connexions");
            socClient = this.socServer.accept();
            socketList.add(socClient);
            String clientIPAddress = socClient.getInetAddress().getHostAddress();

            System.out.println("Connexion acceptée " + clientIPAddress);

            Thread recepdata = new Thread(new Runnable(){

                String message = null;
                BufferedReader dataRec;

                @Override
                public void run() {
                    try{
                        dataRec = new BufferedReader(new InputStreamReader(socClient.getInputStream()));
                        
                        while(!Thread.currentThread().isInterrupted() && (message=dataRec.readLine()) != null){
                            
                            //System.out.println(mGUI.getCorrespondant());
                            //System.out.println(mGUI.getCorrespondant().getIPaddr()+" versus "+clientIPAddress);
                            if(!mGUI.getCorrespondant().equals(null)){
            
                                System.out.println(mGUI.getCorrespondant());
                                System.out.println(mGUI.getCorrespondant().getIPaddr()+" versus "+clientIPAddress);
            
                                if(mGUI.getCorrespondant().getIPaddr().equals(clientIPAddress)){
                                    System.out.println("Est-ce que ca passe ici: " + message);
                                    mGUI.receiveMessage(message, clientIPAddress);
                                }else{
                                    System.out.println("Est-ce que erreur ici: " + message);
                                    mGUI.stockMessage(message, clientIPAddress);
                                    System.out.println("Est-ce que ca passe là");
                                }
                            }
                            else System.out.println("Nom de zeus Marty !");
            
                        }
                    }catch(Exception e){System.out.println("Echec de la réception");}
                    
               
                    //PrintWriter out = new PrintWriter(socClient.getOutputStream(), true);
                    //out.println(message);
                    //System.out.println("Voici le message envoyé" + message);
                    //out.flush();
                }
            });
                threadList.add(recepdata);
                recepdata.start();
        }
        catch(Exception e){
            System.out.println("Echec de l'envoi ou réception côté Serveur");
            e.printStackTrace();
        }
    }

    public ServerSocket getSocServer() {
        return socServer;
    }
 
    public void killserver(){
        try {
            for(Socket soc : socketList)soc.close();
            System.out.println("Fermeture de tous les sockets");
            for(Thread t : threadList){

                System.out.println("Premier thread à virer : " + t);
                //bufferList.get(0).close();
                System.out.println("Premier buffer fermé : " );
                System.out.println("Premier buffer supprimé : ");

                while(t.isAlive()){
                    //bufferList.get(0).close();
                    t.interrupt();
                    System.out.println("Thread " + t);
                }
            }

            socServer.close();
            System.out.println("Serveur TCP fermé avec succès.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
