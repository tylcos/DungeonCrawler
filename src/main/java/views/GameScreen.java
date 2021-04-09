package views;

import core.GameDriver;
import core.GameEngine;
import data.GameEffects;
import data.LerpTimer;
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
    private StackPane top;
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
            Player.setPlayer("Team Azula", "Weapon", "Debug");
        }

        // Update UI
        Player.setUiInfoText(uiInfoText);
        Level.addUiEventHandler(event -> uiMinimap.setText(level.getMinimapString()));

        // Start Game
        GameEngine.start(renderPane);

        level = new Level();
        level.generateMap();

        // Blurs the screen on scene load
        top.setEffect(GameEffects.GAME_BLUR);
        new LerpTimer(1, t -> GameEffects.GAME_BLUR.setRadius(20 * (1 - t)));
    }

    public static Level getLevel() {
        return level;
    }
}
