import core.DungeonCrawlerDriver;
import core.GameManager;
import core.SceneManager;
import core.ScreenManager;
import game.Direction;
import game.Room;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.EnumMap;

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
    
    @Test
    public void testExit() {
        Room end = GameManager.getLevel().getExit();
        assertTrue(end != null);
        int distance = end.getDistanceFromEntrance();
        assertTrue(distance >= 6);
    }
    
    @Test
    public void testEntrance() {
        Room entrance = GameManager.getLevel().getEntrance();
        assertTrue(entrance != null);
        int distance = entrance.getDistanceFromEntrance();
        assertTrue(distance == 0);
        EnumMap<Direction, ArrayList<StackPane>> doors = entrance.getDoors();
        assertTrue(doors.get(Direction.NORTH).size() > 0);
        assertTrue(doors.get(Direction.EAST).size() > 0);
        assertTrue(doors.get(Direction.SOUTH).size() > 0);
        assertTrue(doors.get(Direction.WEST).size() > 0);
        boolean[] active = entrance.getActiveDoors();
        for (boolean b : active) {
            assertTrue(b);
        }
    }
}


