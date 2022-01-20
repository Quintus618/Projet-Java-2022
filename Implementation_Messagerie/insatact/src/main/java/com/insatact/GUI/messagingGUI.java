package com.insatact.GUI;
//Importation Libraries
import javax.swing.*;

import com.insatact.Instant_Messaging.*;
import com.insatact.Controller.*;
import java.awt.event.*;
import java.awt.*;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class messagingGUI extends JFrame{

    //CONSTANTE
    private int MAX_MESS = 10;

    private controllerInstantMessaging controlCHAT;


    private usertype correspondant;
    private Map <usertype, Conversation> mapConvos=new HashMap<usertype, Conversation>();
//permet de gérer les conversations plus facilement qu'une arraylist

    //Buttons of the instant messaging
    private JButton deconnexionButton;
    private JButton mediaButton;
    private JButton fileButton;
    private JButton sendMessageButton;
    private JButton changePeudo;

    //Panels of the instant messaging
    private JPanel chatPanel;
    private JPanel connectedPanel;
    private JPanel messagePanel;
    private JPanel contentPane;
    private JPanel connectedGlobal;

    private JScrollPane scrollPane;
    private JScrollPane scrollPane2;

    private GridBagConstraints gbc = new GridBagConstraints();

    private JTextArea textSenderZone;

    private int numberMessage = 0;

    private GridBagLayout gl;

    private TCPcontrollerServer tcpServer;

    protected JLabel lPseudo;

    public ArrayList<JLabel> messageList;
    public ArrayList<JButton> connectedUsersList;

    protected UDPcontroller udpController;


    private boolean connectedUsermutex = false;
    private Thread updateConnected;
    private Thread listen;

    //Constructor
    public messagingGUI(controllerInstantMessaging controlCHAT, int height, int width, String pseudo){
        
        //Creation of GUI
        super("Insatact");
        setSize(width, height);
        this.setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //ceci permet d'utiliser disconnect() en actionlistener et d'arrêter les broadcasts UDP
        addWindowListener(new WindowListener(){  
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                disconnect();//important
            }

            @Override
            public void windowDeactivated(WindowEvent e) {                
            }

            @Override
            public void windowDeiconified(WindowEvent e) {                
            }

            @Override
            public void windowIconified(WindowEvent e) {                
            }

            @Override
            public void windowOpened(WindowEvent e) {                
            }
            });

        this.controlCHAT=controlCHAT;
        //impératif d'avoir ça avant le new Conversation, sinon erreur car comtoBDD null
        controlCHAT.setmyPseudo(pseudo);
        correspondant=new usertype("", "", "");//permet d'avoir un user "vide" à qui ne PAS envoyer de message
        mapConvos.put(correspondant, new Conversation(correspondant));

        //Creation of graphical components 
        buildComponentInterface(this);

        this.controlCHAT=controlCHAT;

    }

    public controllerInstantMessaging getControlCHAT() {
        return controlCHAT;
    }
    
    //Redimensionner une icone
    public static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
        Image img = icon.getImage();  
        Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);  
        return new ImageIcon(resizedImage);
    }

    //Parameters Buttons
    private void paramButton(JButton b, int resizedWidth, int resizedHeight, ImageIcon i){
        b.setIcon(resizeIcon(i, resizedWidth, resizedHeight));
        b.setBackground(new Color(255,255,255,0));
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
    }

    private void updatescroll(){
        int max=scrollPane.getVerticalScrollBar().getMaximum();
        int now=scrollPane.getVerticalScrollBar().getValue();
        System.out.println("Scrollbar: "+now+" vs "+max);
        if(now<=10 || max-now<=700){
            scrollPane.getVerticalScrollBar().setValue(max);
            System.out.println("Scrollbar set à max");
            SwingUtilities.updateComponentTreeUI(contentPane);
        }
    }

    private void buildComponentInterface(JFrame f){
        
        //Add buttons
        deconnexionButton = new JButton("deconnexion");

        //Add Pictures, Audio
        ImageIcon iconMedia = new ImageIcon("./src/main/java/com/insatact/GUI/Pictures/media.png");
        mediaButton = new JButton(iconMedia);
        paramButton(mediaButton, 40, 40, iconMedia);

        //Add files
        ImageIcon iconFile = new ImageIcon("./src/main/java/com/insatact/GUI/Pictures/file.png");
        fileButton = new JButton(iconFile);
        fileButton.setIcon(resizeIcon(iconFile, 40, 40));
        paramButton(fileButton, 40, 40, iconFile);

        //Send Messages
        ImageIcon iconMessage = new ImageIcon("./src/main/java/com/insatact/GUI/Pictures/message.png");
        sendMessageButton = new JButton(iconMessage);
        paramButton(sendMessageButton, 40, 40, iconMessage);

        //Texte Input zone to send messages
        textSenderZone = new JTextArea(2,80);
        textSenderZone.setLineWrap (true);
        textSenderZone.setWrapStyleWord (false);

        //Change pseudo
        changePeudo = new JButton("Changer le pseudo");

        //Display Pseudo
        lPseudo = new JLabel(controlCHAT.getMyPseudo());

        chatPanel = new JPanel();
        chatPanel.setBackground(Color.gray);
        chatPanel.add(Box.createHorizontalGlue());
        chatPanel.add(mediaButton);
        chatPanel.add(Box.createHorizontalGlue());
        chatPanel.add(fileButton);
        chatPanel.add(Box.createHorizontalGlue());
        chatPanel.add(textSenderZone);
        chatPanel.add(Box.createHorizontalGlue());
        chatPanel.add(sendMessageButton);
        chatPanel.add(Box.createHorizontalGlue());
        chatPanel.add(changePeudo);
        chatPanel.add(Box.createHorizontalGlue());
        chatPanel.add(deconnexionButton);
        chatPanel.add(Box.createHorizontalGlue());
        chatPanel.add(lPseudo);
        chatPanel.add(Box.createHorizontalGlue());
        add(chatPanel, BorderLayout.SOUTH);

        connectedGlobal = new JPanel(new BorderLayout());
        connectedPanel = new JPanel();
        connectedPanel.setLayout(new BoxLayout(connectedPanel, BoxLayout.Y_AXIS));
        connectedPanel.setBackground(Color.decode("#2F2F2F"));
        JLabel lconnecte = new JLabel("Liste des personnes connectées");
        lconnecte.setForeground(Color.WHITE);
        lconnecte.setFont(new Font("Serif", Font.PLAIN, 20));
        connectedPanel.add(lconnecte);
        scrollPane2 = new JScrollPane(connectedPanel);
        scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        connectedGlobal.add(connectedPanel, BorderLayout.CENTER);
        connectedGlobal.add(scrollPane2, BorderLayout.EAST);
        add(connectedGlobal, BorderLayout.EAST);

        //Messages zone
        contentPane = new JPanel(new BorderLayout());
        messagePanel = new JPanel();
        gl = new GridBagLayout();
        messagePanel.setLayout(gl);
        messagePanel.setAutoscrolls(true);
        add(messagePanel, BorderLayout.CENTER);

        scrollPane = new JScrollPane(messagePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        scrollPane.setBounds(50, 30, 1600, 900);
        scrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        contentPane.setPreferredSize(new Dimension(1600, 1000));
        
        //add(scrollPane, BorderLayout.WEST);
        
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.setBackground(Color.decode("#2FFFFF"));
        add(contentPane, BorderLayout.CENTER);
        
        //add(contentPane, BorderLayout.CENTER);
        //add(messagePanel, BorderLayout.CENTER);

        //Initialisation des listes
        messageList = new ArrayList<JLabel>();
        connectedUsersList = new ArrayList<JButton>();

        messagingGUI m = this;

        //ACTION BOUTTON
        /*sendMessageButton.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){ writeMessage(textSenderZone.getText());}});*/
            updateConnected = new Thread(new Runnable() {

                int nbfois = 0;
                JButton buttonselected;

                @Override
                public void run(){
                    while(!Thread.currentThread().isInterrupted()){
                    
                        for (JButton b : connectedUsersList){
                            b.addActionListener(new ActionListener(){  
                                public void actionPerformed(ActionEvent e){ 
                                    for(JButton c : connectedUsersList){
                                        c.setBackground(Color.decode("#2F2F2F"));
                                    }
                                    b.setBackground(Color.decode("#08410f"));
                                
                                    if(buttonselected != b){
                                        buttonselected=b;
                                        nbfois = 0;
                                        System.out.println("Test1");
                                        m.displayConversation(b.getText());
                                        System.out.println("Test2");
                                    }
                                    else{
                                        System.out.println(buttonselected.getText() + "................................");
                                    }
                                }});
                
                                    
                                    
                         }
                        try {

                            Thread.sleep(2000);

                        } catch (InterruptedException e1) {
                            //e1.printStackTrace();
                            System.out.println("Thread interrompu.");
                        }
                    }
                }
            });  

            updateConnected.start();

            //Creation TCP server
             tcpServer = new TCPcontrollerServer(controlCHAT.getMyIdentity());

            //Le serveur écoute
            listen = new Thread(new Runnable(){
                @Override
                public void run(){

                    while(!Thread.currentThread().isInterrupted()){
                        System.out.println("ALLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLOOOOOOOOOOOOOOOOOOOOOOOOOOOo");
                        tcpServer.dataReception(m);
                    }            
                }
            });  
            listen.start();

            //Creation UDPcontroller
            udpController = new UDPcontroller(this);

            //Action when there is a mouse click on a button
            sendMessageButton.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent MOUSE_CLICKED){ 
                    String mess = textSenderZone.getText();
                    writeMessage(mess);
                    if(!mess.isBlank()){
                        mapConvos.get(correspondant).getTCP().sendMessage(mess);
                    }
                }});
            deconnexionButton.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent e){
                    disconnect();
                }
                });
            changePeudo.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent MOUSE_CLICKED){ 
                    //changePseudof();
                    new changePseudoPopUp(m,100,500);
                    changePseudof(lPseudo);
                }
            });

        
        setVisible(true);
    }


    public void disconnect(){
        try {
            udpController.interrupt();
            udpController.udpbroadcastdeco(controlCHAT.getMyIdentity().toString());
            System.out.println("Fait UDP");
        } catch (SocketException e1) {
            e1.printStackTrace();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
        tcpServer.killserver();
        System.out.println("Fait TCP");

        while(listen.isAlive()){
            listen.interrupt();
        }
        System.out.println("Serveur TCP");
        
        while(updateConnected.isAlive()){
            updateConnected.interrupt();
        }
        System.out.println("Fait Liste COnnectés");
        endbackupBDD(); 
        dispose();
    }

    
    private void endbackupBDD(){
        for(Conversation convtobackup:mapConvos.values()){
            controlCHAT.getComtoBDD().archiverConv(convtobackup);
            convtobackup.killTCP();
        }
    }


    public void newUser(usertype corresp){
        mapConvos.put(corresp, new Conversation(corresp));
    }

    private usertype getUserByPseudo(String upseudo){
        usertype thisuser=null;
        for(usertype usrsearch:mapConvos.keySet()){
            if(usrsearch.getPseudo().equals(upseudo)){
                thisuser=usrsearch;
                break;
            }
            //System.out.println("Recherche  de "+upseudo+":"+usrsearch.getPseudo());
        }
        if(thisuser==null){
            System.out.println("Erreur: utilisateur non trouvé ("+upseudo+")");
        }
        return thisuser;
    }

    private usertype getUserByIP(String IPasked){
        usertype thisuser=null;
        for(usertype usrsearch:mapConvos.keySet()){
            if(usrsearch.getIPaddr().equals(IPasked)){
                thisuser=usrsearch;
                break;
            }
            //System.out.println("Recherche  de "+IPasked+":"+usrsearch.getPseudo());
        }
        if(thisuser==null){
            System.out.println("Erreur: utilisateur non trouvé ("+IPasked+")");
        }
        return thisuser;
    }

    public Message stockMessage(String smsstring, String validIP){
        usertype thisUsr=getUserByIP(validIP);
        Message sms = new Message(smsstring, thisUsr.getId(), false);
        mapConvos.get(thisUsr).addMessage(sms);
        mapConvos.get(thisUsr).setHasunread(true);
        return sms;
    }

    //Create conversation
    //!différent de la lancer, on ne la lance que si on write ou reçoit un message
    private void createConversation(String pseudodest){
        //!!nom trompeur, la conversation vide est créée et mise dans la hashmap dès qu'un nnouvel user apparait
        
        mapConvos.get(correspondant).load(this);//récupération des messages
        System.out.println("Récup Historique "+correspondant.getPseudo()+" réussie");
        mapConvos.get(correspondant).launchTCP();
        System.out.println("Création TCP "+correspondant.getPseudo());
        
    }

    private void displayConversation(String unpseudo){
        messagePanel.removeAll();
        System.out.println("Destinataire: "  + unpseudo);
        correspondant=getUserByPseudo(unpseudo);
        if(!mapConvos.get(correspondant).gethasTCP()){
            createConversation(unpseudo);//récup les archives et lance TCP
        }
        mapConvos.get(correspondant).setHasunread(false);
        for(Message smstodisplay:mapConvos.get(correspondant).getMessageList()){
            displayMessage(smstodisplay,true);
        }
        SwingUtilities.updateComponentTreeUI(messagePanel);
        updatescroll();
    }
    
    public void updatePseudo(String oldpseudo, String neopseudo, boolean fromtimer){
        usertype oldusr=getUserByPseudo(oldpseudo);
        usertype upuser=oldusr;
        System.out.println(oldusr);
        if (!(oldusr==null)){
            upuser.setPseudo(neopseudo);
            Conversation upconv=mapConvos.get(oldusr);
            upconv.setCorrespondant(upuser);
            if (fromtimer){//utilisée que si on utilise un timer de déco
                for(Message parkoursms:mapConvos.get(getUserByPseudo(neopseudo)).getMessageList()){
                    upconv.addMessage(parkoursms);
                }
            }
            mapConvos.remove(oldusr);
            mapConvos.put(upuser, upconv);
            if (upuser.getId().equals(correspondant.getId())){
                displayConversation(neopseudo);
            }
        }
    }

    public usertype getCorrespondant() {
        return correspondant;
    }

    //Send a message to another user
    private void writeMessage(String t){
        if(t.length()>=4096){
            JOptionPane.showMessageDialog(null, "Les messages sont limités à 4095 caractères, contre "+t.length()+" ici.");
        }//attention si l'on modifie la valeur max dans les tables
        else if(correspondant.getId().equals("")){
            JOptionPane.showMessageDialog(null, "Merci de choisir un destinataire.");
        }else{  
            if (!t.isBlank()){
                Message message1 = new Message(t, correspondant.getId(),true);
                displayMessage(message1,false);
                mapConvos.get(correspondant).addMessage(message1);
                textSenderZone.setText("");
            }

        } 
    }

    //Receive messages from another user
    public void receiveMessage(String t, String clientIPAddress){
        Message message2=stockMessage(t, clientIPAddress);
        displayMessage(message2,false);
    }

    //plus pratique, notamment pourchanger de conversation affichée
    private void displayMessage(Message sms, Boolean updt){
        numberMessage++;
        boolean sentbyMe=sms.getSender().equals(controllerInstantMessaging.getmyID());
        String colorSMS;
        if(!sentbyMe){
            //gbc.fill = GridBagConstraints.HORIZONTAL;
            colorSMS="#4B6CDB";
        }else{
            gbc.fill = GridBagConstraints.VERTICAL;
            colorSMS="#61DC5A";
        }

        JPanel Messhorodatage = new JPanel();
        Messhorodatage.setLayout(new BorderLayout());
        JPanel Mess = new JPanel();
        LocalDateTime mdate = sms.getHorodata();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        String formattedDate = mdate.format(myFormatObj);
        JLabel dateLab;
        JLabel smsLabel;
        String textalabelliser="<html><p>"+sms.getTextMessage().replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>")+"</p></html>";

        if(!sentbyMe){
            dateLab = new JLabel(formattedDate,SwingConstants.RIGHT);
            smsLabel=new JLabel(textalabelliser,SwingConstants.LEFT);
            gbc.anchor=GridBagConstraints.WEST;
        }else{
            dateLab = new JLabel(formattedDate,SwingConstants.LEFT);
            smsLabel=new JLabel(textalabelliser,SwingConstants.RIGHT);
            gbc.anchor=GridBagConstraints.EAST;
        }

        Mess.setBackground(Color.decode(colorSMS));
        Messhorodatage.setMinimumSize(new Dimension(500, 40));
        Messhorodatage.setPreferredSize(new Dimension(750, 100));
        Mess.add(smsLabel);
        Messhorodatage.add(Mess, BorderLayout.CENTER);
        Messhorodatage.add(dateLab, BorderLayout.SOUTH);

        //l'affichage est sur une grille; chaque Message (Messhorodatage) est d'un côté et MBlanc (le vide) de l'autre
        JPanel MBlanc = new JPanel();
        MBlanc.setMinimumSize(new Dimension(750, 100));
        MBlanc.setPreferredSize(new Dimension(750, 100));
        
        gbc.gridx = 0;
        gbc.gridy = numberMessage;
        if(!sentbyMe){
            messagePanel.add(Messhorodatage,gbc);
        }else{
            messagePanel.add(MBlanc,gbc);
        }
        gbc.gridx = 1;
        gbc.gridy = numberMessage;
        if(!sentbyMe){
            messagePanel.add(MBlanc,gbc);
        }else{
            messagePanel.add(Messhorodatage,gbc);
        }

        messageList.add(smsLabel);

        if(updt){
            SwingUtilities.updateComponentTreeUI(messagePanel);
            updatescroll();
        }
    }


    //Display connected users
    public void displayConnectedUsers(String newuser){

        String pseudo=newuser.split(" ")[1];
        while(connectedUsermutex){try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }}

        connectedUsermutex = true;

        JButton coUsers = new JButton(pseudo);
        coUsers.setForeground(Color.WHITE);
        coUsers.setMaximumSize(new Dimension(Integer.MAX_VALUE, coUsers.getMinimumSize().height));
        coUsers.setBackground(Color.decode("#2F2F2F"));
        //coUsers.setBorderPainted(false);
        //coUsers.setContentAreaFilled(false);
        //coUsers.setFocusPainted(false);
        boolean trouve = false;
        for(JButton jb:connectedUsersList){
            if(jb.getText().equals(pseudo)){
                trouve=true;
                usertype usrdisplayed=getUserByPseudo(pseudo);
                if(mapConvos.get(usrdisplayed).isStarted() && !correspondant.getId().equals(usrdisplayed.getId())){
                    if(mapConvos.get(usrdisplayed).hasunreadsms() ){
                        jb.setBackground(Color.decode("#0040ff"));//message non lu en bleu
                    }else{
                        jb.setBackground(Color.decode("#7D93DE"));//conversation active en lavande
                    }
                }
            }
        }

        if(!trouve){
            connectedPanel.add(coUsers);
            connectedUsersList.add(coUsers);
            this.newUser(new usertype(newuser));
        }

        SwingUtilities.updateComponentTreeUI(connectedPanel);

        System.out.println("Status scrollbar: "+scrollPane.getVerticalScrollBar().getValue()+" vs "+scrollPane.getVerticalScrollBar().getMaximum());

        connectedUsermutex = false;
    }

    //Remove connected user
    public void removeConnectedUsers(usertype usertorm){
        if(getUserByPseudo(usertorm.getPseudo())!=null){
            while(connectedUsermutex){try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }}
            connectedUsermutex = true;

            System.out.println(usertorm.getPseudo()+" se déconnecte");
            for(JButton i : connectedUsersList){
                if (i.getText().equals(usertorm.getPseudo())){
                    connectedPanel.remove(i);
                    connectedUsersList.remove(i);

                    controlCHAT.getComtoBDD().archiverConv(mapConvos.get(correspondant));

                    //attention aussi, il faut gérer les changements de pseudos...
                    if(correspondant.getId().equals(usertorm.getId())){
                        correspondant=new usertype("", "", null);
                        JOptionPane.showMessageDialog(null, usertorm.getPseudo()+" vient de se déconnecter.");
                    }

                    mapConvos.remove(usertorm);
                    SwingUtilities.updateComponentTreeUI(connectedPanel);
                    break;
                }
            }
            
            connectedUsermutex = false;
        }
    }

    public void updateConnectedList(String newPseudo, String oldPseudo){

        while(connectedUsermutex){try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }}

        connectedUsermutex = true;
        for(JButton b: connectedUsersList){
            if (b.getText().equals(oldPseudo)){
                if(oldPseudo.equals(correspondant.getPseudo())){
                    correspondant.setPseudo(newPseudo);
                }
                b.setText(newPseudo);
                SwingUtilities.updateComponentTreeUI(b);
            }
        }

        connectedUsermutex = false;

    }


    public void changePseudof(JLabel chpseudo){
        chpseudo.setText(controlCHAT.getMyPseudo());
        SwingUtilities.updateComponentTreeUI(chpseudo);
    }

    //en rab du cahier des charges...? Est-ce qu'on a le broadcast pour demander s'il est pris ailleurs?
    public Boolean pseudotaken(String pseudal){

        while(connectedUsermutex){try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }}

        connectedUsermutex=true;

        boolean oui=false;
        for(JButton b: connectedUsersList){
            if (b.getText().equals(pseudal)){
                oui=true;
                break;
            }
        }

        connectedUsermutex=false;

        return oui;
    }

  

    public GridBagLayout getGl() {
        return gl;
    }

    public static void main(String[] Args) throws InterruptedException{

    }

}
