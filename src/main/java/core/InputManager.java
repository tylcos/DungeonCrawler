package core;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

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

        addKeyUpListener(InputManager::keyUpEvent);
        addKeyDownListener(InputManager::keyDownEvent);
    }

    private InputManager() { }

    public static void addKeyUpListener(EventHandler<KeyEvent> eventHandler) {
        SceneManager.getStage().addEventFilter(KeyEvent.KEY_RELEASED, eventHandler);
    }

    public static void addKeyDownListener(EventHandler<KeyEvent> eventHandler) {
        SceneManager.getStage().addEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
    }

    public static void addMouseClickListener(EventHandler<MouseEvent> eventHandler) {
        SceneManager.getStage().addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    private static void keyDownEvent(KeyEvent key) {
        inputState.put(key.getCode(), true);
    }

    private static void keyUpEvent(KeyEvent key) {
        inputState.put(key.getCode(), false);
    }

    public static EnumMap<KeyCode, Boolean> getInputState() {
        return inputState;
    }
}
