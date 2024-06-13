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
 * Disabled bomb negative powerup - the player can not place bombs until the effect duration elapses
 */
public class DisabledBomb extends PowerUp {
    private static final long EFFECT_DURATION = 5000; // Az effektus ideje milliszekundumban

    public DisabledBomb() {
        name = "DisabledBomb";
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
     * Applying effect for the disabled bomb negative powerup
     * Setting the players disabledBombActive attribute to true
     * Adding the powerup to the players activePowerUps array list
     *
     * @param gameView  game view
     * @param player    the player who picked up the powerup
     */
    @Override
    public void applyEffect(GameView gameView, Player player) {
        player.disabledBombActive = true;
        player.activePowerUps.add(DisabledBomb.this);

        Timer effectTimer = new Timer();
        effectTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameView.gameModel.entities.remove(DisabledBomb.this);
                player.activePowerUps.remove(DisabledBomb.this);
                player.disabledBombActive = false;
            }
        }, EFFECT_DURATION);
    }
}
