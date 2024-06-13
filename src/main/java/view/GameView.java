package view;

import entities.active.Monster;
import entities.active.Sprite;
import entities.passive.Entity;
import entities.passive.Explosion;
import logic.CollisionChecker;
import logic.GameModel;

import logic.GameSettings;
import tile.TileManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
/**
 * Game view class - visual representation of the game
 */
public class GameView extends JPanel  {

    public TileManager tileManager;
    public GameModel gameModel;
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public Timer player;
    public final int tileSize = 50; // size of each tile in pixels
    public final int maxScreenCol = 20; // max number of columns on the screen
    public final int maxScreenRow = 20; // max number of rows on the screen
    public int screenWidth = tileSize * maxScreenCol;
    public int screenHeight = tileSize * maxScreenRow;
    public int fps = 80;
    public GameSettings settings;
    private Timer timer;

    public GameView(GameSettings settings) {
        this.settings = settings;
        tileManager = new TileManager(this, settings.mapID);
        this.gameModel = new GameModel(this, settings);
        this.setSize(new Dimension(screenWidth, screenHeight));
        setOpaque(false);


        this.addKeyListener(gameModel.keyHandler);
        this.addKeyListener(gameModel.keyHandler2);
        this.setFocusable(true); // allows the panel to get focus

        double interval = 1000.0 / fps;

        timer = new Timer((int) interval, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameModel.update();
                repaint();
            }
        });
        start();
    }
    /**
     * Reset the game view by stopping the timer, creating a new tile manager and starting the timer again
     */
    public void reset(){
        stop();
        tileManager = new TileManager(this, settings.mapID);

        this.addKeyListener(gameModel.keyHandler);
        this.addKeyListener(gameModel.keyHandler2);

        start();
    }
    /**
     * Starts the timer
     */
    public void start() {
        timer.start();
    }
    /**
     * Stops the timer
     */
    public void stop() {
        timer.stop();
    }

    /**
     * Paints the game view
     * Draws the tiles, entities and sprites(players, monsters)
     *
     * @param g graphics
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Calculate total width of the tiles
        int totalTileWidth = tileSize * maxScreenCol;

        // Calculate the left padding needed to center the tiles
        int padding = (getWidth() - totalTileWidth) / 2;

        // Translate the Graphics2D object to the right by the padding amount
        g2.translate(padding, 0);

        // Now draw the tiles and entities
        tileManager.draw(g2);

        //draw entities
        ArrayList<Entity> entitiesCopy = new ArrayList<>(gameModel.entities);
        for (Entity entity : entitiesCopy) {
            entity.drawEntity(g2, this);
        }

        //draw sprites (players, monsters)
        for(Sprite sprite : gameModel.sprites) {
            sprite.drawEntity(g2, gameModel);
        }

        // Reset the translation to the original state
        g2.translate(-padding, 0);

        g2.dispose();
    }
}

