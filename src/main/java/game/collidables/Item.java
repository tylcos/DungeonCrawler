package game.collidables;

import game.entities.Player;
import javafx.geometry.Point2D;
import utilities.RandomUtil;

/**
 * An item the player can collect for an effect.
 */
public class Item extends Collectable {
    /**
     * Creates an instance of a item placed randomly within the room.
     */
    public Item() {
        super("/images/item.gif", RandomUtil.getPoint2D(300), new Point2D(2, 2));
    }

    @Override
    public void onCollision(Collidable other) {
        if (isCollected || !(other instanceof Player)) {
            return;
        }

        setCollected();
    }
}
