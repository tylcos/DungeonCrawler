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

/**
 * A coin that the player can collect to increase their money
 */
public class WeaponItem extends Collectable {
    private Weapon weapon;

    /**
     * Creates an instance of a items placed randomly within the room.
     */
    public WeaponItem() {
        super("blank.png", RandomUtil.getPoint2D(300), new Point2D(64, 64));

        weapon = new Weapon(WeaponType.random(), RandomUtil.getInt(12));
        setImage(weapon.getImage());
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
}
