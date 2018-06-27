package Snake.Items;

import Snake.Snake.Snake;

import java.util.Random;

public class Food {

    private int posX;
    private int posY;
    private boolean isBigFood;

    /**
     * create new food
     * calculate position of it
     * with 20% it is 'bigFood'
     * @param snake Reference of class Snake
     * @param barriers Reference of class Barriers
     * @param special Reference of class SpecialItems
     * @param width Width of GameBoard
     * @param height Height of GameBoard
     */
    public Food(Snake snake, Barriers barriers, SpecialItems special, int width, int height){
        Random random = new Random();
        boolean temp;

        do {
            posX = (random.nextInt(width / Snake.getSize()))  * Snake.getSize();
            posY = (random.nextInt(height / Snake.getSize())) * Snake.getSize();

            temp = snake.isSame(posX, posY);
            try {
                if (barriers.isSame(getPosX(), getPosY())){
                    temp = true;
                    continue;
                }
                if (special.isSame(posX, posY))
                    temp = true;
            } catch (NullPointerException a){
                // nothing to do
            }
        } while (temp);
        isBigFood = random.nextInt(10) >= 8;
    }

    // compare position
    boolean isSame(int posX, int posY) {
        return (this.posX == posX && this.posY == posY);
    }

    // getter
    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public boolean isBigFood() {
        return isBigFood;
    }


}
