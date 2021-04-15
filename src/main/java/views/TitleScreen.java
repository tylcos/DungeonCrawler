package views;

import core.GameDriver;
import core.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import utilities.GameEffects;
import utilities.TimerUtil;

import java.util.function.Supplier;

/**
 * FXML controller for main title screen
 */
public class TitleScreen {
    @FXML
    private StackPane scalePane;
    @FXML
    private ImageView titleImage;
    @FXML
    private ImageView image1;
    @FXML
    private Label     version;

    // Defines how strong the blur is when the scene is loaded
    private static final int START_BLUR_STRENGTH = 100;

    /**
     * Initializes the title screen
     */
    public void initialize() {
        // Maintains image scales
        double titleImageWidth  = titleImage.getFitWidth();
        double titleImageHeight = titleImage.getFitHeight();
        double image1Width      = image1.getFitWidth();
        double image1Height     = image1.getFitHeight();

        Supplier<Double> scaleFactor = () -> scalePane.getWidth() / GameDriver.WIDTH;
        scalePane.widthProperty().addListener(observable -> {
            titleImage.setFitWidth(titleImageWidth * scaleFactor.get());
            titleImage.setFitHeight(titleImageHeight * scaleFactor.get());

            image1.setFitWidth(image1Width * scaleFactor.get());
            image1.setFitHeight(image1Height * scaleFactor.get());
        });

        // Blurs the screen on scene load
        scalePane.setEffect(GameEffects.HORIZONTAL_BLUR);
        TimerUtil.lerp(2, t -> GameEffects.HORIZONTAL_BLUR
                                      .setWidth(START_BLUR_STRENGTH * Math.pow(1 - t, 2)));

        version.setText(GameDriver.GAME_VERSION);
    }

    /**
     * Event listener for mouse click on start button.
     */
    public void onStartClick() {
        SceneManager.loadScene(SceneManager.CONFIG);
    }

    /**
     * Event listener for mouse click on exit button.
     */
    public void onExitClick() {
        System.exit(0);
    }
}