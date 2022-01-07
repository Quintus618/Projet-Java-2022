package GUI;
//Importation Libraries
import javax.swing.*;

import Instant_Messaging.*;
import Controller.*;
import java.awt.event.*;
import java.io.IOException;
import java.awt.*;
import java.lang.*;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
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
    private JPanel infoProfilPanel;
    private JPanel connectedPanel;
    private JPanel messagePanel;

    private JScrollPane scrollPane;

    private GridBagConstraints c = new GridBagConstraints();

    private JTextArea textSenderZone;

    private int numberLine = 0;
    private int numberMessage = 0;

    private GridBagLayout gl;

    private TCPcontrollerClient tcpClient;
    private TCPcontrollerServer tcpServer;

    protected JLabel lPseudo;

    public ArrayList<JLabel> messageList;
    public ArrayList<JButton> connectedUsersList;

    protected UDPcontroller udpController;


    private boolean connectedUsermutex = false;
    private Thread updateConnected;

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
        correspondant=new usertype("", "", null);
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
        ImageIcon iconMedia = new ImageIcon("./GUI/Pictures/media.png");
        mediaButton = new JButton(iconMedia);
        paramButton(mediaButton, 40, 40, iconMedia);

        //Add files
        ImageIcon iconFile = new ImageIcon("./GUI/Pictures/file.png");
        fileButton = new JButton(iconFile);
        fileButton.setIcon(resizeIcon(iconFile, 40, 40));
        paramButton(fileButton, 40, 40, iconFile);

        //Send Messages
        ImageIcon iconMessage = new ImageIcon("./GUI/Pictures/message.png");
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
        messagePanel = new JPanel();
        gl = new GridBagLayout();
        messagePanel.setLayout(gl);
        scrollPane = new JScrollPane(messagePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(20,10));
        add(messagePanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.WEST);

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
                        System.out.println("Test");
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
                                        m.createConversation(b.getText());
                                    }
                                    else{
                                        System.out.println(buttonselected.getText() + "................................");
                                    }
                                }});
                
                                    
                                    
                         }
                        try {

                            Thread.sleep(2000);//TODO attention, ce thread empeche la fermeture

                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            });  

            receiveMessage("Message recu");
            receiveMessage("Message recu");
            updateConnected.start();

            //Creation UDPcontroller
            udpController = new UDPcontroller(this);

            //Action when there is a mouse click on a button
            sendMessageButton.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent MOUSE_CLICKED){ 
                    writeMessage(textSenderZone.getText());
                    //tcpClient.sendMessage(textSenderZone.getText());
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
                    //tcpClient.sendMessage(textSenderZone.getText());
                }
            });

        //Creation TCP server
        //tcpServer = new TCPcontrollerServer();//usertype getIPaddr
        //Conversation.launch()
        
        setVisible(true);
    }



    private void disconnect(){//TODO finir
        try {
            udpController.interrupt();
            udpController.udpbroadcastdeco(controlCHAT.getMyIdentity().toString()) ;
        } catch (SocketException e1) {
            e1.printStackTrace();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }

        while(updateConnected.isAlive()){
            updateConnected.interrupt();
        }
        
        backupBDD(); 
        dispose();
    }

