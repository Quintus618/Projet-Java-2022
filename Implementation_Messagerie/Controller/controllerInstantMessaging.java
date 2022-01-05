package Controller;

import java.net.InetAddress;

import GUI.*;
import Instant_Messaging.usertype;

public class controllerInstantMessaging{

    
private static usertype Me=new usertype("","", InetAddress.getLoopbackAddress());

public static String getmyID(){
    return Me.getId();
}

public void setmyID(String IDgiven){
    Me.setId(IDgiven);
}

private controllerBDD comtoBDD;

public controllerBDD getComtoBDD() {
    return comtoBDD;
}

//le pseudo est dans messagingGUI, rassembler?

 public controllerInstantMessaging(){ 

    comtoBDD = new controllerBDD();
    welcomGUI wGUI = new welcomGUI(this, 500, 700);
    wGUI.setTitle("Insatact: your favorite low quality chat system - Welcome!");
    

}


//ici pour lancer tout le projet
    public static void main(String[] Args){
        
        new controllerInstantMessaging();

        }
    }