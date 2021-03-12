package views;

import core.GameManager;
import core.SceneManager;
import game.MainPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

/**
 * FXML controller for main game screen
 */
public class GameScreen {
    @FXML
    private TextArea uiInfoText;
    @FXML
    private Pane drawPane;

    /**
     * Initializes the game screen
     */
    public void initialize() {
        MainPlayer.setUiInfoText(uiInfoText);

        GameManager.start(drawPane);

        // Loaded the GameScreen without going through the config screen
        if (!SceneManager.CONFIG.equals(SceneManager.getSceneName())) {
            GameManager.setPlayer(new MainPlayer("Team Azula", "Weapon", "Normal"));
        }
    }
}
