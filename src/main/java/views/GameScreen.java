package views;

import core.GameManager;
import core.SceneManager;
import game.collidables.MainPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

/**
 * FXML controller for the main game screen
 */
public class GameScreen {
    @FXML
    private TextArea uiMinimap;
    @FXML
    private TextArea uiInfoText;
    @FXML
    private Pane     drawPane;

    /**
     * Initializes the game screen
     */
    public void initialize() {
        GameManager.start(drawPane);

        // Loaded the GameScreen without going through the config screen
        if (!SceneManager.CONFIG.equals(SceneManager.getSceneName())) {
            MainPlayer.setPlayer("Team Azula", "Weapon", "Normal");
        }

        // Update UI
        MainPlayer.setUiInfoText(uiInfoText);
        uiMinimap.setText(GameManager.getLevel().getMinimapString());
    }
}
