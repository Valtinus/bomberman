package entities.passive;

import entities.Position;
import logic.GameModel;
import view.GameView;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Entity class for all the entities on the map (powerups, boxes, barriers)
 */
public class Entity {
    GameView gameView;
    public Position position = new Position();
    public Rectangle hitbox;
    public int hitboxDefaultX;
    public int hitboxDefaultY;
    public int currentGridX;
    public int currentGridY;
    public BufferedImage img;
    public String name;
    public  boolean collision = false;
    public void drawEntity(Graphics2D g2, GameView gameView) {
        g2.drawImage(img, position.getPosX(), position.getPosY(), gameView);
    }

}
