package GUI;
//Importation Libraries
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.lang.*;
import java.util.*;

public class messagingGUI extends JFrame{

    //CONSTANTE
    private int MAX_MESS = 10;

    //Buttons of the instant messaging
    private JButton deconnexionButton;
    private JButton mediaButton;
    private JButton fileButton;
    private JButton sendMessageButton;

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

        //Display Pseudo
        JLabel lPseudo = new JLabel("Pseudo Utilisateur");

        chatPanel = new JPanel();
        chatPanel.setBackground(Color.gray);
        chatPanel.add(mediaButton);
        chatPanel.add(fileButton);
        chatPanel.add(textSenderZone);
        chatPanel.add(sendMessageButton);
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
        add(scrollPane, BorderLayout.WEST);
        add(messagePanel, BorderLayout.CENTER);

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
                                    b.setBackground(Color.GREEN);}});
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
            updateConnected.start();
            sendMessageButton.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent MOUSE_CLICKED){ writeMessage(textSenderZone.getText());}});
        //actionButton();
        setVisible(true);

    }

    /*private void actionButton(){
        while(true){
            sendMessageButton.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent e){ writeMessage(textSenderZone.getText());}});
            System.out.println("toto");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }*/

    //Send a message to another user
    private void writeMessage(String t){
        numberMessage++;
        //numberLine = numberMessage % MAX_MESS;
        //c.fill = GridBagConstraints.HORIZONTAL;
        JPanel MEnv = new JPanel();
        JLabel message1 = new JLabel(t);
        MEnv.setBackground(Color.decode("#7F7FBF"));
        MEnv.setPreferredSize(new Dimension(750, 100));
        MEnv.add(message1);
        JPanel MBlanc = new JPanel();
        MBlanc.setPreferredSize(new Dimension(750, 100));
        c.gridx = 0;
        c.gridy = numberMessage;
        messagePanel.add(MEnv,c);
        c.gridx = 1;
        c.gridy = numberMessage;
        messagePanel.add(MBlanc,c);
        textSenderZone.setText("");
        messageList.add(message1);
        SwingUtilities.updateComponentTreeUI(this);
    }

    //Receive messages from another user
    public void receiveMessage(){
        numberMessage++;
        numberLine = numberMessage % MAX_MESS;
        c.fill = GridBagConstraints.VERTICAL;
        JPanel MRec = new JPanel();  
        JLabel message2 = new JLabel("Message reçu");
        MRec.setBackground(Color.decode("#7FBF7F"));
        MRec.setPreferredSize(new Dimension(750, 100));
        MRec.add(message2);
        JPanel MBlanc = new JPanel();
        MBlanc.setPreferredSize(new Dimension(750, 100));
        c.gridx = 0;
        c.gridy = numberMessage;
        messagePanel.add(MBlanc,c);
        c.gridx = 1;
        c.gridy = numberMessage;
        messagePanel.add(MRec,c);
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
    public static void main(String[] Args) throws InterruptedException{
        messagingGUI mGUI = new messagingGUI(3000,2000);
        mGUI.displayConnectedUsers("Tintin");
        mGUI.displayConnectedUsers("Milou");
        System.out.println(mGUI.connectedUsersList.size());
    }

}
