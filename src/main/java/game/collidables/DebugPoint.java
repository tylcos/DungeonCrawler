package game.collidables;

import game.collectables.Collectable;
import javafx.geometry.Point2D;

/**
 * An point used for debugging
 */
public class DebugPoint extends Collectable {
    public DebugPoint() {
        super("/images/debugPoint.png", Point2D.ZERO, new Point2D(16, 16));
    }

    @Override
    public void onCollision(Collidable other) {
    }
}
