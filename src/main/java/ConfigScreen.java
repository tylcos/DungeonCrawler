import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class ConfigScreen {

    @FXML
    public TextField TextThing;

    public void OnTextChanged(KeyEvent keyEvent) {
        System.out.println(TextThing.getText());
    }
}