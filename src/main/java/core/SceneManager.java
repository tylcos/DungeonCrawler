package core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Loads initial window and subsequent scenes
 */
public final class SceneManager {
    private static Stage stage;
    private static String sceneName;

    public static final String TITLE = "/views/TitleScreen.fxml";
    public static final String CONFIG = "/views/ConfigScreen.fxml";
    public static final String GAME = "/views/GameScreen.fxml";
    public static final String WIN_SCREEN = "/views/WinGameScreen.fxml";

    /**
     * Private constructor so no instances of SceneManagers can be created.
     */
    private SceneManager() { }

    /**
     * Loads normal scenes and shows them
     * The FXML file needs to have a Scene container as the root
     *
     * @param fxmlPath Scene to load
     */
    public static void loadScene(String fxmlPath) {
        try {
            Parent newParent = FXMLLoader.load(SceneManager.class.getResource(fxmlPath));
            stage.setScene(new Scene(newParent));
            stage.show();

            sceneName = fxmlPath;
        } catch (IOException e) {
            System.err.println("Error loading fxml scene: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Loads "view.TitleScreen.fxml" which contains the program stage as the root.
     */
    public static void loadStage() {
        try {
            stage = FXMLLoader.load(DungeonCrawlerDriver.class.getResource(TITLE));
            stage.show();

            sceneName = TITLE;
        } catch (IOException e) {
            System.err.println("Error loading fxml scene: " + TITLE);
            e.printStackTrace();
        }
    }

    /**
     * Returns the current stage.
     *
     * @return the current stage
     */
    public static Stage getStage() {
        return stage;
    }

    /**
     * Sets the stage shown to a new stage.
     *
     * @param window the new stage
     */
    public static void setStage(Stage window) {
        SceneManager.stage = window;
    }

    /**
     * Returns the name of the current scene.
     *
     * @return the name of the current scene
     */
    public static String getSceneName() {
        return sceneName;
    }
}
