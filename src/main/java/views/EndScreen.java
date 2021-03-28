package views;

import core.SceneManager;
import game.collidables.MainPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * The screen that is displayed upon defeating the last monster and escaping the dungeon.
 */
public class EndScreen {
    @FXML
    private Label endGameText;

    /**
     * Initializes the configuration screen
     */
    public void initialize() {
        String msg = "You Escaped The Dungeon!";
        if (MainPlayer.getPlayer() != null && MainPlayer.getPlayer().isDead()) {
            msg = "You Died In The Dungeon!";
        }

        endGameText.setText(msg);
    }

    /**
     * Event listener for mouse click on the 'Play Again' button.
     * todo reset all aspects of the game
     */
    public void onPlayAgainClicked() {
        SceneManager.loadScene(SceneManager.CONFIG);
    }

    /**
     * Event listener for mouse click on the 'Main Menu' button.
     */
    public void onMainMenuClicked() {
        SceneManager.loadScene(SceneManager.TITLE);
    }

    /**
     * Event listener for mouse click on the 'Exit' button.
     */
    public void onExitClick() {
        System.exit(0);
    }
}
