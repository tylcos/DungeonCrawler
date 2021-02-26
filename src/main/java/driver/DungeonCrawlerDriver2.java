package driver;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

// TODO: Convert to FXML
class DungeonCrawlerDriver2 {
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
}