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
    System.out.println("Succès de la création des tables initiales.");

//créer ces tables une bonne fois pour toutes puis
}


//dangereux, à n'utiliser que pour des tests avant la création des tables définitives!
private void delTablesInitiales(){
    String delAllUsers = "DROP TABLE Users;";
    String delAllArchives = "DROP TABLE Archives;";
    askBDD(new String[] {delAllUsers,delAllArchives});
    System.out.println("Succès de la suppression des tables initiales.");
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

private ResultSet demander(String[] demandes){
    ResultSet reponse;
    try {
        Statement statem = lien.createStatement();
        reponse=statem.execute(demandes[0]);//pas folichon
        for (int i=1;i<demandes.length;i++){
            statem.execute(demandes[i]);
        }
        statem.close();
    } catch (SQLException e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
    }
    return reponse;
}

private ResultSet askBDD(String[] requetes){
    ouvrir();
    ResultSet answer=demander(requetes);
    fermer();
    return answer;
}

//PAS ENCORE TESTEE 
public void addUser(String id, String mdp){
    String insertion = "INSERT INTO Users VALUES ('"+id+"','"+mdp+"') ON DUPLICATE KEY UPDATE id=id;";
    askBDD(new String[] {insertion});
}

//PAS ENCORE TESTEE 
public String getmdp(String id){
    String insertion = "SELECT password FROM Users WHERE id='"+id+"';";
    String mdp=askBDD(new String[] {insertion}).getString("password");
    return mdp;// FINIR AVEC https://alvinalexander.com/java/java-mysql-select-query-example/
}

//@TODO
public void archiverConv(String pseudo, String id){
    String archivage = "INSERT INTO Users VALUES ("+pseudo+","+id+");";
    askBDD(new String[] {archivage});
}

//@TODO
public void recupererConv(String idone, String idtwo){
    String getConv = "SELECT * FROM Archives WHERE (id1='"+idone+"' AND id2='"+idtwo+"') OR (id1='"+idtwo+"' AND id2='"+idone+"');";
    askBDD(new String[] {getConv});
    //return conversation;
}


    public static void main(String[] Args){

            controllerInstantMessaging test= new controllerInstantMessaging();

            String potato="Potato";
            test.addUser(potato, "NotASword");
            System.out.println(test.getmdp(potato));
            
            
            //test.delTablesInitiales();

        }
    }