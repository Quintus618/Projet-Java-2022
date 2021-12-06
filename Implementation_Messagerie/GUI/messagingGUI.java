package GUI;
//Importation Libraries
import javax.swing.*;
import java.awt.*;
import java.lang.*;

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

    private String pseudo;
    private int numberLine = 0;
    private int numberMessage = 0;

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
        JTextArea textSenderZone = new JTextArea(2,80);
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
        JButton b = new JButton("Pseudodel'user");
        connectedPanel.add(b);
        add(connectedPanel, BorderLayout.EAST);

        //Messages zone
        messagePanel = new JPanel();
        messagePanel.setLayout(new GridLayout(numberLine,2));

        //FAIRE UN SCROLL PLUTOT
        receiveMessage();
        receiveMessage();
        receiveMessage();
        receiveMessage();
        receiveMessage();
        receiveMessage();
        receiveMessage();receiveMessage();
        receiveMessage();
        writeMessage();
        receiveMessage();
        writeMessage();
        receiveMessage();
        writeMessage();
        writeMessage();
        receiveMessage();
        receiveMessage();
        writeMessage();
        receiveMessage();

       /* SpringUtilities.makeCompactGrid(messagePanel,
        10, 2, //rows, cols
        6, 6,        //initX, initY
        6, 6);       //xPad, yPad */

        add(messagePanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void writeMessage(){
        numberMessage++;
        numberLine = numberMessage % MAX_MESS;
        c.fill = GridBagConstraints.HORIZONTAL;
        JPanel MEnv = new JPanel();
        JLabel message1 = new JLabel("Message envoyé");
        MEnv.setBackground(Color.decode("#7F7FBF"));
        MEnv.add(message1);
        JPanel MBlanc = new JPanel();
        c.gridx = numberLine;
        c.gridy = 0;
        messagePanel.add(MEnv,c);
        c.gridx = numberLine;
        c.gridy = 1;
        messagePanel.add(MBlanc,c);
    }

    private void receiveMessage(){
        numberMessage++;
        numberLine = numberMessage % MAX_MESS;
        c.fill = GridBagConstraints.HORIZONTAL;
        JPanel MRec = new JPanel();  
        JLabel message2 = new JLabel("Message reçu");
        MRec.setBackground(Color.decode("#7FBF7F"));
        MRec.add(message2);
        JPanel MBlanc = new JPanel();
        c.gridx = numberLine;
        c.gridy = 0;
        messagePanel.add(MBlanc,c);
        c.gridx = numberLine;
        c.gridy = 1;
        messagePanel.add(MRec,c);
    }
    public static void main(String[] Args){
        messagingGUI mGUI = new messagingGUI(3000,2000);
    }

}
