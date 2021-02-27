package views;

import core.GameManager;
import game.Entity;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

public class GameScreen {
    @FXML
    private TextArea uiInfoText;
    @FXML
    private Pane drawPane;


    public void initialize() {
        if (GameManager.isPaused()) {
            throw new IllegalStateException("Loading GameScreen while GameManager paused");
        }


        GameManager.setDrawPane(drawPane);
        GameManager.start();


        uiInfoText.setText(GameManager.getPlayer().toString());


        Entity player = new Entity("/images/Player.png", 500, 500, 5, 5);
        player.setVel(10, 10);
    }
}
