package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class changePseudoPopUp extends JFrame{
    
    private JPanel connexionPanel;
    private JTextField pseudoText;

    //Constructor
    public changePseudoPopUp(messagingGUI mGUI, int height, int width){

        //Creation of GUI
        super("Pseudo modification");
        setSize(width, height);
        this.setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Creation panel
        connexionPanel = new JPanel();
        connexionPanel.setLayout(new SpringLayout());

        JLabel jl = new JLabel("Pseudo: ");
        pseudoText = new JTextField(10);
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

        //Action broadcasting the pseudo on the network
        connexion.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent MOUSE_CLICKED){ 
                mGUI.setPseudo(pseudoText.getText());
                dispose();
            }});


        setVisible(true);

    }

    //GETTER / SETTER
    public JTextField getPseudoText() {
        return pseudoText;
    }

    public static void main(String[] Args){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //changePseudoPopUp cPP = new changePseudoPopUp(100, 500);
            }
        });
    }
}
