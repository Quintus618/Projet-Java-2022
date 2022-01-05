package Instant_Messaging;

import java.net.InetAddress;
import java.util.Timer;

import Controller.controllerInstantMessaging;

public class usertype implements Comparable<usertype>{

    private String id=null;
    private String pseudo=null;
    private InetAddress IPaddr=null;
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

    public InetAddress getIPaddr() {
        return IPaddr;
    }

    public void setIPaddr(InetAddress iPaddr) {
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

    public usertype(String id, String pseudo, InetAddress IP){
        super();
        this.id=id;
        this.pseudo=pseudo;
        this.IPaddr=IP;
    }

 
    public int compareTo(usertype o) {
        return this.id.compareTo(o.getId());
    }



}
