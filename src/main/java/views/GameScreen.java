package views;

import core.*;
import game.Inventory;
import game.entities.Player;
import game.levels.Level;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import utilities.GameEffects;
import utilities.TimerUtil;

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
    private HBox      hotbar;
    @FXML
    private StackPane renderPane;
    @FXML
    private Label     version;

    @FXML
    private static LineChart<Number, Number> fpsGraph;

    private static Level level;

    /**
     * Initializes the game screen
     */
    public void initialize() {
        SceneManager.setOnLoad(() -> {
            // Loaded the GameScreen without going through the config screen
            if (Player.getPlayer() == null) {
                Player.setPlayer("Team Azula", "Sword", "Debug");
            }

            version.setText(GameDriver.GAME_VERSION);

            // Update UI
            Player.setUiInfoText(uiInfoText);
            Level.addUiEventHandler(event -> uiMinimap.setText(level.getMinimapString()));
            Inventory.initializeInventory(hotbar);

            // Start Game
            GameEngine.start(renderPane);

            level = new Level();
            level.generateMap();

            // Blurs the screen on scene load
            top.setEffect(GameEffects.GAME_BLUR);
            TimerUtil.lerp(1, t -> GameEffects.GAME_BLUR.setRadius(20 * (1 - t)));
        });

        // Exiting Game scene
        SceneManager.setOnUnload(() -> {
            GameEngine.stop();

            GameScreen.getLevel().unloadCurrentRoom();
        });
    }

    public static Level getLevel() {
        return level;
    }
}
