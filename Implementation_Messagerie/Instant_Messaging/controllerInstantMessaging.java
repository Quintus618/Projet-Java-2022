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

 public controllerInstantMessaging(String potato){ 
     try {

       //String drv = "com.mysql.jdbc.Driver";
       //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
    //Class.forName(drv);//woops
    Class.forName("com.mysql.cj.jdbc.Driver");

    lien = DriverManager.getConnection(addresse,login,pwd);
    }catch(Exception e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
    }

    System.out.println(potato);

    //la commande sql n'est probablement pas bonne
    /*String commande = "CREATE TABLE IF NOT EXISTS Users (id VARCHAR(63) NOT NULL, pseudo VARCHAR(63), password VARCHAR(63) NOT NULL, PRIMARY KEY (id));";
    try {
        Statement statem = lien.createStatement();
        statem.execute(commande);
        statem.close();
    } catch (SQLException e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
    }*/

    try {
        lien.close();
    } catch (SQLException e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
    }
}
    public static void main(String[] Args){
            controllerInstantMessaging test= new controllerInstantMessaging("potato");
        }
    }