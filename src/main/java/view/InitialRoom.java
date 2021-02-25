package view;

import driver.MainPlayer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class InitialRoom {
    private int width;
    private int height;
    private MainPlayer player;
    private Button shopButton;
    private Rectangle playerSquare;
    private Rectangle exitRectangle;

    public InitialRoom(int width, int height, MainPlayer player) {
        this.width = width;
        this.height = height;
        this.player = player;
        shopButton = new Button("Shop");
        playerSquare = new Rectangle(0, 0, 50, 50);
        exitRectangle = new Rectangle(0, 0, 50, 100);
    }

    public Scene getScene() {
        StackPane centerOfRoom = new StackPane();
        VBox playerStats = new VBox();
        BorderPane firstRoomBorder = new BorderPane();
        HBox playerStatsShopButton = new HBox();
        StackPane weaponDisplay = player.getWeapon().getWeaponDisplay();

        Label name = new Label("Name: " + player.getName());
        Label money = new Label("Money: $" + player.getMoney());
        Label hp = new Label("Hitpoints: " + player.getHitpoints());

        StackPane currPlayer = new StackPane();
        Text playerText = new Text(player.getName());
        playerText.setFill(Color.DARKOLIVEGREEN);
        Rectangle playerSquare = new Rectangle(0, 0, 50, 50);
        currPlayer.getChildren().addAll(playerSquare, playerText);

        StackPane exit = new StackPane();
        Text exitText = new Text("Exit");
        exitText.setFill(Color.RED);
        Rectangle exitRectangle = new Rectangle(0, 0, 50, 100);
        exitRectangle.setFill(Color.BLACK);
        exit.getChildren().addAll(exitRectangle, exitText);

        playerStats.getChildren().addAll(name, money, hp);
        Region leftRegion = new Region();
        Region rightRegion = new Region();
        HBox.setHgrow(leftRegion, Priority.ALWAYS);
        HBox.setHgrow(rightRegion, Priority.ALWAYS);

        playerStatsShopButton.getChildren().addAll(playerStats, leftRegion,
                shopButton, rightRegion, weaponDisplay);
        playerStatsShopButton.setPadding(new Insets(10, 10, 0, 10));
        playerStats.setAlignment(Pos.TOP_LEFT);
        centerOfRoom.getChildren().add(currPlayer);

        firstRoomBorder.setCenter(centerOfRoom);
        firstRoomBorder.setTop(playerStatsShopButton);
        firstRoomBorder.setRight(exit);
        BorderPane.setAlignment(exit, Pos.TOP_CENTER);
        return new Scene(firstRoomBorder, width, height);
    }

    public Button getShopButton() {
        return shopButton;
    }

    public Rectangle getPlayerSquare() {
        return playerSquare;
    }

    public Rectangle getExitRectangle() {
        return exitRectangle;
    }
}
