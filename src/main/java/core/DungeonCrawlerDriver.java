package core;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Launches application
 */
public class DungeonCrawlerDriver extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        core.SceneManager.loadStage();
    }
}