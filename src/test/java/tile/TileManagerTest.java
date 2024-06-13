package tile;

import logic.GameSettings;
import org.junit.Before;
import org.junit.Test;
import view.GameFrame;
import view.GameView;

import static org.junit.Assert.*;

public class TileManagerTest {
    private TileManager tileManager;
    private GameView gameView;
    private GameSettings gameSettings;


    @Before
    public void setUp() {

        gameSettings = new GameSettings(2, new int[]{87, 83, 65, 68, 32, 66, 38, 40, 37, 39, 10, 16}, "player1", "player2",1, 2, 1);
        gameView = new GameView(gameSettings);
        tileManager = new TileManager(gameView, 1);
    }

    @Test
    public void testGetTileImg() {
        tileManager.getTileImg();
        assertNotNull(tileManager.tiles[0].image);
        assertNotNull(tileManager.tiles[1].image);
        assertNotNull(tileManager.tiles[2].image);
    }

    @Test
    public void testLoadMap() {
        tileManager.loadMap();
        for (int i = 0; i < gameView.maxScreenRow; i++) {
            for (int j = 0; j < gameView.maxScreenCol; j++) {
                assertTrue(tileManager.mapTileNum[i][j] >= 0 && tileManager.mapTileNum[i][j] <= 2);
            }
        }
    }

    @Test
    public void testLoadBoxes() {
        assertNotNull(tileManager.loadBoxes());
    }
}