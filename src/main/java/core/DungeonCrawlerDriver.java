package core;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Launches application
 */
public class DungeonCrawlerDriver extends Application {
    public static final double WIDTH      = 1920d;
    public static final double HEIGHT     = 1080d;
    public static final double MIN_WIDTH  = 1280d;
    public static final double MIN_HEIGHT = 720d;

    public static final String GAME_TITLE = "Dungeon Crawler - Team Azula";

    private static boolean debug = true;

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
        primaryStage.setTitle(GAME_TITLE);

        SceneManager.setStage(primaryStage);

        // Implement any program parameters
        boolean             loadedScene = false;
        Map<String, String> named       = getParameters().getNamed();
        List<String> unnamed = getParameters().getUnnamed().stream()
                                       .map(String::toLowerCase)
                                       .collect(Collectors.toList());

        // "--NoDebug" parameter
        if (unnamed.contains("--nodebug")) {
            debug = false;
        }

        // "--scene=ANY_SCENE" parameter
        String sceneArgument = named.getOrDefault("scene", "");
        if (!sceneArgument.isEmpty()) {
            try {
                if (debug) {
                    System.out.println("Loading scene: " + sceneArgument);
                }

                String scene = SceneManager.class.getField(sceneArgument).get(null).toString();
                SceneManager.loadScene(scene);
                loadedScene = true;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                System.err.println("Could not load scene: " + sceneArgument
                                   + " requested by parameter --scene");
                e.printStackTrace();
            }
        }

        // Load default scene
        if (!loadedScene) {
            SceneManager.loadScene(SceneManager.TITLE);
        }
    }

    public static boolean isDebug() {
        return debug;
    }
}