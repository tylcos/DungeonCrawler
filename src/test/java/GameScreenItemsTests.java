import core.GameDriver;
import core.SceneManager;
import game.Weapon;
import game.collidables.Coin;
import game.collidables.Collectable;
import game.collidables.Item;
import game.entities.Entity;
import game.entities.Player;
import game.levels.Level;
import game.potions.AttackPotion;
import game.potions.HealthPotion;
import javafx.event.Event;
import javafx.geometry.Point2D;
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

public class GameScreenItemsTests extends ApplicationTest {

    // Auto starts on the game screen
    @Before
    public void start() throws Exception {
        Level.setSpawnItemsInEntrance(true);

        launch(GameDriver.class, "--scene=GAME");
        assertEquals("Failed to load game screen.", SceneManager.GAME, SceneManager.getSceneName());
    }

    /**
     * Tests if coin was collected
     */
    @Test
    public void testCoinCollected() {
        for (Collectable collectable : GameScreen.getLevel().getCurrentRoom().getCollectables()) {
            if (collectable instanceof Coin) {
                Player.getPlayer().setPosition(collectable.getPosition());
                sleep(2000);
                assertTrue(collectable.isCollected());
            }
        }
    }

    /**
     * Tests if coin added money
     */
    @Test
    public void testCoinMoneyAdded() {
        int startMoney = Player.getPlayer().getMoney();
        for (Collectable collectable : GameScreen.getLevel().getCurrentRoom().getCollectables()) {
            if (collectable instanceof Coin) {
                Player.getPlayer().setPosition(collectable.getPosition());
                sleep(2000);

                // check if money increased from the random value [1, 25]
                assertTrue(Player.getPlayer().getMoney() > startMoney);
            }
        }
    }

    /**
     * Tests if health potion was collected
     */
    @Test
    public void testHealthPotionCollected() {
        for (Collectable collectable : GameScreen.getLevel().getCurrentRoom().getCollectables()) {
            if (collectable instanceof HealthPotion) {
                Player.getPlayer().setPosition(collectable.getPosition());
                sleep(2000);
                assertTrue(collectable.isCollected());
            }
        }
    }

    /**
     * Tests if health potion doesn't give player more health than what they started out with
     */
    @Test
    public void testHealthPotionNotAboveMaxHealth() {
        int startHealth = Player.getPlayer().getHealth();
        for (Collectable collectable : GameScreen.getLevel().getCurrentRoom().getCollectables()) {
            if (collectable instanceof HealthPotion) {
                Player.getPlayer().setPosition(collectable.getPosition());
                sleep(2000);
                assertEquals(startHealth, Player.getPlayer().getHealth());
            }
        }
    }

    /**
     * Tests if attack potion was collected
     */
    @Test
    public void testAttackPotionCollected() {
        for (Collectable collectable : GameScreen.getLevel().getCurrentRoom().getCollectables()) {
            if (collectable instanceof AttackPotion) {
                Player.getPlayer().setPosition(collectable.getPosition());
                sleep(2000);
                assertTrue(collectable.isCollected());
            }
        }
    }

    /**
     * Tests if attack potion only gives player double damage for 5 seconds
     */
    @Test
    public void testAttackPotionDoubleDamageFiveSeconds() {
        int startWeaponDamage = Player.getPlayer().getWeapon().getDamage();
        for (Collectable collectable : GameScreen.getLevel().getCurrentRoom().getCollectables()) {
            if (collectable instanceof AttackPotion) {
                Player.getPlayer().setPosition(collectable.getPosition());
                sleep(1000);
                assertEquals(startWeaponDamage * 2, Player.getPlayer().getWeapon().getDamage());

                sleep(5000); // reset back to normal attack damage after 5 seconds
                assertEquals(startWeaponDamage, Player.getPlayer().getWeapon().getDamage());
            }
        }
    }
}