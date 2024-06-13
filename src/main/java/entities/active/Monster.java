package entities.active;

import entities.Position;
import logic.GameModel;
import view.GameView;

import java.awt.*;
/**
 * Monster class -  parent class for all monsters
 */

public abstract class Monster extends Sprite {
    /**
     * Constructor for the monster class
     * @param gameView the GameView object associated with the monsters
     */
    public Monster(GameView gameView){
        super(gameView);
        position = new Position();

    }
    public abstract void moveMonster();

}
