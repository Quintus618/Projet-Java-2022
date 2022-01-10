package Controller;

import java.io.*;
import java.net.*;

public class TCPcontrollerClient {
    
    private Socket skt;

    public TCPcontrollerClient(InetAddress servAddress, int servport) throws IOException{  
        skt = new Socket(servAddress, servport);

    }


    public void sendMessage(String message){
        try {
            PrintWriter out = new PrintWriter(this.skt.getOutputStream(), true);
            out.println(message);
            System.out.println("Voici le message envoyé" + message);
            out.flush();
            out.close();
        }
        catch(Exception e){
            System.out.println("Echec de l'envoi d'un message");
        }
    }


    public void end(){
        try {
            skt.close();
        } catch (IOException e) {
            System.out.println("Echec de la fermeture d'un socket TCP");
        }      

    }
}
