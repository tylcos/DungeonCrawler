package views;

import core.GameManager;
import core.SceneManager;
import game.Enemy;
import game.MainPlayer;
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

        // Display the minimap
        uiMinimap.setText(GameManager.getLevel().getMinimapString());

        new Enemy(100, 0);
    }
}
