package views;

import core.GameDriver;
import core.SceneManager;
import game.entities.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;

/**
 * The screen that is displayed upon defeating the last monster and escaping the dungeon.
 */
public class EndScreen {
    @FXML
    private Label endGameText;
    @FXML
    private Label version;

    /**
     * Initializes the configuration screen
     */
    public void initialize() {
        version.setText(GameDriver.GAME_VERSION);

        InnerShadow color = new InnerShadow();
        color.setInput(new Glow(1));
        color.setWidth(50);
        color.setHeight(50);

        if (Player.getPlayer() != null && Player.getPlayer().isDead()) {
            endGameText.setText("You Died In The Dungeon!");
            color.setColor(Color.RED);
        } else {
            endGameText.setText("You Escaped The Dungeon!");
            color.setColor(Color.BLUE);
        }

        endGameText.setEffect(color);
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
