package com.insatact.GUI;

import javax.swing.*;

import com.insatact.Controller.*;

import java.awt.*;
import java.awt.event.*;

public class connexionPopUp extends JFrame{
    
    private JPanel connexionPanel;
    private JTextField coidText;
    private JTextField comdpText;
    private JTextField pseudoText;

    private void notwannaconnect(){
        new controllerInstantMessaging();
        dispose();
    }

    //Constructor
    public connexionPopUp(controllerInstantMessaging controlCHAT, int height, int width){

        //Creation of GUI
        super("Connexion");
        setSize(width, height);
        setResizable(false);
        this.setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //ceci permet d'utiliser notwannaconnect() à la fermeture
        addWindowListener(new WindowListener(){  
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                notwannaconnect();//important
            }

            @Override
            public void windowDeactivated(WindowEvent e) {                
            }

            @Override
            public void windowDeiconified(WindowEvent e) {                
            }

            @Override
            public void windowIconified(WindowEvent e) {                
            }

            @Override
            public void windowOpened(WindowEvent e) {                
            }
            });

        //Creation panel
        connexionPanel = new JPanel();
        connexionPanel.setLayout(new SpringLayout());


        JLabel coID = new JLabel("Identifiant : ", JLabel.TRAILING);
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

        JLabel jl = new JLabel("Pseudo: ", JLabel.TRAILING);
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
            public void actionPerformed(ActionEvent MOUSE_CLICKED){ 
                String connectid=coidText.getText();
                String connectmdp=comdpText.getText();
                String connectpseudo=pseudoText.getText();

                if(!controlCHAT.getComtoBDD().idtaken(connectid)){
                    JOptionPane.showMessageDialog(null, "Cet ID ne correspond à aucun utilisateur.");
                }else if(connectid.contains(" ")||connectpseudo.contains(" ")){
                    JOptionPane.showMessageDialog(null, "Les espaces sont interdits!");                    
                }else if(connectpseudo.length()>24||connectpseudo.length()<1){
                    JOptionPane.showMessageDialog(null, "La taille du pseudo doit être entre 1 et 24 caractères ("+connectpseudo.length()+" ici).");
                }else{

                    if(connectmdp.equals(controlCHAT.getComtoBDD().getMDP(connectid))){
                        controlCHAT.setmyID(connectid);
                        controlCHAT.setmyPseudo(connectpseudo);
                        new messagingGUI(controlCHAT, 3000,2000, connectpseudo);
                        dispose();
                    }else{
                        JOptionPane.showMessageDialog(null, "Mot de passe incorrect.");
                    }
                    
                } 
            }
        }
        );


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

}
