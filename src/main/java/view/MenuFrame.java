package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Menu frame class - visual representation of the main menu
 */

public class MenuFrame extends JFrame {
    DesignModel designModel = new DesignModel();

    /**
     * Constructor for the MenuFrame class
     */
    public MenuFrame() {
        super("BomberBlitz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setResizable(false);

        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bgImage = new ImageIcon("./src/assets/bgImage.png");
                g.drawImage(bgImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        bgPanel.setLayout(new BorderLayout());
        this.setContentPane(bgPanel);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setOpaque(false);
        bgPanel.add(contentPanel, BorderLayout.CENTER);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        //titlePanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600, 200));

        JLabel shadowLabel = new JLabel("BomberBlitz");
        shadowLabel.setFont(designModel.font.deriveFont(100f));
        shadowLabel.setForeground(designModel.primaryColor);
        shadowLabel.setSize(shadowLabel.getPreferredSize());
        shadowLabel.setLocation(5, 5);

        JLabel textLabel = new JLabel("BomberBlitz");
        textLabel.setFont(designModel.font.deriveFont(100f));
        textLabel.setForeground(designModel.darkAccentColor);
        textLabel.setSize(textLabel.getPreferredSize());

        layeredPane.add(shadowLabel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(textLabel, JLayeredPane.PALETTE_LAYER);

        titlePanel.add(layeredPane);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(titlePanel, BorderLayout.NORTH);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        contentPanel.add(buttonPanel, BorderLayout.CENTER);

        JButton newGameButton = createButton("New Game");
        newGameButton.addActionListener(e -> StartGame());
        buttonPanel.add(newGameButton);
        buttonPanel.add(Box.createVerticalStrut(20));

        JButton exitButton = createButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitButton);


        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Starts the game
     */
    public void StartGame() {
        this.dispose();
        new NewGameFrame();
    }

    /**
     * Creates a button with the given text (used for exit and new game buttons)
     * @param txt text to be displayed on the button
     * @return JButton with the given text
     */
    public JButton createButton(String txt){
        JButton button = new JButton(txt);
        button.setFont(designModel.font.deriveFont(50f));
        button.setForeground(designModel.textColor);
        button.setBackground(designModel.primaryColor);
        button.setOpaque(true);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 5, 5, designModel.darkAccentColor),
                BorderFactory.createEmptyBorder(10, 10, 7, 10)
        ));

        return button;
    }
}
