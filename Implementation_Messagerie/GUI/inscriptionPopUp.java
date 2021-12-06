package GUI;

import javax.swing.*;
import javax.swing.SpringLayout;
import java.awt.*;
import java.lang.*;

public class inscriptionPopUp extends JFrame {
    
    private JPanel inscriptionPanel;


    public inscriptionPopUp(int height, int width){
        
        //Creation of GUI
        super("Inscription");
        setSize(width, height);
        this.setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Creation panel
        inscriptionPanel = new JPanel();
        inscriptionPanel.setLayout(new SpringLayout());
        
        //Creation des labels
        JLabel ID = new JLabel("ID: ", JLabel.TRAILING);
        JLabel Mdp = new JLabel("Mot de passe: ", JLabel.TRAILING);
        JTextField idText = new JTextField(10);
        JTextField mdpText = new JTextField(10);
        Mdp.setLabelFor(mdpText);
        ID.setLabelFor(idText);
        inscriptionPanel.add(ID);
        inscriptionPanel.add(idText);
        inscriptionPanel.add(Mdp);
        inscriptionPanel.add(mdpText);
        add(inscriptionPanel);

        SpringUtilities.makeCompactGrid(inscriptionPanel,
                                        2, 2, //rows, cols
                                        6, 6,        //initX, initY
                                        6, 6);       //xPad, yPad

        JButton incr = new JButton("Inscription");
        add(incr, BorderLayout.SOUTH);
        setVisible(true);
    }


    public static void main(String[] Args){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                inscriptionPopUp iPP = new inscriptionPopUp(150, 700);
            }
        });
    }
}
