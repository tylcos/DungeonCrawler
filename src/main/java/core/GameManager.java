package core;

import game.Entity;
import game.MainPlayer;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public final class GameManager {
    private static boolean paused;
    private static List<Entity> entities;


    private static MainPlayer player;
    private static Pane drawPane;


    // No instances
    private GameManager() {
    }

    /**
     * Initializes GameManager and starts game loop
     */
    public static void start() {
        paused = false;
        entities = new ArrayList<>();


        AnimationTimer timer = new AnimationTimer() {
            private long lastNanoTime = System.nanoTime();

            public void handle(long currentNanoTime) {
                double dt = (currentNanoTime - lastNanoTime) * 1e-9d;
                lastNanoTime = currentNanoTime;


                GameManager.update(dt);
            }
        };
        timer.start();
    }


    /**
     * Updates position of all entities
     *
     * @param dt Time change since last frame in seconds
     */
    public static void update(double dt) {
        entities.forEach(e -> e.physicsUpdate(dt));
    }


    public static void spawnEntity(Entity entity) {
        entities.add(entity);

        drawPane.getChildren().add(entity.getImage());
    }

    public static boolean isPaused() {
        return paused;
    }


    public static MainPlayer getPlayer() {
        return player;
    }

    public static void setPlayer(MainPlayer player) {
        GameManager.player = player;
    }

    public static Pane getDrawPane() {
        return drawPane;
    }

    public static void setDrawPane(Pane drawPane) {
        GameManager.drawPane = drawPane;
    }
}
