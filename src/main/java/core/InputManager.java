package core;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/**
 * Provides utility for acting on user input
 */
public final class InputManager {
    // No instances
    private InputManager() { }

    // Could move this into GameManager
    public static void addKeyListener(EventHandler<KeyEvent> eventHandler) {
        SceneManager.getStage().addEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
    }

    // todo: 2/27/2021 Add mouse listener
}
