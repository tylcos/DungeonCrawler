package core;

import game.Entity;
import game.Level;
import game.MainPlayer;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all entities and runs game loop
 *
 * todo Changing the window resolution breaks a lot of things right now. We gotta fix that.
 */
public final class GameManager {
    private static boolean paused;
    private static List<Entity> entities;

    private static MainPlayer player;
    private static Level level;
    private static Pane drawPane;

    // No instances
    private GameManager() { }

    /**
     * Initializes GameManager and starts game loop
     */
    public static void start() {
        paused = false;
        entities = new ArrayList<>(16);

        AnimationTimer timer = new AnimationTimer() {
            private long lastNanoTime = System.nanoTime();

            public void handle(long now) {
                double dt = (now - lastNanoTime) * 1e-9d;
                lastNanoTime = now;

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
        // Purposefully runs physics update separate from collision checks
        entities.forEach(e -> e.physicsUpdate(dt));
        entities.forEach(e -> level.runCollisionCheck(e));
    }

    public static void spawnEntity(Entity entity) {
        entities.add(entity);
        level.addEntity(Level.ENTITY, entity);
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
        level = new Level();

        // We must place level on the bottom so that the UI renders on top of it.
        // level should be the only thing in drawPane at all, but I'm specifying to be
        // safe.
        drawPane.getChildren().add(0, level);
    }
}
