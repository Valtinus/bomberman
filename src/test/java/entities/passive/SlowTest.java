package entities.passive;
import entities.active.Player;
import view.GameView;
import logic.GameSettings;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class SlowTest {
    private Player player;
    private GameView gameView;
    private GameSettings gameSettings;

    @Before
    public void setUp() {
        gameSettings = new GameSettings(2, new int[]{87, 83, 65, 68, 32, 66, 38, 40, 37, 39, 10, 16}, "player1", "player2",1, 2, 1);
        player = new Player(gameView,1, "TestPlayer");
        gameView = new GameView(gameSettings);
    }

    @Test
    public void testApplyEffect() {
        Slow slow = new Slow();
        double initialSpeed = player.speed;

        slow.applyEffect(gameView, player);

        assertEquals(initialSpeed * Slow.SPEED_BOOST_FACTOR, player.speed, 0.001);
        assertEquals(1, player.activePowerUps.size());

    }
}