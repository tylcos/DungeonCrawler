import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
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

        setupConfigScreen(primaryStage, configBorderPane);

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
        start.setPrefWidth(125);
        start.setPrefHeight(75);
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
        team.setFont(new Font(
                "Times New Roman", 10
        ));
        version.setTextFill(Color.BLACK);
        version.setTextAlignment(TextAlignment.RIGHT);

        versionTeamNumber.getChildren().addAll(version, team);
        versionTeamNumber.setAlignment(Pos.CENTER_RIGHT);
        versionTeamNumber.setPadding(new Insets(0, 10, 10, 0));

        startScreen.getChildren().addAll(start);

        startBorderPane.setCenter(startScreen);
        startBorderPane.setTop(title);
        startBorderPane.setBottom(versionTeamNumber);
        BorderPane.setAlignment(title, Pos.TOP_CENTER);
    }

    private void setupConfigScreen(Stage primaryStage, BorderPane configBorderPane) {
        StackPane configuration = new StackPane();
        GridPane options = new GridPane();

        options.setAlignment(Pos.CENTER);
        options.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        options.setHgap(8);
        options.setVgap(8);

        ComboBox<String> difficultyOptions = new ComboBox<>();
        difficultyOptions.getItems().add("Easy");
        difficultyOptions.getItems().add("Medium");
        difficultyOptions.getItems().add("Hard");
        difficultyOptions.setValue("Easy");

        ComboBox<String> weaponOptions = new ComboBox<>();
        weaponOptions.getItems().add("Knife");
        weaponOptions.getItems().add("Axe");
        weaponOptions.getItems().add("Sword");
        weaponOptions.setValue("Knife");

        TextField nameTextField = new TextField();
        Text nameError = new Text();
        nameError.setFill(Color.RED);

        options.add(nameError, 1, 0);
        options.add(new Label("Name"), 0, 1);
        options.add(nameTextField, 1, 1);
        options.add(new Label("Difficulty"), 0, 2);
        options.add(difficultyOptions, 1, 2);
        options.add(new Label("Weapon"), 0, 3);
        options.add(weaponOptions, 1, 3);

        // todo change first room into shop
        Button toFirstRoom = new Button("Go to first room");
        toFirstRoom.setOnAction(event -> {
            if (validatePlayerName(nameTextField.getText())) {
                MainPlayer player = new MainPlayer(
                        // todo fix weapon damage and price
                        nameTextField.getText(),
                        new Weapon(weaponOptions.getValue(), 0, 0),
                        difficultyOptions.getValue()
                );
                BorderPane firstRoomBorder = new BorderPane();
                primaryStage.setScene(new Scene(firstRoomBorder, 750, 550));
                displayFirstRoom(player, firstRoomBorder);
            } else {
                nameError.setText("Name is not valid. Try again.");
            }
        });

        options.add(toFirstRoom, 2, 4);
        GridPane.setHalignment(toFirstRoom, HPos.RIGHT);

        configuration.getChildren().add(options);

        configBorderPane.setCenter(configuration);
    }

    private void displayFirstRoom(MainPlayer player, BorderPane firstRoomBorder) {
        StackPane centerOfRoom = new StackPane();
        VBox playerStats = new VBox();

        Label name = new Label("Name: " + player.getName());
        Label money = new Label("Money: $" + player.getMoney());
        Label hp = new Label("Hitpoints: " + player.getHitpoints());

        Button shop = new Button("Shop");
        shop.setOnAction(event -> System.out.println("Currently in development!"));

        StackPane currPlayer = new StackPane();
        Text playerText = new Text(player.getName());
        playerText.setFill(Color.WHITE);
        Rectangle playerSquare = new Rectangle(0, 0, 50, 50);
        currPlayer.getChildren().addAll(playerSquare, playerText);

        StackPane exit = new StackPane();
        Text exitText = new Text("Exit");
        exitText.setFill(Color.RED);
        Rectangle exitRectangle = new Rectangle(0, 0, 50, 100);
        exitRectangle.setFill(Color.BLACK);
        exit.getChildren().addAll(exitRectangle, exitText);

        playerStats.getChildren().addAll(name, money, hp);
        playerStats.setAlignment(Pos.TOP_LEFT);
        playerStats.setPadding(new Insets(10, 0, 0, 10));

        centerOfRoom.getChildren().add(currPlayer);

        firstRoomBorder.setCenter(centerOfRoom);
        firstRoomBorder.setTop(playerStats);
        firstRoomBorder.setRight(exit);
        BorderPane.setAlignment(playerStats, Pos.TOP_LEFT);
        BorderPane.setAlignment(exit, Pos.TOP_CENTER);
    }

    private static boolean validatePlayerName(String name) {
        return name.trim().length() > 0;
    }
}