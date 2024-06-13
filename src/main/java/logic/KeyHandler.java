package logic;

import entities.active.Direction;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

/**
 * Handling all the key events
 */
public class KeyHandler implements KeyListener {
    public Direction direction1;
    public Direction direction2;

    public boolean up1Pressed, down1Pressed, left1Pressed, right1Pressed;
    public boolean up2Pressed, down2Pressed, left2Pressed, right2Pressed;
    public boolean setBombPressed, setBombPressed2;
    public boolean setBarrierPressed, setBarrierPressed2;
    public int[] keyCodes;

    public KeyHandler(int[] keyCodes){
        this.keyCodes = keyCodes;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Key press event
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getExtendedKeyCode();
        if(keyCode == keyCodes[0]){
            up1Pressed = true;
            direction1 = Direction.UP;
        }
        else if(keyCode == keyCodes[1]){
            down1Pressed = true;
            direction1 = Direction.DOWN;
        }
        else if(keyCode == keyCodes[2]){
            left1Pressed = true;
            direction1 = Direction.LEFT;
        }
        else if(keyCode == keyCodes[3]){
            right1Pressed = true;
            direction1 = Direction.RIGHT;
        }
        else if(keyCode == keyCodes[6]){
            up2Pressed = true;
            direction2 = Direction.UP;
        }
        else if(keyCode == keyCodes[7]){
            down2Pressed = true;
            direction2 = Direction.DOWN;
        }
        else if(keyCode == keyCodes[8]){
            left2Pressed = true;
            direction2 = Direction.LEFT;
        }
        else if(keyCode == keyCodes[9]){
            right2Pressed = true;
            direction2 = Direction.RIGHT;
        }
        else if(keyCode == keyCodes[4]){
            setBombPressed = true;
        }
        else if(keyCode == keyCodes[10]){
            setBombPressed2 = true;
        }
        else if(keyCode == keyCodes[5]){
            setBarrierPressed = true;
        }
        else if(keyCode == keyCodes[11]){
            setBarrierPressed2 = true;
        }

    }

    /**
     * Key release event
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getExtendedKeyCode();
        if(keyCode == keyCodes[0]){
            up1Pressed = false;
        }
        else if(keyCode == keyCodes[1]){
            down1Pressed = false;
        }
        else if(keyCode == keyCodes[2]){
            left1Pressed = false;
        }
        else if(keyCode == keyCodes[3]){
            right1Pressed = false;
        }
        else if(keyCode == keyCodes[6]){
            up2Pressed = false;
        }
        else if(keyCode == keyCodes[7]){
            down2Pressed = false;
        }
        else if(keyCode == keyCodes[8]){
            left2Pressed = false;
        }
        else if(keyCode == keyCodes[9]){
            right2Pressed = false;
        }
        else if(keyCode == keyCodes[4]){
            setBombPressed = false;
        }
        else if(keyCode == keyCodes[10]){
            setBombPressed2 = false;
        }
        else if(keyCode == keyCodes[5]){
            setBarrierPressed = false;
        }
        else if(keyCode == keyCodes[11]) {
            setBarrierPressed2 = false;
        }
    }
}
