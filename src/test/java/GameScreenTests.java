import core.GameDriver;
import core.SceneManager;
import game.collidables.Door;
import game.entities.Player;
import game.level.Direction;
import game.level.Room;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import views.GameScreen;

import java.util.*;

import static org.junit.Assert.*;

public class GameScreenTests extends ApplicationTest {

    // Auto starts on the game screen
    @Before
    public void start() throws Exception {
        launch(GameDriver.class, "--scene=GAME");
        assertEquals("Failed to load game screen.", SceneManager.GAME, SceneManager.getSceneName());
    }

    @Test
    public void testMoveLeft() {
        double initialX = Player.getPlayer().getPosition().getX();
        push(KeyCode.LEFT);
        double finalX = Player.getPlayer().getPosition().getX();

        assertTrue(finalX < initialX);
    }

    @Test
    public void testMoveRight() {
        double initialX = Player.getPlayer().getPosition().getX();
        push(KeyCode.RIGHT);
        double finalX = Player.getPlayer().getPosition().getX();

        assertTrue(initialX < finalX);
    }

    @Test
    public void testMoveUp() {
        double initialY = Player.getPlayer().getPosition().getY();
        push(KeyCode.UP);
        double finalY = Player.getPlayer().getPosition().getY();

        assertTrue(initialY > finalY);
    }

    @Test
    public void testMoveDown() {
        double initialY = Player.getPlayer().getPosition().getY();
        push(KeyCode.DOWN);
        double finalY = Player.getPlayer().getPosition().getY();

        assertTrue(initialY < finalY);
    }

    /**
     * Tests if wall collisions works Only works if there is no doors in the corner
     */
    @Test
    public void testWallCollision() {
        press(KeyCode.UP, KeyCode.LEFT);
        sleep(2000);
        release(KeyCode.UP, KeyCode.LEFT);

        double distance = Player.getPlayer().getPosition().magnitude();

        // Current corner of the room is only 800 pixels from center
        assertTrue(distance < 1200d);
    }

    /**
     * Tests if walking into a door changes the level
     */
    @Test
    public void testDoorCollision() {
        Room startRoom = GameScreen.getLevel().getCurrentRoom();

        press(KeyCode.UP);
        sleep(2000);
        release(KeyCode.UP);

        assertNotEquals(startRoom, GameScreen.getLevel().getCurrentRoom());
    }

    @Test
    public void testExitDistance() {
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

    @Test
    public void testExitDoor() {
        Room end = GameScreen.getLevel().getExit();
        int exitDirs = 0;
        for (Direction dir : Direction.values()) {
            List<Door> doors = end.getDoors(dir);
            int numExits = 0;
            for (Door d : doors) {
                if (d.getWin()) {
                    ++numExits;
                }
            }

            assertTrue(numExits == doors.size() || numExits == 0);
            if (numExits > 0) {
                ++exitDirs;
            }
        }

        assertEquals(1, exitDirs);
    }

    @Test
    public void testNumExits() {
        Room[][] rooms = GameScreen.getLevel().getMap();
        int numExit = 0;
        for (int i = 0; i < rooms.length; ++i) {
            for (int j = 0; j < rooms[i].length; ++j) {
                if (rooms[i][j] != null && rooms[i][j].isExit()) {
                    ++numExit;
                }
            }
        }

        assertEquals(1, numExit);
    }

    @Test
    public void testNumChallenges() {
        Room[][] rooms = GameScreen.getLevel().getMap();
        int numChallenge = 0;
        for (int i = 0; i < rooms.length; ++i) {
            for (int j = 0; j < rooms[i].length; ++j) {
                if (rooms[i][j] != null && rooms[i][j].isChallenge()) {
                    ++numChallenge;
                }
            }
        }

        assertTrue(numChallenge >= 2);
    }

    @Test
    public void testNoWeirdRooms() {
        // This might seem stupid but you'd be surprised how frequently this happens when we
        // tweak the level generation algorithm
        Room[][] rooms = GameScreen.getLevel().getMap();
        int weird = 0;
        for (int i = 0; i < rooms.length; ++i) {
            for (int j = 0; j < rooms[i].length; ++j) {
                if (rooms[i][j] != null) {
                    int exit = rooms[i][j].isExit() ? 1 : 0;
                    int entrance = rooms[i][j].isEntrance() ? 1 : 0;
                    int challenge = rooms[i][j].isChallenge() ? 1 : 0;
                    if (exit + entrance + challenge > 1) {
                        weird++;
                    }
                }
            }
        }

        assertEquals(0, weird);
    }

    @Test
    public void testChallengeActivation() {
        Platform.runLater(() -> {
            Room[][] rooms = GameScreen.getLevel().getMap();
            for (int i = 0; i < rooms.length; ++i) {
                for (int j = 0; j < rooms[i].length; ++j) {
                    if (rooms[i][j] != null && rooms[i][j].isChallenge()) {
                        GameScreen.getLevel().loadRoom(rooms[i][j]);
                        i = rooms.length;
                        break;
                    }
                }
            }
            assertTrue(GameScreen.getLevel().getCurrentRoom().isClear());

            sleep(1000);
            press(KeyCode.E);
            sleep(500);
            release(KeyCode.E);

            assertFalse(GameScreen.getLevel().getCurrentRoom().isClear());
        });
    }
}
