package logic;

import entities.active.Monster;
import entities.active.Player;
import entities.active.Sprite;
import entities.passive.Bomb;
import entities.passive.Entity;
import entities.passive.Explosion;
import entities.passive.Wall;
import view.GameView;

public class CollisionChecker {
    GameView gameView;

    public CollisionChecker(GameView gameView) {
        this.gameView = gameView;
    }

    /**
     * Check collision with map tiles for the given sprite
     *
     * @param sprite the sprite (player or monster)
     */
    public void checkTile(Sprite sprite) {
        int spriteLeftPosX = sprite.position.getPosX() + sprite.hitbox.x;
        int spriteRightPosX = sprite.position.getPosX() + sprite.hitbox.x + sprite.hitbox.width - 1;
        int spriteTopPosY = sprite.position.getPosY() + sprite.hitbox.y;
        int spriteBottomPosY = sprite.position.getPosY() + sprite.hitbox.y + sprite.hitbox.height - 1;

        int spriteLeftCol = spriteLeftPosX / gameView.tileSize;
        int spriteRightCol = spriteRightPosX / gameView.tileSize;
        int spriteTopRow = spriteTopPosY / gameView.tileSize;
        int spriteBottomRow = spriteBottomPosY / gameView.tileSize;

        int tileNum1;
        int tileNum2;

        switch (sprite.direction) {
            case UP:
                spriteTopRow = (int) Math.floor((spriteTopPosY - sprite.speed) / gameView.tileSize);
                if (spriteTopRow >= 0 && spriteTopRow < gameView.tileManager.mapTileNum[0].length) {
                    tileNum1 = gameView.tileManager.mapTileNum[spriteLeftCol][spriteTopRow];
                    tileNum2 = gameView.tileManager.mapTileNum[spriteRightCol][spriteTopRow];
                    if (gameView.tileManager.tiles[tileNum1].collision || gameView.tileManager.tiles[tileNum2].collision) {
                        sprite.collision = true;
                    }
                }
                break;
            case DOWN:
                spriteBottomRow = (int) Math.floor((spriteBottomPosY + sprite.speed) / gameView.tileSize);
                if (spriteBottomRow >= 0 && spriteBottomRow < gameView.tileManager.mapTileNum[0].length) {
                    tileNum1 = gameView.tileManager.mapTileNum[spriteLeftCol][spriteBottomRow];
                    tileNum2 = gameView.tileManager.mapTileNum[spriteRightCol][spriteBottomRow];
                    if (gameView.tileManager.tiles[tileNum1].collision || gameView.tileManager.tiles[tileNum2].collision) {
                        sprite.collision = true;
                    }
                }
                break;
            case LEFT:
                spriteLeftCol = (int) Math.floor((spriteLeftPosX - sprite.speed) / gameView.tileSize);
                if (spriteLeftCol >= 0 && spriteLeftCol < gameView.tileManager.mapTileNum.length) {
                    tileNum1 = gameView.tileManager.mapTileNum[spriteLeftCol][spriteTopRow];
                    tileNum2 = gameView.tileManager.mapTileNum[spriteLeftCol][spriteBottomRow];
                    if (gameView.tileManager.tiles[tileNum1].collision || gameView.tileManager.tiles[tileNum2].collision) {
                        sprite.collision = true;
                    }
                }
                break;
            case RIGHT:
                spriteRightCol = (int) Math.floor((spriteRightPosX + sprite.speed) / gameView.tileSize);
                if (spriteRightCol >= 0 && spriteRightCol < gameView.tileManager.mapTileNum.length) {
                    tileNum1 = gameView.tileManager.mapTileNum[spriteRightCol][spriteTopRow];
                    tileNum2 = gameView.tileManager.mapTileNum[spriteRightCol][spriteBottomRow];
                    if (gameView.tileManager.tiles[tileNum1].collision || gameView.tileManager.tiles[tileNum2].collision) {
                        sprite.collision = true;
                    }
                }
                break;
        }
    }

