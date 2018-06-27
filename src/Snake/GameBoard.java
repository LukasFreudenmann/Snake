package Snake;

public class GameBoard {

    private int height;
    private int width;

    /**
     * creates new GameBoard to save width and height
     */
    public GameBoard(int width, int height) {
        this.height = height;
        this.width = width;
    }

    // getter
    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
