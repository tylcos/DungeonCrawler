package game.collidables;

import javafx.geometry.Point2D;

/**
 * An item in the dungeon crawler.
 */
public class Item extends Collectable {
    private boolean isCollected;

    /**
     * Creates an instance of a coin placed randomly within the map.
     */
    public Item() {
        super("/images/item.gif", new Point2D(((Math.random() * (500 - 200)) + 200),
                                              ((Math.random() * (500 - 200)) + 200)),
              new Point2D(2, 2));
    }

    @Override
    public void onCollision(Collidable other) {

    }
}
