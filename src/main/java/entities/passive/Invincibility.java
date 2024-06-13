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
 * Invincibility powerup - the player can not die until the effect duration elapses
 */
public class Invincibility extends PowerUp {
    private static final long EFFECT_DURATION = 8000; // Az effektus ideje milliszekundumban
    private static final long ALERT_TIME = 4000;
    private static final long ALERT_TIME2 = 2000;

    public Invincibility() {
        name = "Invincibility";
        hitbox = new Rectangle(10, 10, 30, 30);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        collision = true;
        try{
            img = ImageIO.read(new File("./src/assets/passive_entities/invincibility.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * Applying effect for the invincibility powerup
     * Setting the players isInvincible attribute to true
     * Adding the powerup to the players activePowerUps array list
     *
     * @param gameView  game view
     * @param player    the player who picked up the powerup
     */
    @Override
    public void applyEffect(GameView gameView, Player player) {
        player.isInvincible = true;
        player.activePowerUps.add(Invincibility.this);

        Timer effectTimer = new Timer();
        effectTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameView.gameModel.entities.remove(Invincibility.this);
                player.activePowerUps.remove(Invincibility.this);
                player.isInvincible = false;
                player.isInvincible2 = false;
                player.isInvincible3 = false;

            }
        }, EFFECT_DURATION);

        Timer alertTimer = new Timer();
        alertTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.isInvincible2 = true;
            }
        }, EFFECT_DURATION - ALERT_TIME);

        Timer alertTimer2 = new Timer();
        alertTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.isInvincible3 = true;
            }
        }, EFFECT_DURATION - ALERT_TIME2);
    }
}