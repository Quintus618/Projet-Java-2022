package Controller;

import java.io.*;
import java.net.*;

import GUI.messagingGUI;

public class TCPcontrollerServer {

    //Creation of the server socket
    private ServerSocket socServer;

    public TCPcontrollerServer(String Host){//pas plutôt inetAdress?
        //dans messagingGUI c'est le pseudo qui est passé! ça doit être l'erreur

        try {
            //Verification if the input IP address is valid
            if (Host != null && !Host.isEmpty()){
                this.socServer = new ServerSocket(0,1,InetAddress.getByName(Host));
            }
            else {
                this.socServer = new ServerSocket(0,1,InetAddress.getLocalHost());
            }
        }
        catch(Exception e){
            System.out.println("Creation of the server socket failed");
        }

    }

    //Listen to receive data from the client
    private void dataReception(messagingGUI mGUI){

        String message = null;
        Socket socClient;

        try {
            socClient = this.socServer.accept();
            String clientIPAddress = socClient.getInetAddress().getHostAddress();

            BufferedReader dataRec = new BufferedReader(new InputStreamReader(socClient.getInputStream()));
            while((message=dataRec.readLine()) != null){
                mGUI.receiveMessage(message);
            }
        }
        catch (Exception e){
            System.out.println("Accept failed");
        }
    }
    
}
