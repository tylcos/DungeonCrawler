package views;

import core.GameManager;
import core.SceneManager;
import data.RandomCoins;
import data.RandomNames;
import game.Coin;
import game.Item;
import game.MainPlayer;
import game.Enemy;
import javafx.fxml.FXML;
import javafx.scene.ImageCursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


/**
 * FXML controller for player creation screen
 */
public class ConfigScreen {
    @FXML
    private TextField inputTextName;
    @FXML
    private ComboBox<String> inputWeapon;
    @FXML
    private ComboBox<String> inputDifficulty;

    public void initialize() {
        inputTextName.setText(RandomNames.getRandomName());
        inputTextName.selectAll();
    }

    public void onNameChange(KeyEvent keyEvent) {
        isNameInvalid();
    }

    public void onStartClick(MouseEvent mouseEvent) {
        if (isNameInvalid()) {
            return;
        }

        SceneManager.loadScene(SceneManager.GAME);

        GameManager.setPlayer(new MainPlayer(inputTextName.getText(),
                inputWeapon.getValue(), inputDifficulty.getValue()));
        GameManager.setEnemy(new Enemy(3,5));
        GameManager.setCoin(new Coin(true));

        GameManager.setItem(new Item(true));



    }

    /**
     * Checks if name is valid and updates TextField color
     * Not sure if setting the style should be in this method
     *
     * @return If user name is invalid
     */
    private boolean isNameInvalid() {
        String name = inputTextName.getText();
        boolean isInvalid = name.trim().isEmpty() || name.length() > 28;

        if (isInvalid) {
            inputTextName.setStyle("-fx-background-color: #ff6868;");
        } else {
            inputTextName.setStyle("-fx-background-color: white;");
        }

        return isInvalid;
    }
}
