package views;

import driver.SceneLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.function.Supplier;

public class TitleScreen {
    public Stage stage;
    public Scene scene;
    public Pane scalePane;


    public ImageView titleImage;
    public ImageView image1;

    private double titleImageWidth;
    private double image1Width;


    private final double windowWidth = 1920d;
    private final double aspectRatio = 9d/16d;
    private final Supplier<Double> scaleFactor = () -> scene.getWidth() / windowWidth;


    public void OnShow(WindowEvent windowEvent) {
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

    public void OnStartClick(MouseEvent mouseEvent) {
        System.out.println("Load config scene");
        SceneLoader.loadScene(SceneLoader.CONFIG);
    }

    public void OnExitClick(MouseEvent mouseEvent) {
        System.exit(0);
    }
}