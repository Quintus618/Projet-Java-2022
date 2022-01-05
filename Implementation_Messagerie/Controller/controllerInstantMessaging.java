package Controller;

import GUI.*;

public class controllerInstantMessaging{

    
private static String MyUserID=null;

public static String getmyID(){
    return MyUserID;
}
//TODO WARNING j'ai l'impression qu'il y a des carabistouilles avec les static, on devrait peut-être juste passer le controller en arg à chaque fois (au lieu du controllerBDD)
public static void setmyID(String IDgiven){
    MyUserID=IDgiven;
}

private controllerBDD comtoBDD;

//le pseudo est dans messagingGUI, rassembler?

 public controllerInstantMessaging(){ 

    //TODO ça ne va pas du tout de passer le comBDD en paramètre à chaque fois, remplacer par un appel à un comBDD static défini ici?
    comtoBDD = new controllerBDD();
    welcomGUI wGUI = new welcomGUI(comtoBDD, 500, 700);
    wGUI.setTitle("Insatact: your favorite low quality chat system - Welcome!");
    

}


    public static void main(String[] Args){
        controllerInstantMessaging CIM = new controllerInstantMessaging();

            
    }
    }