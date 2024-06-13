package entities.active;

import entities.active.Direction;
import entities.active.Player;
import entities.passive.Box;
import entities.passive.PowerUp;
import entities.passive.RollerSkate;
import logic.GameModel;
import logic.GameSettings;
import org.junit.Before;
import org.junit.Test;
import view.GameFrame;
import view.GameView;
import static org.junit.Assert.*;


public class SmartMonsterTest {
    private SmartMonster smartMonster;
    private GameView gameView;
    private GameSettings gameSettings;



    @Before
    public void setUp() {

        gameSettings = new GameSettings(2, new int[]{87, 83, 65, 68, 32, 66, 38, 40, 37, 39, 10, 16}, "player1", "player2",1, 2, 1);
        gameView = new GameView(gameSettings);
        smartMonster = new SmartMonster(gameView);
    }

    @Test
    public void testMoveMonster(){
        //Testing up movement
        smartMonster.direction = Direction.UP;
        int oldPosY = smartMonster.position.getPosY();
        smartMonster.moveMonster();
        assertEquals(oldPosY - smartMonster.speed, smartMonster.position.getPosY());

        //Testing down movement
        smartMonster.direction = Direction.DOWN;
        oldPosY = smartMonster.position.getPosY();
        smartMonster.moveMonster();
        assertEquals(oldPosY + smartMonster.speed, smartMonster.position.getPosY());

        //Testing left movement
        smartMonster.direction = Direction.LEFT;
        int oldPosX = smartMonster.position.getPosX();
        smartMonster.moveMonster();
        assertEquals(oldPosX - smartMonster.speed, smartMonster.position.getPosX());

        //Testing right movement
        smartMonster.direction = Direction.RIGHT;
        oldPosX = smartMonster.position.getPosX();
        smartMonster.moveMonster();
        assertEquals(oldPosX + smartMonster.speed, smartMonster.position.getPosX());
    }

    @Test
    public void testFindNearestPlayer() {
        //create players
        Player player1 = new Player(gameView, 1, "TestPlayer1");
        player1.position.setPosX(500);
        player1.position.setPosY(500);
        gameView.gameModel.sprites.add(player1);

        Player player2 = new Player(gameView, 2, "TestPlayer2");
        player2.position.setPosX(1000);
        player2.position.setPosY(1000);
        gameView.gameModel.sprites.add(player2);

        // Set the smartMonster's position
        smartMonster.position.setPosX(400);
        smartMonster.position.setPosY(400);


        smartMonster.findNearestPlayer();


        assertEquals(player1, smartMonster.nearestPlayer);
    }

}
