package entities.active;

import entities.Position;
import entities.passive.Entity;
import view.GameView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.util.Random;
import java.util.LinkedList;
/**
 * Smart monster class - this monster moves towards the nearest player using BFS algorithm
 */

public class SmartMonster extends Monster{
    Random random = new Random();
    public Player nearestPlayer;
    private int[][] grid;
    private int collisionCounter = 0;

    public SmartMonster(GameView gameView) {
        super(gameView);
        grid = new int[gameView.maxScreenRow][gameView.maxScreenCol];
        nearestPlayer = null;

        speed = 4; // Move faster than the base monster
        position = new Position();
        hitbox = new Rectangle(10, 10, 30, 30);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;

        position.setPosX(350);
        position.setPosY(450);

        direction = Direction.values()[random.nextInt(Direction.values().length)];
        try {
            img = ImageIO.read(new File("./src/assets/active_entities/smart_monster.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * This method moves the monster, and changes the direction towards the nearest player using BFS algorithm when the monster collides with something
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
        // Check for collisions
        gameView.collisionChecker.checkTile(this);
        gameView.collisionChecker.checkSpriteCollision(this);
        Entity entity = gameView.collisionChecker.checkEntity(this, false);


        if(collision){
            position.setPosX(oldPosX);
            position.setPosY(oldPosY);

            collisionCounter++; // Increment the collision counter

            if (collisionCounter >= 3) { // If the monster collides 3 times in one update
                collisionCounter = 0; // Reset the collision counter
                direction = Direction.values()[random.nextInt(Direction.values().length)]; // Change direction randomly
            } else {
                findNearestPlayer();
                direction = getDirectionToNearestPlayer();
            }
        } else {
            collisionCounter = 0; // Reset the collision counter if no collision
        }
    }
    /**
     * This method finds the nearest player to the monster using BFS algorithm
     */
    public void findNearestPlayer(){

        Queue<Position> queue = new LinkedList<>();
        boolean[][] visited = new boolean[grid.length][grid[0].length];

        //Setting the start position to the current position of the monster
        Position start = new Position();
        start.setPosX(position.getPosX() / gameView.tileSize);
        start.setPosY(position.getPosY() / gameView.tileSize);
        //Adding the start position to the queue
        queue.add(start);
        //Marking the start position as visited
        visited[start.getPosX()][start.getPosY()] = true;

        Player closestPlayer = null;
        int closestDistance = Integer.MAX_VALUE;

        while(!queue.isEmpty()){
            Position curr = queue.poll(); //remove the first element from the queue, returns null if empty
            //finding the closest player to the monster using BFS
            for(Sprite sprite : gameView.gameModel.sprites){
                if (sprite instanceof Player) {
                    int playerPosX = sprite.position.getPosX() / gameView.tileSize;
                    int playerPosY = sprite.position.getPosY() / gameView.tileSize;
                    if (playerPosX == curr.getPosX() && playerPosY == curr.getPosY()) {
                        int distance = Math.abs(curr.getPosX() - start.getPosX()) + Math.abs(curr.getPosY() - start.getPosY());
                        if (distance < closestDistance) {
                            closestPlayer = (Player) sprite;
                            closestDistance = distance;
                        }
                    }
                }
            }
            // Add unvisited neighbors to the queue
            for (Direction direction : Direction.values()) {
                int dx =0, dy =0;
                switch(direction){
                    case UP:
                        dy = -1;
                        break;
                    case DOWN:
                        dy = 1;
                        break;
                    case LEFT:
                        dx = -1;
                        break;
                    case RIGHT:
                        dx = 1;
                        break;
                }
                Position next = new Position();
                next.setPosX(curr.getPosX() + dx);
                next.setPosY(curr.getPosY() + dy);
                if (next.getPosX() >= 0 && next.getPosX() < grid.length && next.getPosY() >= 0 && next.getPosY() < grid[0].length && !visited[next.getPosX()][next.getPosY()]) {
                    queue.add(next);
                    visited[next.getPosX()][next.getPosY()] = true; // Mark as visited here
                }
            }
        }
        nearestPlayer = closestPlayer;
    }/**
        * @return the direction to the nearest player
        */
    public Direction getDirectionToNearestPlayer() {
        // Random direction if no player is found
        if (nearestPlayer == null) {
            return Direction.values()[random.nextInt(Direction.values().length)];
        }
        // Calculate the direction to the nearest player
        int dx = nearestPlayer.position.getPosX() - position.getPosX();
        int dy = nearestPlayer.position.getPosY() - position.getPosY();

        if (Math.abs(dx) > Math.abs(dy)) {
            // The target is mainly in the horizontal direction
            return dx > 0 ? Direction.RIGHT : Direction.LEFT;
        } else {
            // The target is mainly in the vertical direction
            return dy > 0 ? Direction.DOWN : Direction.UP;
        }
    }
}
