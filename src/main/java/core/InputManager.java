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

    private InputManager() { }

    public static void addMouseClickListener(EventHandler<MouseEvent> eventHandler) {
        SceneManager.getStage().addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    public static boolean get(KeyCode keyCode) {
        return inputState.get(keyCode);
    }
}
