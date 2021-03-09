package core;

import game.MainPlayer;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * Launches application
 */
public class DungeonCrawlerDriver extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parameters params = getParameters();
        // getParameters() should never return null as we are outside of init() per javadocs
        Map<String, String> parameters = params == null ? new HashMap<>() : params.getNamed();

        // Implements "--scene=GAME" parameter
        String sceneArgument = parameters.getOrDefault("scene", "");
        if (!sceneArgument.isEmpty()) {
            try {
                System.out.println("Loading scene: " + sceneArgument);
                String scene = SceneManager.class.getField(sceneArgument).get(null).toString();

                SceneManager.loadStage();
                SceneManager.loadScene(scene);
                GameManager.setPlayer(new MainPlayer("Team Azula", "Weapon", "Normal"));

                return;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                System.err.println("Could not load scene: " + sceneArgument
                                           + " requested by parameter --scene");
                e.printStackTrace();
            }
        }

        // Default scene
        SceneManager.loadStage();
    }
}