    /**
     * Check collision with entities for the given sprite
     *
     * @param sprite the given sprite
     * @param player is the sprite a player
     * @return the collided entity (or null)
     */
    public Entity checkEntity(Sprite sprite, boolean player) {
        Entity collidedEntity = null;

        for (Entity entity : gameView.gameModel.entities) {
            if (entity != null) {
                sprite.hitbox.x = sprite.position.getPosX() + sprite.hitbox.x;
                sprite.hitbox.y = sprite.position.getPosY() + sprite.hitbox.y;

                entity.hitbox.x = entity.position.getPosX() + entity.hitbox.x;
                entity.hitbox.y = entity.position.getPosY() + entity.hitbox.y;

                switch (sprite.direction) {
                    case UP:
                        sprite.hitbox.y -= sprite.speed;
                        if (sprite.hitbox.intersects(entity.hitbox)) {
                            if (sprite.onBomb && (entity instanceof Bomb || entity instanceof Wall)) {
                                collidedEntity = entity;
                            } else {
                                if (entity.collision) {
                                    sprite.collision = true;
                                }
                                if (player) {
                                    collidedEntity = entity;
                                }
                            }
                            if (entity instanceof Explosion && !sprite.isInvincible) {
                                sprite.isAlive = false;
                            }
                        }
                        break;
                    case DOWN:
                        sprite.hitbox.y += sprite.speed;
                        if (sprite.hitbox.intersects(entity.hitbox)) {
                            if (sprite.onBomb && (entity instanceof Bomb || entity instanceof Wall)) {
                                collidedEntity = entity;
                            } else {
                                if (entity.collision) {
                                    sprite.collision = true;
                                }
                                if (player) {
                                    collidedEntity = entity;
                                }
                            }
                            if (entity instanceof Explosion && !sprite.isInvincible) {
                                sprite.isAlive = false;
                            }
                        }
                        break;
                    case LEFT:
                        sprite.hitbox.x -= sprite.speed;
                        if (sprite.hitbox.intersects(entity.hitbox)) {
                            if (sprite.onBomb && (entity instanceof Bomb || entity instanceof Wall)) {
                                collidedEntity = entity;
                            } else {
                                if (entity.collision) {
                                    sprite.collision = true;
                                }
                                if (player) {
                                    collidedEntity = entity;
                                }
                            }
                            if (entity instanceof Explosion && !sprite.isInvincible) {
                                sprite.isAlive = false;
                            }
                        }
                        break;
                    case RIGHT:
                        sprite.hitbox.x += sprite.speed;
                        if (sprite.hitbox.intersects(entity.hitbox)) {
                            if (sprite.onBomb && (entity instanceof Bomb || entity instanceof Wall)) {
                                collidedEntity = entity;
                            } else {
                                if (entity.collision) {
                                    sprite.collision = true;
                                }
                                if (player) {
                                    collidedEntity = entity;
                                }
                            }
                            if (entity instanceof Explosion && !sprite.isInvincible) {
                                sprite.isAlive = false;
                            }
                        }
                        break;
                }
                sprite.hitbox.x = sprite.hitboxDefaultX;
                sprite.hitbox.y = sprite.hitboxDefaultY;

                entity.hitbox.x = entity.hitboxDefaultX;
                entity.hitbox.y = entity.hitboxDefaultY;
            }
        }
        return collidedEntity;
    }

    /**
     * Check collision between sprites
     *
     * @param sprite the sprite
     */
    public void checkSpriteCollision(Sprite sprite) {
        for (Sprite collidingSprite : gameView.gameModel.sprites) {
            if (collidingSprite != null && !collidingSprite.equals(sprite)) {
                if (((sprite instanceof Player && collidingSprite instanceof Monster) && !sprite.isInvincible) ||
                        ((sprite instanceof Monster && collidingSprite instanceof Player) && !collidingSprite.isInvincible)) {
                    sprite.hitbox.x = sprite.position.getPosX() + sprite.hitbox.x;
                    sprite.hitbox.y = sprite.position.getPosY() + sprite.hitbox.y;

                    collidingSprite.hitbox.x = collidingSprite.position.getPosX() + collidingSprite.hitbox.x;
                    collidingSprite.hitbox.y = collidingSprite.position.getPosY() + collidingSprite.hitbox.y;

                    switch (sprite.direction) {
                        case UP:
                            sprite.hitbox.y -= sprite.speed;
                            if (sprite.hitbox.intersects(collidingSprite.hitbox)) {
                                setPlayerAlive(sprite);
                                setPlayerAlive(collidingSprite);
                            }
                            break;
                        case DOWN:
                            sprite.hitbox.y += sprite.speed;
                            if (sprite.hitbox.intersects(collidingSprite.hitbox)) {
                                setPlayerAlive(sprite);
                                setPlayerAlive(collidingSprite);
                            }
                            break;
                        case LEFT:
                            sprite.hitbox.x -= sprite.speed;
                            if (sprite.hitbox.intersects(collidingSprite.hitbox)) {
                                setPlayerAlive(sprite);
                                setPlayerAlive(collidingSprite);
                            }
                            break;
                        case RIGHT:
                            sprite.hitbox.x += sprite.speed;
                            if (sprite.hitbox.intersects(collidingSprite.hitbox)) {
                                setPlayerAlive(sprite);
                                setPlayerAlive(collidingSprite);
                            }
                            break;
                    }
                    sprite.hitbox.x = sprite.hitboxDefaultX;
                    sprite.hitbox.y = sprite.hitboxDefaultY;

                    collidingSprite.hitbox.x = collidingSprite.hitboxDefaultX;
                    collidingSprite.hitbox.y = collidingSprite.hitboxDefaultY;
                }
            }
        }
    }

