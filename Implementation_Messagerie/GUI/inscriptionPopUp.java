package GUI;

import javax.swing.*;
import javax.swing.*;
import java.awt.*;
import java.lang.*;

public class inscriptionPopUp extends JFrame {
    
    JPanel inscriptionPanel;


    public inscriptionPopUp(int height, int width){
        
        //Creation of GUI
        super("Inscription");
        setSize(width, height);
        this.setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Creation panel
        inscriptionPanel = new JPanel();
        inscriptionPanel.setLayout(new GridLayout(2,2));
        
        //Creation des labels
        JLabel ID = new JLabel("ID: ");
        JLabel Mdp = new JLabel("Mot de passe: ");
        JTextField idText = new JTextField();
        JTextField mdpText = new JTextField();
        inscriptionPanel.add(ID);
        inscriptionPanel.add(idText);
        inscriptionPanel.add(Mdp);
        inscriptionPanel.add(mdpText);
        add(inscriptionPanel);
        setVisible(true);
    }

    public static void main(String[] Args){
        inscriptionPopUp iPP = new inscriptionPopUp(300, 700);
    }
}
