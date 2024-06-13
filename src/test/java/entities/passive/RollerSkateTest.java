
package entities.passive;

import entities.active.Player;
import entities.passive.Bomb;
import view.GameFrame;
import view.GameView;
import logic.GameSettings;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class RollerSkateTest {
    private Barrier barrier;
    private Player player;
    private GameView gameView;
    private GameSettings gameSettings;

    @Before
    public void setUp() {
        gameSettings = new GameSettings(2, new int[]{87, 83, 65, 68, 32, 66, 38, 40, 37, 39, 10, 16}, "player1", "player2",1, 2, 1);
        barrier = new Barrier();
        player = new Player(gameView, 1, "TestPlayer");
        gameView = new GameView(gameSettings); // You need to provide gameSettings here
    }

    @Test
    public void testApplyEffect() {
        RollerSkate rollerSkate = new RollerSkate();
        double initialSpeed = player.speed;
        rollerSkate.applyEffect(gameView, player);

        assertEquals(initialSpeed * RollerSkate.SPEED_BOOST_FACTOR, player.speed, 0.001);
        assertEquals(1, player.activePowerUps.size());

    }
}