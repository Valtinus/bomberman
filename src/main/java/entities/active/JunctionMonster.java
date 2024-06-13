package entities.active;

import entities.Position;
import entities.passive.Box;
import entities.passive.Entity;
import view.GameView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Junction monster class - this monster changes directions when it reaches a junction
 */
public class JunctionMonster extends Monster {
    Random random = new Random();
    private Player nearestPlayer;
    private int[][] grid;
    private int collisionCounter = 0;


    public JunctionMonster(GameView gameView) {
        super(gameView);
        grid = new int[gameView.maxScreenRow][gameView.maxScreenCol];
        nearestPlayer = null;

        speed = 3;
        position = new Position();
        hitbox = new Rectangle(10, 10, 30, 30);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;

        position.setPosX(500);
        position.setPosY(600);

        direction = getDirectionToNearestPlayer();
        try {
            img = ImageIO.read(new File("./src/assets/active_entities/junction_monster.png"));
        } catch (
                IOException e) {
            e.printStackTrace();

        }
    }
    @Override
    public void moveMonster(){
        collision = false;
        // Store the current position
        int oldPosX = position.getPosX();
        int oldPosY = position.getPosY();
        boolean reachedCenter = false;

        currentGridX = (position.getPosX() + gameView.tileSize / 2) / gameView.tileSize;
        currentGridY = (position.getPosY() + gameView.tileSize / 2) / gameView.tileSize;

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
        gameView.collisionChecker.checkTile(this);
        Entity entity = gameView.collisionChecker.checkEntity(this, false);

        if(collision) {
            position.setPosX(oldPosX);
            position.setPosY(oldPosY);

            collisionCounter++; // Increment the collision counter

            if (collisionCounter >= 5) { // If the monster collides 3 times in one update
                collisionCounter = 0; // Reset the collision counter
                findNearestPlayer();
                direction = getDirectionToNearestPlayer();
            } else {
                findNearestPlayer();
                direction = getDirectionToNearestPlayer();
            }
        } else {
            collisionCounter = 0; // Reset the collision counter if no collision

            findNearestPlayer();
            direction = getDirectionToNearestPlayer();
        }
        // If the monster is at a junction, find the nearest player and move in that direction
        if (isJunction() && !collision) {
            if (isJunction() && !collision) {
                // Check if the monster is at the center of a cell
                if (position.getPosX() % gameView.tileSize == 0 && position.getPosY() % gameView.tileSize == 0) {
                    reachedCenter = true;
                } else {
                    reachedCenter = false;
                }

                if (reachedCenter) {
                    findNearestPlayer();
                    direction = getDirectionToNearestPlayer();
                }
            }

        }
    }
    /**
     * This method checks if the monster is at a junction
     * @return true if the monster is at a junction, false otherwise
     */
    public boolean isJunction() {
        int accessibleTiles = 0;

        // Check only perpendicular directions
        Direction[] directionsToCheck;
        if (direction == Direction.UP || direction == Direction.DOWN) {
            directionsToCheck = new Direction[]{Direction.LEFT, Direction.RIGHT};
        } else {
            directionsToCheck = new Direction[]{Direction.UP, Direction.DOWN};
        }

        for (Direction direction : directionsToCheck) {
            int dx = 0, dy = 0;
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
            int nextPosX = position.getPosX() / gameView.tileSize + dx;
            int nextPosY = position.getPosY() / gameView.tileSize + dy;
            // Check if the tile is accessible
            if (isTileAccessible(nextPosX, nextPosY)) {
                accessibleTiles++;
            }
        }

        // If there are at least 2 accessible tiles, the monster is at a junction
        return accessibleTiles >= 2;
    }
    /**
     * This method finds the nearest player to the monster using BFS
     */
    private void findNearestPlayer(){
        //Disable BFS until the monster doesn't reach the center of the next cell where it steps => it won't move diagonally

        //BFS to find the nearest player
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
    }
    /**
     * This method returns the direction to the nearest player
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
        } else if (Math.abs(dy) > Math.abs(dx)) {
            // The target is mainly in the vertical direction
            return dy > 0 ? Direction.DOWN : Direction.UP;
        } else {
            // If dx and dy are equal, choose a direction randomly
            return dx > 0 ? (random.nextBoolean() ? Direction.RIGHT : Direction.UP) : (random.nextBoolean() ? Direction.LEFT : Direction.DOWN);
        }
    }
    /**
     * This method checks if there is a box at the given position
     * @param posX the x-coordinate of the position
     * @param posY the y-coordinate of the position
     * @return true if there is a box at the given position, false otherwise
     */
    public boolean isTileABox(int posX, int posY) {
        // Iterate over all entities
        for (Entity entity : gameView.gameModel.entities) {
            // Check if the entity is a box
            if (entity instanceof Box) {
                // Check if the box is at the given position
                if (entity.position.getPosX() == posX && entity.position.getPosY() == posY) {
                    return true;
                }
            }
        }
        // No box is at the given position
        return false;
    }
    /**
     * This method checks if the tile at the given position is accessible
     * @param posX the x-coordinate of the position
     * @param posY the y-coordinate of the position
     * @return true if the tile is accessible, false otherwise
     */
    public boolean isTileAccessible(int posX, int posY) {
        // Check if the tile is within the grid
        if (posX < 0 || posX >= grid.length || posY < 0 || posY >= grid[0].length) {
            return false;
        }
        // Check if there is a box at the position
        if (isTileABox(posX, posY)) {
            return false;
        }
        // The tile is accessible if it's within the grid and there's no box at the position
        return true;
    }

}


