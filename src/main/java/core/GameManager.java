package core;

import game.collidables.Entity;
import game.collidables.MainPlayer;
import game.level.Level;
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
    private static List<Entity> entities;

    private static MainPlayer player;
    private static Level level;
    private static Pane drawPane;

    private static boolean paused = true;
    private static AnimationTimer frameTimer;

    /**
     * Private constructor so no instances of GameManager can be created.
     */
    private GameManager() { }

    /**
     * Initializes GameManager and starts game loop.
     *
     * @param drawPane Pane used to render all entities
     */
    public static void start(Pane drawPane) {
        GameManager.drawPane = drawPane;
        entities = new ArrayList<>(16);

        frameTimer = new AnimationTimer() {
            private long lastNanoTime = System.nanoTime();

            public void handle(long now) {
                double dt = (now - lastNanoTime) * 1e-9d;
                lastNanoTime = now;

                GameManager.update(dt);
            }
        };
        setPaused(false);

        // Start level spawning
        // Trying to minimize what code has access to the drawPane by only passsing the
        // drawPane in constructors
        level = new Level(drawPane);
        // Separating to prevent enemies spawned in the level from referencing level before its
        // returned from the constructor
        level.generateMap();
    }

    /**
     * Updates position of all entities.
     *
     * @param dt Time change since last frame in seconds
     */
    public static void update(double dt) {
        if (paused) {
            return;
        }

        // Purposefully runs physics update separate from collision checks
        // Copies entities to avoid concurrent modification errors
        Entity[] currentEntities = entities.toArray(Entity[]::new);
        for (Entity entity : currentEntities) {
            entity.physicsUpdate(dt);
        }
        for (Entity entity : currentEntities) {
            level.runCollisionCheck(entity);
        }
    }

    /**
     * Spawns an entity.
     *
     * @param entity the entity to spawn
     */
    public static void spawnEntity(Entity entity) {
        entities.add(entity);
        level.addEntity(Level.ENTITY, entity);
    }

    /**
     * Returns if game is paused.
     *
     * @return true if the game is paused; false otherwise
     */
    public static boolean isPaused() {
        return paused;
    }

    /**
     * Pauses the game manager and update methods on all entities
     *
     * @param paused if GameManager is paused
     */
    public static void setPaused(boolean paused) {
        if (paused) {
            frameTimer.stop();
        } else {
            frameTimer.start();
        }

        GameManager.paused = paused;
    }

    /**
     * Returns the current level.
     *
     * @return the current level
     */
    public static Level getLevel() {
        return level;
    }
}
