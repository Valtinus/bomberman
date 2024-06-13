package entities.active;

import entities.active.JunctionMonster;
import entities.active.Direction;
import entities.active.Player;
import entities.active.Sprite;
import entities.passive.Bomb;
import entities.passive.Box;
import entities.passive.Explosion;
import entities.passive.Wall;
import logic.GameSettings;
import org.junit.Before;
import org.junit.Test;
import view.GameFrame;
import view.GameView;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;


public class JunctionMonsterTest {
    private JunctionMonster junctionMonster;
    private GameView gameView;
    private GameSettings gameSettings;

    @Before
    public void setUp() {
        gameSettings = new GameSettings(2, new int[]{87, 83, 65, 68, 32, 66, 38, 40, 37, 39, 10, 16}, "player1", "player2", 1, 2, 1);
        gameView = new GameView(gameSettings);
        junctionMonster = new JunctionMonster(gameView);
    }
    public void testMoveMonster(){
        //Testing up movement
        junctionMonster.direction = Direction.UP;
        int oldPosY = junctionMonster.position.getPosY();
        junctionMonster.moveMonster();
        assertEquals(oldPosY - junctionMonster.speed, junctionMonster.position.getPosY());

        //Testing down movement
        junctionMonster.direction = Direction.DOWN;
        oldPosY = junctionMonster.position.getPosY();
        junctionMonster.moveMonster();
        assertEquals(oldPosY + junctionMonster.speed, junctionMonster.position.getPosY());

        //Testing left movement
        junctionMonster.direction = Direction.LEFT;
        int oldPosX = junctionMonster.position.getPosX();
        junctionMonster.moveMonster();
        assertEquals(oldPosX - junctionMonster.speed, junctionMonster.position.getPosX());

        //Testing right movement
        junctionMonster.direction = Direction.RIGHT;
        oldPosX = junctionMonster.position.getPosX();
        junctionMonster.moveMonster();
        assertEquals(oldPosX + junctionMonster.speed, junctionMonster.position.getPosX());
    }
    @Test
    public void testIsJunction() {

        junctionMonster.position.setPosX(100);
        junctionMonster.position.setPosY(100);

        assertTrue(junctionMonster.isJunction());
    }
    @Test
    public void testGetDirectionToNearestPlayer() {
        Player player = new Player(gameView,1,"TestPlayer");
        player.position.setPosX(200);
        player.position.setPosY(200);
        gameView.gameModel.sprites.add(player);

        Direction direction = junctionMonster.getDirectionToNearestPlayer();


        assertNotNull(direction);
    }
    @Test
    public void testIsTileABox() {

        Box box = new Box();
        box.position.setPosX(1);
        box.position.setPosY(1);
        gameView.gameModel.entities.add(box);

        assertTrue(junctionMonster.isTileABox(1, 1));
    }

}
