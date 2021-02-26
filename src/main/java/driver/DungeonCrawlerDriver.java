package driver;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import view.ConfigScreen;
import view.InitialRoom;
import view.StartScreen;

import java.io.FileNotFoundException;

public class DungeonCrawlerDriver extends Application {
    private final int WIDTH = 750;
    private final int HEIGHT = 550;
    private Stage primaryStage;
    private MainPlayer mainPlayer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
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
                    this.mainPlayer = new MainPlayer(
                        // todo fix weapon damage and price
                        configScreen.getNameTextField().getText(),
                        new Weapon(configScreen.getWeaponOptions().getValue(), 0, 0),
                        configScreen.getDifficultyOptions().getValue()
                );
                primaryStage.setScene(configScreen.getScene());
                showInitialRoom();
            } else {
                configScreen.getNameError().setText("Name is not valid. Try again.");
            }
        });
        primaryStage.setScene(configScreen.getScene());
        primaryStage.show();
    }

    private void showInitialRoom() {
        InitialRoom initialRoom = new InitialRoom(WIDTH, HEIGHT, mainPlayer);
        Button shopButton = initialRoom.getShopButton();
        shopButton.setOnAction(event -> System.out.println("Currently in development!"));
        primaryStage.setScene(initialRoom.getScene());
        primaryStage.show();
    }

    private static boolean validatePlayerName(String name) {
        return name.trim().length() > 0;
    }
}