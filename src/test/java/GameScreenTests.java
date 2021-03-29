import core.DungeonCrawlerDriver;
import core.GameEngine;
import core.SceneManager;
import game.Weapon;
import game.entities.Entity;
import game.entities.MainPlayer;
import game.entities.Slime;
import game.levels.Direction;
import game.levels.Room;
import javafx.event.Event;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import views.GameScreen;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;

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


    //see if clicking puts enemy on attack mode
    //assumes if enemies r in starting room
    @Test
    public void testEnemyClickedEvent() {
        MainPlayer player = MainPlayer.getPlayer();
        List<Entity> list = GameEngine.getDynamicBodies();
        for (Entity body : list) {
            if (body instanceof Slime) {
                AtomicInteger timesClicked = new AtomicInteger();
                body.setOnMouseClicked(event -> timesClicked.getAndIncrement());
                Event.fireEvent(body, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                        0, 0, 0, MouseButton.PRIMARY, 1, true, true,
                        true, true,
                        true, true, true, true, true, true, null));
                assertTrue(timesClicked.get() != 0);
            }
        }
    }

    //see if clicking decreases enemy's hp
    //assumes enemies r in starting room
    @Test
    public void testEnemyHP() {
        MainPlayer player = MainPlayer.getPlayer();
        List<Entity> list = GameEngine.getDynamicBodies();
        for (Entity body : list) {
            if (body instanceof Slime) {
                int healthBefore = ((Slime) body).getHealth();
                Event.fireEvent(body, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                        0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                        true, true, true, true, true, true, null));
                int healthAfter = ((Slime) body).getHealth();
                assertNotEquals(healthBefore, healthAfter);
                break;
            }
        }
    }

    @Test
    public void testPlayerHPWhenAttacked() {
        MainPlayer player = MainPlayer.getPlayer();
        int prevHealth = player.getHealth();
        sleep(100000000);
        assertNotEquals(prevHealth, player.getHealth());
    }

    //see if player dies once hp = 0
    @Test
    public void testPlayerDeathWhenHP0() {
        MainPlayer player = MainPlayer.getPlayer();
        player.setHealth(0);
        assertTrue(player.isDead());
        assertNotEquals(90, player.getRotate());
    }

    @Test
    public void changeBow() {
        press(KeyCode.DIGIT1);
        Weapon bow = new Weapon("Bow",0,0);
        String bow1 = MainPlayer.getPlayer().getWeapon().getName();
        assertEquals(bow1, bow.getName());
    }
    @Test
    public void changeSword() {
        press(KeyCode.DIGIT3);
        Weapon sword = new Weapon("Sword",0,0);
        String sword1 = MainPlayer.getPlayer().getWeapon().getName();
        assertEquals(sword1, sword.getName());
    }
    @Test
    public void changeAxe() {
        press(KeyCode.DIGIT2);
        Weapon axe = new Weapon("Axe",0,0);
        String axe1 = MainPlayer.getPlayer().getWeapon().getName();
        assertEquals(axe1, axe.getName());
    }

    @Test
    public void testGameOverScreen(){

        MainPlayer.getPlayer().setHealth(0);
        sleep(7000);
        verifyThat("You Died In The Dungeon!", NodeMatchers.isVisible());
        verifyThat("Play Again", NodeMatchers.isVisible());
        verifyThat("Main Menu", NodeMatchers.isVisible());
        verifyThat("Exit Game", NodeMatchers.isVisible());
        }
    }







