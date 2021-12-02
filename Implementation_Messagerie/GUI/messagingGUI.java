package GUI;
//Importation Libraries
import javax.swing.*;

public class messagingGUI extends JFrame{
    
    //Constructor
    public messagingGUI(int height, int width){
        super("Insatact");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] Args){
        messagingGUI mGUI = new messagingGUI(3000,2000);
    }

}
