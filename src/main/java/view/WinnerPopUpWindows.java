package view;

import entities.active.Player;
import logic.GameModel;

import javax.swing.*;
import java.awt.*;

/**
 * WinnerPopUpWindows class - pop up window that appears when the game or the round is over
 */
public class WinnerPopUpWindows extends JFrame {
    DesignModel designModel = new DesignModel();
    public JButton continueButton;

    /**
     * Constructor for the WinnerPopUpWindows class
     * @param msg either round or game
     * @param player the player that won
     * @param playerName the name of the player
     * @param isDraw if the game is a draw
     * @param gameModel the game model for necessary data
     * @param p1Score the score of player 1
     * @param p2Score the score of player 2
     */
    public WinnerPopUpWindows(String msg, Player player, String playerName, boolean isDraw, GameModel gameModel, String p2Name, String p1Name, int p1Score, int p2Score){
        super();

        setUndecorated(true);
        setSize(400, 330);
        setLocationRelativeTo(null);
        setResizable(false);
        JDialog dialog = new JDialog();
        dialog.setUndecorated(true);
        dialog.setSize(400, 330);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        dialog.setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(designModel.darkAccentColor);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(designModel.bgcolor.brighter());
                g.fillRect(3, 3, getWidth() - 6, getHeight() - 6);
            }
        });

        JLabel endMessage = new JLabel();
        endMessage.setForeground(designModel.textColor);
        endMessage.setFont(designModel.font.deriveFont(20f));
        dialog.getContentPane().add(endMessage);

        dialog.getContentPane().add(Box.createVerticalStrut(5));

        JLabel scoreMessage = new JLabel();
        scoreMessage.setForeground(designModel.textColor);
        scoreMessage.setFont(designModel.font.deriveFont(20f));
        scoreMessage.setText(p1Name + ": " + p1Score + " - " + p2Name + ": " + p2Score);
        scoreMessage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialog.getContentPane().add(scoreMessage);
        if(isDraw){
            endMessage.setText("It's a draw!");
            endMessage.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));

            ImageIcon drawIcon = new ImageIcon("./src/assets/draw.png");
            drawIcon = new ImageIcon(drawIcon.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH));
            JLabel drawLabel = new JLabel(drawIcon);
            drawLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            drawLabel.setPreferredSize(new Dimension(400, 130));
            dialog.getContentPane().add(drawLabel);
            dialog.getContentPane().add(Box.createVerticalStrut(10));
        }
        else{
            endMessage.setText(playerName + " has won the " + msg + "!");
            endMessage.setBorder(BorderFactory.createEmptyBorder(40, 10, 30, 10));

            ImageIcon playerCharacter = new ImageIcon(player.img);
            playerCharacter = new ImageIcon(playerCharacter.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
            JLabel playerIcon = new JLabel(playerCharacter);
            playerIcon.setBorder(BorderFactory.createEmptyBorder(20, 10, 50, 10));
            playerIcon.setPreferredSize(new Dimension(400, 100));
            dialog.getContentPane().add(playerIcon);
            dialog.getContentPane().add(Box.createVerticalStrut(30));
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 40));
        buttonPanel.setPreferredSize(new Dimension(400, 100));
        dialog.getContentPane().add(buttonPanel);

        continueButton = new JButton();
        if(msg.equalsIgnoreCase("round")){
            continueButton.setText("Next round");
        }
        else{
            continueButton.setText("Play again");
        }
        continueButton.setFont(designModel.font.deriveFont(20f));
        continueButton.setForeground(designModel.textColor);
        continueButton.setBackground(designModel.primaryColor);
        continueButton.setOpaque(true);
        continueButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 3, designModel.darkAccentColor),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        if(msg.equals("round")){
            continueButton.addActionListener(e -> {
                gameModel.nextRound();
                dispose();
                dialog.dispose();
            });
        }
        else if (msg.equals("game")){
            continueButton.addActionListener(e -> {
                gameModel.nextGame();
                dispose();
                dialog.dispose();
            });
        }
        buttonPanel.add(continueButton);
        dialog.setVisible(true);
    }

}
