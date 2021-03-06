package core;

import game.MainPlayer;
import javafx.application.Application;
import javafx.stage.Stage;

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
        Map<String, String> parameters = getParameters().getNamed();

        String requestedScene = parameters.getOrDefault("scene", "");
        if (!requestedScene.isEmpty()) {
            System.out.println("Loading scene: " + requestedScene);
            try {
                String scene = SceneManager.class.getField(requestedScene).get(null).toString();

                SceneManager.loadStage();
                SceneManager.loadScene(scene);
                GameManager.setPlayer(new MainPlayer("Azula", "Weapon", "Normal"));
                return;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                System.err.println("Could not load scene: " + requestedScene + " requested by "
                                           + "parameter --scene");
                e.printStackTrace();
            }
        }

        SceneManager.loadStage();
    }
}