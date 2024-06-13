package entities.passive;

import entities.active.Player;
import entities.active.Sprite;
import view.GameView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Roller skate powerup - speeds up the player
 */
public class RollerSkate extends PowerUp {
    public static final double SPEED_BOOST_FACTOR = 1.5; // A sebesség növelésének tényezője

    public RollerSkate() {
        name = "RollerSkate";
        hitbox = new Rectangle(10, 10, 30, 30);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        collision = true;
        try{
            img = ImageIO.read(new File("./src/assets/passive_entities/rollerskate.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * Applying effect for the roller skate powerup
     * Changing the players speed by the speed boost factor
     * Adding the powerup to the players activePowerUps array list
     *
     * @param gameView  game view
     * @param player    the player who picked up the powerup
     */
    @Override
    public void applyEffect(GameView gameView, Player player) {
        player.changeSpeed(SPEED_BOOST_FACTOR);
        player.activePowerUps.add(RollerSkate.this);
    }
}
