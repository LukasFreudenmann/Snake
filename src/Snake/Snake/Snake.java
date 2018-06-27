package Snake.Snake;

import Snake.Items.Direction;

import java.util.ArrayList;
import java.util.Collections;

public class Snake {

    private ArrayList<SnakePart> snake;
    private static final int size = 22;

    /**
     * creates new snake with length of 4
     * @param width Width of GameBoard
     * @param height Height of GameBoard
     */
    public Snake(int width, int height) {
        snake = new ArrayList<>();
        snake.add(new SnakePart(width / 2, height / 2,"EAST"));
        snake.add(new SnakePart(width / 2 - size, height / 2,"EAST"));
        snake.add(new SnakePart(width / 2 - (size * 2), height / 2,"EAST"));
        snake.add(new SnakePart(width / 2 - (size * 3), height / 2,"EAST"));
    }

    /**
     * moves the snake and add a new bodyPart
     */
    public void addPart() {
        snake.add(new SnakePart(0,0,"EAST"));
    }

    /**
     *  moves the snake
     * @param direction Direction snake should move
     */
    public void move(String direction) {
        int newPosX;
        int newPosY;
        String newDirection;
        for (int i = snake.size() - 1; i > 0; i--) {
            newPosX = snake.get(i - 1).getPosition().getPosX();
            newPosY = snake.get(i - 1).getPosition().getPosY();
            newDirection = snake.get(i - 1).getDirection();
            snake.set(i, new SnakePart(newPosX, newPosY, newDirection));
        }
        newPosX = snake.get(0).getPosition().getPosX() + Direction.valueOf(direction).getDirection()[0];
        newPosY = snake.get(0).getPosition().getPosY() + Direction.valueOf(direction).getDirection()[1];
        snake.set(0, new SnakePart(newPosX, newPosY, direction));
    }

    /**
     * turn snake around
     */
    public void reverse(){
        Collections.reverse(snake);
    }

    /**
     * delete 2 last bodyPart of snake
     */
    public void delete(){
        if (snake.size() >= 4) {
            snake.subList(snake.size() / 2, snake.size()).clear();
        }
    }

    // compare position
    public boolean isSame(int posX, int posY) {
        for (SnakePart part : snake) {
            if (part.getPosition().getPosX() == posX && part.getPosition().getPosY() == posY) {
                return true;
            }
        }
        return false;
    }

    // getter
    public int getLength() {
        return snake.size();
    }

    public Position getSnake(int index) {
        return snake.get(index).getPosition();
    }

    public Position getHead() {
        return snake.get(0).getPosition();
    }

    public String getHeadDirection(){
        return snake.get(0).getDirection();
    }

    public static int getSize(){
        return size;
    }
}