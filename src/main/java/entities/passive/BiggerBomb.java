package entities.passive;

import entities.active.Player;
import view.GameView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Bigger bomb powerup - the players bombs range increase by 1 for each of this powerup
 */
public class BiggerBomb extends PowerUp {
    public BiggerBomb() {
        name = "BiggerBomb";
        hitbox = new Rectangle(10, 10, 30, 30);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        collision = true;
        try{
            img = ImageIO.read(new File("./src/assets/passive_entities/biggerbomb.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * Applying effect for the bigger bomb powerup
     * Adding 1 to the players bomb range
     * Adding the powerup to the players activePowerUps array list
     *
     * @param gameView  game view
     * @param player    the player who picked up the powerup
     */
    @Override
    public void applyEffect(GameView gameView, Player player) {
        player.bombRange += 1;
        player.activePowerUps.add(BiggerBomb.this);
        gameView.gameModel.entities.remove(BiggerBomb.this);
    }
}