//TODO faudrait-il fusionner la liste des users du controller et la hashmap des conversations?
//aussi, sera-t' ilutile d'avoir des numéros de port? Parce qu'alors ils pourraient servir à la hashmap
//au lieu de l'ID (qui est déjà dans la conv).
    public void newUser(usertype corresp){
        controlCHAT.addUser(corresp);
        mapConvos.put(corresp, new Conversation(corresp));
    }

    private void backupBDD(){
        for(Conversation convtobackup:mapConvos.values()){
            controlCHAT.getComtoBDD().archiverConv(convtobackup);
        }
    }


    //Create conversation
    //!différent de la lancer, on ne la lance que si on write ou reçoit un message
    private void createConversation(String dest){
        messagePanel.removeAll();
        correspondant=controlCHAT.getUserByPseudo(dest);
        mapConvos.get(correspondant).load(this);
        //Creation TCP client->donc non, pas ici; conversation.launch
        //tcpClient = new TCPcontrollerClient(,);
        mapConvos.put(correspondant, new Conversation(correspondant));
        mapConvos.get(correspondant).load(this);//récupération des messages
        
        SwingUtilities.updateComponentTreeUI(this);
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
                numberMessage++;
                //numberLine = numberMessage % MAX_MESS;
                //c.fill = GridBagConstraints.HORIZONTAL;
                //some identical code at receiveMessage, create a createMessage function?
                JPanel MEnvhorodatage = new JPanel();
                MEnvhorodatage.setLayout(new BorderLayout());
                JPanel MEnv = new JPanel();
                Message message1 = new Message(t, correspondant.getId(),true);
                //String mdate = message1.getHorodata().toString();

                LocalDateTime mdate = message1.getHorodata();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
                String formattedDate = mdate.format(myFormatObj);
                JLabel messageLab = new JLabel(formattedDate);

                MEnv.setBackground(Color.decode("#7F7FBF"));
                MEnvhorodatage.setMinimumSize(new Dimension(750, 100));
                MEnvhorodatage.setPreferredSize(new Dimension(750, 100));
                MEnv.add(message1);
                MEnvhorodatage.add(MEnv, BorderLayout.CENTER);
                MEnvhorodatage.add(messageLab, BorderLayout.SOUTH);
                JPanel MBlanc = new JPanel();
                MBlanc.setMinimumSize(new Dimension(750, 100));
                MBlanc.setPreferredSize(new Dimension(750, 100));
                c.gridx = 0;
                c.gridy = numberMessage;
                messagePanel.add(MEnvhorodatage,c);
                c.gridx = 1;
                c.gridy = numberMessage;
                messagePanel.add(MBlanc,c);
                textSenderZone.setText("");
                messageList.add(message1);//TODO conversation?
                SwingUtilities.updateComponentTreeUI(this);
            } 
        } 
    }

    //Receive messages from another user
    public void receiveMessage(String t){
        numberMessage++;
        numberLine = numberMessage % MAX_MESS;
        c.fill = GridBagConstraints.VERTICAL;
        JPanel MRecHoradate = new JPanel();
        MRecHoradate.setLayout(new BorderLayout());
        JPanel MRec = new JPanel();  

        //Creation of the message
        Message message2 = new Message(t, correspondant.getId(), false);
        LocalDateTime mdate = message2.getHorodata();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        String formattedDate = mdate.format(myFormatObj);
        JLabel messageLab = new JLabel(formattedDate);
        JLabel MRecLab = new JLabel(formattedDate);

        MRec.setBackground(Color.decode("#7FBF7F"));
        MRecHoradate.setMinimumSize(new Dimension(750, 100));
        MRecHoradate.setPreferredSize(new Dimension(750, 100));
        MRec.add(message2);
        MRecHoradate.add(MRec, BorderLayout.CENTER);
        MRecHoradate.add(MRecLab, BorderLayout.SOUTH);
        JPanel MBlanc = new JPanel();
        MBlanc.setMinimumSize(new Dimension(750, 100));
        MBlanc.setPreferredSize(new Dimension(750, 100));
        c.gridx = 0;
        c.gridy = numberMessage;
        messagePanel.add(MBlanc,c);
        c.gridx = 1;
        c.gridy = numberMessage;
        messagePanel.add(MRecHoradate,c);
        messageList.add(message2);
        SwingUtilities.updateComponentTreeUI(this);
    }

    //Display connected users
    public void displayConnectedUsers(String pseudo){

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
            }
        }

        if(!trouve){
            connectedPanel.add(coUsers);
            connectedUsersList.add(coUsers);
        }

        SwingUtilities.updateComponentTreeUI(this);

        connectedUsermutex = false;
    }

    //Remove connected user
    public void removeConnectedUsers(String pseudo){

        while(connectedUsermutex){try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }}
        connectedUsermutex = true;

        String[] ps = pseudo.split(" ");
        System.out.println(ps[0]);
        for(JButton i : connectedUsersList){
            if (i.getText().equals(ps[0])){
                connectedPanel.remove(i);
                connectedUsersList.remove(i);
                SwingUtilities.updateComponentTreeUI(this);
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
            if (b.getText().equals(oldPseudo)){
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

    //en rab du cahier des charges
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
