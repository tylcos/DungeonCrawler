package core;

import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Launches application
 */
public class GameDriver extends Application {
    public static final String GAME_TITLE   = "Dungeon Crawler - Team Azula";
    public static final String GAME_VERSION = "Version 1.00";

    public static final double WIDTH      = 1920d;
    public static final double HEIGHT     = 1080d;
    public static final double MIN_WIDTH  = 1280d;
    public static final double MIN_HEIGHT = 720d;

    private static boolean debug;

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
        primaryStage.widthProperty().addListener(observable -> ScreenManager.updateScreen());
        primaryStage.heightProperty().addListener(observable -> ScreenManager.updateScreen());

        primaryStage.setRenderScaleX(1d);
        primaryStage.setRenderScaleY(1d);
        primaryStage.setMaximized(true);
        primaryStage.setTitle(GAME_TITLE);

        SceneManager.setStage(primaryStage);

        primaryStage.setFullScreen(true);
        InputManager.addKeyHandler(KeyCode.F11,
                                   () -> primaryStage.setFullScreen(!primaryStage.isFullScreen()));

        // Implement any program parameters
        Map<String, String> named = getParameters().getNamed();
        List<String> unnamed = getParameters().getUnnamed().stream()
                                       .map(String::toLowerCase)
                                       .collect(Collectors.toList());

        // "-NoDebug" parameter
        if (unnamed.contains("-debug")) {
            debug = true;
        }

        // "--scene=ANY_SCENE" parameter
        String  sceneArgument = named.getOrDefault("scene", "");
        boolean loadedScene   = false;
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