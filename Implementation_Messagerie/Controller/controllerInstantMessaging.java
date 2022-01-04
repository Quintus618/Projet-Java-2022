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
//peut-être créer le type User comprenant pseudo+id(+mdp si soi-même)

 public controllerInstantMessaging(){ 

    comtoBDD = new controllerBDD();
    welcomGUI wGUI = new welcomGUI(comtoBDD, 500, 700);
    wGUI.setTitle("Low quality chat system - Welcome!");
    

}


    public static void main(String[] Args){
        controllerInstantMessaging CIM = new controllerInstantMessaging();

            
    }
    }