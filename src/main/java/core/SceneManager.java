package core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import views.GameScreen;

import java.io.IOException;

/**
 * Loads initial window and subsequent scenes
 */
public final class SceneManager {
    private static Stage  stage;
    private static Scene  scene;
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
            GameEngine.stop();

            GameScreen.getLevel().unloadCurrentRoom();
        }

        try {
            sceneName = fxmlPath;

            Parent newRoot = FXMLLoader.load(SceneManager.class.getResource(fxmlPath));
            scene.setRoot(newRoot);

            stage.setScene(scene);
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
     * Sets the current stage and scene.
     *
     * @param stage the current stage
     */
    public static void setStage(Stage stage) {
        SceneManager.stage = stage;

        SceneManager.scene = new Scene(new Label());
        scene.setFill(Color.valueOf("#3C1F2A"));
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
