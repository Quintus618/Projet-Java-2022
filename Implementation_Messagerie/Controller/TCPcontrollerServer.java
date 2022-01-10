package Controller;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import GUI.messagingGUI;
import Instant_Messaging.Message;
import Instant_Messaging.usertype;

public class TCPcontrollerServer {

    //Creation of the server socket
    private ServerSocket socServer;
    private ArrayList<Socket> socClientList = new ArrayList<Socket>();

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

        String message = null;
        Socket socClient;

        try {
            System.out.println("En attente de connexions");
            socClient = this.socServer.accept();
            String clientIPAddress = socClient.getInetAddress().getHostAddress();

            System.out.println("Connexion acceptée " + clientIPAddress);

            BufferedReader dataRec = new BufferedReader(new InputStreamReader(socClient.getInputStream()));
            while((message=dataRec.readLine()) != null){
                
                //System.out.println(mGUI.getCorrespondant());
                //System.out.println(mGUI.getCorrespondant().getIPaddr()+" versus "+clientIPAddress);
                if(!mGUI.getCorrespondant().equals(null)){

                    System.out.println(mGUI.getCorrespondant());
                    System.out.println(mGUI.getCorrespondant().getIPaddr()+" versus "+clientIPAddress);

                    if(mGUI.getCorrespondant().getIPaddr().equals(clientIPAddress)){
                        System.out.println("Est-ce que ca passe ici");
                        mGUI.receiveMessage(message, clientIPAddress);
                    }else{
                        System.out.println("Est-ce que erreur ici");
                        mGUI.stockMessage(message, clientIPAddress);
                        System.out.println("Est-ce que ca passe là");
                    }
                }
                else System.out.println("Nom de zeus Marty !");

            }
       
            //PrintWriter out = new PrintWriter(socClient.getOutputStream(), true);
            //out.println(message);
            //System.out.println("Voici le message envoyé" + message);
            //out.flush();
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
            socServer.close();
            System.out.println("Serveur TCP fermé avec succès.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
