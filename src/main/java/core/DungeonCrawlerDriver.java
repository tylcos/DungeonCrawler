package core;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Map;

/**
 * Launches application
 */
public class DungeonCrawlerDriver extends Application {
    public static final double WIDTH = 1920d;
    public static final double HEIGHT = 1080d;
    public static final double MIN_WIDTH = 1280d;
    public static final double MIN_HEIGHT = 720d;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialization of the game window properties
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);

        primaryStage.setRenderScaleX(1d);
        primaryStage.setRenderScaleY(1d);
        primaryStage.setMaximized(true);

        SceneManager.setStage(primaryStage);

        // Implement any program parameters
        Map<String, String> parameters = getParameters().getNamed();

        // Implements "--scene=GAME" parameter
        String sceneArgument = parameters.getOrDefault("scene", "");
        if (!sceneArgument.isEmpty()) {
            try {
                System.out.println("Loading scene: " + sceneArgument);
                String scene = SceneManager.class.getField(sceneArgument).get(null).toString();

                SceneManager.loadScene(scene);
                return;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                System.err.println("Could not load scene: " + sceneArgument
                                           + " requested by parameter --scene");
                e.printStackTrace();
            }
        }

        // Default scene
        SceneManager.loadScene(SceneManager.TITLE);
    }
}