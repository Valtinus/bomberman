package entities.passive;

import entities.active.Player;
import logic.GameSettings;
import org.junit.Before;
import org.junit.Test;
import view.GameView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class DetonatorTest {
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
        Detonator detonator = new Detonator();
        detonator.applyEffect(gameView, player);
        assertTrue(player.detonatorActive);
        assertEquals(detonator, player.activePowerUps.get(player.activePowerUps.size() - 1));
    }
}
