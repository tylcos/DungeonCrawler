package views;

import driver.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.function.Supplier;

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

    private double titleImageWidth;
    private double image1Width;


    private final double windowWidth = 1920d;
    private final double aspectRatio = 9d / 16d;
    private final Supplier<Double> scaleFactor = () -> scene.getWidth() / windowWidth;


    public void initialize() {
        titleImageWidth = titleImage.getFitWidth();
        image1Width = image1.getFitWidth();


        // Maintains window aspect ratio
        stage.widthProperty().addListener(observable -> {
            stage.setMinHeight(stage.getWidth() * aspectRatio);
            stage.setMaxHeight(stage.getWidth() * aspectRatio);
        });


        // Maintains image scales
        scene.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
            titleImage.setFitWidth(scaleFactor.get() * titleImageWidth);
            image1.setFitWidth(scaleFactor.get() * image1Width);
            image1.setFitHeight(scaleFactor.get() * image1Width * aspectRatio);
        });


        // Sets windows initial size
        stage.setWidth(windowWidth);
    }


    public void onStartClick(MouseEvent mouseEvent) {
        System.out.println("Load config scene");
        SceneLoader.loadScene(SceneLoader.CONFIG);
    }

    public void onExitClick(MouseEvent mouseEvent) {
        System.exit(0);
    }
}