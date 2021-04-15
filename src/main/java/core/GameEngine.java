package core;

import game.collidables.Collidable;
import game.entities.Entity;
import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import views.GameScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the spawning, removal, updating, and collision detecting of all entities and runs
 * the game loop.
 */
public final class GameEngine {
    // All static Collidables including stationary things like Tiles and Collectables.
    private static List<Collidable> staticBodies  = new ArrayList<>(256);
    // All dynamic Collidables including moving things like Entities.
    private static List<Entity>     dynamicBodies = new ArrayList<>(32);

    // Timing utilities used for the game loop
    private static AnimationTimer frameTimer;
    private static long           frameCounter;
    private static double         t;
    private static double         dt;
    private static double         averageFps;

    private static final int       RENDER_LAYERS = 4;
    private static       StackPane renderPane;
    private static       Pane[]    renderLayers  = new Pane[RENDER_LAYERS];

    // Layers are rendered from the bottom up, so layer 1 appears above layer 0
    public static final int ROOM   = 0;
    public static final int ITEM   = 1;
    public static final int ENTITY = 2;
    public static final int VFX    = 3;

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
            private long lastFrameTime = System.nanoTime();

            public void handle(long now) {
                dt            = (now - lastFrameTime) * 1e-9d;
                lastFrameTime = now;
                t += dt;
                averageFps += (1d / dt - averageFps) * .1;

                GameEngine.update();
                frameCounter++;

                // Debug FPS graph
                if (GameDriver.isDebug()) {
                    ObservableList<XYChart.Data<Number, Number>> data =
                            GameScreen.getFpsGraph().getData().get(0).getData();

                    XYChart.Data<Number, Number> point = new XYChart.Data<>(frameCounter % 600,
                                                                            averageFps);
                    Region pointRegion = new Region();
                    pointRegion.setShape(new Circle(0));
                    point.setNode(pointRegion);

                    if (data.size() >= 600) {
                        data.set((int) (frameCounter % 600), point);
                    } else {
                        data.add(point);
                    }
                }
            }
        };
        frameTimer.start();

        // Setup rendering layers
        ObservableList<Node> children = renderPane.getChildren();
        for (int i = 0; i < RENDER_LAYERS; i++) {
            renderLayers[i] = new StackPane();
            children.add(renderLayers[i]);
        }

        renderLayers[VFX].setMouseTransparent(true);

        // Clear up variables
        staticBodies.clear();
        dynamicBodies.clear();

        t            = 0;
        frameCounter = 0;
    }

    /**
     * Updates position of all entities and checks for collisions.
     */
    public static void update() {
        // Allow the scene to build on the first frame which is necessary for some reason
        if (frameCounter == 0) {
            return;
        }

        // Copies entities to avoid concurrent modification errors
        // TODO 3/17/2021 convert to swapping between two lists to avoid allocating every frame
        Entity[]     currentDynamicBodies = dynamicBodies.toArray(Entity[]::new);
        Collidable[] currentStaticBodies  = staticBodies.toArray(Collidable[]::new);

        for (Entity entity : currentDynamicBodies) {
            entity.physicsUpdate(dt);
            entity.update();
        }

        // Purposefully runs physics update before collision checks
        for (Collidable entity : currentDynamicBodies) {
            runCollisionCheck(entity, currentDynamicBodies, currentStaticBodies);
        }
    }

    /**
     * Stops the game manager and stops rendering all entities.
     */
    public static void stop() {
        frameTimer.stop();
    }

    /**
     * Time since the game was started.
     *
     * @return t
     */
    public static double getT() {
        return t;
    }

    /**
     * Time change since last frame in seconds.
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
        for (T collidable : objects) {
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
                ((Entity) collidable).setVelocity(Point2D.ZERO);
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
     * Clears the VFX layer which is usually used for debug or visual effects in a level
     */
    public static void clearVFX() {
        renderLayers[VFX].getChildren().clear();
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
