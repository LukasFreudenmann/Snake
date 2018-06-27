package Snake;

import Snake.Display.ColorScheme;
import Snake.Display.Score;
import Snake.Items.*;
import Snake.Display.Display;
import Snake.Snake.Position;
import Snake.Snake.Snake;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GameEngine implements Runnable {

    private Display display;
    private GameBoard field;
    private Snake snake;
    private Food food;
    private Barriers barriers;
    private SpecialItems specialItems;
    private Score score;
    private Stage window;

    private String direction = "EAST";

    private boolean isDirectionChanged = false;
    private boolean running = false;
    private boolean isPaused = false;
    private boolean endPaused = false;
    private boolean isReset = true;

    private static int widthField;   // standard value
    private static int heightField;  // standard value
    private static String color = "BLUE";
    private static int snakeSpeed = 125; // in ms
    private static boolean barrier = false;
    private static boolean specialItem = false;

    /**
     * constructor to setup a new game and change the size of the field
     * @param window current Stage
     * @param width Width of GameBoard (x16)
     * @param height Height of GameBoard (x16)
     */
    public GameEngine(Stage window, int width, int height) {
        GameEngine.widthField = width * Snake.getSize();
        GameEngine.heightField = height * Snake.getSize();
        this.window = window;
        display = new Display(this, width, height);
        new GameEngine(window);
    }

    /**
     * constructor to setup a new Game
     * @param window current Stage
     */
    public GameEngine(Stage window){
        this.window = window;
        display = new Display(this, widthField, heightField);
        field = new GameBoard(widthField, heightField);
        init();
    }

    /**
     * initialises new Game,
     * creates new Snake, Food, Barrier
     * reset all variables
     */
    public void init(){
        snake = new Snake(field.getWidth(), field.getHeight());
        food = new Food(snake, barriers, specialItems, field.getWidth(), field.getHeight());
        if (barrier)
            barriers = new Barriers(field.getWidth(), field.getHeight(), 5, snake, food);
        else if (specialItem) {
            barriers = new Barriers(field.getWidth(), field.getHeight(), 2, snake, food);
            specialItems = new SpecialItems(field.getWidth(), field.getHeight(), snake, food, barriers);
        } else
            barriers = new Barriers(field.getWidth(), field.getHeight(), 0, snake, food);
        score = new Score();
        direction = "EAST";
        running = false;
        isPaused = false;
        endPaused = false;
        isReset = true;
        render();
        setOverlay("Press 'SPACE' or 'ENTER' to start the game");
        display.updateMenuStart();
    }

    /**
     * starts the Game
     * creates new Thread
     */
    public void start(){
        if (running){
            return;
        }
        running = true;
        display.setOverlay("GO!");
        display.updateMenuStart();
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * runs the game, with delay
     * move snake
     * checks for collision
     * shows end-pause-screen
     */
    public synchronized void run() {
        waitSeconds(1);
        setOverlay("");
        while (running) {
            if(isPaused) {
                waitSeconds(1);
                if (endPaused) {
                    // creates countdown before pause ends
                    for (int i = 3; i >= 0; i--){
                        if(i == 0){
                            setOverlay("GO!");
                        } else {
                            setOverlay(" " + String.valueOf(i) + " ");
                        }
                        waitSeconds(1);
                    }
                    // setup for pause end
                    setOverlay("");
                    endPaused = false;
                    isPaused = false;
                }
            // normal loop
            } else {
                // checks for food and adds points
                // move snake and creates new food
                if (eatFood()) {
                    if (food.isBigFood())
                        score.addScore(5);
                    else
                        score.addScore();
                    updateScoreDisplay();
                    food = new Food(snake, barriers, specialItems, field.getWidth(), field.getHeight());
                    snake.addPart();
                }
                snake.move(direction);
                // checks for specialItem
                if (getSpecialItem()){
                    specialAction();
                    specialItems = new SpecialItems(field.getWidth(), field.getHeight(), snake, food, barriers);
                }
                // render
                render();
                // check for collision
                if (isCollide()) {
                    running = false;
                }
                isDirectionChanged = false;
            }
            // wait
            try {
                wait(snakeSpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stop();
    }

    /**
     * stops the game
     * shows new Score-Screen
     */
    private void stop(){
        isReset = false;
        setOverlay("SCORE: " + score.getScore() + " Points" + "\n" + "\n" + "to reset game press 'R' or 'Enter'");
        display.updateMenuStart();
    }

    /**
     * starts new pause
     * shows new Overlay
     */
    public void startPause(){
        isPaused = true;
    }

    /**
     * ends pause
     */
    public void endPause() {
        this.isPaused = false;
        this.endPaused = true;
    }

    /**
     * method to wait a specific time
     * @param time Time in Seconds
     */
    private synchronized void waitSeconds(double time){
        try {
            wait((long) time * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * renders the whole surface
     */
    private void render(){
        Canvas canvas = display.getCanvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // reset all
        gc.clearRect(0,0, field.getWidth(), field.getHeight());

        // render Food
        if (food.isBigFood()) {
            gc.setFill(ColorScheme.valueOf(color).big());
        } else {
            gc.setFill(ColorScheme.valueOf(color).food());
        }
        gc.fillRect(food.getPosX(), food.getPosY(), Snake.getSize(), Snake.getSize());
        // render head on snake
        gc.setFill(ColorScheme.valueOf(color).head());
        gc.fillRect(snake.getHead().getPosX(), snake.getHead().getPosY(), Snake.getSize(), Snake.getSize());
        // render rest of snake
        gc.setFill(ColorScheme.valueOf(color).body());
        for (int i = 1; i < snake.getLength(); i++){
            gc.fillRect(snake.getSnake(i).getPosX(), snake.getSnake(i).getPosY(), Snake.getSize(),Snake.getSize());
        }
        // render barriers
        gc.setFill(Color.WHITE);
        for (Position pos : barriers.getBarriers())
            gc.fillRect(pos.getPosX(), pos.getPosY(), Snake.getSize(), Snake.getSize());

        try {
            gc.setFill(Color.TOMATO);
            gc.fillRect(specialItems.getPosX(), specialItems.getPosY(), Snake.getSize(), Snake.getSize());
            gc.setFill(Color.BLACK);
            gc.setFont(Font.font(null, FontWeight.BOLD, 18));
            gc.fillText(specialItems.getLetter(), specialItems.getPosX() + 4, specialItems.getPosY() + 15);
            display.setCanvas(canvas);
        } catch (NullPointerException a) {
            // nothing to do
        }

    }

    /**
     * set overlay-screen over the current game
     * @param text Text of screen
     */
    private void setOverlay(String text){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                display.setOverlay(text);
            }
        });
    }

    /**
     * updates the score indicator
     */
    private void updateScoreDisplay(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                display.setScore(score.getScore());
            }
        });
    }

    /**
     * change direction of snake
     * prevents 180°  turn
     * @param direction Direction snake should move
     */
    public synchronized void setDirection(String direction){
        if (isDirectionChanged || isPaused) {
            return;
        } else if (this.direction.equals("NORTH") && direction.equals("SOUTH")){
            return;
        } else if (this.direction.equals("EAST") && direction.equals("WEST")){
            return;
        } else if (this.direction.equals("SOUTH") && direction.equals("NORTH")){
            return;
        } else if (this.direction.equals("WEST") && direction.equals("EAST")){
            return;
        }
        this.direction = direction;
        isDirectionChanged = true;
    }

    /**
     * check if snake collides with something
     * @return true if snake collided with something
     */
    private boolean isCollide(){
        // check snake is outside the field
        if (snake.getHead().getPosX() < 0 || snake.getHead().getPosX() >= field.getWidth()){
            return true;
        } else if (snake.getHead().getPosY() < 0 || snake.getHead().getPosY() >= field.getHeight()){
            return true;
        }
        // checks snake collides with itself
        for (int i = 1; i < snake.getLength(); i++){
            if (snake.getSnake(i).getPosX() == snake.getHead().getPosX()
                    && snake.getSnake(i).getPosY() == snake.getHead().getPosY()){
                return true;
            }
        }
        // checks snake collides with a barrier
        try {
            for (Position pos : barriers.getBarriers()){
                if (snake.getHead().getPosX() == pos.getPosX() && snake.getHead().getPosY() == pos.getPosY()){
                    return true;
                }
            }
        } catch (NullPointerException a) {
            // nothing to do
        }

        return false;
    }

    /**
     * checks snake collides with food
     * @return true if snake collided with food
     */
    private boolean eatFood(){
        return (snake.getHead().getPosX() == food.getPosX() && snake.getHead().getPosY() == food.getPosY());
    }

    /**
     * checks snake collides with specialItem
     * @return true if snake collided with specialItem
     */
    private boolean getSpecialItem(){
        try {
            return snake.getHead().getPosX() == specialItems.getPosX() && snake.getHead().getPosY() == specialItems.getPosY();
        } catch (NullPointerException a){
            return false;
        }
    }

    /**
     * special action for SpecialItem
     */
    private void specialAction(){
        if (specialItems.getNumber() == 0){
            snake.delete();
        } else if (specialItems.getNumber() >= 1 && specialItems.getNumber() <= 3){
            score.addScore(10);
            updateScoreDisplay();
        } else if (specialItems.getNumber() >= 4 && specialItems.getNumber() <= 8){
            snake.reverse();
            turnDirection();
        }
    }

    /**
     * turn around direction of snake
     */
    private void turnDirection(){
        switch (snake.getHeadDirection()) {
            case "NORTH":
                direction = "SOUTH";
                break;
            case "SOUTH":
                direction = "NORTH";
                break;
            case "EAST":
                direction = "WEST";
                break;
            case "WEST":
                direction = "EAST";
                break;
        }
    }

    /**
     * change color of snake and food
     * and renders the surface
     * @param color Color of snake/food
     */
    public void changeColor(String color){
        GameEngine.color = color;
        render();
    }

    // getter
    public Stage getWindow() {
        return window;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isEndPaused() {
        return endPaused;
    }

    public boolean isReset() {
        return isReset;
    }

    // static
    public static void setSnakeSpeed(int snakeSpeed) {
        GameEngine.snakeSpeed = snakeSpeed;
    }

    public static void setBarrier(boolean barrier) {
        GameEngine.barrier = barrier;
    }

    public static void setSpecialItems(boolean specialItems) {
        GameEngine.specialItem = specialItems;
    }

    public static String getColor() {
        return color;
    }

    public static int getSnakeSpeed() {
        return snakeSpeed;
    }

    public static int getWidth() {
        return widthField / Snake.getSize();
    }
}