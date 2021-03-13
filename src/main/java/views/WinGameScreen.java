package views;

import core.SceneManager;
import javafx.scene.input.MouseEvent;

/**
 * The screen that is displayed upon defeating the last monster and escaping the dungeon.
 */
public class WinGameScreen {
    /**
     * Event listener for mouse click on the Play Again button.
     * todo reset all aspects of the game
     *
     * @param mouseEvent the event inputted by the mouse
     */
    public void onPlayAgainClicked(MouseEvent mouseEvent) {
        SceneManager.loadScene(SceneManager.CONFIG);
    }

    /**
     * Event listener for mouse click on exit button.
     *
     * @param mouseEvent the event inputted by the mouse
     */
    public void onExitClick(MouseEvent mouseEvent) {
        System.exit(0);
    }
}
