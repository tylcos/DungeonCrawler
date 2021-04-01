package core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import views.GameScreen;

import java.io.IOException;

/**
 * Loads initial window and subsequent scenes
 */
public final class SceneManager {
    private static Stage  stage;
    private static String sceneName;

    public static final String TITLE  = "/views/TitleScreen.fxml";
    public static final String CONFIG = "/views/ConfigScreen.fxml";
    public static final String GAME   = "/views/GameScreen.fxml";
    public static final String END    = "/views/EndScreen.fxml";

    /**
     * Private constructor so no instances of SceneManagers can be created.
     */
    private SceneManager() { }

    /**
     * Loads normal scenes and displays them
     *
     * @param fxmlPath Scene to load
     */
    public static void loadScene(String fxmlPath) {
        // Exiting Game scene
        if (GAME.equals(sceneName)) {
            GameEngine.setPaused(true);

            GameScreen.getLevel().unloadCurrentRoom();
        }

        try {
            sceneName = fxmlPath;

            Parent newParent = FXMLLoader.load(SceneManager.class.getResource(fxmlPath));
            stage.setScene(new Scene(newParent));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading fxml scene: " + fxmlPath);
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
     * Sets the current stage.
     *
     * @param stage the current stage
     */
    public static void setStage(Stage stage) {
        SceneManager.stage = stage;
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
