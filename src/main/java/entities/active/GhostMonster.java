package entities.active;

import entities.Position;
import entities.passive.Bomb;
import entities.passive.Box;
import entities.passive.Entity;
import entities.passive.Wall;
import view.GameView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Ghost monster class - this monster moves randomly and has no collision with walls or boxes, only bombs
 */
public class GhostMonster extends Monster{
    public Random random = new Random();
    public Direction previousCollisionDirection = null;
    public GhostMonster(GameView gameView) {
        super(gameView);
        speed = 2; // Move slower than the base monster
        position = new Position();
        hitbox = new Rectangle(10, 10, 30, 30);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;

        position.setPosX(500);
        position.setPosY(500);

        direction = Direction.values()[random.nextInt(Direction.values().length)];
        try {
            img = ImageIO.read(new File("./src/assets/active_entities/ghost_monster.png"));
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * This method moves the monster, and changes the direction randomly when the monster collides with a bomb
     * The monster does not collide with walls or boxes
     */
    @Override
    public void moveMonster() {
        collision = false;
        // Store the current position

        int oldPosX = position.getPosX();
        int oldPosY = position.getPosY();

        currentGridX = (position.getPosX() + gameView.tileSize / 2) / gameView.tileSize;
        currentGridY = (position.getPosY() + gameView.tileSize / 2) / gameView.tileSize;

        // Move the monster
        switch (direction) {
            case UP:
                position.setPosY(position.getPosY() - speed);
                break;
            case DOWN:
                position.setPosY(position.getPosY() + speed);
                break;
            case LEFT:
                position.setPosX(position.getPosX() - speed);
                break;
            case RIGHT:
                position.setPosX(position.getPosX() + speed);
                break;
        }

        // Check for a collision with an entity
        gameView.collisionChecker.checkSpriteCollision(this);
        boolean isCollidingWithBomb = gameView.collisionChecker.isCollidingWithBomb(this);

        if (isCollidingWithBomb) {
            position.setPosX(oldPosX);
            position.setPosY(oldPosY);
            // Store the direction of the collision
            previousCollisionDirection = direction;
            // Generate a new random direction that is not the same as the previous collision direction
            do {
                direction = Direction.values()[random.nextInt(Direction.values().length)];
            } while (direction == previousCollisionDirection);
        }
        // Check for a collision with the game map boundaries
        if (isOutsideMap(position.getPosX(), position.getPosY())) {
            // If the new position is outside the map, treat it as a collision and move back to the old position
            position.setPosX(oldPosX);
            position.setPosY(oldPosY);
            // Store the direction of the collision
            previousCollisionDirection = direction;
            // Generate a new random direction that is not the same as the previous collision direction
            do {
                direction = Direction.values()[random.nextInt(Direction.values().length)];
            } while (direction == previousCollisionDirection);
        }

        //randomly change direction
        if(!collision){
            int chanceToChangeDirection = random.nextInt(500);
            if (chanceToChangeDirection < 2) {
                Direction oldDirection = direction;
                do {
                    direction = Direction.values()[random.nextInt(Direction.values().length)];
                } while (direction == oldDirection);
            }

        }
    }
    /**
     * This method checks if the monster is outside the map boundaries
     * @param posX the x position of the monster
     * @param posY the y position of the monster
     * @return true if the monster is outside the map boundaries, false otherwise
     */
    public boolean isOutsideMap(int posX, int posY) {
        int MAP_WIDTH = 20; // replace with your actual map width
        int MAP_HEIGHT = 20; // replace with your actual map height
        int CELL_SIZE = 50; // replace with your actual cell size
        int BUFFER = 50; // buffer distance from the edge

        int leftEdge = BUFFER;
        int rightEdge = (MAP_WIDTH - 1) * CELL_SIZE - BUFFER;
        int topEdge = BUFFER;
        int bottomEdge = (MAP_HEIGHT - 1) * CELL_SIZE - BUFFER;

        return posX < leftEdge || posX > rightEdge || posY < topEdge || posY > bottomEdge;
    }
}
