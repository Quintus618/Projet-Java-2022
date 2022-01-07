package Controller;

import java.io.*;
import java.io.ObjectInputStream.GetField;
import java.net.*;

import GUI.messagingGUI;
import Instant_Messaging.usertype;

public class TCPcontrollerServer {

    //Creation of the server socket
    private ServerSocket socServer;

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

            System.out.println("Connexion acceptée" + clientIPAddress);

            BufferedReader dataRec = new BufferedReader(new InputStreamReader(socClient.getInputStream()));
            while((message=dataRec.readLine()) != null){
                System.out.println(message);
                mGUI.receiveMessage(message);
            }
        }
        catch (Exception e){
            System.out.println("Refus TCP");
        }
    }


    public ServerSocket getSocServer() {
        return socServer;
    }
 
}
