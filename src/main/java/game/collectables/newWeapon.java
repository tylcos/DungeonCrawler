package game.collectables;

import core.SoundManager;
import game.collidables.Collidable;
import game.entities.Player;
import javafx.geometry.Point2D;
import utilities.RandomUtil;

/**
 * A coin that the player can collect to increase their money
 */
public class newWeapon extends Collectable {
    /**
     * Creates an instance of a items placed randomly within the room.
     */
    public newWeapon() {
        super("/images/sword.gif", RandomUtil.getPoint2D(300), new Point2D(2, 2));
    }

    @Override
    public void onCollision(Collidable other) {
        if (isCollected || !(other instanceof Player)) {
            return;
        }

        Player.getPlayer().setWeaponObtained();
        setCollected();
        SoundManager.playCoinOrKeyCollected();
    }
}
