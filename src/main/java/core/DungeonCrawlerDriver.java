package core;

import data.RandomCoins;
import game.Coin;
import game.Enemy;
import game.Item;
import game.MainPlayer;
import javafx.application.Application;
import javafx.beans.binding.When;
import javafx.scene.input.InputMethodTextRun;
import javafx.stage.Stage;

import java.util.ArrayList;
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
        ArrayList<Coin> coins = new ArrayList<Coin>();



        // Implements "--scene=GAME" parameter
        String sceneArgument = parameters.getOrDefault("scene", "");
        if (!sceneArgument.isEmpty()) {
            try {
                System.out.println("Loading scene: " + sceneArgument);
                String scene = SceneManager.class.getField(sceneArgument).get(null).toString();

                SceneManager.loadStage();
                SceneManager.loadScene(scene);
                GameManager.setPlayer(new MainPlayer("Team Azula", "Weapon", "Normal"));
                GameManager.setEnemy(new Enemy(3, 5));
                //maximum 6 coin
                GameManager.setCoin(new Coin(RandomCoins.getCoin()));
                GameManager.setCoin(new Coin(RandomCoins.getCoin()));
                GameManager.setCoin(new Coin(RandomCoins.getCoin()));
                GameManager.setCoin(new Coin(RandomCoins.getCoin()));
                GameManager.setCoin(new Coin(RandomCoins.getCoin()));
                GameManager.setCoin(new Coin(RandomCoins.getCoin()));




                return;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                System.err.println("Could not load scene: " + sceneArgument
                        + " requested by parameter --scene");
                e.printStackTrace();
            }
        }

        SceneManager.loadStage();
    }

    public int getRandomNumber(int min, int max) {
        if (min < 0 || max < 0) {
            throw new IllegalArgumentException("Put positive number only");
        }
        return (int) ((Math.random() * (max - min)) + min);
    }
}