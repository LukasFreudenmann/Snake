package Snake;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Launch extends Application {

    private Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;

        Label madeBy = new Label("Made by Lukas Freudenmann");
        Label version = new Label("Version 1.0");
        Label space = new Label("");
        Button button = new Button("Start Game");
        button.setOnAction(e -> {
            new GameEngine(window, 32, 26);
        });

        VBox startLayout = new VBox();
        startLayout.getChildren().addAll(button, space, madeBy, version);
        startLayout.setAlignment(Pos.CENTER);
        Scene startGame = new Scene(startLayout, 250, 200);
        window.getIcons().add(new Image("Snake/imgs/Snake.png"));
        window.setScene(startGame);
        window.setTitle("Snake");
        window.show();
    }
}