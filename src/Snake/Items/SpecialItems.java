package Snake.Items;

import Snake.Snake.Position;
import Snake.Snake.Snake;

import java.util.Random;

public class SpecialItems extends Thread {

    private Position specialItem;
    private int number;
    private String[] letters = {"R", "P", "P", "P", "R", "R", "R", "R", "R", "X", "X", "X", "X", "X", "X"};

    /**
     * create new barriers (5% of size of gameBoard)
     * @param width Width of GameBoard
     * @param height Height of GameBoard
     * @param snake Reference of class Snake
     * @param food Reference of class Food
     * @param barriers Reference of class Barriers
     */
    public SpecialItems(int width, int height, Snake snake, Food food, Barriers barriers) {
        Random random = new Random();
        int posX;
        int posY;
        do {
            posX = (random.nextInt(width / Snake.getSize()))  * Snake.getSize();
            posY = (random.nextInt(height / Snake.getSize())) * Snake.getSize();
        } while (snake.isSame(posX, posY) || food.isSame(posX, posY) || barriers.isSame(posX, posY));

        specialItem = new Position(posX, posY);
        number = random.nextInt(letters.length);
    }

    boolean isSame(int posX, int posY) {
        return (this.getPosX() == posX && this.getPosY() == posY);
    }

    public int getPosX() {
        return specialItem.getPosX();
    }

    public int getPosY() {
        return specialItem.getPosY();
    }

    public String getLetter(){
        return letters[number];
    }

    public int getNumber(){
        return number;
    }
}
