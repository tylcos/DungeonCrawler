package driver;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import view.ConfigScreen;
import view.StartScreen;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class DungeonCrawlerDriver extends Application {
    private final int WIDTH = 750;
    private final int HEIGHT = 550;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        BorderPane startBorderPane = new BorderPane();
        BorderPane configBorderPane = new BorderPane();


        //setupConfigScreen(configBorderPane);

        this.primaryStage.setTitle("Dungeon Crawler");
        showStartScreen();
    }

    private void showStartScreen() throws FileNotFoundException {
        StartScreen startScreen = new StartScreen(WIDTH, HEIGHT);
        Button startButton = startScreen.getStartButton();
        startButton.setOnAction(event -> showConfigScreen());
        primaryStage.setScene(startScreen.getScene());
        primaryStage.show();
    }

    private void showConfigScreen() {
        ConfigScreen configScreen = new ConfigScreen(WIDTH, HEIGHT);
        Button goToFirstRoomButton = configScreen.getGoToFirstRoomButton();
        goToFirstRoomButton.setOnAction(event -> {
            if (validatePlayerName(configScreen.getNameTextField().getText())) {
                MainPlayer player = new MainPlayer(
                        // todo fix weapon damage and price
                        configScreen.getNameTextField().getText(),
                        new Weapon(configScreen.getWeaponOptions().getValue(), 0, 0),
                        configScreen.getDifficultyOptions().getValue()
                );
                BorderPane firstRoomBorder = new BorderPane();
                primaryStage.setScene(new Scene(firstRoomBorder, WIDTH, HEIGHT));
                displayFirstRoom(player, firstRoomBorder);
            } else {
                configScreen.getNameError().setText("Name is not valid. Try again.");
            }
        });
        primaryStage.setScene(configScreen.getScene());
        primaryStage.show();
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