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
 * Place bomb instantly negative powerup - the player places down every available bomb instantly until the effect duration elapses
 */
public class PlaceBombInstantly extends PowerUp {
    private static final long EFFECT_DURATION = 10000; // Az effektus ideje milliszekundumban

    public PlaceBombInstantly() {
        name = "PlaceBombInstantly";
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
     * Applying effect for the place bomb instantly negative powerup
     * Setting the players placeBombInstantlyActive attribute to true
     * Adding the powerup to the players activePowerUps array list
     *
     * @param gameView  game view
     * @param player    the player who picked up the powerup
     */
    @Override
    public void applyEffect(GameView gameView, Player player) {
        player.placeBombInstantlyActive = true;
        player.activePowerUps.add(PlaceBombInstantly.this);

        Timer effectTimer = new Timer();
        effectTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameView.gameModel.entities.remove(PlaceBombInstantly.this);
                player.activePowerUps.remove(PlaceBombInstantly.this);
                player.placeBombInstantlyActive = false;
            }
        }, EFFECT_DURATION);
    }
}
