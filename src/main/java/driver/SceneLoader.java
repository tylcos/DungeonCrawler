package driver;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneLoader {
    public static Stage window;


    public static final String TITLE  = "/views/TitleScreen.fxml";
    public static final String CONFIG = "/views/ConfigScreen.fxml";
    public static final String GAME   = "/views/GameScreen.fxml";


    /**
     * Loads normal scenes and shows them
     * The FXML file needs to have a Scene container as the root
     * @param fxmlPath Scene to load
     */
    public static void loadScene(String fxmlPath) {
        try {
            Parent newParent = FXMLLoader.load(SceneLoader.class.getResource(fxmlPath));
            window.setScene(new Scene(newParent));
            window.show();
        } catch (IOException e) {
            System.err.println("Error loading fxml scene: " + fxmlPath);
        }
    }

    /**
     * Loads "view.TitleScreen.fxml" which contains the program stage as the root
     */
    public static void loadWindow() {
        try {
            window = FXMLLoader.load(DungeonCrawlerDriver.class.getResource(TITLE));
            window.show();
        } catch (IOException e) {
            System.err.println("Error loading fxml scene: " + TITLE);
        }
    }
}
