import core.DungeonCrawlerDriver;
import core.SceneManager;
import game.collidables.MainPlayer;
import game.levels.Direction;
import game.levels.Room;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import views.GameScreen;

import java.util.ArrayList;
import java.util.EnumMap;

import static org.junit.Assert.*;

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
        double initialX = MainPlayer.getPlayer().getPosition().getX();
        push(KeyCode.LEFT);
        double finalX = MainPlayer.getPlayer().getPosition().getX();

        assertTrue(finalX < initialX);
    }

    @Test
    public void testMoveRight() {
        double initialX = MainPlayer.getPlayer().getPosition().getX();
        push(KeyCode.RIGHT);
        double finalX = MainPlayer.getPlayer().getPosition().getX();

        assertTrue(initialX < finalX);
    }

    @Test
    public void testMoveUp() {
        double initialY = MainPlayer.getPlayer().getPosition().getY();
        push(KeyCode.UP);
        double finalY = MainPlayer.getPlayer().getPosition().getY();

        assertTrue(initialY > finalY);
    }

    @Test
    public void testMoveDown() {
        double initialY = MainPlayer.getPlayer().getPosition().getY();
        push(KeyCode.DOWN);
        double finalY = MainPlayer.getPlayer().getPosition().getY();

        assertTrue(initialY < finalY);
    }

    /**
     * Tests if wall collisions works
     * Only works if there is no doors in the corner
     */
    @Test
    public void testWallCollision() {
        press(KeyCode.UP, KeyCode.LEFT);
        sleep(2000);
        release(KeyCode.UP, KeyCode.LEFT);

        double distance = MainPlayer.getPlayer().getPosition().magnitude();

        // Current corner of the room is only 800 pixels from center
        assertTrue(distance < 1200d);
    }

    @Test
    public void testExit() {
        Room end = GameScreen.getLevel().getExit();
        assertNotNull(end);
        int distance = end.getDistanceFromEntrance();

        assertTrue(distance >= 6);
    }

    @Test
    public void testEntrance() {
        Room entrance = GameScreen.getLevel().getEntrance();
        assertNotNull(entrance);
        int distance = entrance.getDistanceFromEntrance();
        assertEquals(0, distance);

        EnumMap<Direction, ArrayList<StackPane>> doors = entrance.getDoors();
        assertFalse("Missing north door", doors.get(Direction.NORTH).isEmpty());
        assertFalse("Missing east door", doors.get(Direction.EAST).isEmpty());
        assertFalse("Missing south door", doors.get(Direction.SOUTH).isEmpty());
        assertFalse("Missing west door", doors.get(Direction.WEST).isEmpty());

        boolean[] activeDoors = entrance.getActiveDoors();
        for (boolean door : activeDoors) {
            assertTrue(door);
        }
    }
}


