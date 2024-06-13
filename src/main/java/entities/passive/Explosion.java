package entities.passive;

import view.GameView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Explosion on the map
 * Killing sprites on collision
 */
public class Explosion extends Entity {
    public boolean removable;
    private long placementTime;
    private long explosionTime = 1000;
    public Explosion(GameView gameView) {
        name = "Explosion";
        hitbox = new Rectangle(0, 0, 50, 50);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        this.gameView = gameView;
        try{
            img = ImageIO.read(new File("./src/assets/explosion.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        removable = false;
        this.placementTime = System.currentTimeMillis();
    }

    /**
     * Updates the removable value if the explosion is over
     */
    public void update(){
        if (System.currentTimeMillis() - placementTime >= explosionTime) {
            removable = true;
        }
    }

    /**
     * Returns the value of the removable attribute
     *
     * @return  true or false if the explosion is removable
     */
    public boolean isRemovable(){
        return removable;
    }
}
