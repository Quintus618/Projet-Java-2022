package Controller;


public class controllerInstantMessaging{

    
private static String MyUserID=null;

public static String getmyID(){
    return MyUserID;
}

protected static void setmyID(String IDgiven){
    MyUserID=IDgiven;
}

//le pseudo est dans messagingGUI, rassembler?
//peut-être créer le type User comprenant pseudo+id(+mdp si soi-même)

 public controllerInstantMessaging(){ 

}


    public static void main(String[] Args){

            String potato="Potato";
            System.out.println(potato);
            
        }
    }