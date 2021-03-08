package core;

import game.*;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all entities and runs game loop
 * <p>
 * todo Changing the window resolution breaks a lot of things right now. We gotta fix that.
 */
public final class GameManager {
    private static boolean paused;
    private static List<Entity> entities;

    private static MainPlayer player;
    private static Enemy enemy;
    private static Coin coin;
    private static Item item;
    private static Level level;
    private static Pane drawPane;

    /**
     * Private constructor so no instances of GameManager can be created.
     */
    private GameManager() { }

    /**
     * Initializes GameManager and starts game loop.
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

        // Start level spawning
        level = new Level();
        // We must place level on the bottom so that the UI renders on top of it.
        // level should be the only thing in drawPane at all, but I'm specifying to be
        // safe.
        drawPane.getChildren().add(0, level);
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
        entities.forEach(e -> e.physicsUpdate(dt));
        entities.forEach(e -> level.runCollisionCheck(e));
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
     * Returns the player.
     *
     * @return the player
     */
    public static MainPlayer getPlayer() {
        return player;
    }

    /**
     * Sets the player to a new player.
     *
     * @param player the player for the MainPlayer to be set to
     */
    public static void setPlayer(MainPlayer player) {
        GameManager.player = player;
    }

    /**
     * Sets the enemy to a new enemy.
     *
     * @param enemy the new enemy
     */
    public static void setEnemy(Enemy enemy) {
        GameManager.enemy = enemy;
    }

    /**
     * Sets the coin to a new coin.
     *
     * @param coin the new coin
     */
    public static void setCoin(Coin coin) {
        GameManager.coin = coin;
    }

    /**
     * Sets the item to a new item.
     *
     * @param item the new item
     */
    public static void setItem(Item item) {
        GameManager.item = item;
    }

    /**
     * Returns the draw pane.
     *
     * @return the draw pane
     */
    public static Pane getDrawPane() {
        return drawPane;
    }

    /**
     * Sets the draw pane to a new draw pane.
     *
     * @param drawPane the new draw pane
     */
    public static void setDrawPane(Pane drawPane) {
        GameManager.drawPane = drawPane;
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
