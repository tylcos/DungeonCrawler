package views;

import core.GameManager;
import core.SceneManager;
import data.RandomUtil;
import game.MainPlayer;
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
        isNameInvalid();
    }

    /**
     * Event listener for mouse click on the start adventure button.
     */
    public void onStartClick() {
        if (isNameInvalid()) {
            return;
        }

        SceneManager.loadScene(SceneManager.GAME);

        GameManager.setPlayer(new MainPlayer(inputTextName.getText(),
                                             inputWeapon.getValue(),
                                             inputDifficulty.getValue()));
    }

    /**
     * Checks if name is valid and updates TextField color
     * Not sure if setting the style should be in this method
     *
     * @return If user name is invalid
     */
    private boolean isNameInvalid() {
        String  name      = inputTextName.getText();
        boolean isInvalid = name.trim().isEmpty() || name.length() > 28;

        if (isInvalid) {
            inputTextName.setStyle("-fx-background-color: #ff6868;");
        } else {
            inputTextName.setStyle("-fx-background-color: white;");
        }

        return isInvalid;
    }
}
