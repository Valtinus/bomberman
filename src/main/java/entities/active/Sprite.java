package entities.active;

import entities.Position;
import logic.GameModel;
import view.GameView;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Sprite {
    public Position position;
    public int speed;
    public int originalSpeed;
    public boolean collision = false;
    public Rectangle hitbox;
    public int hitboxDefaultX;
    public int hitboxDefaultY;
    public int currentGridX;
    public int currentGridY;
    public boolean onBomb = false;
    public boolean isAlive = true;
    public boolean isInvincible = false;
    public boolean isInvincible2 = false;
    public boolean isInvincible3 = false;
    public boolean isGhost = false;
    public boolean isGhost2 = false;
    public boolean isGhost3 = false;

    public Direction direction;
    GameView gameView;
    public BufferedImage img;
    public BufferedImage ghostimg;
    public BufferedImage ghostimg2;
    public BufferedImage ghostimg3;
    Color invincibilityColor = new Color(245, 206, 66, 240);
    Color invincibilityColor2 = new Color(245, 206, 66, 160);
    Color invincibilityColor3 = new Color(245, 206, 66, 100);

    Color ghostColor = new Color(66, 72, 245, 240);
    Color ghostColor2 = new Color(66, 72, 245, 160);
    Color ghostColor3 = new Color(66, 72, 245, 100);




    public Sprite(GameView gameView){ this.gameView = gameView; }

    /**
     * Drawing sprites according to current state
     *
     * @param g2 graphics panel
     * @param gameModel game logic
     */
    public void drawEntity(Graphics2D g2, GameModel gameModel) {
        if(this.isInvincible) {
            if(this.isInvincible3) {
                g2.setColor(invincibilityColor3);
                g2.fillOval(position.getPosX(), position.getPosY(), gameView.tileSize, gameView.tileSize);
            }
            else if (this.isInvincible2) {
                g2.setColor(invincibilityColor2);
                g2.fillOval(position.getPosX(), position.getPosY(), (gameView.tileSize + (gameView.tileSize / 20)), (gameView.tileSize + (gameView.tileSize / 20)));
            }
            else {
                g2.setColor(invincibilityColor);
                g2.fillOval(position.getPosX(), position.getPosY(), (gameView.tileSize + (gameView.tileSize / 10)), (gameView.tileSize + (gameView.tileSize / 10)));
            }

            g2.drawImage(img, position.getPosX(), position.getPosY(), gameModel.gameView);
        }
        if (this.isGhost) {
            if(this.isGhost3) {
                g2.drawImage(ghostimg3, position.getPosX(), position.getPosY(), gameModel.gameView);
            }
            else if (this.isGhost2) {
                g2.drawImage(ghostimg2, position.getPosX(), position.getPosY(), gameModel.gameView);
            }
            else {
                g2.drawImage(ghostimg, position.getPosX(), position.getPosY(), gameModel.gameView);
            }
        }
        else {
            g2.drawImage(img, position.getPosX(), position.getPosY(), gameModel.gameView);
        }
    }

}
