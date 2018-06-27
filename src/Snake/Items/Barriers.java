package Snake.Items;

import Snake.Snake.Position;
import Snake.Snake.Snake;

import java.util.ArrayList;
import java.util.Random;

public class Barriers {

    private ArrayList<Position> barriers;

    /**
     * create new barriers (5% of size of gameBoard)
     * @param width Width of GameBoard
     * @param height Height of GameBoard
     * @param amount Amount of barriers in percentage
     * @param snake Reference of class Snake
     */
    public Barriers(int width, int height, int amount, Snake snake, Food food) {
        barriers = new ArrayList<>();
        Random random = new Random();
        int posX;
        int posY;
        for (int i = 0; i < (width / 16) * (height / 16) / 100 * amount; i++ ){

            do {
                posX = (random.nextInt(width / Snake.getSize()))  * Snake.getSize();
                posY = (random.nextInt(height / Snake.getSize())) * Snake.getSize();
            } while (snake.isSame(posX, posY) || food.isSame(posX, posY));
            barriers.add(new Position(posX, posY));
        }
    }
    // compare position
    boolean isSame(int posX, int posY) {
        for (Position pos : barriers) {
            if (pos.getPosX() == posX && pos.getPosY() == posY)
                return true;
        }
        return false;
    }

    public ArrayList<Position> getBarriers() {
        return barriers;
    }
}
