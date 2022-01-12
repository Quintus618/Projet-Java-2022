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

    private JScrollPane scrollPane;

    private GridBagConstraints c = new GridBagConstraints();

    private JTextArea textSenderZone;

    private int numberLine = 0;
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

    private void buildComponentInterface(JFrame f){
        
        //Add buttons
        deconnexionButton = new JButton("deconnexion");

        //Add Pictures, Audio
        ImageIcon iconMedia = new ImageIcon("./insatact/src/main/java/com/insatact/GUI/Pictures/media.png");
        mediaButton = new JButton(iconMedia);
        paramButton(mediaButton, 40, 40, iconMedia);

        //Add files
        ImageIcon iconFile = new ImageIcon("./insatact/src/main/java/com/insatact/GUI/Pictures/file.png");
        fileButton = new JButton(iconFile);
        fileButton.setIcon(resizeIcon(iconFile, 40, 40));
        paramButton(fileButton, 40, 40, iconFile);

        //Send Messages
        ImageIcon iconMessage = new ImageIcon("./insatact/src/main/java/com/insatact/GUI/Pictures/message.png");
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

        connectedPanel = new JPanel();
        connectedPanel.setLayout(new BoxLayout(connectedPanel, BoxLayout.Y_AXIS));
        connectedPanel.setBackground(Color.decode("#2F2F2F"));
        JLabel lconnecte = new JLabel("Liste des personnes connectées");
        lconnecte.setForeground(Color.WHITE);
        lconnecte.setFont(new Font("Serif", Font.PLAIN, 20));
        connectedPanel.add(lconnecte);
        add(connectedPanel, BorderLayout.EAST);

        //Messages zone
        JPanel contentPane = new JPanel(new BorderLayout());
        messagePanel = new JPanel();
        gl = new GridBagLayout();
        messagePanel.setLayout(gl);
        messagePanel.setAutoscrolls(true);
        add(messagePanel, BorderLayout.CENTER);

        scrollPane = new JScrollPane(messagePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //scrollPane.setPreferredSize(new Dimension(20,10));
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

                            Thread.sleep(2000);//TODO attention, ce thread empeche la fermeture

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


    private void disconnect(){//TODO finir
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
        for(Conversation convtobackup:mapConvos.values()){//TODO attention user null
            controlCHAT.getComtoBDD().archiverConv(convtobackup);
            //le correspondant aussi archive quand on se déconnecte, mais la bdd ne prend que si on est l'expéditeur
            //donc TODO éviter la redondance dans tout ça
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
        return sms;
    }

    //Create conversation
    //!différent de la lancer, on ne la lance que si on write ou reçoit un message
    private void createConversation(String pseudodest){
        //!!nom trompeur, la conversation vide est créée et mise dans la hashmap dès qu'un nnouvel user apparait
        
        mapConvos.get(correspondant).load(this);//récupération des messages(pour l'instant ne fait rien, déconnecté)
        System.out.println("Récup Historique "+correspondant.getPseudo()+" réussie");
        mapConvos.get(correspondant).launchTCP();
        System.out.println("Création TCP "+correspondant.getPseudo());
        
        SwingUtilities.updateComponentTreeUI(messagePanel);
    }

    private void displayConversation(String unpseudo){
        messagePanel.removeAll();
        SwingUtilities.updateComponentTreeUI(messagePanel);
        System.out.println("Destinataire: "  + unpseudo);
        correspondant=getUserByPseudo(unpseudo);
        Conversation ConvoActive=mapConvos.get(correspondant);

        if(!ConvoActive.gethasTCP()){
            createConversation(unpseudo);
        }
        for(Message smstodisplay:ConvoActive.getMessageList()){
            displayMessage(smstodisplay);
        }

    }
    
    public void updatePseudo(String oldpseudo, String neopseudo, boolean fromtimer){
        //TODO !!! comment marche le passage du client TCP??
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
                displayMessage(message1);
                mapConvos.get(correspondant).addMessage(message1);
                textSenderZone.setText("");
            }
        } 
    }

    //Receive messages from another user
    public void receiveMessage(String t, String clientIPAddress){
        Message message2=stockMessage(t, clientIPAddress);
        displayMessage(message2);
    }

    //plus pratique, notamment pourchanger de conversation affichée
    private void displayMessage(Message sms){
        numberMessage++;
        boolean sentbyMe=sms.getSender().equals(controllerInstantMessaging.getmyID());
        //TODO fix le issender au désarchivage, voire supression
        String colorSMS;
        if(!sentbyMe){
            //numberLine = numberMessage % MAX_MESS;
            //c.fill = GridBagConstraints.HORIZONTAL;
            colorSMS="#7F7FBF";
        }else{
            numberLine = numberMessage % MAX_MESS;
            c.fill = GridBagConstraints.VERTICAL;
            colorSMS="#7FBF7F";
        }

        JPanel Messhorodatage = new JPanel();
        Messhorodatage.setLayout(new BorderLayout());
        JPanel Mess = new JPanel();
        LocalDateTime mdate = sms.getHorodata();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        String formattedDate = mdate.format(myFormatObj);
        JLabel messageLab = new JLabel(formattedDate);

        Mess.setBackground(Color.decode(colorSMS));
        Messhorodatage.setMinimumSize(new Dimension(750, 100));
        Messhorodatage.setPreferredSize(new Dimension(750, 100));
        Mess.add(sms);
        Messhorodatage.add(Mess, BorderLayout.CENTER);
        Messhorodatage.add(messageLab, BorderLayout.SOUTH);
        JPanel MBlanc = new JPanel();
        MBlanc.setMinimumSize(new Dimension(750, 100));
        MBlanc.setPreferredSize(new Dimension(750, 100));
        
        c.gridx = 0;
        c.gridy = numberMessage;
        if(!sentbyMe){
            messagePanel.add(Messhorodatage,c);
        }else{
            messagePanel.add(MBlanc,c);
        }
        c.gridx = 1;
        c.gridy = numberMessage;
        if(!sentbyMe){
            messagePanel.add(MBlanc,c);
        }else{
            messagePanel.add(Messhorodatage,c);
        }

        messageList.add(sms);
        SwingUtilities.updateComponentTreeUI(connectedPanel);
    }


    //Display connected users
    public void displayConnectedUsers(String newuser){

        String pseudo=newuser.split(" ")[1];
        while(connectedUsermutex){try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
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
                    if(!mapConvos.get(usrdisplayed).hasunreadsms() ){
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

        connectedUsermutex = false;
    }

    //Remove connected user
    public void removeConnectedUsers(usertype usertorm){

        while(connectedUsermutex){try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
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

                mapConvos.remove(usertorm);//TODO et si on reçoit un deconncted avant de le voir connected?
                SwingUtilities.updateComponentTreeUI(connectedPanel);
                break;
            }
        }
        
        connectedUsermutex = false;
    }

    public void updateConnectedList(String newPseudo, String oldPseudo){

        while(connectedUsermutex){try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }}

        connectedUsermutex = true;
        for(JButton b: connectedUsersList){
            if (b.getText().equals(oldPseudo)){//TODO TODO TODO gérer mapConvosla màj dans mapconvos!
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
            // TODO Auto-generated catch block
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
        //ne marchent probablement plus maintenant
        /*messagingGUI mGUI = new messagingGUI(3000,2000, "Thomas");
        mGUI.displayConnectedUsers("Tintin");
        mGUI.displayConnectedUsers("Milou");
        mGUI.displayConnectedUsers("Hadock");
        mGUI.displayConnectedUsers("Tournesol");
        UDPcontroller udp = new UDPcontroller(mGUI);
        Thread.sleep(5000);
        try {
            udp.udpbroadcastco("Tintin");
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Thread.sleep(5000);
        try {
            udp.udpbroadcastdeco("Tintin");
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(mGUI.connectedUsersList.size());
        */
    }

}