package view;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ConfigScreen {
    private int width;
    private int height;
    private ComboBox<String> difficultyOptions;
    private ComboBox<String> weaponOptions;
    private TextField nameTextField;
    private Button goToFirstRoom;
    private Text nameError;

    public ConfigScreen(int width, int height) {
        this.width = width;
        this.height = height;

        difficultyOptions = new ComboBox<>();
        difficultyOptions.getItems().add("Easy");
        difficultyOptions.getItems().add("Medium");
        difficultyOptions.getItems().add("Hard");
        difficultyOptions.setValue("Easy");

        weaponOptions = new ComboBox<>();
        weaponOptions.getItems().add("Knife");
        weaponOptions.getItems().add("Axe");
        weaponOptions.getItems().add("Sword");
        weaponOptions.setValue("Knife");

        nameTextField = new TextField();

        goToFirstRoom = new Button("Go to first room");
        nameError = new Text();
        nameError.setFill(Color.RED);
    }

    public Scene getScene() {
        StackPane configuration = new StackPane();
        GridPane options = new GridPane();

        options.setAlignment(Pos.CENTER);
        options.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        options.setHgap(8);
        options.setVgap(8);
        options.setAlignment(Pos.CENTER);
        options.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        options.setHgap(8);
        options.setVgap(8);

        options.add(nameError, 1, 0);
        options.add(new Label("Name"), 0, 1);
        options.add(nameTextField, 1, 1);
        options.add(new Label("Difficulty"), 0, 2);
        options.add(difficultyOptions, 1, 2);
        options.add(new Label("Weapon"), 0, 3);
        options.add(weaponOptions, 1, 3);

        options.add(goToFirstRoom, 2, 4);
        GridPane.setHalignment(goToFirstRoom, HPos.RIGHT);
        configuration.getChildren().add(options);
        return new Scene(configuration, width, height);
    }

    public ComboBox<String> getDifficultyOptions() {
        return difficultyOptions;
    }

    public ComboBox<String> getWeaponOptions() {
        return weaponOptions;
    }

    public TextField getNameTextField() {
        return nameTextField;
    }

    public Button getGoToFirstRoomButton() {
        return goToFirstRoom;
    }

    public Text getNameError() {
        return nameError;
    }
}
