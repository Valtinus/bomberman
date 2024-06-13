package entities.passive;

import entities.active.Player;
import view.GameView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Double bomb powerup - the player can place on more bomb with each of this powerup
 */
public class DoubleBomb extends PowerUp {
    public DoubleBomb() {
        name = "DoubleBomb";
        hitbox = new Rectangle(10, 10, 30, 30);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        collision = true;
        try{
            img = ImageIO.read(new File("./src/assets/passive_entities/doublebomb.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * Applying effect for the double bomb powerup
     * Adds 1 to the players availableBombs
     * Adding the powerup to the players activePowerUps array list
     *
     * @param gameView  game view
     * @param player    the player who picked up the powerup
     */
    @Override
    public void applyEffect(GameView gameView, Player player) {
        player.availableBombs += 1;
        player.activePowerUps.add(DoubleBomb.this);
        gameView.gameModel.entities.remove(DoubleBomb.this);
    }
}
