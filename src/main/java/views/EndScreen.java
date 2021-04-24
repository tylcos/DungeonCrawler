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
    @FXML
    private Label totalPostion;
    @FXML
    private Label totalKill;
    @FXML
    private Label totalNuke;

    //stats variable
    private static int totalNumKill = 0;
    private static int totalPostionObtain = 0;
    private static int totalNuked = 0;

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
            endGameText.setText("You Died");
            totalKill.setText("total monster kill: " + totalNumKill);
            totalPostion.setText("total position obtained: " + totalPostionObtain);
            totalNuke.setText("number of nuke used: " + totalNuked);


            color.setColor(Color.RED);
        } else {
            endGameText.setText("You Escaped The Dungeon!");
            totalKill.setText("total monster kill: " + totalNumKill);
            totalPostion.setText("total position obtained: " + totalPostionObtain);
            totalNuke.setText("total number of nuke used: " + totalNuked);
            color.setColor(Color.BLUE);
        }

        endGameText.setEffect(color);
    }

    /**
     * Event listener for mouse click on the 'Play Again' button.
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

    public static void addTotalPostionObtain() {
        totalPostionObtain++;
    }

    public static void addTotalNumKill() {
        totalNumKill++;
    }

    public static void addTotalNukedUsed() {
        totalNuked++;
    }


}
