import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class DungeonCrawlerDriver extends Application {
    public static Stage mainWindow;


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        mainWindow = primaryStage;

        SceneLoader.loadScene(SceneLoader.TITLE);
    }
}