import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class SceneLoader {
    public static final String TITLE  = "TitleScreen.fxml";
    public static final String CONFIG = "ConfigScreen.fxml";


    public static void loadScene(String fxml) {
        try {
            System.out.println("loadScene");
            Parent newParent = FXMLLoader.load(SceneLoader.class.getResource(fxml));
            DungeonCrawlerDriver.mainWindow.setScene(new Scene(newParent));
            DungeonCrawlerDriver.mainWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
