package GUI;

import javax.swing.*;
import javax.swing.SpringLayout;

import Controller.*;

import java.awt.*;
import java.awt.event.*;

public class inscriptionPopUp extends JFrame {
    
    private JPanel inscriptionPanel;

    private JTextField idText;
    private JTextField mdpText;

    public inscriptionPopUp(controllerInstantMessaging controlCHAT, int height, int width){
        
        //Creation of GUI
        super("Inscription");
        setSize(width, height);
        this.setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//permet de ne pas tuer welcomGUI quand fermée

        //Creation panel
        inscriptionPanel = new JPanel();
        inscriptionPanel.setLayout(new SpringLayout());
        
        //Creation des labels
        JLabel ID = new JLabel("ID: ", JLabel.TRAILING);
        JLabel Mdp = new JLabel("Mot de passe: ", JLabel.TRAILING);
        idText = new JTextField(10);
        mdpText = new JTextField(10);
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

        //Action connecting to the DB
        incr.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent MOUSE_CLICKED){ // INFO A ENVOYER DANS LA BDD
                String wantedid=idText.getText();
                String wantedmdp=mdpText.getText();
                if(wantedid.length()>0 && wantedmdp.length()>3){
                    if(wantedid.contains(" ")||wantedmdp.contains(" ")){
                        JOptionPane.showMessageDialog(null, "Les espaces sont interdits.");
                    }else if(controlCHAT.getComtoBDD().addUser(wantedid,wantedmdp)){
                        JOptionPane.showMessageDialog(null, "Succès de l'inscription; bienvenue "+wantedid+"!");
                        dispose();
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Merci de choisir un identifiant ainsi qu'un mot de passe d'au moins quatre caractères.");
                }
            }});

        setVisible(true);
    }




    // ZONE GETTER / SETTER
    public JTextField getIdText() {
        return idText;
    }

    public JTextField getMdpText() {
        return mdpText;
    }


    //à supprimer?
    public static void main(String[] Args){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //inscriptionPopUp iPP = new inscriptionPopUp(comtoBDD, 150, 700);
            }
        });
    }
}
