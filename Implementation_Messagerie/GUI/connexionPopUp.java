package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class connexionPopUp extends JFrame{
    
    private JPanel connexionPanel;
    private JTextField coidText;
    private JTextField comdpText;
    private JTextField pseudoText;

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


        JLabel coID = new JLabel("ID: ", JLabel.TRAILING);
        JLabel coMdp = new JLabel("Mot de passe: ", JLabel.TRAILING);
        coidText = new JTextField(10);
        comdpText = new JTextField(10);
        coMdp.setLabelFor(comdpText);
        coID.setLabelFor(coidText);
        connexionPanel.add(coID);
        connexionPanel.add(coidText);
        connexionPanel.add(coMdp);
        connexionPanel.add(comdpText);
        add(connexionPanel);

        JLabel jl = new JLabel("Pseudo: ");
        pseudoText = new JTextField(10);
        jl.setLabelFor(pseudoText);
        connexionPanel.add(jl);
        connexionPanel.add(pseudoText);
        add(connexionPanel);

        SpringUtilities.makeCompactGrid(connexionPanel,
        3, 2, //rows, cols
        6, 6,        //initX, initY
        6, 6);       //xPad, yPad

        JButton connexion = new JButton("Connexion");
        add(connexion, BorderLayout.SOUTH);

        //Action broadcasting the pseudo on the network
        connexion.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent MOUSE_CLICKED){ // INFO TO BROADCAST IN UDP
            }});


        setVisible(true);

    }

    //GETTER / SETTER
    public JTextField getPseudoText() {
        return pseudoText;
    }

    public JTextField getCoidText() {
        return coidText;
    }

    public JTextField getComdpText() {
        return comdpText;
    }

    public static void main(String[] Args){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                connexionPopUp cPP = new connexionPopUp(200, 500);
            }
        });
    }
}
