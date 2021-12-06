package GUI;
import java.awt.*;
import java.awt.Component;

//Importation Libraries
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class welcomGUI extends JFrame{

    private JButton connexionButton;
    private JButton inscriptionButton;
    private JLabel logo;
    private JLabel texte;
    private JPanel panel;
    private JPanel buttonsPanel;
    private Container ContentPane;
    
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
        logo = new JLabel(new ImageIcon(new ImageIcon("./GUI/Pictures/chat.png").getImage().getScaledInstance(180, 180, java.awt.Image.SCALE_SMOOTH)));//à quoi sert le troisième int?
        //voir quels critères de privacy pour appeler resizeIcon de welcomGUI (créer un .java de méthodes réutilisables partout type "webtools.java"?)
        texte= new JLabel("Bienvenue!");

        texte.setForeground(Color.CYAN);
        texte.setFont(new Font("Serif", Font.BOLD, 40));

        setVisible(true);

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        buttonsPanel.add(connexionButton);
        buttonsPanel.add(inscriptionButton);

        panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setAlignmentY(Component.CENTER_ALIGNMENT);//ne marchent pas, parce que.
        panel.add(logo);
        panel.add(texte);
        panel.add(buttonsPanel);
//et je ne sais pas si ça marche car montp n'a pas le bon JDK, bien sûr
        ContentPane = getContentPane();
        ContentPane.add(panel, BorderLayout.CENTER);
        ContentPane.add(buttonsPanel, BorderLayout.PAGE_END);
    }


    public static void main(String[] Args){
        welcomGUI wGUI = new welcomGUI(500, 700);
        wGUI.setTitle("Low quality chat system - Welcome!");
    }

}