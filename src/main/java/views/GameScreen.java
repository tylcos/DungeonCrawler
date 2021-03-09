package views;

import core.GameManager;
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
    }
}
