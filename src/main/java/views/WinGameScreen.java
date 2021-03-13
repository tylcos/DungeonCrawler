package views;

import core.SceneManager;

/**
 * The screen that is displayed upon defeating the last monster and escaping the dungeon.
 */
public class WinGameScreen {
    /**
     * Event listener for mouse click on the 'Play Again' button.
     * todo reset all aspects of the game
     */
    public void onPlayAgainClicked() {
        SceneManager.loadScene(SceneManager.CONFIG);
    }

    /**
     * Event listener for mouse click on the 'Main Menu' button.
     */
    public void onMainMenuClicked() {
        SceneManager.loadScene(SceneManager.TITLE);
    }

    /**
     * Event listener for mouse click on the 'Exit' button.
     */
    public void onExitClick() {
        System.exit(0);
    }
}
