package views;

import core.GameDriver;
import core.SceneManager;
import javafx.fxml.FXML;
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