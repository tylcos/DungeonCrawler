import core.GameDriver;
import core.SceneManager;
import game.collectables.*;
import game.entities.Player;
import game.inventory.IItem;
import game.level.Level;
import javafx.scene.input.KeyCode;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import views.GameScreen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
                sleep(500);

                assertTrue(collectable.isCollected());
            }
        }
    }

    /**
     * Tests if coin adds money when collected
     */
    @Test
    public void testCoinMoneyAdded() {
        int startMoney = Player.getPlayer().getMoney();
        for (Collectable collectable : GameScreen.getLevel().getCurrentRoom().getCollectables()) {
            if (collectable instanceof Coin) {
                Player.getPlayer().setPosition(collectable.getPosition());
                sleep(500);

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
                sleep(500);
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
                sleep(500);
                press(KeyCode.getKeyCode(String.valueOf(((IItem) collectable).getItemID() + 1)));

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
            System.out.println(collectable);
            if (collectable instanceof AttackPotion) {
                Player.getPlayer().setPosition(collectable.getPosition());
                sleep(500);
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
                sleep(500);
                push(KeyCode.getKeyCode(String.valueOf(((IItem) collectable).getItemID() + 1)));
                sleep(500);
                assertEquals(startWeaponDamage * 2, Player.getPlayer().getWeapon().getDamage());

                sleep(5000); // reset back to normal attack damage after 5 seconds
                assertEquals(startWeaponDamage, Player.getPlayer().getWeapon().getDamage());
            }
        }
    }

    /**
     * Test if the weapon item was collected
     */
    @Test
    public void testWeaponItemCollected() {
        for (Collectable collectable : GameScreen.getLevel().getCurrentRoom().getCollectables()) {
            if (collectable instanceof WeaponItem) {
                Player.getPlayer().setPosition(collectable.getPosition());
                sleep(500);
                assertTrue(collectable.isCollected());
            }
        }
    }

    /**
     * Test if weapon is changeable after the weapon item collected
     */
    @Test
    public void testWeaponChangedAfterCollected() {
        for (Collectable collectable : GameScreen.getLevel().getCurrentRoom().getCollectables()) {
            if (collectable instanceof WeaponItem) {
                Player.getPlayer().setPosition(collectable.getPosition());
                sleep(500);
                Player.getPlayer().getWeapon().getName();
                push(KeyCode.TAB);
                assertEquals("Axe", Player.getPlayer().getWeapon().getName());

            }
        }
    }

    /**
     * Test if weapon is not changeable until player collect the weapon item.
     */
    @Test
    public void testNoWeaponChange() {
        for (Collectable collectable : GameScreen.getLevel().getCurrentRoom().getCollectables()) {
            if (collectable instanceof WeaponItem) {
                Player.getPlayer().getWeapon().getName();
                push(KeyCode.TAB);
                assertEquals("Starting Weapon", Player.getPlayer().getWeapon().getName());
            }
        }

    }

    /**
     * Tests if a potion can be used from the inventory
     */
    @Test
    public void testInventoryUsingItem() {
        int          startWeaponDamage = Player.getPlayer().getWeapon().getDamage();
        AttackPotion attackPotion      = new AttackPotion();
        attackPotion.activate();

        push(KeyCode.getKeyCode(String.valueOf(attackPotion.getItemID() + 1)));
        assertEquals(startWeaponDamage * 2, Player.getPlayer().getWeapon().getDamage());
    }
}