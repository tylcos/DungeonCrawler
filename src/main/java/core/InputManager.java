package core;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.EnumMap;

/**
 * Provides utility for acting on user input
 */
public final class InputManager {
    private static EnumMap<KeyCode, Boolean> inputState = new EnumMap<>(KeyCode.class);

    // Initializer for setting up inputState
    static {
        for (KeyCode keyCode : KeyCode.values()) {
            inputState.put(keyCode, false);
        }

        Stage stage = SceneManager.getStage();
        stage.addEventFilter(KeyEvent.KEY_PRESSED, key -> inputState.put(key.getCode(), true));
        stage.addEventFilter(KeyEvent.KEY_RELEASED, key -> inputState.put(key.getCode(), false));
    }

    /**
     * Private constructor so no instances of InputManager can be created.
     */
    private InputManager() { }

    /**
     * Adds a mouse clicked listener.
     *
     * @param eventHandler the event handler that handles the mouse clicked event
     */
    public static void addMouseClickListener(EventHandler<MouseEvent> eventHandler) {
        SceneManager.getStage().addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    /**
     * Returns the key that was pressed.
     *
     * @param keyCode the current key code inputted
     * @return the key that was pressed
     */
    public static boolean get(KeyCode keyCode) {
        return inputState.get(keyCode);
    }
}
