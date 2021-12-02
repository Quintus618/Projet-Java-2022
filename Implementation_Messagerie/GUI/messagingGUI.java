package GUI;
//Importation Libraries
import javax.swing.*;

public class messagingGUI extends JFrame{

    private JButton deconnexionButton;
    private JButton mediaButton;
    private JButton fileButton;
    private JButton sendMessage;
    
    //Constructor
    public messagingGUI(int height, int width){
        
        //Creation of GUI
        super("Insatact");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add buttons
        deconnexionButton = new JButton("deconnexion");
        deconnexionButton.setSize(1000, 50);

        mediaButton = new JButton("media");
        fileButton = new JButton("file");
        sendMessage = new JButton("sendMessage");
        JPanel panel = new JPanel();
        panel.add(deconnexionButton);
        panel.add(mediaButton);
        panel.add(fileButton);
        panel.add(sendMessage);
        add(panel);
        setVisible(true);
    }

    public static void main(String[] Args){
        messagingGUI mGUI = new messagingGUI(3000,2000);
    }

}
