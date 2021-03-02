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

    public void initialize() {
        MainPlayer.setUiInfoText(uiInfoText);

        GameManager.setDrawPane(drawPane);
        GameManager.start();
    }
}
