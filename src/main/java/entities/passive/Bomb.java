package entities.passive;

import entities.active.Monster;
import entities.active.Player;
import entities.active.Sprite;
import view.GameView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Bomb class - solid obstacle, explodes boxes, barriers, sprites
 */
public class Bomb extends Entity {
    Player owner;
    public boolean exploded = false;
    public long placementTime;
    public Bomb(GameView gameView, Player player){
        this.gameView = gameView;
        name = "Bomb";
        this.owner = player;
        hitbox = new Rectangle(10, 10, 30, 30);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        collision = true;
        placementTime = System.currentTimeMillis();
        try{
            img = ImageIO.read(new File("./src/assets/passive_entities/bomb.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Bomb explosion
     * Explodes the center and explodes nearby tiles according the player's active powerups
     */
    public void explode() {
        exploded = true;
        // Booleans for the blocked directions (if the explosion hits a wall or box or barrier)
        AtomicBoolean upBlocked = new AtomicBoolean(false);
        AtomicBoolean downBlocked = new AtomicBoolean(false);
        AtomicBoolean leftBlocked = new AtomicBoolean(false);
        AtomicBoolean rightBlocked = new AtomicBoolean(false);

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        explodeInDirection(0, 0); // Center explosion

        // 200 milliseconds delay for the first round of explosion
        executorService.schedule(() -> {
            upBlocked.set(explodeInDirection(0, -1)); // Up explosion
            downBlocked.set(explodeInDirection(0, 1));  // Down explosion
            leftBlocked.set(explodeInDirection(-1, 0)); // Left explosion
            rightBlocked.set(explodeInDirection(1, 0));  // Right explosion
        }, 200, TimeUnit.MILLISECONDS);

        // Explodes further tiles if the smaller bomb powerup is not active on the player
        if (!owner.smallerBombActive) {
            // Exploding the bomb in the range of the player's bomb range (bigger bomb powerup)
            for (int i = 2; i <= owner.bombRange; i++) {
                int finalI = i;
                int delay = i * 200;

                // 200 milliseconds delay for the next round of explosion
                executorService.schedule(() -> {
                    if (!upBlocked.get()) {
                        upBlocked.set(explodeInDirection(0, -finalI)); // Up explosion
                    }
                    if (!downBlocked.get()) {
                        downBlocked.set(explodeInDirection(0, finalI));  // Down explosion
                    }
                    if (!leftBlocked.get()) {
                        leftBlocked.set(explodeInDirection(-finalI, 0)); // Left explosion
                    }
                    if (!rightBlocked.get()) {
                        rightBlocked.set(explodeInDirection(finalI, 0));  // Right explosion
                    }
                }, delay, TimeUnit.MILLISECONDS);
            }
        }

        executorService.shutdown();
    }

    /**
     * Explode on the given tile (x, y coordinates from the center)
     *
     * @param dx    x coordinates from the center
     * @param dy    y coordinates from the center
     * @return      true or false if the direction is blocked
     */
    private boolean explodeInDirection(int dx, int dy) {
        boolean directionBlocked = false;
        int x = currentGridX + dx;
        int y = currentGridY + dy;

        Entity explodedBox = null;
        Entity randomPowerUp = null;

        // Checking if the explosion is on the map and the tile is not a wall
        if (x > 0 && x < gameView.maxScreenCol && y > 0 && y < gameView.maxScreenRow && gameView.tileManager.mapTileNum[x][y] != 0) {
            Explosion explosion = new Explosion(gameView);
            explosion.position.setPosX(x * gameView.tileSize);
            explosion.position.setPosY(y * gameView.tileSize);
            explosion.currentGridX = x;
            explosion.currentGridY = y;
            // Add an explosion to the tile
            gameView.gameModel.entities.add(explosion);
        }

        // List of all the other bombs the current bomb has in its range
        ArrayList<Bomb> nearbyBombs = new ArrayList<Bomb>();
        if (!gameView.gameModel.entities.isEmpty()) {
            for (Entity entity : gameView.gameModel.entities) {
                // If the tile has a box on it
                // Set the exploded box to that box
                // Explode the box and spawn powerup
                // Block the direction
                if (entity.currentGridX == x && entity.currentGridY == y && entity instanceof Box) {
                    explodedBox = entity;
                    randomPowerUp = ((Box) entity).explode(gameView);
                    directionBlocked = true;
                }
                // If the tile has a wall (placed by a player) on it
                // Set the exploded box to that wall
                // Block the direction
                if(entity.currentGridX == x && entity.currentGridY == y && entity instanceof Wall){
                    explodedBox = entity;
                    directionBlocked = true;
                }
                // If the tile has a wall (solid block) on it
                // Block the direction
                if (x > 0 && x < gameView.maxScreenCol && y > 0 && y < gameView.maxScreenRow && gameView.tileManager.mapTileNum[x][y] == 0) {
                    directionBlocked = true;
                }
                // If the tile has a bomb on it
                // Add the bomb to the nearby bombs list
                if (!this.equals(entity) && entity instanceof Bomb && entity.currentGridX == x && entity.currentGridY == y) {
                    nearbyBombs.add((Bomb)entity);
                }
            }
        }

        // Remove the exploded box from the game entities
        gameView.gameModel.entities.remove(explodedBox);

        // If the exploded box is a wall (placed by a player)
        // Remove it from the player's placed walls list
        if(explodedBox instanceof Wall) {
            for(Sprite owner : gameView.gameModel.sprites) {
                if(owner.equals(((Wall) explodedBox).owner)){
                    ((Player) owner).placedWalls.remove(explodedBox);
                }
            }
        }
        // Iterating on the nearby bombs list
        // Exploding the nearby bombs
        for (Bomb bomb : nearbyBombs) {
            Player bombOwner = bomb.owner;
            gameView.gameModel.entities.remove(bomb);
            bombOwner.placedBombs.remove(bomb);
            bomb.explode();
        }
        // If the spawned powerup is not null add it to the gam entities
        if(randomPowerUp != null) {
            gameView.gameModel.entities.add(randomPowerUp);
        }
        // If there is a player or a monster on the exploding tile kill it
        for (Sprite sprite : gameView.gameModel.sprites) {
            if (sprite.currentGridX == x && sprite.currentGridY == y) {
                if (sprite instanceof Monster) {
                    sprite.isAlive = false;
                }
                if (sprite instanceof Player && !((Player) sprite).isInvincible) {
                    sprite.isAlive = false;
                }
            }
        }
        return directionBlocked;
    }
}