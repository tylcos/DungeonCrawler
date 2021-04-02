import core.GameDriver;
import core.SceneManager;
import game.Weapon;
import game.entities.Entity;
import game.entities.Player;
import game.levels.Level;
import javafx.event.Event;
import javafx.scene.input.*;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import views.GameScreen;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;

public class GameScreenEnemyTests extends ApplicationTest {

    // Auto starts on the game screen
    @Before
    public void start() throws Exception {
        Level.setSpawnEnemiesInEntrance(true);

        launch(GameDriver.class, "--scene=GAME");
        assertEquals("Failed to load game screen.", SceneManager.GAME, SceneManager.getSceneName());
    }

    /**
     * Tests if enemies fire events when clicked on
     */
    @Test
    public void testEnemyClickedEvent() {
        AtomicInteger timesClicked = new AtomicInteger(0);

        clickOnEnemies(entity -> entity.setOnMouseClicked(event -> timesClicked.getAndIncrement()),
                       entity -> { });

        assertTrue(timesClicked.get() != 0);
    }

    /**
     * Tests if enemies health decreases when clicked on
     */
    @Test
    public void testEnemyHP() {
        AtomicInteger initialHealth = new AtomicInteger(0);

        clickOnEnemies(entity -> initialHealth.set(entity.getHealth()),
                       entity -> assertTrue(entity.getHealth() < initialHealth.get()));
    }

    /**
     * Tests if the player can still attack after they die
     */
    @Test
    public void testAttackAfterPlayerDead() {
        Player.getPlayer().damage(Integer.MAX_VALUE);

        AtomicInteger initialHealth = new AtomicInteger(0);

        clickOnEnemies(entity -> initialHealth.set(entity.getHealth()),
                       entity -> assertEquals(entity.getHealth(), initialHealth.get()));
    }

    @Test
    public void testPlayerHPWhenAttacked() {
        Player player     = Player.getPlayer();
        int    prevHealth = player.getHealth();

        sleep(10000);
        assertNotEquals(prevHealth, player.getHealth());
    }

    /**
     * Tests if player dies when HP equals zero
     */
    @Test
    public void testPlayerDeathWhenHp0() {
        Player player = Player.getPlayer();
        player.damage(Integer.MAX_VALUE);

        assertTrue(player.isDead());
        assertNotEquals(90, player.getRotate());
    }

    @Test
    public void testChangeBow() {
        press(KeyCode.DIGIT1);
        Weapon bow  = new Weapon("Bow", 0, 0);
        String bow1 = Player.getPlayer().getWeapon().getName();
        assertEquals(bow1, bow.getName());
    }

    @Test
    public void testChangeSword() {
        press(KeyCode.DIGIT3);
        Weapon sword  = new Weapon("Sword", 0, 0);
        String sword1 = Player.getPlayer().getWeapon().getName();
        assertEquals(sword1, sword.getName());
    }

    @Test
    public void testChangeAxe() {
        press(KeyCode.DIGIT2);
        Weapon axe  = new Weapon("Axe", 0, 0);
        String axe1 = Player.getPlayer().getWeapon().getName();
        assertEquals(axe1, axe.getName());
    }

    @Test
    public void testGameOverScreen() {
        Player player = Player.getPlayer();
        player.damage(Integer.MAX_VALUE);
        player.setOpacity(0);
        sleep(6000);

        verifyThat("You Died In The Dungeon!", NodeMatchers.isVisible());
        verifyThat("Play Again", NodeMatchers.isVisible());
        verifyThat("Main Menu", NodeMatchers.isVisible());
        verifyThat("Exit Game", NodeMatchers.isVisible());
    }

    /**
     * Clicks on each enemy in the current room
     *
     * @param beforeClick Runs before the enemy is clicked on
     * @param afterClick  Runs after the enemy is clicked on
     */
    private void clickOnEnemies(Consumer<Entity> beforeClick, Consumer<Entity> afterClick) {
        for (Entity enemy : GameScreen.getLevel().getCurrentRoom().getEntities()) {
            beforeClick.accept(enemy);

            Event.fireEvent(enemy, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0,
                                                  MouseButton.PRIMARY, 1,
                                                  true, true, true, true, true, true, true, true,
                                                  true, true, null));

            afterClick.accept(enemy);
        }
    }
}
