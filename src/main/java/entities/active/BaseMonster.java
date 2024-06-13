package entities.active;

import entities.Position;
import entities.passive.Entity;
import logic.GameModel;
import view.GameView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Base monster class - this monster changes directions randomly when collides with something
 *
 */

public class BaseMonster extends Monster{
    public Direction previousCollisionDirection = null;
    private Random random = new Random();
    GameView gameView;
    public BaseMonster(GameView gameView){
        super(gameView);
        this.gameView = gameView;
        speed = 3;
        position = new Position();
        hitbox = new Rectangle(10, 10, 30, 30);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;

        position.setPosX(550);
        position.setPosY(600);

        Random random = new Random();
        Direction[] directions = Direction.values(); //gets values of the direction enum
        Direction randDirection = directions[random.nextInt(directions.length)];
        direction = randDirection;

        try {
            img = ImageIO.read(new File("./src/assets/active_entities/base_monster.png"));
        } catch (IOException e) {
            e.printStackTrace();

        }

    }
    /**
     * This method moves the monster in the current direction, and changes the direction randomly when collides with something
     */
    @Override
    public void moveMonster() {

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

        // Randomly change direction
        int chanceToChangeDirection = random.nextInt(500);
        if (chanceToChangeDirection < 3) {
            Direction oldDirection = direction;
            do {
                direction = Direction.values()[random.nextInt(Direction.values().length)];
            } while (direction == oldDirection);
        }
        // Check for a collision
        collision = false;
        gameView.collisionChecker.checkTile(this);
        gameView.collisionChecker.checkSpriteCollision(this);
        Entity entity=gameView.collisionChecker.checkEntity(this, false);

        // if collides, move back to the old position and generate a new direction
        if(collision){
            position.setPosX(oldPosX);
            position.setPosY(oldPosY);
            // Store the direction of the collision
            previousCollisionDirection = direction;
            // Generate a new random direction that is not the same as the previous collision direction
            do {
                direction = Direction.values()[random.nextInt(Direction.values().length)];
            } while (direction == previousCollisionDirection);
        }
    }

}
