package entities.passive;
import entities.active.Player;
import view.GameView;
import logic.GameSettings;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class SmallerBombTest {
    private Player player;
    private GameView gameView;
    private GameSettings gameSettings;

    @Before
    public void setUp() {
        gameSettings = new GameSettings(2, new int[]{87, 83, 65, 68, 32, 66, 38, 40, 37, 39, 10, 16}, "player1", "player2",1, 2, 1);
        player = new Player(gameView, 1, "TestPlayer");
        gameView = new GameView(gameSettings);
    }

    @Test
    public void testApplyEffect() {
        SmallerBomb smallerBomb = new SmallerBomb();
        smallerBomb.applyEffect(gameView, player);

        assertTrue(player.smallerBombActive);
        assertEquals(smallerBomb, player.activePowerUps.get(player.activePowerUps.size() - 1));
    }
}