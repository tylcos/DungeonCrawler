package views;

import core.GameDriver;
import core.GameEngine;
import game.entities.Player;
import game.levels.Level;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    @FXML
    private Label     version;

    private static Level level;

    /**
     * Initializes the game screen
     */
    public void initialize() {
        version.setText(GameDriver.GAME_VERSION);

        // Loaded the GameScreen without going through the config screen
        if (Player.getPlayer() == null) {
            Player.setPlayer("Team Azula", "Weapon", "Normal");
        }

        // Update UI
        Player.setUiInfoText(uiInfoText);
        Level.addUiEventHandler(event -> uiMinimap.setText(level.getMinimapString()));

        // Start Game
        GameEngine.start(renderPane);

        level = new Level();
        level.generateMap();
    }

    public static Level getLevel() {
        return level;
    }
}
