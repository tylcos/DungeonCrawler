package views;

import core.*;
import data.GameEffects;
import data.LerpTimer;
import game.Inventory;
import game.entities.Player;
import game.levels.Level;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

/**
 * FXML controller for the main game screen
 */
public class GameScreen {
    @FXML
    private StackPane top;
    @FXML
    private TextArea  uiMinimap;
    @FXML
    private TextArea  uiInfoText;
    @FXML
    private HBox      hotbar;
    @FXML
    private StackPane renderPane;
    @FXML
    private Label     version;

    @FXML
    private static LineChart<Number, Number> fpsGraph;

    private static Level level;

    /**
     * Initializes the game screen
     */
    public void initialize() {
        version.setText(GameDriver.GAME_VERSION);

        // Loaded the GameScreen without going through the config screen
        if (Player.getPlayer() == null) {
            Player.setPlayer("Team Azula", "Weapon", "Debug");
        }

        // Update UI
        Player.setUiInfoText(uiInfoText);
        Level.addUiEventHandler(event -> uiMinimap.setText(level.getMinimapString()));
        Inventory.initializeInventory(hotbar);

        if (GameDriver.isDebug()) {
            addFPSGraph();
        }

        // Start Game
        GameEngine.start(renderPane);

        level = new Level();
        level.generateMap();

        // Blurs the screen on scene load
        top.setEffect(GameEffects.GAME_BLUR);
        new LerpTimer(1, t -> GameEffects.GAME_BLUR.setRadius(20 * (1 - t)));
    }

    public static Level getLevel() {
        return level;
    }

    public static LineChart<Number, Number> getFpsGraph() {
        return fpsGraph;
    }

    private void addFPSGraph() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Frame Number");
        xAxis.setUpperBound(600);
        xAxis.setAutoRanging(false);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("FPS");

        fpsGraph = new LineChart<>(xAxis, yAxis);
        fpsGraph.getData().add(new XYChart.Series<>());
        fpsGraph.setAnimated(false);
        fpsGraph.setMaxHeight(300);
        fpsGraph.setMaxWidth(600);
        fpsGraph.setLegendVisible(false);
        fpsGraph.setVisible(false);

        VBox graphPane = new VBox(fpsGraph);
        graphPane.setAlignment(Pos.BOTTOM_LEFT);
        graphPane.setMouseTransparent(true);
        top.getChildren().add(graphPane);

        // Press F1 to show FPS graph
        InputManager.setEventHandler(KeyCode.F1, () -> fpsGraph.setVisible(!fpsGraph.isVisible()));
    }
}
