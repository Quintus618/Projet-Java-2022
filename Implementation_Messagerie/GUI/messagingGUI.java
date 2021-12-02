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
        setVisible(true);

        //Add buttons
        deconnexionButton = new JButton("deconnexion");
        mediaButton = new JButton("media");
        fileButton = new JButton("file");
        sendMessage = new JButton("sendMessage");
    }

    public static void main(String[] Args){
        messagingGUI mGUI = new messagingGUI(3000,2000);
    }

}
