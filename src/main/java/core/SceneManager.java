package core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Loads initial window and subsequent scenes
 */
public final class SceneManager {
    private static Stage stage;
    private static Scene scene;
    private static Pane  root;

    private static String sceneName;

    private static Runnable onLoad;
    private static Runnable onUnload;

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
     * @param fxmlPath FXML path to load
     */
    public static void loadScene(String fxmlPath) {
        if (onUnload != null) {
            onUnload.run();
            onUnload = null;
        }

        try {
            sceneName = fxmlPath;

            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Pane       pane   = loader.load();
            root = pane;

            if (onLoad != null) {
                onLoad.run();
                onLoad = null;
            }

            scene.setRoot(pane);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading fxml file: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Adds a pane to the current scene
     *
     * @param fxmlPath FXML path to load
     */
    public static void loadPane(String fxmlPath) {
        try {
            sceneName = fxmlPath;

            FXMLLoader fxmlLoader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Pane       pane       = fxmlLoader.load();

            root = new StackPane(root, pane);

            if (onLoad != null) {
                onLoad.run();
                onLoad = null;
            }

            scene.setRoot(root);
        } catch (IOException e) {
            System.err.println("Error loading fxml file: " + fxmlPath);
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
     * Returns root node for the current scene.
     *
     * @return root node
     */
    public static Pane getRoot() {
        return root;
    }

    /**
     * Returns the name of the current scene.
     *
     * @return the name of the current scene
     */
    public static String getSceneName() {
        return sceneName;
    }

    /**
     * Sets runnable for when a scene is loaded
     *
     * @param onLoad runnable to run
     */
    public static void setOnLoad(Runnable onLoad) {
        SceneManager.onLoad = onLoad;
    }

    /**
     * Sets runnable for when a scene is unloaded
     *
     * @param onUnload runnable to run
     */
    public static void setOnUnload(Runnable onUnload) {
        SceneManager.onUnload = onUnload;
    }
}
