package entities.passive;

import entities.active.Player;
import view.GameView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Detonator powerup - the player must place down all their available bombs (these bombs do not explode by themselves)
 * After all the players bombs are placed with pressing the bomb placing key again the player detonates all their bombs
 */
public class Detonator extends PowerUp {
    public Detonator() {
        name = "Detonator";
        hitbox = new Rectangle(10, 10, 30, 30);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        collision = true;
        try{
            img = ImageIO.read(new File("./src/assets/passive_entities/detonator.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * Applying effect for the detonator powerup
     * Setting the players detonatorActive attribute to true
     * Adding the powerup to the players activePowerUps array list
     *
     * @param gameView  game view
     * @param player    the player who picked up the powerup
     */
    @Override
    public void applyEffect(GameView gameView, Player player) {
        player.detonatorActive = true;
        player.activePowerUps.add(Detonator.this);
        gameView.gameModel.entities.remove(Detonator.this);
    }
}
