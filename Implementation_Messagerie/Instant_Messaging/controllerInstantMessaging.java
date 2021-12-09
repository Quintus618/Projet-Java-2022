package Instant_Messaging;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class controllerInstantMessaging{

    private String login = "tp_servlet_003";
    private String pwd = "ulah5Bee";
    //incidemment le login est aussi le nom de la BDD
    private String addresse = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/"+login;//?useSSL=false ?
    private Connection lien;
    //y accéder via terminal: mysql -h srv-bdens.insa-toulouse.fr -P 3306 -D tp_servlet_003 -u tp_servlet_003 -pulah5Bee
    //puis show tables; (toutes les commandes mysql doivent finir par ;)(pas d'espace entre le -p et le mdp)


 public controllerInstantMessaging(){ 

    //intialisations, à modifier
    String createUsers = "CREATE TABLE IF NOT EXISTS Users (id VARCHAR(63) NOT NULL, password VARCHAR(63) NOT NULL, PRIMARY KEY (id));";
    String createArchives = "CREATE TABLE IF NOT EXISTS Archives (id1 VARCHAR(63) NOT NULL, id2 VARCHAR(63) NOT NULL, message VARCHAR(8000), chrono TIME, PRIMARY KEY (id1,id2));";
    //gérer ensuite les archivages indépendemment de la source et du destinataire
    String[] initialisation={createUsers,createArchives};

    askBDD(initialisation);


}

private void ouvrir(){
//nécessité de rajouter les .jar de /Library dans le classPath
//VScode: Java projects en bas à gauche, clic droit sur Implementation_messagerie, configure class path
try {
    Class.forName("com.mysql.cj.jdbc.Driver");

    lien = DriverManager.getConnection(addresse,login,pwd);
    }catch(Exception e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
    }
}

private void fermer(){
    try {
        lien.close();
    } catch (SQLException e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
    }
}

private void demander(String[] demandes){
    try {
        Statement statem = lien.createStatement();
        for (int i=0;i<demandes.length;i++){
            statem.execute(demandes[i]);
        }
        statem.close();
    } catch (SQLException e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
    }

}

protected void askBDD(String[] requetes){
    ouvrir();
    demander(requetes);
    fermer();
}

//PAS ENCORE TESTEE 
protected void addUser(String pseudo, String id){
    String insertion = "INSERT INTO Users VALUES ("+pseudo+","+id+");";
    askBDD(new String[] {insertion});
}

    public static void main(String[] Args){
            controllerInstantMessaging test= new controllerInstantMessaging();
            System.out.println("Succès de la création des tables initiales.");
            String delAllUsers = "DROP TABLE Users;";
            String delAllArchives = "DROP TABLE Archives;";
            test.askBDD(new String[] {delAllUsers,delAllArchives});
            System.out.println("Succès de la suppression des tables initiales.");
        }
    }