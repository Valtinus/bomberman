package entities.passive;

import entities.active.Player;
import view.GameView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Barrier powerup - the player can place 3 more walls for each of this powerup
 */
public class Barrier extends PowerUp {
    public Barrier() {
        name = "Barrier";
        hitbox = new Rectangle(2, 2, 46, 46);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        collision = true;
        try {
            img = ImageIO.read(new File("./src/assets/passive_entities/barrier.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Applying effect for the barrier powerup
     * Adding 3 to the players placeable walls
     * Adding the powerup to the players activePowerUps array list
     *
     * @param gameView  game view
     * @param player    the player who picked up the powerup
     */
    @Override
    public void applyEffect(GameView gameView, Player player) {
        player.placeableWalls += 3;
        player.activePowerUps.add(Barrier.this);
        gameView.gameModel.entities.remove(Barrier.this);
    }
}
