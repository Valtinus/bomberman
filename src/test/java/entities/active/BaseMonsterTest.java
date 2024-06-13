package entities.active;

import logic.GameSettings;
import org.junit.Before;
import org.junit.Test;
import view.GameFrame;
import view.GameView;
import entities.active.Direction;

import static org.junit.Assert.*;

public class BaseMonsterTest {
    private BaseMonster baseMonster;
    private GameView gameView;
    private GameSettings gameSettings;

    @Before
    public void setUp() {
        gameSettings = new GameSettings(2, new int[]{87, 83, 65, 68, 32, 66, 38, 40, 37, 39, 10, 16}, "player1", "player2",1, 2, 1);
        gameView = new GameView(gameSettings);
        baseMonster = new BaseMonster(gameView);
    }

    @Test
    public void testMoveMonster() {
            //Testing up movement
            baseMonster.direction = Direction.UP;
            int oldPosY = baseMonster.position.getPosY();
            baseMonster.moveMonster();
            assertEquals(oldPosY - baseMonster.speed, baseMonster.position.getPosY());

            //Testing down movement
            baseMonster.direction = Direction.DOWN;
            oldPosY = baseMonster.position.getPosY();
            baseMonster.moveMonster();
            assertEquals(oldPosY + baseMonster.speed, baseMonster.position.getPosY());

            //Testing left movement
            baseMonster.direction = Direction.LEFT;
            int oldPosX = baseMonster.position.getPosX();
            baseMonster.moveMonster();
            assertEquals(oldPosX - baseMonster.speed, baseMonster.position.getPosX());

            //Testing right movement
            baseMonster.direction = Direction.RIGHT;
            oldPosX = baseMonster.position.getPosX();
            baseMonster.moveMonster();
            assertEquals(oldPosX + baseMonster.speed, baseMonster.position.getPosX());
    }

}