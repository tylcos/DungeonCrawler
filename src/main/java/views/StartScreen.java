package views;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class StartScreen {
    private int width;
    private int height;
    private Button startButton;

    public StartScreen(int width, int height) {
        this.width = width;
        this.height = height;
        this.startButton = new Button("Start");
    }
    public Scene getScene() throws FileNotFoundException {
        FileInputStream titleImageFile = new FileInputStream(
                "src/main/java/images/Dungeon Crawler Font imag resize.gif");

        FileInputStream backgroundImageFile = new FileInputStream(
                "src/main/java/images/IntroPage.gif");

        // For testing purpose. This may need change as we progress. (NULL background)
        FileInputStream whiteBackground = new FileInputStream(
                "src/main/java/images/WhiteBackground.gif");

        Image backgroundImage = new Image(backgroundImageFile);
        Image titleImage = new Image(titleImageFile);
        Image whiteBackgroundImage = new Image(whiteBackground);

        ImageView backgroundView = new ImageView(backgroundImage);
        ImageView titleImageView = new ImageView(titleImage);
        ImageView whiteImageView = new ImageView(whiteBackgroundImage);

        int buttonWidth = 125;
        int buttonHeight = 75;
        startButton.setPrefWidth(buttonWidth);
        startButton.setPrefHeight(buttonHeight);

        // COORDINATE FOR EACH NODE
        int backgroundStartingPointX = 0;
        int backgroundStartingPointY = 0;
        backgroundView.setTranslateX(backgroundStartingPointX);
        backgroundView.setTranslateY(backgroundStartingPointY);

        whiteImageView.setTranslateX(0);
        whiteImageView.setTranslateY(0);

        int titlePixelSizeX = 200;
        int titleStartingPointX = (width / 2) - (titlePixelSizeX / 2);

        titleImageView.setTranslateX(titleStartingPointX);
        titleImageView.setTranslateY(0);

        int startButtonLocX = (width / 2) - (buttonWidth / 2);
        int startButtonLocY = (height / 2) - (buttonHeight / 2);
        startButton.setTranslateX(startButtonLocX);
        startButton.setTranslateY(startButtonLocY);

        Label version = new Label("version 1.0");
        version.setFont(new Font(
                "Times New Roman", 10
        ));
        version.setTextFill(Color.BLACK);
        version.setTextAlignment(TextAlignment.RIGHT);
        version.setTranslateX(width - 45);
        version.setTranslateY(height - 30);

        Label team = new Label("Team 03: Team Azula");
        team.setFont(new Font(
                "Times New Roman", 10
        ));

        team.setTranslateX(width - 90);
        team.setTranslateY(height - 15);
        BorderPane borderPane = new BorderPane();
        Group initialScreenGroup = new Group();
        initialScreenGroup.getChildren().addAll(whiteImageView, backgroundView,
                titleImageView, startButton, version, team);

        borderPane.setTop(initialScreenGroup);
        return new Scene(borderPane, width, height);
    }

    public Button getStartButton() {
        return startButton;
    }
}
