package Controller;

import GUI.*;

public class controllerInstantMessaging{

    
private static String MyUserID=null;

public static String getmyID(){
    return MyUserID;
}

protected static void setmyID(String IDgiven){
    MyUserID=IDgiven;
}

public controllerBDD comtoBDD;

//le pseudo est dans messagingGUI, rassembler?
//TODO peut-être créer le type User comprenant pseudo+id+IP+TIMER à utiliser pour le broadcast (+mdp si soi-même)

 public controllerInstantMessaging(){ 

    comtoBDD = new controllerBDD();
    welcomGUI wGUI = new welcomGUI(comtoBDD, 500, 700);
    wGUI.setTitle("Insatact: your favorite low quality chat system - Welcome!");
    

}


    public static void main(String[] Args){
        controllerInstantMessaging CIM = new controllerInstantMessaging();

            
    }
    }