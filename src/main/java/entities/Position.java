package entities;

public class Position {
     int posX;
     int posY;

    /**
     * Position with X and Y coordinates
     */
    public Position() {
        this.posX = 0;
        this.posY = 0;
    }

    /**
     * Getter for position X
     *
     * @return  X value
     */
    public int getPosX() {
        return posX;
    }

    /**
     * Getter for position Y
     *
     * @return  Y value
     */
    public int getPosY() {
        return posY;
    }

    /**
     * Setter for position X
     *
     * @param posX  X value
     */
    public void setPosX(int posX) {
        this.posX = posX;
    }

    /**
     * Setter for position Y
     *
     * @param posY  Y value
     */
    public void setPosY(int posY) {
        this.posY = posY;
    }
}
