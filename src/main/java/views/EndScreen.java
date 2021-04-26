package views;

import core.SceneManager;
import game.entities.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import utilities.TimerUtil;

/**
 * The screen that is displayed upon defeating the last monster and escaping the dungeon.
 */
public class EndScreen {
    @FXML
    private StackPane endTop;
    @FXML
    private Label     endGameText;
    @FXML
    private Label     totalMoney;
    @FXML
    private Label     totalKills;
    @FXML
    private Label     totalPotions;
    @FXML
    private Label     totalNukes;

    // Stats variables
    private static int totalKilled;
    private static int totalPotionsObtained;
    private static int totalNukesUsed;

    /**
     * Initializes the configuration screen
     */
    public void initialize() {
        TimerUtil.lerp(1, t -> endTop.setOpacity(t));

        InnerShadow color = new InnerShadow();
        color.setInput(new Glow());
        color.setWidth(50);
        color.setHeight(50);
        DropShadow shadow = new DropShadow(20, Color.BLACK);
        shadow.setInput(color);

        Player player = Player.getPlayer();
        if (player != null && player.isDead()) {
            endGameText.setText("You Died In The Dungeon");

            color.setColor(Color.PURPLE);
        } else {
            endGameText.setText("You Escaped The Dungeon!");

            color.setColor(Color.BLUE);
        }

        int money = player == null ? 0 : player.getMoney();
        totalMoney.setText("Money Gathered:   " + money);
        totalKills.setText("Monsters Killed:  " + totalKilled);
        totalPotions.setText("Potions Obtained: " + totalPotionsObtained);
        totalNukes.setText("Nukes Used:       " + totalNukesUsed);

        totalKilled          = 0;
        totalPotionsObtained = 0;
        totalNukesUsed       = 0;

        endGameText.setEffect(shadow);
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

    public static void addTotalPotionsObtained() {
        totalPotionsObtained++;
    }

    public static void addTotalKilled() {
        totalKilled++;
    }

    public static void addTotalNukesUsed() {
        totalNukesUsed++;
    }

    public static int getTotalKill() {
        return totalKilled;
    }

    public static int getTotalNukesUsed() {
        return totalNukesUsed;
    }

    public static int getTotalPotionsObtained() {
        return totalPotionsObtained;
    }
}
