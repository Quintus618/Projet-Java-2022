package Controller;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPcontrollerClient {
    
    private Socket socket;

    public TCPcontrollerClient(InetAddress servAddress, int servPort){

        try {
            this.socket = new Socket(servAddress,servPort);
        }catch(Exception e){
            System.out.println("Creation of the socket failed");
        }
        
    }

    public void sendMessage(String message){
        try {
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            out.println(message);
            out.flush();
        }
        catch(Exception e){
            System.out.println("Message sending Failed");
        }

    }
}
