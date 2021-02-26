package driver;

import javafx.application.Application;
import javafx.stage.Stage;

public class DungeonCrawlerDriver extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        SceneLoader.loadWindow();
    }
}