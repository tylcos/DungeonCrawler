import javafx.scene.input.MouseEvent;

public class TitleScreen {

    private void OnStartClick(MouseEvent event) {
        SceneLoader.loadScene(SceneLoader.CONFIG);
    }

    public void OnExitClick(MouseEvent mouseEvent) {
        System.exit(0);
    }
}