package core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    private static Stage stage;


    public static final String TITLE = "/views/TitleScreen.fxml";
    public static final String CONFIG = "/views/ConfigScreen.fxml";
    public static final String GAME = "/views/GameScreen.fxml";


    /**
     * Loads normal scenes and shows them
     * The FXML file needs to have a Scene container as the root
     * @param fxmlPath Scene to load
     */
    public static void loadScene(String fxmlPath) {
        try {
            Parent newParent = FXMLLoader.load(SceneManager.class.getResource(fxmlPath));
            stage.setScene(new Scene(newParent));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading fxml scene: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Loads "view.TitleScreen.fxml" which contains the program stage as the root
     */
    public static void loadStage() {
        try {
            stage = FXMLLoader.load(DungeonCrawlerDriver.class.getResource(TITLE));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading fxml scene: " + TITLE);
            e.printStackTrace();
        }
    }


    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage window) {
        SceneManager.stage = window;
    }
}
