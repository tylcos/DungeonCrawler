package views;

import core.GameDriver;
import core.SceneManager;
import data.LerpTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

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
        BoxBlur blur = new BoxBlur(START_BLUR_STRENGTH, 0, 1);
        scalePane.setEffect(blur);
        new LerpTimer(2, t -> blur.setWidth(START_BLUR_STRENGTH * (1 - t)));

        /*
        if (GameDriver.isDebug()) {
            Stage stage = SceneManager.getStage();
            System.out.println("\nDebug Screen Scaling");
            System.out.println("Screen Scale: " + Screen.getPrimary().getOutputScaleX()
                               + ", " + Screen.getPrimary().getOutputScaleY());
            System.out.println("Output Scale: " + stage.getOutputScaleX()
                               + ", " + stage.getOutputScaleY());
            System.out.println("Render Scale: " + stage.getRenderScaleX()
                               + ", " + stage.getRenderScaleY());
        }*/

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