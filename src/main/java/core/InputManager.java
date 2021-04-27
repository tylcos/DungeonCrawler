package core;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Provides utility for acting on user input in a state based or event driven manner.
 */
public final class InputManager {
    private static EnumMap<KeyCode, Long>           keyStates = new EnumMap<>(KeyCode.class);
    private static EnumMap<KeyCode, List<Runnable>> keyEvents = new EnumMap<>(KeyCode.class);

    // Initializer for setting up inputState
    static {
        for (KeyCode keyCode : KeyCode.values()) {
            keyStates.put(keyCode, Long.MIN_VALUE);
        }

        Stage stage = SceneManager.getStage();
        stage.addEventFilter(KeyEvent.KEY_RELEASED,
                             key -> keyStates.put(key.getCode(), -GameEngine.getFrameCounter()));
        stage.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            if (keyStates.get(key.getCode()) <= 0) {
                keyStates.put(key.getCode(), GameEngine.getFrameCounter());

                // Run all events assigned to this key
                if (keyEvents.containsKey(key.getCode())) {
                    keyEvents.get(key.getCode()).forEach(Runnable::run);
                }
            }
        });
    }

    /**
     * Private constructor so no instances of InputManager can be created.
     */
    private InputManager() { }

    /**
     * Returns whether the key is currently pressed.
     * Used for state based input management.
     *
     * @param keyCode the key code to be checked
     * @return whether the key is currently pressed
     */
    public static boolean get(KeyCode keyCode) {
        return keyStates.get(keyCode) > 0;
    }

    /**
     * Returns whether the key was pressed during this frame.
     * Used for state based input management.
     *
     * @param keyCode the key code to be checked
     * @return whether the key was pressed during this frame.
     */
    public static boolean getKeyDown(KeyCode keyCode) {
        return keyStates.get(keyCode) == GameEngine.getFrameCounter();
    }

    /**
     * Returns whether the key was released during this frame.
     * Used for state based input management.
     *
     * @param keyCode the key code to be checked
     * @return whether the key was released during this frame.
     */
    public static boolean getKeyUp(KeyCode keyCode) {
        return keyStates.get(keyCode) == -GameEngine.getFrameCounter();
    }

    /**
     * Returns the mouse position.
     *
     * @return the mouse position
     */
    public static Point2D getMousePosition() {
        Point point = MouseInfo.getPointerInfo().getLocation();

        return ScreenManager.screenToGame(new Point2D(point.x, point.y));
    }

    /**
     * Adds a event handler for a specific key.
     * Used for event driven input management.
     *
     * @param keyCode  the key to subscribe to
     * @param runnable the event to run once the key is pressed
     */
    public static void addKeyHandler(KeyCode keyCode, Runnable runnable) {
        keyEvents.putIfAbsent(keyCode, new ArrayList<>(1));

        keyEvents.get(keyCode).add(runnable);
    }

    /**
     * Removes a event handler for a specific key.
     * Used for event driven input management.
     *
     * @param keyCode  the key to unsubscribe from
     * @param runnable the event to remove
     */
    public static void removeKeyHandler(KeyCode keyCode, Runnable runnable) {
        keyEvents.get(keyCode).remove(runnable);
    }

    /**
     * Sets a event handler for a specific key.
     * Used for event driven input management.
     *
     * @param keyCode  the key to subscribe to
     * @param runnable the event to run once the key is pressed
     */
    public static void setKeyHandler(KeyCode keyCode, Runnable runnable) {
        keyEvents.put(keyCode, List.of(runnable));
    }

    /**
     * Clears all event handlers for a specific key.
     * Used for event driven input management.
     *
     * @param keyCode the key to unsubscribe all events from
     */
    public static void clearKeyHandler(KeyCode keyCode) {
        if (keyEvents.containsKey(keyCode)) {
            keyEvents.get(keyCode).clear();
        }
    }
}
