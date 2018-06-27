package Snake.Snake;

public class Position {

    private int posX;
    private int posY;

    /**
     * constructor of position
     * class to save coordinates
     * @param posX X - Coordinate
     * @param posY  Y - Coordinate
     */
    public Position(int posX, int posY){
        this.posX = posX;
        this.posY = posY;
    }

    // getter
    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}
