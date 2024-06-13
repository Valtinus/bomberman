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
 * Ghost powerup - the player can walk through solid obstacles until the effect duration elapses
 */
public class Ghost extends PowerUp {
    private static final long EFFECT_DURATION = 10000; // Az effektus ideje milliszekundumban
    private static final long ALERT_TIME = 4000;
    private static final long ALERT_TIME2 = 2000;
    public Ghost() {
        name = "Ghost";
        hitbox = new Rectangle(10, 10, 30, 30);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        collision = true;
        try{
            img = ImageIO.read(new File("./src/assets/passive_entities/ghost.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * Applying effect for the ghost powerup
     * Setting the players isGhost attribute to true
     * Adding the powerup to the players activePowerUps array list
     *
     * @param gameView  game view
     * @param player    the player who picked up the powerup
     */
    @Override
    public void applyEffect(GameView gameView, Player player) {
        player.isGhost = true;
        player.activePowerUps.add(Ghost.this);
        gameView.gameModel.entities.remove(Ghost.this);

        Timer effectTimer = new Timer();
        effectTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameView.gameModel.entities.remove(Ghost.this);
                player.activePowerUps.remove(Ghost.this);
                player.isGhost = false;
                player.isGhost2 = false;
                player.isGhost3 = false;

                if(gameView.tileManager.mapTileNum[player.currentGridX][player.currentGridY] == 0){
                    player.isAlive = false;
                }
                for(Entity entity : gameView.gameModel.entities){
                    if (entity.currentGridX == player.currentGridX && entity.currentGridY == player.currentGridY && !(entity instanceof Bomb)) {
                        player.isAlive = false;
                        break;
                    }
                }

            }
        }, EFFECT_DURATION);

        Timer alertTimer = new Timer();
        alertTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.isGhost2 = true;
            }
        }, EFFECT_DURATION - ALERT_TIME);

        Timer alertTimer2 = new Timer();
        alertTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.isGhost3 = true;
            }
        }, EFFECT_DURATION - ALERT_TIME2);
    }
}