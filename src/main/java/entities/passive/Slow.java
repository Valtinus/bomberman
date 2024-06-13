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
 * Slow negative powerup - slowing the player until the effect duration elapses
 */
public class Slow extends PowerUp {
    public static final double SPEED_BOOST_FACTOR = 0.5; // A sebesség növelésének tényezője
    private static final long EFFECT_DURATION = 3000; // Az effektus ideje milliszekundumban


    public Slow() {
        name = "Slow";
        hitbox = new Rectangle(10, 10, 30, 30);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        collision = true;
        try{
            img = ImageIO.read(new File("./src/assets/passive_entities/negative_powerup.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Applying effect for the slow negative powerup
     * Setting the players original speed to the current speed
     * Changing the players speed by the speed boost factor
     * Adding the powerup to the players activePowerUps array list
     *
     * @param gameView  game view
     * @param player    the player who picked up the powerup
     */
    @Override
    public void applyEffect(GameView gameView, Player player) {
        int originalSpeed = player.speed;
        player.changeSpeed(SPEED_BOOST_FACTOR);
        player.activePowerUps.add(Slow.this);

        Timer effectTimer = new Timer();
        effectTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.speed = originalSpeed;
                gameView.gameModel.entities.remove(Slow.this);
                player.activePowerUps.remove(Slow.this);
            }
        }, EFFECT_DURATION);
    }
}
