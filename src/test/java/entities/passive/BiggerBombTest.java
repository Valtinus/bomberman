package entities.passive;

import entities.active.Player;
import logic.GameSettings;
import org.junit.Before;
import org.junit.Test;
import view.GameView;

import static org.junit.Assert.assertEquals;


public class BiggerBombTest {

    private Player player;
    private GameView gameView;
    private GameSettings gameSettings;


    @Before
    public void setUp(){
        gameSettings = new GameSettings(2, new int[]{87, 83, 65, 68, 32, 66, 38, 40, 37, 39, 10, 16}, "player1", "player2",1, 2, 1);
        gameView = new GameView(gameSettings);
        player = new Player(gameView,1, "TestPlayer");

    }
    @Test
    public void testApplyEffect(){
        int initialBombRadius = player.bombRange;
        BiggerBomb biggerBomb = new BiggerBomb();
        biggerBomb.applyEffect(gameView, player);
        assertEquals(initialBombRadius + 1, player.bombRange);
        assertEquals(biggerBomb, player.activePowerUps.get(player.activePowerUps.size() - 1));
    }
}
