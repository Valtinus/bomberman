package entities.passive;

import view.GameView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Box obstacle - solid, can explode, can spawn powerup
 */
public class Box extends Obstacle {
    public Box() {
        name = "Box";
        hitbox = new Rectangle(2, 2, 46, 46);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        collision = true;
        try {
            img = ImageIO.read(new File("./src/assets/passive_entities/box.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Explosion for a box, chance to spawn a powerup
     *
     * @param gameView  game view
     * @return          random spawned powerup or null
     */
    public PowerUp explode(GameView gameView) {
        // Probability of powerup spawning
        double probability = 1.5;
        // Spawning a random powerup (can be null)
        PowerUp randomPowerUp = spawnRandomPowerUp(probability);
        // If the spawned powerup is not null places it on the map at the right positions
        if (randomPowerUp != null) {
            randomPowerUp.position.setPosX(currentGridX * gameView.tileSize);
            randomPowerUp.position.setPosY(currentGridY * gameView.tileSize);
            randomPowerUp.currentGridX = (randomPowerUp.position.getPosX() + gameView.tileSize / 2) / gameView.tileSize;
            randomPowerUp.currentGridY = (randomPowerUp.position.getPosY() + gameView.tileSize / 2) / gameView.tileSize;

            return randomPowerUp;
        }
        return null;
    }

    /**
     * Spawn a random powerup by chance
     *
     * @param probability   probability of powerup spawning
     * @return              the powerup or null
     */
    public PowerUp spawnRandomPowerUp(double probability) {
        Random random = new Random();
        double chance = random.nextDouble(); // Random number between 0 and 1

        // If the random number is less or equal with the probability a powerup spawns
        if (chance <= probability) {
            int randomPowerUpType = random.nextInt(11); // 11 kinds of powerups

            switch (randomPowerUpType) {
                case 0:
                    return new RollerSkate();
                case 1:
                    return new Slow();
                case 2:
                    return new DoubleBomb();
                case 3:
                    return new BiggerBomb();
                case 4:
                    return new SmallerBomb();
                case 5:
                    return new DisabledBomb();
                case 6:
                    return new PlaceBombInstantly();
                case 7:
                    return new Invincibility();
                case 8:
                    return new Barrier();
                case 9:
                    return new Ghost();
                case 10:
                    return new Detonator();
                default:
                    return null;
            }
        }
        return null;
    }
}
