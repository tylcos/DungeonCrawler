import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;

public class DungeonCrawlerDriver extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane startBorderPane = new BorderPane();
        BorderPane configBorderPane = new BorderPane();
        initialGameScreen(primaryStage, startBorderPane, configBorderPane);

        primaryStage.setTitle("Dungeon Crawler");
        primaryStage.setScene(new Scene(startBorderPane, 750, 550));
        primaryStage.show();
    }

    private void initialGameScreen(Stage primaryStage, BorderPane startBorderPane, BorderPane configuration) {
        StackPane startScreen = new StackPane();
        VBox versionTeamNumber = new VBox();

        Label title = new Label("Dungeon Crawler");
        title.setFont(new Font("Times New Roman", 40));
        title.setTextFill(Color.BLACK);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setPadding(new Insets(30, 0, 0, 0));

        Button start = new Button();
        start.setText("Start");
        start.setOnAction(event -> {
            primaryStage.setScene(new Scene(configuration, 750, 550));
        });

        Label version = new Label("version 1.0");
        version.setFont(new Font(
                "Times New Roman", 10
        ));
        version.setTextFill(Color.BLACK);
        version.setTextAlignment(TextAlignment.RIGHT);

        Label team = new Label("Team 03: Team Azula");
        version.setTextFill(Color.BLACK);
        version.setTextAlignment(TextAlignment.RIGHT);

        versionTeamNumber.getChildren().addAll(version, team);

        startScreen.getChildren().addAll(start);

        startBorderPane.setCenter(startScreen);
        startBorderPane.setTop(title);
        startBorderPane.setBottom(versionTeamNumber);
    }

/*
    private void setupConfigScreen(Stage primaryStage) {
        StackPane configuration = new StackPane();
        Label nameLabel = new Label("Name: ");
        TextField nameText = new TextField();
        HBox name = new HBox();
        name.getChildren().addAll(nameLabel, nameText);
        ComboBox difficultyOptions = new ComboBox();
        difficultyOptions.getItems().add("Easy");
        difficultyOptions.getItems().add("Medium");
        difficultyOptions.getItems().add("Hard");
        Label difficultyLabel = new Label("Select your difficulty");
        HBox difficulty = new HBox();
        difficulty.getChildren().addAll(difficultyLabel, difficultyOptions);
        ComboBox weaponOptions = new ComboBox();
        difficultyOptions.getItems().add("Knife");
        difficultyOptions.getItems().add("Axe");
        difficultyOptions.getItems().add("Sword");
        Label weaponLabel = new Label("Select your weapon");
        HBox weapon = new HBox();
        weapon.getChildren().addAll(weaponLabel, weaponOptions);
        Button toFirstRoom = new Button("Go to first room");
        VBox vbox = new VBox(name, difficulty, weapon, toFirstRoom);
        configuration.getChildren().add(vbox);
    }*/
}