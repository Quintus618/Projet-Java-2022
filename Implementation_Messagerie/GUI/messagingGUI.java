package GUI;
//Importation Libraries
import javax.swing.*;

import Instant_Messaging.Conversation;
import Instant_Messaging.Message;
import Controller.*;
import java.awt.event.*;
import java.io.IOException;
import java.awt.*;
import java.lang.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class messagingGUI extends JFrame{

    //CONSTANTE
    private int MAX_MESS = 10;

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

    private String pseudo;
    private int numberLine = 0;
    private int numberMessage = 0;

    private GridBagLayout gl;

    private TCPcontrollerClient tcpClient;
    private TCPcontrollerServer tcpServer;

    public ArrayList<JLabel> messageList;
    public ArrayList<JButton> connectedUsersList;

    //Constructor
    public messagingGUI(int height, int width, String pseudo){
        
        //Creation of GUI
        super("Insatact");
        setSize(width, height);
        this.setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pseudo = pseudo;

        //Creation of graphical components 
        buildComponentInterface(this);

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
        JLabel lPseudo = new JLabel(pseudo);

        chatPanel = new JPanel();
        chatPanel.setBackground(Color.gray);
        chatPanel.add(mediaButton);
        chatPanel.add(fileButton);
        chatPanel.add(textSenderZone);
        chatPanel.add(sendMessageButton);
        chatPanel.add(changePeudo);
        chatPanel.add(deconnexionButton);
        chatPanel.add(lPseudo);
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
            Thread updateConnected = new Thread(new Runnable() {

                int nbfois = 0;
                JButton buttonselected;

                @Override
                public void run(){
                    while(true){
            
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
                                        System.out.println("testeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
                                    }
                                    else{
                                        System.out.println(buttonselected.getText() + "................................");
                                    }
                                }});
                
                                    
                                    
                         }
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                }
            });  

            receiveMessage("Message recu");
            receiveMessage("Message recu");
            updateConnected.start();

            //Creation UDPcontroller
            UDPcontroller udpController = new UDPcontroller(this);

            //Action when there is a mouse click on a button
            sendMessageButton.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent MOUSE_CLICKED){ 
                    writeMessage(textSenderZone.getText());
                    //tcpClient.sendMessage(textSenderZone.getText());
                }});
            deconnexionButton.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent e){ try {
                    udpController.udpbroadcastdeco(pseudo) ;
                } catch (SocketException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (UnknownHostException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } dispose();}});
    
        //Creation TCP server
        tcpServer = new TCPcontrollerServer(this.pseudo);
        
        setVisible(true);

    }

    //Create conversation
    private void createConversation(String dest){
        messagePanel.removeAll();

        
        //Creation TCP client
        //tcpClient = new TCPcontrollerClient(,);

        //Instant_Messaging.Conversation?

        SwingUtilities.updateComponentTreeUI(this);
    }

    //Send a message to another user
    private void writeMessage(String t){
        if(t.length()>=4096){
            JOptionPane.showMessageDialog(null, "Les messages sont limités à 4096 caractères.");
        }//attention si l'on modifie la valeur max dans les tables
        else{  
            if (!t.isBlank()){
                numberMessage++;
                //numberLine = numberMessage % MAX_MESS;
                //c.fill = GridBagConstraints.HORIZONTAL;
                //some identical code at receiveMessage, create a createMessage function?
                JPanel MEnvhorodatage = new JPanel();
                MEnvhorodatage.setLayout(new BorderLayout());
                JPanel MEnv = new JPanel();
                Message message1 = new Message(t, "ERROR_ERROR_ERROR",true);
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
                messageList.add(message1);
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
        //TODO PAS LE BON ARGUMENT (idem à l'envoi de message)(j'ai l'impression qu'on ne s'occupe pas de l'ID du correspondant, ça va demander de se pencher sur CreationConversation bientôt je sens)
        Message message2 = new Message(t, "ERROR_ERROR_ERROR", false);
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
        JButton coUsers = new JButton(pseudo);
        coUsers.setForeground(Color.WHITE);
        coUsers.setMaximumSize(new Dimension(Integer.MAX_VALUE, coUsers.getMinimumSize().height));
        coUsers.setBackground(Color.decode("#2F2F2F"));
        //coUsers.setBorderPainted(false);
        //coUsers.setContentAreaFilled(false);
        //coUsers.setFocusPainted(false);
        connectedPanel.add(coUsers);
        connectedUsersList.add(coUsers);
        SwingUtilities.updateComponentTreeUI(this);
    }

    //Remove connected user
    public void removeConnectedUsers(String pseudo){
        for(JButton i : connectedUsersList){
            if (i.getText().equals(pseudo)){
                connectedUsersList.remove(i);
                connectedPanel.remove(i);
            }
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
    

    public GridBagLayout getGl() {
        return gl;
    }

    public static void main(String[] Args) throws InterruptedException{
        messagingGUI mGUI = new messagingGUI(3000,2000, "Thomas");
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
    }

}
