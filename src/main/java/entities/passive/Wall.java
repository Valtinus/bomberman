package entities.passive;

import entities.active.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Wall obstacle (solid block like a box placed by a player)
 */
public class Wall extends Obstacle {
    public Player owner;
    public Wall(Player owner) {
        name = "Wall";
        this.owner = owner;
        hitbox = new Rectangle(2, 2, 46, 46);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        collision = true;
        try {
            img = ImageIO.read(new File("./src/assets/passive_entities/wall.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
