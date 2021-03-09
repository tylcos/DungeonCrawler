package views;

import core.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.function.Supplier;

/**
 * FXML controller for main title screen
 */
public class TitleScreen {
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Pane scalePane;
    @FXML
    private ImageView titleImage;
    @FXML
    private ImageView image1;

    private double windowWidth = 1920d;
    private final Supplier<Double> scaleFactor = () -> scene.getWidth() / windowWidth;

    /**
     * Initializes the title screen
     */
    public void initialize() {
        // Maintains image scales
        double titleImageWidth = titleImage.getFitWidth();
        double titleImageHeight = titleImage.getFitHeight();
        double image1Width = image1.getFitWidth();
        double image1Height = image1.getFitHeight();

        scene.widthProperty().addListener(observable -> {
            titleImage.setFitWidth(titleImageWidth * scaleFactor.get());
            titleImage.setFitHeight(titleImageHeight * scaleFactor.get());

            image1.setFitWidth(image1Width * scaleFactor.get());
            image1.setFitHeight(image1Height * scaleFactor.get());
        });

        System.out.println("Debug Screen Scaling");
        System.out.println("dpi: " + Screen.getPrimary().getDpi());
        System.out.println("Screen Scale: " + Screen.getPrimary().getOutputScaleX()
                                   + ", " + Screen.getPrimary().getOutputScaleY());
        System.out.println("Output Scale: " + stage.getOutputScaleX()
                                   + ", " + stage.getOutputScaleY());
        System.out.println("Render Scale: " + stage.getRenderScaleX()
                                   + ", " + stage.getRenderScaleY());
    }

    /**
     * Event listener for mouse click to start button.
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