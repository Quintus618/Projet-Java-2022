package Controller;

import java.io.*;
import java.net.*;
import java.util.*;

public class TCPcontrollerClient {
    
    private Socket socket;

    public TCPcontrollerClient(InetAddress servAddress){
        /*
        Socket skt;
        try {
            skt = new Socket(servAddress, 1234);
        BufferedReader in = new BufferedReader(new
           InputStreamReader(skt.getInputStream()));
        System.out.print("Received string: '");

        while (!in.ready()) {}
        System.out.println(in.readLine()); // Read one line and output it

        System.out.print("'\n");
        in.close();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }*/
        
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
