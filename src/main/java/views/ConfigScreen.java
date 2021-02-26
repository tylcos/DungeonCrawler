package views;

import data.RandomNames;
import driver.MainPlayer;
import driver.SceneLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class ConfigScreen  {
    public TextField inputTextName;
    public ComboBox<String> inputWeapon;
    public ComboBox<String> inputDifficulty;


    public void initialize() {
        inputTextName.setText(RandomNames.getRandomName());
    }


    public void OnNameChange(KeyEvent keyEvent) {
        isNameInvalid();
    }

    public void OnStartClick(MouseEvent mouseEvent) {
        if (isNameInvalid())
            return;


        MainPlayer player = new MainPlayer(inputTextName.getText(), inputWeapon.getValue(), inputDifficulty.getValue());

        SceneLoader.loadScene(SceneLoader.GAME);
    }


    /**
     * Checks if name is valid and updates TextField color
     * Not sure if setting the style should be in this method
     *
     * @return If user name is invalid
     */
    private boolean isNameInvalid() {
        String name = inputTextName.getText();
        boolean isInvalid = name.trim().length() == 0 || name.length() > 28;

        if (isInvalid) {
            inputTextName.setStyle("-fx-background-color: #ff6868;");
        } else {
            inputTextName.setStyle("-fx-background-color: white;");
        }

        return isInvalid;
    }
}
