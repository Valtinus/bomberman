package view;

import entities.active.Player;
import logic.GameSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

/**
 * Game frame class - frame for the game, contains the game view and a back to main menu button
 */
public class GameFrame extends JFrame {
    GameView gameView;
    DesignModel designModel = new DesignModel();

    /**
     * Constructor for the game frame
     * @param settings - game settings
     */
    public GameFrame(GameSettings settings){

        super("BomberBlitz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setResizable(false);

        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bgImage = new ImageIcon("./src/assets/bg2.png");
                g.drawImage(bgImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        bgPanel.setLayout(new BoxLayout(bgPanel, BoxLayout.Y_AXIS));
        this.setContentPane(bgPanel);

        JPanel scorePanel = new JPanel();
        scorePanel.setOpaque(false);
        scorePanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));
        scorePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        bgPanel.add(scorePanel);

        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(designModel.font.deriveFont(Font.BOLD, 30f));
        backButton.setForeground(designModel.textColor);
        backButton.setBackground(designModel.primaryColor);
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        backButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 5, 5, designModel.darkAccentColor),
                BorderFactory.createEmptyBorder(10, 10, 7, 10)
        ));

        backButton.addActionListener(e -> {
            dispose();
            new MenuFrame();
        });
        scorePanel.add(backButton);

        JPanel gamePanel = new JPanel();
        gamePanel.setOpaque(false);
        gamePanel.setLayout(new BorderLayout());

        gameView = new GameView(settings);
        gameView.setFocusable(true);
        gameView.requestFocusInWindow();
        gameView.setPreferredSize(new Dimension(gameView.screenWidth, gameView.screenHeight));
        gamePanel.setPreferredSize(new Dimension(gameView.screenWidth, gameView.screenHeight));

        gamePanel.add(gameView);
        bgPanel.add(gamePanel);

        this.setTitle("BomberBlitz");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1920,1080);
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                gameView.requestFocusInWindow();
            }
        });
    }

    public static void disposeFrame(){
        GameFrame.getFrames()[1].dispose();
    }
}
