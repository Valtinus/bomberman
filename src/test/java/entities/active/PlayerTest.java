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

public class PlayerTest {
    private GameView gameView;

    private GameSettings gameSettings;
    private GameFrame gameFrame;    private Player player;

    @Before
    public void setUp() {
        gameSettings = new GameSettings(2, new int[]{87, 83, 65, 68, 32, 66, 38, 40, 37, 39, 10, 16}, "player1", "player2",1, 2, 1);
        gameView = new GameView(gameSettings);
        player = new Player(gameView, 1, "TestPlayer");
    }

    @Test
    public void testReset() {
        player.reset();
        assertTrue(player.isAlive);
        assertFalse(player.isGhost);
        assertFalse(player.isInvincible);
        assertEquals(0, player.position.getPosX());
        assertEquals(0, player.position.getPosY());
        assertEquals(6, player.speed);
        assertEquals(1, player.availableBombs);
        assertEquals(3000, player.detonationTime);
        assertEquals(0, player.placeableWalls);
        assertEquals(2, player.bombRange);
        assertTrue(player.placedWalls.isEmpty());
        assertTrue(player.placedBombs.isEmpty());
        assertTrue(player.activePowerUps.isEmpty());
    }

    @Test
    public void testMove() {
        player.position.setPosX(300);
        player.position.setPosY(300);
        int posY = player.position.getPosY();
        player.move(Direction.UP);
        assertEquals(posY - player.speed, player.position.getPosY());

        posY = player.position.getPosY();
        player.move(Direction.DOWN);
        assertEquals(posY + player.speed, player.position.getPosY());

        int posX = player.position.getPosX();
        player.move(Direction.LEFT);
        assertEquals(posX - player.speed, player.position.getPosX());

        posX = player.position.getPosX();
        player.move(Direction.RIGHT);
        assertEquals(posX + player.speed, player.position.getPosX());
    }

    @Test
    public void testIsOnBombOrWall() {
        player.position.setPosX(300);
        player.position.setPosY(300);
        assertFalse(player.isOnBombOrWall());
        player.placeBomb();
        assertTrue(player.isOnBombOrWall());
        player.placeWall();
        assertTrue(player.isOnBombOrWall());
    }

    @Test
    public void testPlaceBomb() {
        player.position.setPosX(300);
        player.position.setPosY(300);
        assertTrue(player.placedBombs.isEmpty());
        player.placeBomb();
        assertFalse(player.placedBombs.isEmpty());
    }

    @Test
    public void testIsDelayOver() {
        player.position.setPosX(300);
        player.position.setPosY(300);
        assertTrue(player.isDelayOver(System.currentTimeMillis()));
        player.placeBomb();
        assertFalse(player.isDelayOver(System.currentTimeMillis()));
        try {
            Thread.sleep(player.placedBombs.get(0).placementTime + 200 - System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(player.isDelayOver(System.currentTimeMillis()));
    }

    @Test
    public void testPlaceWall() {
        player.position.setPosX(300);
        player.position.setPosY(300);
        player.placeableWalls = 3;
        assertTrue(player.placedWalls.isEmpty());
        player.placeWall();
        assertFalse(player.placedWalls.isEmpty());
    }

    @Test
    public void testIsTileFree() {
        player.position.setPosX(200);
        player.position.setPosY(200);
        player.currentGridX = 2;
        player.currentGridY = 2;
        assertTrue(player.isTileFree());
        player.placeableWalls = 3;
        player.placeWall();
        assertFalse(player.isTileFree());
    }

    @Test
    public void testCheckSpriteOnTile() {
        player.position.setPosX(200);
        player.position.setPosY(200);
        player.currentGridX = 2;
        player.currentGridY = 2;
        assertFalse(player.checkSpriteOnTile());
        Player sprite = new Player(gameView, 1, "TestSprite");
        sprite.position.setPosX(player.position.getPosX());
        sprite.position.setPosY(player.position.getPosY());
        sprite.currentGridX = player.currentGridX;
        sprite.currentGridY = player.currentGridY;
        gameView.gameModel.sprites.add(sprite);
        assertTrue(player.checkSpriteOnTile());
    }

    @Test
    public void testCheckEntityOnTile() {
        assertFalse(player.checkEntityOnTile());
        Box entity = new Box();
        entity.position.setPosX(player.position.getPosX());
        entity.position.setPosY(player.position.getPosY());
        gameView.gameModel.entities.add(entity);
        assertTrue(player.checkEntityOnTile());
    }

    @Test
    public void testChangeSpeed() {
        assertEquals(6, player.speed);
        player.changeSpeed(2);
        assertEquals(12, player.speed);
    }

    @Test
    public void testRemoveActivePowerUp() {
        assertTrue(player.activePowerUps.isEmpty());
        PowerUp powerUp = new RollerSkate();
        player.activePowerUps.add(powerUp);
        player.removeActivePowerUp(RollerSkate.class);
        assertTrue(player.activePowerUps.isEmpty());
    }
}
