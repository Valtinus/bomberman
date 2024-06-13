package entities.active;

import entities.Position;
import entities.passive.*;
import view.GameView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public  class Player extends Sprite {
    public String name;
    public int charId;
    public int score;
    int reloadTime;
    public int bombRange;
    public int detonationTime;
    public boolean smallerBombActive = false;
    public boolean disabledBombActive = false;
    public boolean placeBombInstantlyActive = false;
    public boolean detonatorActive = false;
    public boolean reachedEdge = false;
    public int placeableWalls;
    public long lastDetonated;
    public ArrayList<Wall> placedWalls;
    public ArrayList<PowerUp> activePowerUps;
    public int availableBombs;
    public ArrayList<Bomb> placedBombs;
    GameView gameView;

    /**
     * Constructor for Player class
     *
     * @param gameView the GameView object associated with the player
     */
    public Player(GameView gameView, int charId, String name){
        super(gameView);
        this.gameView = gameView;
        this.position = new Position();
        this.direction = Direction.DOWN;
        this.originalSpeed = 6;
        this.speed = originalSpeed;
        this.availableBombs = 1;
        this.detonationTime = 3000;
        this.placeableWalls = 0;
        this.bombRange = 2;
        this.name = name;
        this.charId = charId;
        this.placedWalls = new ArrayList<Wall>();
        this.placedBombs = new ArrayList<Bomb>();
        this.activePowerUps = new ArrayList<PowerUp>();

        hitbox = new Rectangle(10, 20, 30, 30);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;

        try {
            switch (charId) {
                case 1:
                    img = ImageIO.read(new File("./src/assets/characters/character1.png"));
                    break;
                case 2:
                    img = ImageIO.read(new File("./src/assets/characters/character2.png"));
                    break;
                case 3:
                    img = ImageIO.read(new File("./src/assets/characters/character3.png"));
                    break;
                case 4:
                    img = ImageIO.read(new File("./src/assets/characters/character4.png"));
                    break;
            }

            ghostimg = ImageIO.read(new File("./src/assets/ghost_player.png"));
            ghostimg2 = ImageIO.read(new File("./src/assets/ghost_player_2.png"));
            ghostimg3 = ImageIO.read(new File("./src/assets/ghost_player_3.png"));
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * Reset the player attributes for a new round
     */
    public void reset() {
        this.isAlive = true;
        this.isGhost = false;
        this.isInvincible = false;
        this.position = new Position();
        this.direction = Direction.DOWN;
        this.speed = originalSpeed;
        this.availableBombs = 1;
        this.detonationTime = 3000;
        this.placeableWalls = 0;
        this.bombRange = 2;
        this.placedWalls = new ArrayList<Wall>();
        this.placedBombs = new ArrayList<Bomb>();
        this.activePowerUps = new ArrayList<PowerUp>();
    }

    /**
     * Move the player in the specified direction
     *
     * @param direction the direction in which the player should move
     */
    public void move(Direction direction) {
        currentGridX = (position.getPosX() + gameView.tileSize / 2) / gameView.tileSize;
        currentGridY = (position.getPosY() + gameView.tileSize / 2) / gameView.tileSize;
        switch (direction) {
            case UP:
                this.direction = Direction.UP;
                break;
            case DOWN:
                this.direction = Direction.DOWN;
                break;
            case LEFT:
                this.direction = Direction.LEFT;
                break;
            case RIGHT:
                this.direction = Direction.RIGHT;
                break;
        }

        collision = false;
        reachedEdge = false;

        gameView.collisionChecker.checkTile(this);
        gameView.collisionChecker.checkSpriteCollision(this);
        Entity entity = gameView.collisionChecker.checkEntity(this, true);
        pickUpPowerUp(entity);

        reachedEdge = gameView.collisionChecker.reachedEdge(this);

        if((!collision || isGhost) && !reachedEdge) {
            switch (this.direction) {
                case UP:
                    onBomb = isOnBombOrWall();
                    position.setPosY(position.getPosY() - speed);
                    break;
                case DOWN:
                    onBomb = isOnBombOrWall();
                    position.setPosY(position.getPosY() + speed);
                    break;
                case LEFT:
                    onBomb = isOnBombOrWall();
                    position.setPosX(position.getPosX() - speed);
                    break;
                case RIGHT:
                    onBomb = isOnBombOrWall();
                    position.setPosX(position.getPosX() + speed);
                    break;
            }
        }
    }

    /**
     * Check if player is currently on a bomb or wall
     *
     * @return true or false
     */
    public boolean isOnBombOrWall() {
        for (Bomb entity : placedBombs) {
            if(isIntersectingWithBombOrWall(entity)) {
                return true;
            }
        }

        for (Wall entity : placedWalls) {
            if(isIntersectingWithBombOrWall(entity)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if player is currently intersecting with a bomb or a wall
     *
     * @param entity the given bomb or wall entity
     * @return true or false
     */
    public boolean isIntersectingWithBombOrWall(Entity entity) {
        hitbox.x = position.getPosX() + hitbox.x;
        hitbox.y = position.getPosY() + hitbox.y;

        entity.hitbox.x = entity.position.getPosX() + entity.hitbox.x;
        entity.hitbox.y = entity.position.getPosY() + entity.hitbox.y;

        if (hitbox.intersects(entity.hitbox)) {
            return true;
        }

        hitbox.x = hitboxDefaultX;
        hitbox.y = hitboxDefaultY;

        entity.hitbox.x = entity.hitboxDefaultX;
        entity.hitbox.y = entity.hitboxDefaultY;

        return false;
    }

    /**
     * Pick up the given entity and apply the correct effect
     *
     * @param entity the given entity
     */
    public void pickUpPowerUp(Entity entity) {
        if (entity != null && !(entity instanceof Box) && !(entity instanceof Bomb) && !(entity instanceof Wall) && !(entity instanceof Explosion)) {
            if(entity instanceof RollerSkate && !isPowerUpActive(RollerSkate.class)) {
                ((RollerSkate) entity).applyEffect(gameView, this);
            }
            else if (entity instanceof Slow && !isPowerUpActive(Slow.class)) {
                ((Slow) entity).applyEffect(gameView, this);
            }
            else if (entity instanceof DoubleBomb) {
                ((DoubleBomb) entity).applyEffect(gameView, this);
            }
            else if (entity instanceof BiggerBomb) {
                ((BiggerBomb) entity).applyEffect(gameView, this);
            }
            else if (entity instanceof SmallerBomb && !isPowerUpActive(SmallerBomb.class)) {
                ((SmallerBomb) entity).applyEffect(gameView, this);
            }
            else if (entity instanceof DisabledBomb && !isPowerUpActive(DisabledBomb.class)) {
                ((DisabledBomb) entity).applyEffect(gameView, this);
            }
            else if (entity instanceof PlaceBombInstantly && !isPowerUpActive(PlaceBombInstantly.class)) {
                ((PlaceBombInstantly) entity).applyEffect(gameView, this);
            }
            else if (entity instanceof Invincibility && !isPowerUpActive(Invincibility.class)) {
                ((Invincibility) entity).applyEffect(gameView, this);
            }
            else if (entity instanceof Barrier) {
                ((Barrier) entity).applyEffect(gameView, this);
            }
            else if (entity instanceof Ghost && !isPowerUpActive(Ghost.class)) {
                ((Ghost) entity).applyEffect(gameView, this);
            }
            else if (entity instanceof Detonator && !isPowerUpActive(Detonator.class)) {
                ((Detonator) entity).applyEffect(gameView, this);
            }
            gameView.gameModel.entities.remove(entity);
        }
    }

    /**
     * Check if given powerup type is currently active
     *
     * @param powerUpClass the powerup type
     * @return true or false
     */
    private boolean isPowerUpActive(Class<?> powerUpClass) {
        for (PowerUp activePowerUp : activePowerUps) {
            if (powerUpClass.isInstance(activePowerUp)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Place a bomb at the player's current position
     *
     * @return the placed bomb object, or null if bomb placement is not possible
     */
    public Bomb placeBomb(){
        currentGridX = (position.getPosX() + gameView.tileSize / 2) / gameView.tileSize;
        currentGridY = (position.getPosY() + gameView.tileSize / 2) / gameView.tileSize;
        if(!disabledBombActive && placedBombs.size() < availableBombs && isTileFree()){
            onBomb = true;
            Bomb bomb = new Bomb(gameView, this);

            bomb.position.setPosX((currentGridX * gameView.tileSize));
            bomb.position.setPosY((currentGridY * gameView.tileSize));

            bomb.currentGridX = this.currentGridX;
            bomb.currentGridY = this.currentGridY;

            placedBombs.add(bomb);
            return bomb;
        } else {
            return null;
        }
    }

    /**
     * Check if the delay for placing bombs is over
     *
     * @param currentTimeMillis The current system time in milliseconds
     * @return true if the delay is over, otherwise false
     */
    public boolean isDelayOver(long currentTimeMillis) {
        for (Bomb bomb : placedBombs) {
            if (bomb.placementTime + 200 > currentTimeMillis) {
                return false;
            }
        }
        return true;
    }

    /**
     * Place a wall at the player's current position
     *
     * @return the placed wall object, or null if wall placement is not possible
     */
    public Wall placeWall() {
        currentGridX = (position.getPosX() + gameView.tileSize / 2) / gameView.tileSize;
        currentGridY = (position.getPosY() + gameView.tileSize / 2) / gameView.tileSize;

        if(placeableWalls > 0 && placedWalls.size() < placeableWalls && isTileFree()){
            onBomb = true;
            Wall wall = new Wall(this);

            wall.position.setPosX((currentGridX * gameView.tileSize));
            wall.position.setPosY((currentGridY * gameView.tileSize));

            wall.currentGridX = this.currentGridX;
            wall.currentGridY = this.currentGridY;

            placedWalls.add(wall);
            return wall;
        } else {
            return null;
        }
    }

    /**
     * Check if the current tile is free to place objects (bombs or walls)
     *
     * @return true if the tile is free, otherwise false
     */
    public boolean isTileFree(){
        if((gameView.tileManager.mapTileNum[currentGridX][currentGridY] == 0) || checkSpriteOnTile() || checkEntityOnTile()) {
            return false;
        }
        return true;
    }

    /**
     * Check if there is a sprite occupying the current tile
     *
     * @return true if a sprite is on the tile, otherwise false
     */
    public boolean checkSpriteOnTile() {
        for (Sprite sprite : gameView.gameModel.sprites) {
            if(sprite.currentGridX == this.currentGridX && sprite.currentGridY == this.currentGridY && !(this == sprite)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if there is an entity occupying the current tile
     *
     * @return true if an entity is on the tile, otherwise false
     */
    public boolean checkEntityOnTile() {
        for (Entity entity : gameView.gameModel.entities) {
            if(entity.currentGridX == this.currentGridX && entity.currentGridY == this.currentGridY) {
                return true;
            }
        }
        return false;
    }

    /**
     * Change the player's speed by the specified factor
     *
     * @param factor the factor by which to change the speed
     */
    public void changeSpeed(double factor) {
        speed = (int)(speed * factor);
    }

    /**
     * Remove the active power-up of the specified type from the player
     *
     * @param powerUpType the class object representing the type of power-up to remove
     */
    public void removeActivePowerUp(Class<? extends PowerUp> powerUpType) {
        List<PowerUp> toRemove = new ArrayList<>();
        for (PowerUp powerUp : activePowerUps) {
            if (powerUp.getClass() == powerUpType) {
                toRemove.add(powerUp);
            }
        }
        activePowerUps.removeAll(toRemove);
    }
}
