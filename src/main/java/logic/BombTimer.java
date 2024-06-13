package logic;

import entities.active.Player;
import entities.passive.Bomb;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BombTimer {
    private GameModel gameModel;
    private Bomb bomb;
    private int detonationTime;
    private ActionListener actionListener;
    private Timer timer;

    /**
     * Start a timer for the placed bomb
     * When the time elapses explodes the bomb
     *
     * @param gamemodel game logic
     * @param player    the player who placed down the bomb
     * @param bomb      the bomb
     */
    public BombTimer(GameModel gamemodel, Player player, Bomb bomb) {
        this.detonationTime = player.detonationTime;
        this.bomb = bomb;

        // After the time elapses
        actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!bomb.exploded) {
                    bomb.explode();
                    player.placedBombs.remove(bomb);
                    gamemodel.entities.remove(bomb);
                    timer.stop();
                }
            }
        };

        timer = new Timer(detonationTime, actionListener);
    }

    /**
     * Start the timer
     */
    public void start() {
        timer.start();
    }

    /**
     * Stop the timer
     */
    public void stop() {
        timer.stop();
    }

    /**
     * Getter for timer to check if it is running (needed for testing)
     *
     * @return  true or false if the timer is running
     */
    public  boolean isRunning(){
        return timer.isRunning();
    }
}