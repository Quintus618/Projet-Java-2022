package GUI;
//Importation Libraries
import javax.swing.*;
import java.awt.Image;
import java.awt.Color;

public class messagingGUI extends JFrame{

    private JButton deconnexionButton;
    private JButton mediaButton;
    private JButton fileButton;
    private JButton sendMessageButton;
    
    //Constructor
    public messagingGUI(int height, int width){
        
        //Creation of GUI
        super("Insatact");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add buttons
        deconnexionButton = new JButton("deconnexion");

        //Add Pictures, Audio
        ImageIcon iconMedia = new ImageIcon("./GUI/Pictures/media.png");
        mediaButton = new JButton(iconMedia);
        mediaButton.setIcon(resizeIcon(iconMedia, 100, 100));
        mediaButton.setBackground(new Color(255,255,255,0));
        mediaButton.setBorderPainted(false);
        mediaButton.setContentAreaFilled(false);
        mediaButton.setFocusPainted(false);


        //Add files
        ImageIcon iconFile = new ImageIcon("./GUI/Pictures/file.png");
        fileButton = new JButton(iconFile);
        fileButton.setIcon(resizeIcon(iconFile, 100, 100));
        fileButton.setBackground(new Color(255,255,255,0));
        fileButton.setBorderPainted(false);
        fileButton.setContentAreaFilled(false);
        fileButton.setFocusPainted(false);

        //Send Messages
        ImageIcon iconMessage = new ImageIcon("./GUI/Pictures/message.png");
        sendMessageButton = new JButton(iconMessage);
        sendMessageButton.setIcon(resizeIcon(iconMessage, 100, 100));
        sendMessageButton.setBackground(new Color(255,255,255,0));
        sendMessageButton.setBorderPainted(false);
        sendMessageButton.setContentAreaFilled(false);
        sendMessageButton.setFocusPainted(false);

        //Texte Input zone to send messages
        JTextArea textSenderZone = new JTextArea("Message à saisir"); 

        JPanel panel = new JPanel();
        panel.add(deconnexionButton);
        panel.add(mediaButton);
        panel.add(fileButton);
        panel.add(sendMessageButton);
        panel.add(textSenderZone);
        add(panel);
        setVisible(true);
    }

    //Redimensionner une icone
    private static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
        Image img = icon.getImage();  
        Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);  
        return new ImageIcon(resizedImage);
    }
    public static void main(String[] Args){
        messagingGUI mGUI = new messagingGUI(3000,2000);
    }

}
