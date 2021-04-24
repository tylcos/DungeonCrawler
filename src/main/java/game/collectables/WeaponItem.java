package game.collectables;

import core.InputManager;
import core.SoundManager;
import game.Weapon;
import game.WeaponType;
import game.collidables.Collidable;
import game.entities.Player;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import utilities.RandomUtil;
import views.GameScreen;

/**
 * A weapon that the player can pickup by pressing E
 */
public class WeaponItem extends Collectable {
    private Weapon weapon;

    private static int highestTierSpawned;

    public WeaponItem() {
        super("blank.png", RandomUtil.getPoint2D(300), new Point2D(64, 64));

        int tier = highestTierSpawned + RandomUtil.getInt(1, 3);
        weapon = new Weapon(WeaponType.random(), tier);
        setImage(weapon.getImage());

        // Was spawned by level, not the best way to do this
        if (GameScreen.getLevel() != null) {
            highestTierSpawned = tier;
        }
    }

    @Override
    public void onCollision(Collidable other) {
        if (isCollected || !(other instanceof Player) || !InputManager.getKeyDown(KeyCode.E)) {
            return;
        }

        SoundManager.playCoinOrKeyCollected();

        Weapon replaced = Player.getPlayer().addWeapon(weapon);
        if (replaced != null) {
            weapon = replaced;
            setImage(replaced.getImage());
        } else {
            setCollected();
        }
    }

    public static void resetHighestTierSpawned() {
        highestTierSpawned = 0;
    }
}
