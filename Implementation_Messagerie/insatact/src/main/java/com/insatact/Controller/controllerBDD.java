package com.insatact.Controller;
import java.sql.*;
import java.util.ArrayList;
import javax.sql.rowset.*;
import javax.swing.JOptionPane;

import com.insatact.Instant_Messaging.Conversation;
import com.insatact.Instant_Messaging.Message;
import com.insatact.Instant_Messaging.usertype;


public class controllerBDD{

    private String loginBDD = "tp_servlet_003";
    private String pwdBDD = "ulah5Bee";
    //incidemment le login est aussi le nom de la BDD
    private String addresse = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/"+loginBDD;//?useSSL=false ?
    private  Connection lien;
    //y accéder via terminal: mysql -h srv-bdens.insa-toulouse.fr -P 3306 -D tp_servlet_003 -u tp_servlet_003 -pulah5Bee
    //puis 'show tables;' (toutes les commandes mysql doivent finir par ;)(pas d'espace entre le -p et le mdp)
    
    //il est un peu bête d'avoir les ids et mdps de la BDD directement dans le code...

    //pour avoir des sets après la fermeture du statement
    RowSetFactory factory;
    CachedRowSet rowset;


 public controllerBDD(){ 
    /*
    //intialisations, à modifier
    //à la fin elle n'auront pas lieu d'être, les tables devront être crées une bonne fois pour toutes
    String createUsers = "CREATE TABLE IF NOT EXISTS Users (id VARCHAR(32) NOT NULL, password VARCHAR(32) NOT NULL, PRIMARY KEY (id));";
    //TODO set les bonnes tailles
    String createArchives = "CREATE TABLE IF NOT EXISTS Archives (fromID VARCHAR(32) NOT NULL, toID VARCHAR(32) NOT NULL, message VARCHAR(4096), chrono TIMESTAMP, PRIMARY KEY (fromID,toID,chrono));";
    //comment va vraiment marcher le temps? Il faudrait un timestamp plutôt qu'un TIME...
    //ATTENTION, TOUT CHANGEMENT DE CES DEUX LIGNES PEUT ENTRAINER UN CHANGEMENT DE TOUTES LES REQUETES SQL HARDCODEES
    String createFichiers = "CREATE TABLE IF NOT EXISTS Fichiers (fromID VARCHAR(32) NOT NULL, file MEDIUMBLOB NOT NULL, chrono TIMESTAMP, PRIMARY KEY (fromID,chrono));";
    
    String[] initialisation={createUsers,createArchives,createFichiers};

    askBDDmulti(initialisation);
    System.out.println("Tables initialisées.");
    */

    //ROWSET
     try {
        factory = RowSetProvider.newFactory();
        rowset = factory.createCachedRowSet();
        rowset.setUrl(addresse);
        rowset.setUsername(loginBDD);
        rowset.setPassword(pwdBDD);
    } catch (SQLException e) {
        e.printStackTrace();
    }
    //est-il utile de garder askBDD plutôt qu'un rowset.execute()?

}


//dangereux, à n'utiliser que pour des tests avant la création des tables définitives!
/*private void delTablesInitiales(){
    String delAllUsers = "DROP TABLE Users;";
    String delAllArchives = "DROP TABLE Archives;";
    askBDDmulti(new String[] {delAllUsers,delAllArchives});
    System.out.println("Succès de la suppression des tables initiales.");
}*/


//GESTION DES REQUETES A LA BDD
//---------------------------------------------------------------------------------------------------------------------------------

private void ouvrir(){
//nécessité de rajouter les .jar de /Library dans le classPath
//VScode: Java projects en bas à gauche, clic droit sur Implementation_messagerie, configure class path
try {
    Class.forName("com.mysql.cj.jdbc.Driver");

    lien = DriverManager.getConnection(addresse,loginBDD,pwdBDD);
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


private void demander(ArrayList<String> demandes){

    try {
        Statement statem = lien.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY,ResultSet.HOLD_CURSORS_OVER_COMMIT);

        for (String i:demandes){
            statem.executeUpdate(i);
        }
        statem.close();
    } catch (SQLException e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
    }
}


//le résultat est récupéré dans rowset!
private void readBDD(String demande){

    try {
        rowset.setCommand(demande);
        rowset.execute();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

private void askBDDmono(String requete){
    ouvrir();
    ArrayList<String> ask=new ArrayList<String>();
    ask.add(requete);
    demander(ask);
    fermer();
}

//utilisée pour la partie de l'initialisation commentée, peut toujours servir
private void askBDDmulti(ArrayList<String> requetes){
    ouvrir();
    demander(requetes);
    fermer();
}

/*
private int combiendeja(String idone, String idtwo){
    int dejala=0;
    readBDD("SELECT COUNT(*) AS STOCKED_SMS FROM Archives WHERE (fromID='"+idone+"' AND toID='"+idtwo+"') OR (fromID='"+idtwo+"' AND toID='"+idone+"');");
    if (rowset!=null){
        try {
            rowset.next();
            dejala=rowset.getInt("STOCKED_SMS");
            System.out.println("BDD: "+Integer.toString(dejala)+" messages déjà stockés avec "+idtwo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return dejala;
}
*/

/*private Conversation suppMessages(Conversation convtoarchive, int todel){
    usertype corresp=convtoarchive.getCorrespondant();
    ArrayList<Message> olconv=recupererConv(corresp);
    deleteconv(controllerInstantMessaging.getmyID(),corresp.getId());
    Conversation convo=new Conversation(corresp);
    for(Message sms:oldconv){//automatiquement les messages les plus vieux seront supprimés
        convo.addMessage(sms);
    }
    return convtoarchive;
}*/

private void deleteconv(usertype corresp){
    String idone=controllerInstantMessaging.getmyID();
    String idtwo=corresp.getId();
    askBDDmono("DELETE FROM Archives WHERE (fromID='"+idone+"' AND toID='"+idtwo+"') OR (fromID='"+idtwo+"' AND toID='"+idone+"');");
}


//GESTION DE LA TABLE USERS
//----------------------------------------------------------------------------------------------------------------

public boolean addUser(String id, String mdp){
    boolean ok=!idtaken(id);
    if (ok){
        String insertion = "INSERT INTO Users VALUES ('"+id+"','"+mdp+"');";
        askBDDmono(insertion);
    }else{
        JOptionPane.showMessageDialog(null,"Identifiant déjà utilisé.\nMerci d'en choisir un autre!");
    }
    return ok;
}


//fonction "bonus", permet d'update un mot de passe
public boolean updateMDP(String id, String mdp){
    boolean ok=idtaken(id);
    if (ok){
        String insertion = "INSERT INTO Users VALUES ('"+id+"','"+mdp+"') ON DUPLICATE KEY UPDATE password='"+mdp+"';";
        askBDDmono(insertion);
    }else{
        System.out.println("Erreur; cet id ("+id+") n'est pas utilisé.");
    }
    //inutile de proposer de créer l'utilisateur s'il n'existepas déjà, vus les usecases ça n'est pas censé arriver
    return ok;
}


//à utiliser pour log in
public String getMDP(String id){
    String insertion = "SELECT password FROM Users WHERE id='"+id+"';";
    String mdp=null;
    readBDD(insertion);
    try {
        rowset.next();
        mdp= rowset.getString("password");
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return mdp;
}

public boolean idtaken(String idtest){
    boolean taken=true;
    String demande="SELECT id FROM Users WHERE id='"+idtest+"';";
    readBDD(demande);
    taken=rowset.size()>0;
    return taken;
}

  
//GESTION DE LA TABLE ARCHIVES
//--------------------------------------------------------------------------------------------------------------------------


private String escapeString(String s) {
    return s.replaceAll("\\\\", "\\\\\\\\").replaceAll("\b","\\b").replaceAll("\n","\\n").replaceAll("\r", "\\r").replaceAll("\t", "\\t").replaceAll("\\x1A", "\\Z").replaceAll("\\x00", "\\0").replaceAll("'", "\\'").replaceAll("\"", "\\\"");
}

private String escapeWildcards(String s) {
    return escapeString(s).replaceAll("%", "\\%").replaceAll("_","\\_");
}



public void archiverMessage(Message sms){
    //format d'archives: id1,id2,message,horodatage (penser à set autrement les tailles à la création de la table)
    String archivage = "INSERT INTO Archives VALUES ('"+sms.getSender()+"','"+sms.getDest()+"','"+escapeWildcards(sms.getTextMessage())+"','"+Timestamp.valueOf(sms.getHorodata()).toString()+"')ON DUPLICATE KEY UPDATE chrono=chrono;";
    //NOTE: méfiance sur le toString du timestamp
    askBDDmono(archivage);
}//!!! Il faudra trouver un moyen (stocker un booleen par conversation, faire un on duplicate key...)
//pour ne pas avoir les messages stockés en double!


public void archiverConv(Conversation conv){
    ArrayList<String> archivage=new ArrayList<String>();
    /*String idcorr=conv.getCorrespondant().getId();
    int nbnouvos=conv.nbMess()-conv.cmbFromArchives();
    int nbtotal=nbnouvos+combiendeja(controllerInstantMessaging.getmyID(), idcorr);
    int asupprimer=nbtotal-controllerInstantMessaging.getMaxmessToArchive();
    if(asupprimer>0){
        conv=suppMessages(idcorr,asupprimer);
        System.out.println("BDD: "+Integer.toString(asupprimer)+" messages supprimés avec "+idcorr);
    }else{
        System.out.println("BDD: rien à supprimer pour archiver la conversation avec "+idcorr+" ("+nbtotal+" messages au total)");
    }*/
    deleteconv(conv.getCorrespondant());
    for (Message sms:conv.getMessageList()){
            archivage.add("INSERT INTO Archives VALUES ('"+sms.getSender()+"','"+sms.getDest()+"','"+escapeWildcards(sms.getTextMessage())+"','"+Timestamp.valueOf(sms.getHorodata()).toString()+"') ON DUPLICATE KEY UPDATE chrono=chrono;");
    }
    askBDDmulti(archivage);
}


public ArrayList <Message> recupererConv(usertype corr){
    
    String idone=controllerInstantMessaging.getmyID();
    String idtwo=corr.getId();

    String getConv = "SELECT * FROM Archives WHERE (fromID='"+idone+"' AND toID='"+idtwo+"') OR (fromID='"+idtwo+"' AND toID='"+idone+"') ORDER BY chrono ASC;";
    readBDD(getConv);

    ArrayList<Message> conv=new ArrayList<Message>();
    String txt=null;
    String from=null;
    String to=null;
    Timestamp chrono=null;
    if (rowset!=null){
        try {
            while (rowset.next()){
                txt=rowset.getString("message");
                from=rowset.getString("fromID");
                to=rowset.getString("toID");
                chrono=rowset.getTimestamp("chrono");
                conv.add(new Message(txt,from,to,chrono.toLocalDateTime()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        return conv;
    }




    public static void main(String[] Args){
            
        //reset pour le dev !!!WARNING, DANGEREUX!!!
        //new controllerBDD().delTablesInitiales();
       /* new controllerBDD();*/


            /*String potato="Potato";
            test.addUser(potato, "NotASword");
            System.out.println(test.getMDP(potato));*/
            
            //tests divers
            /*test.addUser("Jean","Valjean");
            System.out.println(test.getMDP("Jean")+"\n");
            test.addUser("Jean","Mireille");
            System.out.println(test.getMDP("Jean")+"\n");
            test.updateMDP("Jean","Mireille");
            System.out.println(test.getMDP("Jean")+"\n");
            test.updateMDP("Bernard","Mireille");*/

            /*controllerBDD test=new controllerBDD();
            Message sms=new Message("Salut, comment ça va?", "Tintin", true);
            test.archiverMessage(sms);
            //id null car null par défaut
            System.out.println(test.recupererConv(new usertype("Tintin", null, null)).get(0).toString());*/
        
        
        }
    }