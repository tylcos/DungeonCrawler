package views;

import core.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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
    }
    
    public void onStartClick(MouseEvent mouseEvent) {
        SceneManager.loadScene(SceneManager.CONFIG);
    }

    public void onExitClick(MouseEvent mouseEvent) {
        System.exit(0);
    }
}