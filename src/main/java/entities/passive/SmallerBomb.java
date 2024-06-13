package entities.passive;

import entities.active.Player;
import view.GameView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Smaller bomb powerup - the bombs range are reduced to 1 until the effect duration elapses
 */
public class SmallerBomb extends PowerUp {
    private static final long EFFECT_DURATION = 10000; // Duration of the effect in milliseconds

    public SmallerBomb() {
        name = "SmallerBomb";
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
     * Applying effect for the smaller bomb powerup
     * Setting the players smallerBombActive attribute to true
     * Adding the powerup to the players activePowerUps array list
     *
     * @param gameView  game view
     * @param player    the player who picked up the powerup
     */
    @Override
    public void applyEffect(GameView gameView, Player player) {
        player.smallerBombActive = true;
        player.activePowerUps.add(SmallerBomb.this);

        Timer effectTimer = new Timer();
        effectTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameView.gameModel.entities.remove(SmallerBomb.this);
                player.activePowerUps.remove(SmallerBomb.this);
                player.smallerBombActive = false;
            }
        }, EFFECT_DURATION);
    }
}
