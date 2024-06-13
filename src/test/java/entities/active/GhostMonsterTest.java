package entities.active;

import logic.GameSettings;
import org.junit.Before;
import org.junit.Test;
import view.GameFrame;
import view.GameView;

import static org.junit.Assert.*;

public class GhostMonsterTest {
    private GhostMonster ghostMonster;
    private GameView gameView;
    private GameSettings gameSettings;



    @Before
    public void setUp() {

        gameSettings = new GameSettings(2, new int[]{87, 83, 65, 68, 32, 66, 38, 40, 37, 39, 10, 16}, "player1", "player2", 1, 2, 1);
        gameView = new GameView(gameSettings);
        ghostMonster = new GhostMonster(gameView);
    }

    @Test
    public void testMoveMonster(){
        //Testing up movement
        ghostMonster.direction = Direction.UP;
        int oldPosY = ghostMonster.position.getPosY();
        ghostMonster.moveMonster();
        assertEquals(oldPosY - ghostMonster.speed, ghostMonster.position.getPosY());

        //Testing down movement
        ghostMonster.direction = Direction.DOWN;
        oldPosY = ghostMonster.position.getPosY();
        ghostMonster.moveMonster();
        assertEquals(oldPosY + ghostMonster.speed, ghostMonster.position.getPosY());

        //Testing left movement
        ghostMonster.direction = Direction.LEFT;
        int oldPosX = ghostMonster.position.getPosX();
        ghostMonster.moveMonster();
        assertEquals(oldPosX - ghostMonster.speed, ghostMonster.position.getPosX());

        //Testing right movement
        ghostMonster.direction = Direction.RIGHT;
        oldPosX = ghostMonster.position.getPosX();
        ghostMonster.moveMonster();
        assertEquals(oldPosX + ghostMonster.speed, ghostMonster.position.getPosX());
    }

    @Test
    public void testIsOutsideMap() {
        // Test with positions outside the map boundaries
        ghostMonster.position.setPosX(0);
        ghostMonster.position.setPosY(0);
        assertTrue(ghostMonster.isOutsideMap(ghostMonster.position.getPosX(), ghostMonster.position.getPosY()));

        ghostMonster.position.setPosX(1000); // greater than rightEdge
        ghostMonster.position.setPosY(1000); // greater than bottomEdge
        assertTrue(ghostMonster.isOutsideMap(ghostMonster.position.getPosX(), ghostMonster.position.getPosY()));

        // Test with a position inside the map boundaries
        ghostMonster.position.setPosX(500); // inside the map
        ghostMonster.position.setPosY(500); // inside the map
        assertFalse(ghostMonster.isOutsideMap(ghostMonster.position.getPosX(), ghostMonster.position.getPosY()));
    }
}