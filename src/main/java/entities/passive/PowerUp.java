package entities.passive;

import entities.active.Player;
import view.GameView;

/**
 * Power up class with applyEffect method - Each power up overwrites the effect
 */
public abstract class PowerUp extends Entity {
    public abstract void applyEffect(GameView gameView, Player player);
}
