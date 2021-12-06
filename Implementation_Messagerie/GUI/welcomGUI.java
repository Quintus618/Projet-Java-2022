package GUI;
import java.awt.*;
import java.awt.Component;

//Importation Libraries
import javax.swing.*;

public class welcomGUI extends JFrame{

    private JButton connexionButton;
    private JButton inscriptionButton;
    private JLabel logo;
    private JLabel texte;
    private JPanel panel;
    private JPanel buttonsPanel;
    
    //Constructor
    public welcomGUI(int height, int width){
        
        //Creation of GUI
        super("Insatact");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        inscriptionButton = new JButton("Inscription");
        connexionButton = new JButton("Connexion");
        //très sale
        logo = new JLabel(new ImageIcon(new ImageIcon("./GUI/Pictures/chat.png").getImage().getScaledInstance(180, 180, java.awt.Image.SCALE_SMOOTH)));
        //voir quels critères de privacy pour appeler resizeIcon de welcomGUI (créer un .java de méthodes réutilisables partout type "webtools.java"?)
        texte= new JLabel("Bienvenue!");
        texte.setAlignmentX(Component.CENTER_ALIGNMENT);
        texte.setForeground(Color.CYAN);
        texte.setFont(new Font("Serif", Font.BOLD, 40));

        setVisible(true);

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
        buttonsPanel.add(connexionButton);
        buttonsPanel.add(inscriptionButton);

        panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setAlignmentY(Component.CENTER_ALIGNMENT);//ne marchent pas, parce que.
        panel.add(logo);
        panel.add(texte);
        panel.add(buttonsPanel);

        add(panel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.PAGE_END);
    }


    public static void main(String[] Args){
        welcomGUI wGUI = new welcomGUI(500, 700);
        wGUI.setTitle("Low quality chat system - Welcome!");
    }

}