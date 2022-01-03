package Controller;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import javax.sql.rowset.*;

import Instant_Messaging.Conversation;
import Instant_Messaging.Message;

public class controllerBDD{

    private String loginBDD = "tp_servlet_003";
    private String pwdBDD = "ulah5Bee";
    //incidemment le login est aussi le nom de la BDD
    private String addresse = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/"+loginBDD;//?useSSL=false ?
    private Connection lien;
    //y accéder via terminal: mysql -h srv-bdens.insa-toulouse.fr -P 3306 -D tp_servlet_003 -u tp_servlet_003 -pulah5Bee
    //puis 'show tables;' (toutes les commandes mysql doivent finir par ;)(pas d'espace entre le -p et le mdp)
    
    //il est un peu bête d'avoir les ids et mdps de la BDD directement dans le code...

    //pour avoir des sets après la fermeture du statement
    RowSetFactory factory;
    CachedRowSet rowset;


 public controllerBDD(){ 

    //intialisations, à modifier
    //à la fin elle n'auront pas lieu d'être, les tables devront être crées une bonne fois pour toutes
    String createUsers = "CREATE TABLE IF NOT EXISTS Users (id VARCHAR(64) NOT NULL, password VARCHAR(64) NOT NULL, PRIMARY KEY (id));";
    //TODO set les bonnes tailles
    String createArchives = "CREATE TABLE IF NOT EXISTS Archives (fromID VARCHAR(64) NOT NULL, toID VARCHAR(64) NOT NULL, message VARCHAR(4096), chrono TIMESTAMP, PRIMARY KEY (fromID,toID,chrono));";
    //comment va vraiment marcher le temps? Il faudrait un timestamp plutôt qu'un TIME...
    //ATTENTION, TOUT CHANGEMENT DE CES DEUX LIGNES PEUT ENTRAINER UN CHANGEMENT DE TOUTES LES REQUETES SQL HARDCODEES
    //gérer ensuite les archivages indépendemment de la source et du destinataire
    String[] initialisation={createUsers,createArchives};

    askBDDmulti(initialisation);
    System.out.println("Succès de la création des tables initiales.");

    //ROWSET
     try {
        factory = RowSetProvider.newFactory();
        rowset = factory.createCachedRowSet();
        rowset.setUrl(addresse);
        rowset.setUsername(loginBDD);
        rowset.setPassword(pwdBDD);
    } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    //est-il utile de garder askBDD plutôt qu'un rowset.execute()?

//créer ces tables une bonne fois pour toutes puis
}


//dangereux, à n'utiliser que pour des tests avant la création des tables définitives!
private void delTablesInitiales(){
    String delAllUsers = "DROP TABLE Users;";
    String delAllArchives = "DROP TABLE Archives;";
    askBDDmulti(new String[] {delAllUsers,delAllArchives});
    System.out.println("Succès de la suppression des tables initiales.");
}


//GESTION DES REQUETES A LA BDD
//---------------------------------------------------------------

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


private void demander(String[] demandes){

    try {
        Statement statem = lien.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY,ResultSet.HOLD_CURSORS_OVER_COMMIT);

        for (int i=0;i<demandes.length;i++){
            statem.executeUpdate(demandes[i]);
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
    demander(new String[] {requete});
    fermer();
}

private void askBDDmulti(String[] requetes){
    ouvrir();
    demander(requetes);
    fermer();
}


//GESTION DE LA TABLE USERS
//-----------------------------------------------------

//testée et fonctionnelle
public boolean addUser(String id, String mdp){
    boolean ok=!idtaken(id);
    if (ok){
        String insertion = "INSERT INTO Users VALUES ('"+id+"','"+mdp+"');";
        askBDDmono(insertion);
    }else{
        System.out.println("Erreur; identifiant déjà existant.");
    }
    /*si besoin faire dans le bon .java
    javax.swing.JOptionPane.showMessageDialog(null, "Cet ID est déjà pris!");
    ou créerun message apparaissant dynamiquement dans le même JPane,au choix*/
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


//testée et fonctionnelle
public boolean idtaken(String idtest){
    boolean taken=true;
    String demande="SELECT id FROM Users WHERE id='"+idtest+"';";
    readBDD(demande);
    taken=rowset.size()>0;
    return taken;
}

  
//GESTION DE LA TABLE ARCHIVES
//----------------------------------------------

public void archiverMessage(Message sms){
    //format d'archives: id1,id2,message,horodatage (penser à set autrement les tailles à la création de la table)
    String archivage = "INSERT INTO Archives VALUES ('"+sms.getSender()+"','"+sms.getDest()+"','"+sms.getTextMessage()+"','"+Timestamp.valueOf(sms.getHorodata()).toString()+"');";
    //NOTE: méfiance sur le toString du timestamp
    askBDDmono(archivage);
}

public void archiverConv(Conversation conv){
    for (Message sms:conv.getMessageList()){
        archiverMessage(sms);
    }
}


//à finir: TODO la méthode d'initialisation dans Message.java(ET LE TEMPS)
public ArrayList <Message> recupererConv(String idone, String idtwo){
    
    String getConv = "SELECT * FROM Archives WHERE (id1='"+idone+"' AND id2='"+idtwo+"') OR (id1='"+idtwo+"' AND id2='"+idone+"');";
    readBDD(getConv);

    ArrayList<Message> conv=new ArrayList<Message>();
    String txt=null;
    String from=null;
    String to=null;
    Timestamp chrono=null;

    try {
        while (rowset.next()){
            txt=rowset.getString("message");
            from=rowset.getString("fromID");
            to=rowset.getString("toID");
            chrono=rowset.getTimestamp("chrono");
            conv.add(new Message(txt,from,to,chrono.toLocalDateTime()));
        }
    } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return conv;
}




    public static void main(String[] Args){

            controllerBDD test= new controllerBDD();

            /*String potato="Potato";
            test.addUser(potato, "NotASword");
            System.out.println(test.getMDP(potato));*/
            
            //tests divers
            test.addUser("Jean","Valjean");
            System.out.println(test.getMDP("Jean")+"\n");
            test.addUser("Jean","Mireille");
            System.out.println(test.getMDP("Jean")+"\n");
            test.updateMDP("Jean","Mireille");
            System.out.println(test.getMDP("Jean")+"\n");
            test.updateMDP("Bernard","Mireille");

//TODO mais complexe: tester archivage/désarchivage

            test.delTablesInitiales();

        }
    }