package core;

import game.collidables.Collidable;
import game.collidables.Entity;
import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the spawning, removal, updating, and collision detecting of all entities and runs
 * the game loop
 */
public final class GameEngine {
    // All static Collidables including stationary things like Tiles and Collectables.
    private static List<Collidable> staticBodies  = new ArrayList<>(256);
    // All dynamic Collidables including moving things like Entities.
    private static List<Entity>     dynamicBodies = new ArrayList<>(32);

    // Timing utilities used for the game loop
    private static boolean        paused = true;
    private static AnimationTimer frameTimer;
    private static double         dt;

    // Layers are rendered from the bottom up, so layer 2 appears above layer 1
    private static final int       RENDER_LAYERS = 4;
    private static       StackPane renderPane;
    private static       Pane[]    renderLayers  = new Pane[RENDER_LAYERS];

    // Constants for all the render layers so we don't go insane
    public static final int ROOM   = 0;
    public static final int ITEM   = 1;
    public static final int ENTITY = 2;
    public static final int VFX    = 3;
    // TODO Using a similar, 2D array, we could also create a collision matrix if necessary

    /**
     * Private constructor so no instances of GameEngine can be created.
     */
    private GameEngine() { }

    /**
     * Initializes GameEngine and starts game loop.
     *
     * @param renderPane Pane used to render all entities
     */
    public static void start(StackPane renderPane) {
        GameEngine.renderPane = renderPane;

        // Setup game loop
        frameTimer = new AnimationTimer() {
            private long lastNanoTime = System.nanoTime();

            public void handle(long now) {
                dt           = (now - lastNanoTime) * 1e-9d;
                lastNanoTime = now;

                GameEngine.update();
            }
        };
        setPaused(false);

        // Setup rendering layers
        ObservableList<Node> children = renderPane.getChildren();
        for (int i = 0; i < RENDER_LAYERS; i++) {
            renderLayers[i] = new StackPane();
            children.add(renderLayers[i]);
        }

        renderLayers[VFX].setMouseTransparent(true);
    }

    /**
     * Updates position of all entities and checks for collisions.
     */
    public static void update() {
        // Copies entities to avoid concurrent modification errors
        // TODO 3/17/2021 convert to swapping between two lists to avoid allocating every frame
        Entity[]     currentDynamicBodies = dynamicBodies.toArray(Entity[]::new);
        Collidable[] currentStaticBodies  = staticBodies.toArray(Collidable[]::new);

        for (Entity entity : currentDynamicBodies) {
            entity.physicsUpdate(dt);
            entity.update();
        }

        // Purposefully runs physics update before collision checks
        for (Entity entity : currentDynamicBodies) {
            runCollisionCheck(entity, currentDynamicBodies, currentStaticBodies);
        }
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
     * @param paused if GameEngine is paused
     */
    public static void setPaused(boolean paused) {
        if (paused) {
            frameTimer.stop();
        } else {
            frameTimer.start();
        }

        GameEngine.paused = paused;
    }

    /**
     * Time change since last frame in seconds
     *
     * @return dt
     */
    public static double getDt() {
        return dt;
    }

    /*
     * Object spawning, removing, and collision detection
     */

    /**
     * Adds the given collidable to the layer and to physics.
     *
     * @param <T>    any type that extends Collidable
     * @param layer  the render layer to add to
     * @param object the object to add
     */
    public static <T extends Collidable> void instantiate(int layer, T object) {
        instantiate(layer, List.of(object));
    }

    /**
     * Adds all the given collidables to the layer and to physics.
     *
     * @param <T>     any type that extends Collidable
     * @param layer   the render layer to add to
     * @param objects the objects to add
     */
    public static <T extends Collidable> void instantiate(int layer, List<T> objects) {
        addToLayer(layer, objects);
        addToPhysics(objects);
    }

    /**
     * Removes the given collidable from the layer and from physics.
     *
     * @param <T>    any type that extends Collidable
     * @param layer  the render layer to add to
     * @param object the object to add
     */
    public static <T extends Collidable> void destroy(int layer, T object) {
        destroy(layer, List.of(object));
    }

    /**
     * Removes all the given collidables from the layer and from physics.
     *
     * @param <T>     any type that extends Collidable
     * @param layer   the render layer to add to
     * @param objects the objects to add
     */
    public static <T extends Collidable> void destroy(int layer, List<T> objects) {
        removeFromLayer(layer, objects);
        removeFromPhysics(objects);
    }

    /**
     * Removes all given objects from the render layer.
     * Used to render objects that do not need physics
     *
     * @param <T>     any type that extends Node
     * @param layer   the render layer to remove from
     * @param objects the objects to remove
     */
    public static <T extends Node> void addToLayer(int layer, List<T> objects) {
        renderLayers[layer].getChildren().addAll(objects);
    }

    /**
     * Removes all given objects from the render layer.
     * Used to render objects that do not need physics
     *
     * @param <T>     any type that extends Node
     * @param layer   the render layer to remove from
     * @param objects the objects to remove
     */
    public static <T extends Node> void removeFromLayer(int layer, List<T> objects) {
        renderLayers[layer].getChildren().removeAll(objects);
    }

    /**
     * Adds all given objects to the physics update system.
     * Used to add physics to objects rendered elsewhere
     *
     * @param <T>     any type that extends Collidable
     * @param objects the objects to add
     */
    public static <T extends Collidable> void addToPhysics(List<T> objects) {
        for (Collidable collidable : objects) {
            if (collidable.isStatic()) {
                staticBodies.add(collidable);
            } else {
                dynamicBodies.add((Entity) collidable);
            }
        }
    }

    /**
     * Removes all given objects from the physics update system.
     * Used to add physics to objects rendered elsewhere
     *
     * @param <T>     any type that extends Collidable
     * @param objects the objects to remove
     */
    public static <T extends Collidable> void removeFromPhysics(List<T> objects) {
        for (Collidable collidable : objects) {
            if (collidable.isStatic()) {
                staticBodies.remove(collidable);
            } else {
                dynamicBodies.remove(collidable);
            }
        }
    }

    /**
     * Replace the current pane on the given render layer with a new pane.
     * Used for completely switching out a pane such as when changing levels
     *
     * @param layer   the render layer to set
     * @param newPane the pane the render layer is being set to
     */
    public static void setRenderLayer(int layer, Pane newPane) {
        renderPane.getChildren().set(layer, newPane);

        renderLayers[layer] = newPane;
    }

    /**
     * Checks and handles collisions on the target collidable.
     *
     * @param target               the collidable to check for collisions on
     * @param currentDynamicBodies a copy of dynamicBodies
     * @param currentStaticBodies  a copy of staticBodies
     */
    public static void runCollisionCheck(Collidable target,
                                         Entity[] currentDynamicBodies,
                                         Collidable[] currentStaticBodies) {
        for (Collidable collidable : currentStaticBodies) {
            if (collidable != target && collidable.intersects(target)) {
                collidable.onCollision(target);
                target.onCollision(collidable);
            }
        }

        for (Collidable collidable : currentDynamicBodies) {
            if (collidable != target && collidable.intersects(target)) {
                collidable.onCollision(target);
                target.onCollision(collidable);
            }
        }
    }
}