    /**
     * Set a player sprite as not alive
     *
     * @param sprite the sprite we want to set as not alive
     */
    public void setPlayerAlive(Sprite sprite) {
        if (sprite instanceof Player) {
            sprite.isAlive = false;
        }
    }

    /**
     * Check if the sprite has reached the edge of the map
     *
     * @param sprite the sprite
     * @return true or false if the sprite would reach (or go past) the edge with the next movement
     */
    public boolean reachedEdge(Sprite sprite) {
        int nextLeftPosX = sprite.position.getPosX() + sprite.hitbox.x;
        int nextRightPosX = sprite.position.getPosX() + sprite.hitbox.x + sprite.hitbox.width;
        int nextTopPosY = sprite.position.getPosY() + sprite.hitbox.y;
        int nextBottomPosY = sprite.position.getPosY() + sprite.hitbox.y + sprite.hitbox.height;

        switch (sprite.direction) {
            case UP:
                nextTopPosY -= sprite.speed;
                break;
            case DOWN:
                nextBottomPosY += sprite.speed;
                break;
            case LEFT:
                nextLeftPosX -= sprite.speed;
                break;
            case RIGHT:
                nextRightPosX += sprite.speed;
                break;
        }
        if (nextLeftPosX < 0 || nextRightPosX > gameView.screenWidth ||
                nextTopPosY < 0 || nextBottomPosY > gameView.screenHeight) {
            return true;
        }
        return false;
    }

    /**
     * Check if given sprite is colliding with bomb
     *
     * @param sprite the given sprite
     * @return the collided entity (or null)
     */
    public boolean isCollidingWithBomb(Sprite sprite) {
        boolean isCollidingWithBomb = false;

        for (Entity entity : gameView.gameModel.entities) {
            if (entity instanceof Bomb) {
                sprite.hitbox.x = sprite.position.getPosX() + sprite.hitbox.x;
                sprite.hitbox.y = sprite.position.getPosY() + sprite.hitbox.y;

                entity.hitbox.x = entity.position.getPosX() + entity.hitbox.x;
                entity.hitbox.y = entity.position.getPosY() + entity.hitbox.y;

                switch (sprite.direction) {
                    case UP:
                        sprite.hitbox.y -= sprite.speed;
                        if (sprite.hitbox.intersects(entity.hitbox)) {
                            isCollidingWithBomb = true;
                        }
                        break;
                    case DOWN:
                        sprite.hitbox.y += sprite.speed;
                        if (sprite.hitbox.intersects(entity.hitbox)) {
                            isCollidingWithBomb = true;
                        }
                        break;
                    case LEFT:
                        sprite.hitbox.x -= sprite.speed;
                        if (sprite.hitbox.intersects(entity.hitbox)) {
                            isCollidingWithBomb = true;
                        }
                        break;
                    case RIGHT:
                        sprite.hitbox.x += sprite.speed;
                        if (sprite.hitbox.intersects(entity.hitbox)) {
                            isCollidingWithBomb = true;
                        }
                        break;
                }
                sprite.hitbox.x = sprite.hitboxDefaultX;
                sprite.hitbox.y = sprite.hitboxDefaultY;

                entity.hitbox.x = entity.hitboxDefaultX;
                entity.hitbox.y = entity.hitboxDefaultY;
            }
        }
        return isCollidingWithBomb;
    }
}