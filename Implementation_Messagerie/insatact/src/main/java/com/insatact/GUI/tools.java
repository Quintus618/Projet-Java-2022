package com.insatact.GUI;
import java.awt.*;
import javax.swing.*;

public class tools {
   
    public static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
        Image img = icon.getImage();  
        Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);  
        return new ImageIcon(resizedImage);
    }

    public static JButton paramButton(JButton b, int resizedWidth, int resizedHeight, ImageIcon i){
        b.setIcon(resizeIcon(i, resizedWidth, resizedHeight));
        b.setBackground(new Color(255,255,255,0));
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false); 
        return b;
    }

    /*Ancienne version de readBDD (avant rowset)
    ouvrir();
    ResultSet reponse=null;
    try {
        Statement statem = lien.createStatement();

            reponse=statem.executeQuery(demande);

        statem.close();
    } catch (SQLException e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
    }
    fermer();*/
}