package views;

import core.*;
import game.entities.Player;
import game.inventory.Inventory;
import game.inventory.WeaponType;
import game.level.Level;
import javafx.fxml.FXML;
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

    private static Level level;

    /**
     * Initializes the game screen
     */
    public void initialize() {
        SceneManager.setOnLoad(() -> {
            // Loaded the GameScreen without going through the config screen
            if (Player.getPlayer() == null) {
                Player.setPlayer("Team Azula", WeaponType.Sword, "Debug");
            }

            version.setText(GameDriver.GAME_VERSION);

            // Update UI
            Player.setUiInfoText(uiInfoText);
            Level.addUiEventHandler(event -> uiMinimap.setText(level.getMinimapString()));
            Inventory.initializeInventory(hotbar);

            // Start Game
            SoundManager.playSound(true);
            GameEngine.start(renderPane);

            level = new Level();
            level.generateMap();

            // Blurs the screen on scene load
            top.setEffect(GameEffects.GAME_BLUR);
            TimerUtil.lerp(2, t -> GameEffects.GAME_BLUR.setRadius(30 * (1 - t)));
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
