package Instant_Messaging;

import java.net.InetAddress;
import java.net.UnknownHostException;

//import java.util.Timer;

import Controller.controllerInstantMessaging;

public class usertype implements Comparable<usertype>{

    private String id=null;
    private String pseudo=null;
    private String IPaddr=null;
    private int port;
    //InetAddresscontient le hostname et le hostaddress, c'est trop

    //private Timer chrono=null;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getIPaddr() {
        return IPaddr;
    }
    
    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getInetAddr() {
        InetAddress returnip=null;
        try {
            returnip=InetAddress.getByName(IPaddr);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return returnip;
    }

    public void setIPaddr(String iPaddr) {
        IPaddr = iPaddr;
    }

    /*
    public Timer getChrono() {
        return chrono;
    }

    public void setChrono() {
        this.chrono = new Timer();
    }*/

    public boolean isMe(){
        return id==controllerInstantMessaging.getmyID();
    }


    public usertype(String id, String pseudo, String IP){
        super();
        this.id=id;
        this.pseudo=pseudo;
        this.IPaddr=IP;
    }


    //constructeur utilisé pour l'UDP
    public usertype(String idpseudoIPport){
        super();
        String[] identifiers=idpseudoIPport.split(" ");
            if(identifiers.length==4){
            this.id=identifiers[0];
            this.pseudo=identifiers[1];
            this.IPaddr=identifiers[2];
            this.port=Integer.valueOf(identifiers[3]);
        }else{
            System.out.println("ALERTE erreur à l'initialisation de l'usertype!");
        } 
    }


 
    public int compareTo(usertype o) {
        return this.id.compareTo(o.getId());
    }

    
    public String toString() {
        return id+" "+pseudo+" "+IPaddr+" "+Integer.toString(port);
    }

}
