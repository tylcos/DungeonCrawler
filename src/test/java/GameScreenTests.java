import core.DungeonCrawlerDriver;
import core.GameManager;
import core.SceneManager;
import core.ScreenManager;
import javafx.scene.input.KeyCode;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameScreenTests extends ApplicationTest {

    // Auto starts on the game screen
    @Before
    public void start() throws Exception {
        launch(DungeonCrawlerDriver.class, "--scene=GAME", "--NoDebug");
        assertEquals("Failed to load game screen.",
                     SceneManager.GAME, SceneManager.getSceneName());
    }

    @Test
    public void testMoveLeft() {
        double initialX = GameManager.getPlayer().getX();
        push(KeyCode.LEFT);
        double finalX = GameManager.getPlayer().getX();

        assertTrue(finalX < initialX);
    }

    @Test
    public void testMoveRight() {
        double initialX = GameManager.getPlayer().getX();
        push(KeyCode.RIGHT);
        double finalX = GameManager.getPlayer().getX();

        assertTrue(initialX < finalX);
    }

    @Test
    public void testMoveUp() {
        double initialY = GameManager.getPlayer().getY();
        push(KeyCode.UP);
        double finalY = GameManager.getPlayer().getY();

        assertTrue(initialY > finalY);
    }

    @Test
    public void testMoveDown() {
        double initialY = GameManager.getPlayer().getY();
        push(KeyCode.DOWN);
        double finalY = GameManager.getPlayer().getY();

        assertTrue(initialY < finalY);
    }

    /**
     * Tests if wall collisions works
     * Only works if there is no doors in the corner
     *
     * @throws InterruptedException InterruptedException
     */
    @Test
    public void testWallCollision() throws InterruptedException {
        // Needed to prevent IllegalMonitorStateException
        synchronized (this) {
            press(KeyCode.UP, KeyCode.LEFT);
            wait(2000);
            release(KeyCode.UP, KeyCode.LEFT);
        }

        double distance = GameManager.getPlayer().getPosition()
                                  .subtract(ScreenManager.getScreenCenter()).magnitude();

        // Current corner of the room is only 800 pixels from center
        assertTrue(distance < 1200d);
    }
}


