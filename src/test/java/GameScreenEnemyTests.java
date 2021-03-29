import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import core.DungeonCrawlerDriver;
import core.GameEngine;
import core.SceneManager;
import game.Weapon;
import game.entities.Entity;
import game.entities.MainPlayer;
import game.entities.Slime;
import game.levels.Level;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class GameScreenEnemyTests extends ApplicationTest {

    // Auto starts on the game screen
    @Before
    public void start() throws Exception {
        Level.spawnStuffInEntrance = true;
        launch(DungeonCrawlerDriver.class, "--scene=GAME", "--NoDebug");
        assertEquals("Failed to load game screen.", SceneManager.GAME, SceneManager.getSceneName());
    }

    // see if clicking puts enemy on attack mode
    // assumes if enemies r in starting room
    @Test
    public void testEnemyClickedEvent() {
        MainPlayer player = MainPlayer.getPlayer();
        List<Entity> list = GameEngine.getDynamicBodies();
        for (Entity body : list) {
            if (body instanceof Slime) {
                AtomicInteger timesClicked = new AtomicInteger();
                body.setOnMouseClicked(event -> timesClicked.getAndIncrement());
                Event.fireEvent(body,
                        new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1,
                                true, true, true, true, true, true, true, true, true, true, null));
                assertTrue(timesClicked.get() != 0);
            }
        }
    }

    // see if clicking decreases enemy's hp
    // assumes enemies r in starting room
    @Test
    public void testEnemyHP() {
        MainPlayer player = MainPlayer.getPlayer();
        List<Entity> list = GameEngine.getDynamicBodies();
        for (Entity body : list) {
            if (body instanceof Slime) {
                int healthBefore = ((Slime) body).getHealth();
                Event.fireEvent(body,
                        new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1,
                                true, true, true, true, true, true, true, true, true, true, null));
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

    // see if player dies once hp = 0
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
        Weapon bow = new Weapon("Bow", 0, 0);
        String bow1 = MainPlayer.getPlayer().getWeapon().getName();
        assertEquals(bow1, bow.getName());
    }

    @Test
    public void changeSword() {
        press(KeyCode.DIGIT3);
        Weapon sword = new Weapon("Sword", 0, 0);
        String sword1 = MainPlayer.getPlayer().getWeapon().getName();
        assertEquals(sword1, sword.getName());
    }

    @Test
    public void changeAxe() {
        press(KeyCode.DIGIT2);
        Weapon axe = new Weapon("Axe", 0, 0);
        String axe1 = MainPlayer.getPlayer().getWeapon().getName();
        assertEquals(axe1, axe.getName());
    }

    @Test
    public void testGameOverScreen() {

        MainPlayer.getPlayer().setHealth(0);
        sleep(7000);
        verifyThat("You Died In The Dungeon!", NodeMatchers.isVisible());
        verifyThat("Play Again", NodeMatchers.isVisible());
        verifyThat("Main Menu", NodeMatchers.isVisible());
        verifyThat("Exit Game", NodeMatchers.isVisible());
    }

}
