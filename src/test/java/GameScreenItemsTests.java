import core.GameDriver;
import core.SceneManager;
import game.collectables.*;
import game.entities.Player;
import game.entities.WeaponHolder;
import game.inventory.IItem;
import game.level.Level;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import views.GameScreen;

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
        int startWeaponDamage = Player.getPlayer().getWeaponHolder().getWeapon().getDamage();
        for (Collectable collectable : GameScreen.getLevel().getCurrentRoom().getCollectables()) {
            if (collectable instanceof AttackPotion) {
                Player.getPlayer().setPosition(collectable.getPosition());
                sleep(500);
                push(KeyCode.getKeyCode(String.valueOf(((IItem) collectable).getItemID() + 1)));
                sleep(500);
                assertEquals(startWeaponDamage * 2,
                        Player.getPlayer().getWeaponHolder().getWeapon().getDamage());

                sleep(5000); // reset back to normal attack damage after 5 seconds
                assertEquals(startWeaponDamage,
                        Player.getPlayer().getWeaponHolder().getWeapon().getDamage());
            }
        }
    }

    /**
     * Tests if speed potion was collected
     */
    @Test
    public void testSpeedPotionCollected() {
        for (Collectable collectable : GameScreen.getLevel().getCurrentRoom().getCollectables()) {
            System.out.println(collectable);
            if (collectable instanceof SpeedPotion) {
                Player.getPlayer().setPosition(collectable.getPosition());
                sleep(500);
                assertTrue(collectable.isCollected());
            }
        }
    }

    /**
     * Tests if speed potion only gives player extra speed for 5 seconds
     */
    @Test
    public void testSpeedPotionDuration() {
        double startSpeed = Player.getPlayer().getSpeedMultiplier();
        for (Collectable collectable : GameScreen.getLevel().getCurrentRoom().getCollectables()) {
            if (collectable instanceof SpeedPotion) {
                Player.getPlayer().setPosition(collectable.getPosition());
                sleep(500);
                push(KeyCode.getKeyCode(String.valueOf(((IItem) collectable).getItemID() + 1)));
                sleep(500);
                assertEquals(startSpeed * (4d / 3d),
                        Player.getPlayer().getSpeedMultiplier(), 0.01);

                sleep(5000); // reset back to normal attack damage after 5 seconds
                assertEquals(startSpeed,
                        Player.getPlayer().getSpeedMultiplier(), 0.01);
            }
        }
    }

    /**
     * Tests if nuke item was collected
     */
    @Test
    public void testNukePotionCollected() {
        for (Collectable collectable : GameScreen.getLevel().getCurrentRoom().getCollectables()) {
            if (collectable instanceof NukeItem) {
                Player.getPlayer().setPosition(collectable.getPosition());
                sleep(500);
                assertTrue(collectable.isCollected());
            }
        }
    }

    /**
     * Tests if weapon items can be collected
     */
    @Test
    public void testWeaponItemCollected() {
        for (Collectable collectable : GameScreen.getLevel().getCurrentRoom().getCollectables()) {
            if (collectable instanceof WeaponItem) {
                Player.getPlayer().setPosition(collectable.getPosition());
                sleep(500);
                push(KeyCode.E);
                assertTrue(collectable.isCollected());
            }
        }
    }

    /**
     * Tests if the weapon changes after the weapon item is collected
     */
    @Test
    public void testWeaponChangesAfterCollected() {
        WeaponHolder weaponHolder = Player.getPlayer().getWeaponHolder();
        String initialWeapon = weaponHolder.getWeapon().getName();

        for (Collectable collectable : GameScreen.getLevel().getCurrentRoom().getCollectables()) {
            if (collectable instanceof WeaponItem) {
                Player.getPlayer().setPosition(collectable.getPosition());
                sleep(500);
                push(KeyCode.E);

                assertNotEquals(initialWeapon, weaponHolder.getWeapon().getName());
            }
        }
    }

    /**
     * Tests that the key E has to be pressed to collect weapon items.
     */
    @Test
    public void testPressEToCollectWeapon() {
        for (Collectable collectable : GameScreen.getLevel().getCurrentRoom().getCollectables()) {
            if (collectable instanceof WeaponItem) {
                Player.getPlayer().setPosition(collectable.getPosition());
                sleep(500);
                assertEquals("Starting Weapon",
                        Player.getPlayer().getWeaponHolder().getWeapon().getName());
            }
        }
    }

    /**
     * Tests if a potion can be used from the inventory
     */
    @Test
    public void testInventoryUsingItem() {
        int startWeaponDamage = Player.getPlayer().getWeaponHolder().getWeapon().getDamage();
        AttackPotion attackPotion = new AttackPotion();
        attackPotion.activate();

        push(KeyCode.getKeyCode(String.valueOf(attackPotion.getItemID() + 1)));
        assertEquals(startWeaponDamage * 2,
                Player.getPlayer().getWeaponHolder().getWeapon().getDamage());
    }

    /**
     * Tests if the nukes used statistic increases after using a nuke
     */
    @Test
    public void testNukesUsedWhenPressed() {
        Platform.runLater(() -> {
            new NukeItem().activate();

            SceneManager.loadScene(SceneManager.END);

            verifyThat("Nukes Used:       1", NodeMatchers.isVisible());
        });
    }
}