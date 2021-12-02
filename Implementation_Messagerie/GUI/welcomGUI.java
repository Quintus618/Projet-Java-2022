package GUI;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

//Importation Libraries
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class welcomGUI extends JFrame{

    private JButton connexionButton;
    private JButton inscriptionButton;
    private JLabel logo;
    private JLabel texte;
    
    //Constructor
    public welcomGUI(int height, int width){
        
        //Creation of GUI
        super("Insatact");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inscriptionButton = new JButton("Inscription");
        connexionButton = new JButton("Connexion");
        //très sale
        logo = new JLabel(new ImageIcon(new ImageIcon("./GUI/Pictures/chat.png").getImage().getScaledInstance(180, 180, 10)));//à quoi sert le troisième int?
        texte= new JLabel("Bienvenue!");

        texte.setForeground(Color.CYAN);
        texte.setFont(new Font("Serif", Font.BOLD, 40));

        setVisible(true);
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setAlignmentY(Component.CENTER_ALIGNMENT);//ne marchent pas, parce que.
        panel.add(logo);
        panel.add(texte);
        panel.add(connexionButton);
        panel.add(inscriptionButton);
        add(panel);
    }


    public static void main(String[] Args){
        welcomGUI wGUI = new welcomGUI(500, 700);
        wGUI.setTitle("Low quality chat system - Welcome!");
    }

}