package GUI;

import javax.swing.*;
import java.awt.*;

public class connexionPopUp extends JFrame{
    
    private JPanel connexionPanel;

    //Constructor
    public connexionPopUp(int height, int width){

        //Creation of GUI
        super("Connexion");
        setSize(width, height);
        this.setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Creation panel
        connexionPanel = new JPanel();
        connexionPanel.setLayout(new SpringLayout());

        JLabel jl = new JLabel("Pseudo: ");
        JTextField pseudoText = new JTextField(10);
        jl.setLabelFor(pseudoText);
        connexionPanel.add(jl);
        connexionPanel.add(pseudoText);
        add(connexionPanel);

        SpringUtilities.makeCompactGrid(connexionPanel,
        1, 2, //rows, cols
        6, 6,        //initX, initY
        6, 6);       //xPad, yPad

        JButton connexion = new JButton("Connexion");
        add(connexion, BorderLayout.SOUTH);

        setVisible(true);

    }

    public static void main(String[] Args){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                connexionPopUp cPP = new connexionPopUp(100, 500);
            }
        });
    }
}
