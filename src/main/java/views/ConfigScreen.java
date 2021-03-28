package views;

import core.SceneManager;
import data.RandomUtil;
import game.entities.MainPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * FXML controller for player creation screen
 */
public class ConfigScreen {
    @FXML
    private TextField        inputTextName;
    @FXML
    private ComboBox<String> inputWeapon;
    @FXML
    private ComboBox<String> inputDifficulty;

    /**
     * Initializes the configuration screen
     */
    public void initialize() {
        inputTextName.setText(RandomUtil.getRandomName());
        inputTextName.selectAll();
    }

    /**
     * Checks if the name of the player is valid.
     */
    public void onNameChange() {
        isNameValid();
    }

    /**
     * Event listener for mouse click on the start adventure button.
     */
    public void onStartClick() {
        if (isNameValid()) {
            MainPlayer.setPlayer(inputTextName.getText(),
                                 inputWeapon.getValue(),
                                 inputDifficulty.getValue());
            SceneManager.loadScene(SceneManager.GAME);
        }
    }

    /**
     * Checks if name is valid and updates TextField color.
     * Not sure if setting the style should be in this method.
     *
     * @return If user name is invalid
     */
    private boolean isNameValid() {
        String  name      = inputTextName.getText();
        boolean isValid = !name.trim().isEmpty() && name.length() <= 28;

        if (isValid) {
            inputTextName.setStyle("-fx-background-color: white;");
        } else {
            inputTextName.setStyle("-fx-background-color: #ff6868;");
        }

        return isValid;
    }
}
