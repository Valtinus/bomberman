package logic;
import entities.active.BaseMonster;
import entities.active.Direction;
import entities.active.Player;
import entities.active.Sprite;
import entities.passive.Bomb;
import entities.passive.Explosion;
import entities.passive.Wall;
import org.junit.Before;
import org.junit.Test;
import view.GameFrame;
import view.GameView;

import static org.junit.Assert.*;

public class GameModelTest {
    private GameModel gameModel;
    private GameView gameView;
    private GameSettings gameSettings;

    @Before
    public void setUp() {

        gameSettings = new GameSettings(2, new int[]{87, 83, 65, 68, 32, 66, 38, 40, 37, 39, 10, 16}, "player1", "player2",1, 2, 1);
        gameView = new GameView(gameSettings);
        gameModel = new GameModel(gameView, gameSettings);
    }

    @Test
    public void testUpdate(){
        int initPosX = gameModel.testPlayer.position.getPosX();
        int initPosY = gameModel.testPlayer.position.getPosY();
        gameModel.testPlayer.move(Direction.UP);

        gameModel.update();

        assertTrue(initPosX != gameModel.testPlayer.position.getPosX() || initPosY != gameModel.testPlayer.position.getPosY());
    }
    @Test
    public void testEndOfRound(){
       //kill all players but one, and remove all bombs
        for (Sprite player : gameModel.sprites) {
            if (player != gameModel.testPlayer) {
                player.isAlive = false;
            }
        }
        gameModel.entities.removeIf(entity -> entity instanceof Bomb);

        gameModel.checkEndOfRound();
        assertEquals(gameSettings.rounds-1, gameModel.rounds);
    }
    @Test
    public void testEndOfRoundDraw(){
        //kill all players and remove all bombs
        for (Sprite player : gameModel.sprites) {
            player.isAlive = false;
        }
        gameModel.entities.removeIf(entity -> entity instanceof Bomb);

        gameModel.checkEndOfRound();
        assertEquals(gameSettings.rounds-1, gameModel.rounds);
    }

    @Test
    public void testUpdateExplosions1(){
        Explosion explosion = new Explosion(gameView);
        gameModel.entities.add(explosion);
        gameModel.updateExplosions();
        assertTrue(gameModel.entities.contains(explosion));
    }
    @Test
    public void testUpdateExplosions2(){
        Explosion explosion = new Explosion(gameView);
        explosion.removable = true;
        gameModel.entities.add(explosion);
        gameModel.updateExplosions();
        assertFalse(gameModel.entities.contains(explosion));
    }
    @Test
    public void testMoveMonsters(){
        BaseMonster testMonster = new BaseMonster(gameView);
        gameModel.sprites.add(testMonster);
        testMonster.position.setPosX(300);
        testMonster.position.setPosY(300);
        int testMonsterX = testMonster.position.getPosX();
        int testMonsterY = testMonster.position.getPosY();
        gameModel.moveMonsters();
        assertTrue(testMonster.position.getPosX() != testMonsterX || testMonster.position.getPosY() != testMonsterY);

    }
    @Test
    public void testRemoveDeadSprites(){
        gameModel.sprites.clear();
        BaseMonster testMonster1 = new BaseMonster(gameView);
        BaseMonster testMonster2 = new BaseMonster(gameView);
        Player testPlayer = new Player(gameView,1, "TestPlayer");

        testMonster1.isAlive = false;

        gameModel.sprites.add(testMonster1);
        gameModel.sprites.add(testMonster2);
        gameModel.sprites.add(testPlayer);

        gameModel.removeDeadSprites();

        assertNotEquals(3, gameModel.sprites.size());
        assertFalse(gameModel.sprites.contains(testMonster1));
        assertTrue(gameModel.sprites.contains(testMonster2));
        assertTrue(gameModel.sprites.contains(testPlayer));

        testPlayer.isAlive = false;

        gameModel.removeDeadSprites();

        assertNotEquals(2, gameModel.sprites.size());
        assertFalse(gameModel.sprites.contains(testMonster1));
        assertTrue(gameModel.sprites.contains(testMonster2));
        assertFalse(gameModel.sprites.contains(testPlayer));
    }
    @Test
    public void testPlaceBomb(){
        Player testPlayer = new Player(gameView,1, "TestPlayer");
        testPlayer.position.setPosX(300);
        testPlayer.position.setPosY(300);
        testPlayer.disabledBombActive = true;
        gameModel.placeBombForPlayer(testPlayer, true);
        assertTrue(testPlayer.placedBombs.isEmpty());
        assertFalse(gameModel.entities.stream().anyMatch(entity -> entity instanceof Bomb));

        testPlayer.disabledBombActive = false;
        gameModel.placeBombForPlayer(testPlayer, true);
        System.out.println(testPlayer.placedBombs);
        System.out.println(gameModel.entities);
        assertFalse(testPlayer.placedBombs.isEmpty());
        assertTrue(gameModel.entities.stream().anyMatch(entity -> entity instanceof Bomb));
    }
    @Test
    public void testDetonateBomb(){
        Player testPlayer = new Player(gameView,1, "TestPlayer");
        testPlayer.detonatorActive = true;
        gameModel.placeBombForPlayer(testPlayer, true);
        gameModel.detonateBombsForPlayer(testPlayer, true);
        assertTrue(testPlayer.placedBombs.isEmpty());
        assertFalse(gameModel.entities.stream().anyMatch(entity -> entity instanceof Bomb));
    }
    @Test
    public void testPlaceWall(){
        Player testPlayer = new Player(gameView,1, "TestPlayer");
        testPlayer.position.setPosX(300);
        testPlayer.position.setPosY(300);
        testPlayer.placeableWalls = 3;
        gameModel.placeWallForPlayer(testPlayer, true);
        assertFalse(testPlayer.placedWalls.isEmpty());
        assertTrue(gameModel.entities.stream().anyMatch(entity -> entity instanceof Wall));
    }
}
