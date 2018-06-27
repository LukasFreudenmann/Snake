package Snake.Display;

import Snake.GameEngine;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Display{

    private GameEngine gameEngine;
    private Stage window;
    private Canvas canvas;
    private Label overlay;
    private Label score;
    private StackPane gameField;
    private Menu start;
    private MenuItem startGame;

    // size of gameBoard
    private int width, height;

    private static String background = "black";
    private static String scoreColor = "rgba(255, 0, 0, 0.5)";


    /**
     * constructor of the game
     * @param gameEngine reference from class GameEngine
     * @param width of field
     * @param height of field
     */
    public Display(GameEngine gameEngine, int width, int height){
        this.gameEngine = gameEngine;
        this.window = gameEngine.getWindow();
        // size of gameBoard
        this.width = width;
        this.height = height;
        createDisplay();
    }

    /**
     * creates the display of the game
     */
    private void createDisplay(){
        // gameBoard - screen
        canvas = new Canvas();
        canvas.setWidth(width);
        canvas.setHeight(height);

        // overlay for text
        overlay = new Label();
        // overlay for score
        score = new Label("Your Score: 0");
        score.setStyle("-fx-text-fill: " + scoreColor + "; -fx-font-size: 15px; -fx-font-weight: bold");
        score.setTranslateX(-width / 2 + 55);
        score.setTranslateY(-height / 2 + 20);

        gameField = new StackPane();
        gameField.setStyle("-fx-background-color: " + background);
        gameField.setAlignment(Pos.CENTER);
        gameField.getChildren().addAll(canvas, overlay, score);

        // layout
        VBox layout = new VBox();
        layout.getChildren().addAll(createMenuBar(), gameField);

        //scene
        Scene scene = new Scene(layout);
        // set keyPressed options
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();
                if (gameEngine.isRunning()) {
                    if (code == KeyCode.LEFT || code == KeyCode.A)
                        gameEngine.setDirection("WEST");
                    else if (code == KeyCode.UP || code == KeyCode.W)
                        gameEngine.setDirection("NORTH");
                    else if (code == KeyCode.RIGHT || code == KeyCode.D)
                        gameEngine.setDirection("EAST");
                    else if (code == KeyCode.DOWN || code == KeyCode.S)
                        gameEngine.setDirection("SOUTH");
                    else if (code == KeyCode.SPACE || code == KeyCode.P || code == KeyCode.ENTER) {
                        if (gameEngine.isEndPaused()){
                            // not allowed to do something
                        } else if (gameEngine.isPaused()){
                            gameEngine.endPause();
                        } else {
                            gameEngine.startPause();
                            setOverlay("GAME IS PAUSED!");
                        }
                    } else if (code == KeyCode.ESCAPE){
                        gameEngine.startPause();
                        setOverlay("GAME IS PAUSED!");
                        start.show();
                    }
                } else if (!gameEngine.isReset()){
                    if (code == KeyCode.ENTER || code == KeyCode.R)
                        gameEngine.init();
                } else if (gameEngine.isReset() && !gameEngine.isRunning()){
                    if (code == KeyCode.ENTER || code == KeyCode.SPACE)
                        gameEngine.start();
                }
            }
        });
        window.setScene(scene);
        window.setTitle("Snake");
        window.setResizable(false);
        window.setOnCloseRequest(e -> System.exit(1));
        window.show();
    }

    /**
     * creates the menuBar
     * @return menuBar
     */
    private MenuBar createMenuBar(){

        MenuBar menuBar = new MenuBar();

        // help
        Menu help = new Menu("Help");
        MenuItem findHelp = new MenuItem("Help");
        MenuItem about = new MenuItem("About");
        // setOnAction
        findHelp.setOnAction(e -> {
            if (gameEngine.isRunning()) {
                gameEngine.startPause();
            }
            overlay.setText("Controlling:       \n" +
                    "[W] or [UP]    Move UP \n" +
                    "[A] or [LEFT]  Move LEFT \n" +
                    "[D] or [RIGHT] Move RIGHT \n" +
                    "[S] or [DOWN]  Move DOWN \n" +
                    "[P] or [SPACE] Pause GAME \n" +
                    "[ENTER] Start GAME");
            overlay.setStyle("-fx-text-fill: black; -fx-background-color: grey; -fx-padding: 6px;" +
                    " -fx-font-size: 20px; -fx-font-weight: bold; -fx-text-alignment: left");
        });
        about.setOnAction(e -> {
            if (gameEngine.isRunning()) {
                gameEngine.startPause();
            }
            overlay.setText("Game build by: \n" +
                    "Lukas Freudenmann \n" +
                    "\n" +
                    "Version 1.0");
            overlay.setStyle("-fx-text-fill: black; -fx-background-color: grey; -fx-padding: 6px;" +
                    " -fx-font-size: 20px; -fx-font-weight: bold; -fx-text-alignment: center");
        });
        help.getItems().addAll(findHelp, about);

        // add all to menuBar
        menuBar.getMenus().addAll(startMenu(), settingsMenu(), modeMenu(), sizeMenu(), difficultyMenu(), help);
        return menuBar;
    }

    /**
     * setup start-Menu
     * includes start, restart and exit
     */
    private Menu startMenu(){
        start = new Menu("Start");
        // MenuItems
        startGame = new MenuItem("Start");
        MenuItem restart = new MenuItem("Restart");
        MenuItem exit = new MenuItem("Exit");
        // setOnAction
        restart.setOnAction(e -> new GameEngine(window));
        exit.setOnAction(e -> System.exit(1));
        startGame.setOnAction(e -> gameEngine.start());

        updateMenuStart();
        start.getItems().addAll(startGame, restart, exit);

        return start;
    }

    /**
     * setup settings-Menu
     * DarkTheme, LightTheme
     * Color - blue, red, orange, green, purple
     */
    private Menu settingsMenu(){
        Menu settings = new Menu("Settings");

        // Theme
        RadioMenuItem darkTheme = new RadioMenuItem("DarkTheme");
        RadioMenuItem lightTheme = new RadioMenuItem("LightTheme");
        if (background.equals("black"))
            darkTheme.setSelected(true);
        if (background.equals("lightgray"))
            lightTheme.setSelected(true);
        ToggleGroup themeGroup = new ToggleGroup();
        darkTheme.setToggleGroup(themeGroup);
        lightTheme.setToggleGroup(themeGroup);
        // setOnAction
        darkTheme.setOnAction(e -> {
            background = "black";
            gameField.setStyle("-fx-background-color: " + background);
        });
        lightTheme.setOnAction(e -> {
            background = "lightgray";
            gameField.setStyle("-fx-background-color: " + background);
        });

        SeparatorMenuItem separatorSettings = new SeparatorMenuItem();

        // Color Snake&Food
        RadioMenuItem blue = new RadioMenuItem("Blue");
        RadioMenuItem red = new RadioMenuItem("Red");
        RadioMenuItem orange = new RadioMenuItem("Orange");
        RadioMenuItem green = new RadioMenuItem("Green");
        RadioMenuItem purple = new RadioMenuItem("Purple");
        // set selected
        switch (GameEngine.getColor()){
            case "BLUE":
                blue.setSelected(true);
                break;
            case "RED":
                red.setSelected(true);
                break;
            case "ORANGE":
                orange.setSelected(true);
                break;
            case "GREEN":
                green.setSelected(true);
                break;
            case "PURPLE":
                purple.setSelected(true);
                break;
        }
        // new ToggleGroup
        ToggleGroup colorGroup = new ToggleGroup();
        blue.setToggleGroup(colorGroup);
        orange.setToggleGroup(colorGroup);
        red.setToggleGroup(colorGroup);
        green.setToggleGroup(colorGroup);
        purple.setToggleGroup(colorGroup);
        //setOnAction
        blue.setOnAction(e -> gameEngine.changeColor("BLUE"));
        red.setOnAction(e -> gameEngine.changeColor("RED"));
        orange.setOnAction(e -> gameEngine.changeColor("ORANGE"));
        green.setOnAction(e -> gameEngine.changeColor("GREEN"));
        purple.setOnAction(e -> gameEngine.changeColor("PURPLE"));

        Menu color = new Menu("Color");
        color.getItems().addAll(blue, red, orange, green, purple);

        settings.getItems().addAll(lightTheme, darkTheme, separatorSettings, color);
        return settings;
    }

    /**
     * setup mode-Menu
     * includes standard and arcade
     */
    private Menu modeMenu(){
        Menu mode = new Menu("Mode");
        // MenuItems
        MenuItem standard = new MenuItem("Standard");
        MenuItem arcade = new MenuItem("Arcade");
        MenuItem fun = new MenuItem("Fun");
        // setOnAction
        standard.setOnAction(e -> {
            GameEngine.setBarrier(false);
            GameEngine.setSpecialItems(false);
            new GameEngine(window);
        });
        arcade.setOnAction(e -> {
            GameEngine.setBarrier(true);
            GameEngine.setSpecialItems(false);
            new GameEngine(window);
        });
        fun.setOnAction(e -> {
            GameEngine.setBarrier(false);
            GameEngine.setSpecialItems(true);
            new GameEngine(window);
        });

        mode.getItems().addAll(standard, arcade, fun);
        return mode;
    }

    /**
     * setup size-Menu
     * includes small, normal, big
     */
    private Menu sizeMenu(){
        Menu size = new Menu("Change Size");
        // MenuItems
        RadioMenuItem small = new RadioMenuItem("Small");
        RadioMenuItem normal = new RadioMenuItem("Normal");
        RadioMenuItem big = new RadioMenuItem("Big");
        RadioMenuItem custom = new RadioMenuItem("Custom");
        // do it into a group
        ToggleGroup sizeGroup = new ToggleGroup();
        small.setToggleGroup(sizeGroup);
        normal.setToggleGroup(sizeGroup);
        big.setToggleGroup(sizeGroup);
        // set selected
        switch (GameEngine.getWidth()){
            case 24:
                small.setSelected(true);
                break;
            case 32:
                normal.setSelected(true);
                break;
            case 44:
                big.setSelected(true);
                break;
            default:
                custom.setSelected(true);
        }
        // setOnAction
        small.setOnAction(e -> new GameEngine(window, 24, 16));
        normal.setOnAction(e -> new GameEngine(window, 32, 26));
        big.setOnAction(e -> new GameEngine(window, 44, 36));
        custom.setOnAction(e -> {
            if (gameEngine.isRunning())
                gameEngine.startPause();
            setCustomizeSizeWindow();
        });

        size.getItems().addAll(small, normal, big, custom);
        return size;
    }

    /**
     * setup difficulty-Menu
     * includes easy, medium, hard
     */
    private Menu difficultyMenu(){
        Menu difficulty = new Menu("Difficulty");
        // RadioMenuItems
        RadioMenuItem easy = new RadioMenuItem("Easy");
        RadioMenuItem medium = new RadioMenuItem("Medium");
        RadioMenuItem hard = new RadioMenuItem("Hard");
        // do it into a group
        ToggleGroup difficultyToggle = new ToggleGroup();
        easy.setToggleGroup(difficultyToggle);
        medium.setToggleGroup(difficultyToggle);
        hard.setToggleGroup(difficultyToggle);
        // set selected
        switch (GameEngine.getSnakeSpeed()){
            case 250:
                easy.setSelected(true);
                break;
            case 125:
                medium.setSelected(true);
                break;
            case 75:
                hard.setSelected(true);
                break;
        }
        // setOnAction
        easy.setOnAction(e -> GameEngine.setSnakeSpeed(250));
        medium.setOnAction(e -> GameEngine.setSnakeSpeed(125));
        hard.setOnAction(e -> GameEngine.setSnakeSpeed(75));

        difficulty.getItems().addAll(easy, medium, hard);
        return difficulty;
    }

    /**
     * disable/enable 'start game' in Menu
     */
    public void updateMenuStart(){
        if (!gameEngine.isReset() || gameEngine.isRunning())
            startGame.setDisable(true);
        else
            startGame.setDisable(false);
    }

    private void setCustomizeSizeWindow() {

        Stage customize = new Stage();
        customize.initModality(Modality.APPLICATION_MODAL);
        customize.setMinWidth(285);
        customize.setMinHeight(200);


        Label width = new Label("Set width: ");
        Label height = new Label("Set height: ");

        Slider setWidth = new Slider();
        setWidth.setMax(100);
        setWidth.setMin(20);
        setWidth.setValue(32);
        setWidth.setShowTickMarks(true);
        setWidth.setShowTickLabels(true);
        setWidth.setBlockIncrement(5);
        setWidth.setMinorTickCount(10);
        setWidth.setMajorTickUnit(10);

        Slider setHeight = new Slider();
        setHeight.setMax(60);
        setHeight.setMin(10);
        setHeight.setValue(32);
        setHeight.setShowTickMarks(true);
        setHeight.setShowTickLabels(true);
        setHeight.setBlockIncrement(5);
        setHeight.setMinorTickCount(10);
        setHeight.setMajorTickUnit(10);

        HBox buttons = new HBox();
        Button submit = new Button("Submit");
        submit.setOnAction(e -> {
            new GameEngine(window, (int) setWidth.getValue(), (int) setHeight.getValue());
            customize.close();
        });
        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> customize.close());
        buttons.getChildren().setAll(submit, cancel);
        buttons.setSpacing(20);
        buttons.setAlignment(Pos.CENTER);

        Label gab = new Label();

        // set-up grid
        GridPane grid = new GridPane();
        GridPane.setConstraints(width, 0,0);
        GridPane.setConstraints(setWidth, 1, 0);
        GridPane.setConstraints(height, 0, 1);
        GridPane.setConstraints(setHeight, 1, 1);
        GridPane.setConstraints(gab, 0, 2, 2, 1);
        GridPane.setConstraints(buttons, 0, 3, 2, 1);
        grid.setPadding(new Insets(20,30, 20, 30));
        grid.setVgap(10);
        grid.getChildren().setAll(width, setWidth, height, setHeight, gab, buttons);

        Scene customizeScene = new Scene(grid);
        customize.setScene(customizeScene);
        customize.initStyle(StageStyle.UTILITY);
        customize.show();
}

    public void setOverlay(String text){
        overlay.setStyle("-fx-text-fill: black; -fx-background-color: grey; -fx-padding: 6px;" +
                " -fx-font-size: 20px; -fx-font-weight: bold; -fx-text-alignment: center");
        if(text.equals(""))
            overlay.setStyle("-fx-text-fill: black; -fx-background-color: lightgray; -fx-padding: 0px");
        if(text.equals("GO!"))
            overlay.setStyle("-fx-text-fill: black; -fx-background-color: rgba(211, 211, 211, 0.8);" +
                    " -fx-padding: 2px; -fx-font-size: 25; -fx-font-weight: bold; -fx-text-alignment: center");
        this.overlay.setText(text);
    }

    public void setScore(int points){
        this.score.setText("Your Score: " + points);
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    // getter
    public Canvas getCanvas() {
        return canvas;
    }
}