package GUI;
//Importation Libraries
import javax.swing.*;

import Instant_Messaging.Message;

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

    private GridBagConstraints c = new GridBagConstraints();

    private JTextArea textSenderZone;

    private String pseudo;
    private int numberLine = 0;
    private int numberMessage = 0;

    private GridBagLayout gl;

    public ArrayList<JLabel> messageList;
    public ArrayList<JButton> connectedUsersList;

    //Constructor
    public messagingGUI(int height, int width){
        
        //Creation of GUI
        super("Insatact");
        setSize(width, height);
        this.setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
        pseudo = "Tintin";
        //Display Pseudo
        JLabel lPseudo = new JLabel("Pseudo Utilisateur");

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
        JScrollPane scrollPane = new JScrollPane(messagePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(20,10));
        add(messagePanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.WEST);
       

        //Initialisation des listes
        messageList = new ArrayList<JLabel>();
        connectedUsersList = new ArrayList<JButton>();

        //ACTION BOUTTON
        /*sendMessageButton.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){ writeMessage(textSenderZone.getText());}});*/
            Thread updateConnected = new Thread(new Runnable() {
                @Override
                public void run(){
                    while(true){
                        for (JButton b : connectedUsersList){
                            b.addActionListener(new ActionListener(){  
                                public void actionPerformed(ActionEvent e){ 
                                    for(JButton c : connectedUsersList){
                                        c.setBackground(Color.decode("#2F2F2F"));
                                    }
                                    b.setBackground(Color.decode("#08410f"));}});
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

            receiveMessage();
            receiveMessage();
            updateConnected.start();

            Thread receiveBroadcast = new Thread(new Runnable(){
                @Override
                public void run(){
                    DatagramSocket soc;
                    try {

                        soc = new DatagramSocket(7000, InetAddress.getByName("0.0.0.0"));
                        soc.setBroadcast(true);

                        while(true){
                            byte[] bufrecep = new byte[10000];
                            DatagramPacket packet = new DatagramPacket(bufrecep, bufrecep.length);

                            try {
                                soc.receive(packet);
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
    
                            String message = new String(packet.getData()).trim();

                            String[] messagesplit = message.split(":");
                            String[] messages = new String[2];
                            int index = 0;
                            for (String m : messagesplit){
                                messages[index] = m;
                                index++;
                            }
                            if(messages[0].equals("USERCONNECTED")){
                                if (messages[1].equals(pseudo)){
                                   udpbroadcastChangePseudo(packet);
                                }
                                else {
                                    displayConnectedUsers(messages[1]);
                                }
                            }
                            else if (messages[0].equals("USERDISCONNECTED")){
                                removeConnectedUsers("Milou");
                            }
                            else if (messages[0].equals("CHANGEPSEUDO")){
                                //System.out.println("Tata");
                            }
                        }
                    } catch (SocketException | UnknownHostException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });

            receiveBroadcast.start();
            sendMessageButton.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent MOUSE_CLICKED){ writeMessage(textSenderZone.getText());}});
            deconnexionButton.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent e){ try {
                    udpbroadcastdeco() ;
                } catch (SocketException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (UnknownHostException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } dispose();}});
        //actionButton();
        setVisible(true);

    }

    //Send a message to another user
    private void writeMessage(String t){
        if (!t.isBlank()){
            numberMessage++;
            //numberLine = numberMessage % MAX_MESS;
            //c.fill = GridBagConstraints.HORIZONTAL;
            //some identical code at receiveMessage, create a createMessage function?
            JPanel MEnvhorodatage = new JPanel();
            MEnvhorodatage.setLayout(new BorderLayout());
            JPanel MEnv = new JPanel();
            Message message1 = new Message(t,true);
            String mdate = message1.getHorodata().toString();
            JLabel messageLab = new JLabel(mdate);
            MEnv.setBackground(Color.decode("#7F7FBF"));
            MEnvhorodatage.setPreferredSize(new Dimension(750, 100));
            MEnv.add(message1);
            MEnvhorodatage.add(MEnv, BorderLayout.CENTER);
            MEnvhorodatage.add(messageLab, BorderLayout.SOUTH);
            JPanel MBlanc = new JPanel();
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

    //Receive messages from another user
    public void receiveMessage(){
        numberMessage++;
        numberLine = numberMessage % MAX_MESS;
        c.fill = GridBagConstraints.VERTICAL;
        JPanel MRecHoradate = new JPanel();
        MRecHoradate.setLayout(new BorderLayout());
        JPanel MRec = new JPanel();  

        //Creation of the message
        Message message2 = new Message("Message reçu",false);
        String mdate = message2.getHorodata().toString();
        JLabel MRecLab = new JLabel(mdate);

        MRec.setBackground(Color.decode("#7FBF7F"));
        MRecHoradate.setPreferredSize(new Dimension(750, 100));
        MRec.add(message2);
        MRecHoradate.add(MRec, BorderLayout.CENTER);
        MRecHoradate.add(MRecLab, BorderLayout.SOUTH);
        JPanel MBlanc = new JPanel();
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

    //broadcastUDP to notify connexion
    public void udpbroadcastco() throws SocketException, UnknownHostException{
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);

        // Broadcast the message over all the network interfaces
        Enumeration interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {

            NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
    

            if (/*networkInterface.isLoopback() ||*/ !networkInterface.isUp()) {
                continue; 
            }

            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast == null) {
                    continue;
                }
                
                try {
                    //Send a message to show that we are connected
                    String coPseudo = "USERCONNECTED" + ":Tintin";
                    byte[] sendconnexion = coPseudo.getBytes();
                    DatagramPacket sendpaqconnexion = new DatagramPacket(sendconnexion, sendconnexion.length, broadcast,7000);
                    socket.send(sendpaqconnexion);
                }
                catch (Exception e){}

            }
        }

    }

    //broadcastUDP to notify connexion
    public void udpbroadcastdeco() throws SocketException, UnknownHostException{
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
    
        // Broadcast the message over all the network interfaces
        Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
    
        while (interfaces.hasMoreElements()) {
    
            NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
        
    
            if (/*networkInterface.isLoopback() ||*/ !networkInterface.isUp()) {
                continue; 
            }
    
            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast == null) {
                    continue;
                }
                    
                try {
                    //Send a message to show that we are connected
                    String coPseudo = "USERDISCONNECTED" + ":Tintin";
                    byte[] sendconnexion = coPseudo.getBytes();
                    DatagramPacket sendpaqconnexion = new DatagramPacket(sendconnexion, sendconnexion.length, broadcast,7000);
                    socket.send(sendpaqconnexion);
                }
                catch (Exception e){}
    
            }
        }
    
    }

        //broadcastUDP to notify connexion
        public void udpbroadcastChangePseudo(DatagramPacket packet) throws SocketException, UnknownHostException{
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
        
            // Broadcast the message over all the network interfaces
            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
        
            while (interfaces.hasMoreElements()) {
        
                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
            
        
                if (/*networkInterface.isLoopback() ||*/ !networkInterface.isUp()) {
                    continue; 
                }
        
                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null) {
                        continue;
                    }
                        
                    try {
                        //Send a message to show that we are connected
                        byte[] sendurg = "CHANGEPSEUDO".getBytes();
                        DatagramPacket sendpaqconnexion = new DatagramPacket(sendurg, sendurg.length, packet.getAddress(),7000);
                        socket.send(sendpaqconnexion);
                    }
                    catch (Exception e){}
        
                }
            }
        
        }
    public static void main(String[] Args) throws InterruptedException{
        messagingGUI mGUI = new messagingGUI(3000,2000);
        mGUI.displayConnectedUsers("Tintin");
        mGUI.displayConnectedUsers("Milou");
        Thread.sleep(5000);
        try {
            mGUI.udpbroadcastco();
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Thread.sleep(5000);
        try {
            mGUI.udpbroadcastdeco();
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
