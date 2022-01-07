package Controller;

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

//TODO TODO TODO
public usertype getUserByPseudo(String pseudal){
    usertype usr=null;
    for(usertype usrsearch:ListeConnectes){
        if(usrsearch.getPseudo().equals(pseudal)){
            usr=usrsearch;
            break;
        }
    }
    return usr;
}


public void setmyPseudo(String pseudal) {
    Me.setPseudo(pseudal);
}
public String getMyPseudo() {
    return Me.getPseudo();
}

//le pseudo est dans messagingGUI, rassembler?
 public controllerInstantMessaging(){ 

    Me=new usertype("","", "127.0.0.1");

    comtoBDD = new controllerBDD();
    welcomGUI wGUI = new welcomGUI(this, 500, 700);
    wGUI.setTitle("Insatact: your favorite low quality chat system - Welcome!");
    
}



//ici pour lancer tout le projet
    public static void main(String[] Args){
        
        new controllerInstantMessaging();

        //tests
        /*

        
        */
        }

    }