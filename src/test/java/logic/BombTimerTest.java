package logic;

import entities.active.Player;
import entities.passive.Bomb;
import view.GameFrame;
import view.GameView;
import logic.GameSettings;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BombTimerTest{
    private GameModel gameModel;
    private GameView gameView;
    private GameSettings gameSettings;
    private Player player;
    private Bomb bomb;
    private BombTimer bombTimer;


    @Before
    public void setUp(){


        gameSettings = new GameSettings(2, new int[]{87, 83, 65, 68, 32, 66, 38, 40, 37, 39, 10, 16}, "player1", "player2",1, 2, 1);

        gameView = new GameView(gameSettings);
        gameModel = new GameModel(gameView, gameSettings);
        player = new Player(gameView,1, "TestPlayer");
        bomb = new Bomb(gameView, player);
        bombTimer = new BombTimer(gameModel, player, bomb);
    }

    @Test
    public void testStart(){
        bombTimer.start();
        assertTrue(bombTimer.isRunning());
    }

    @Test
    public void testStop(){
        bombTimer.start();
        bombTimer.stop();
        assertFalse(bombTimer.isRunning());
    }



}