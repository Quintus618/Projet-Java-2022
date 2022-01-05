package Controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

import GUI.*;
import Instant_Messaging.usertype;
import java.util.*;

public class controllerInstantMessaging{

    
private static usertype Me=null;

public usertype getMyIdentity() {
    return Me;
}

public static String getmyID(){
    return Me.getId();
}

public void setmyID(String IDgiven){
    Me.setId(IDgiven);
}

private static controllerBDD comtoBDD;

public controllerBDD getComtoBDD() {
    return comtoBDD;
}

//est-ce qu'il faudrait fusionner ça avec mapConvos?
private static ArrayList<usertype> ListeConnectes= new ArrayList<usertype>();

public ArrayList<usertype> getListeConnectes() {
    return ListeConnectes;
}

public void rmUser(usertype user) {
     ListeConnectes.remove(user);
}

public void addUser(usertype user) {
    ListeConnectes.add(user);
}



//le pseudo est dans messagingGUI, rassembler?
 public controllerInstantMessaging(){ 

    try {
        Me=new usertype("","", InetAddress.getLocalHost());
    } catch (UnknownHostException e) {
        System.out.println("Echec de la recherche de l'adresse locale.");
        e.printStackTrace();
    }

    comtoBDD = new controllerBDD();
    welcomGUI wGUI = new welcomGUI(this, 500, 700);
    wGUI.setTitle("Insatact: your favorite low quality chat system - Welcome!");
    
}



//ici pour lancer tout le projet
    public static void main(String[] Args){
        
        new controllerInstantMessaging();

        //tests
        /*
        System.out.println((new usertype("id", "pseudo", InetAddress.getLoopbackAddress())).toString());
        */
        }
    }