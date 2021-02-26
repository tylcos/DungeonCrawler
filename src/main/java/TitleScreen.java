import javafx.scene.input.MouseEvent;

public class TitleScreen {

    public void OnStartClick(MouseEvent mouseEvent) {
        SceneLoader.loadScene(SceneLoader.CONFIG);
    }

    public void OnExitClick(MouseEvent mouseEvent) {
        System.exit(0);
    }
}