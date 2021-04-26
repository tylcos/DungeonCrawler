import core.GameDriver;
import core.SceneManager;
import game.entities.Player;
import game.inventory.WeaponType;
import game.level.Level;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

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

    @Test
    public void testPlayerHPWhenAttacked() {
        Player player     = Player.getPlayer();
        int    prevHealth = player.getHealth();

        sleep(5000);
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

        Player.setPlayer("Team Azula", WeaponType.Sword, "Debug");
    }

    @Test
    public void testGameOverScreen() {
        Player player = Player.getPlayer();
        player.damage(player.getHealth());
        sleep(7000);

        //verifyThat("You Died In The Dungeon!", NodeMatchers.isVisible());
        verifyThat("Play Again", NodeMatchers.isVisible());
        verifyThat("Main Menu", NodeMatchers.isVisible());
        verifyThat("Exit Game", NodeMatchers.isVisible());
    }
}
