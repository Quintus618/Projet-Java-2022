package GUI;
import java.awt.*;

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
        inscriptionButton.setPreferredSize(new Dimension(100, 100));
        connexionButton = new JButton("Connexion");
        connexionButton.setPreferredSize(new Dimension(100, 100));
        logo = new JLabel(tools.resizeIcon(new ImageIcon("./GUI/Pictures/chat.png"),180, 180));

        texte= new JLabel("Bienvenue!");
        texte.setForeground(Color.CYAN);
        texte.setFont(new Font("Serif", Font.BOLD, 40));

        setVisible(true);

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
        buttonsPanel.add(Box.createHorizontalGlue());
        buttonsPanel.add(connexionButton);
        buttonsPanel.add(Box.createHorizontalGlue());
        buttonsPanel.add(inscriptionButton);
        buttonsPanel.add(Box.createHorizontalGlue());
        buttonsPanel.setBackground(Color.BLACK);

        panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        texte.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalGlue());
        panel.add(logo);
        panel.add(Box.createVerticalGlue());
        panel.add(texte);
        panel.add(Box.createVerticalGlue());
        panel.add(buttonsPanel);
        buttonsPanel.add(Box.createVerticalGlue());

        add(panel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.PAGE_END);
    }


    public static void main(String[] Args){
        welcomGUI wGUI = new welcomGUI(500, 700);
        wGUI.setTitle("Low quality chat system - Welcome!");
    }

}