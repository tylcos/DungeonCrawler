package views;

import core.GameEngine;
import core.SceneManager;
import game.collidables.MainPlayer;
import game.levels.Level;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;

/**
 * FXML controller for the main game screen
 */
public class GameScreen {
    @FXML
    private TextArea  uiMinimap;
    @FXML
    private TextArea  uiInfoText;
    @FXML
    private StackPane renderPane;

    private static Level level;

    /**
     * Initializes the game screen
     */
    public void initialize() {
        GameEngine.start(renderPane);

        // Loaded the GameScreen without going through the config screen
        if (!SceneManager.CONFIG.equals(SceneManager.getSceneName())) {
            MainPlayer.setPlayer("Team Azula", "Weapon", "Normal");
        }

        // Update UI
        MainPlayer.setUiInfoText(uiInfoText);
        Level.addUiEventHandler(event -> uiMinimap.setText(level.getMinimapString()));

        // Start level spawning
        level = new Level();
        level.generateMap();
    }

    public static Level getLevel() {
        return level;
    }
}
