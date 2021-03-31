package game.collidables;

import javafx.geometry.Point2D;

/**
 * An point used for debugging
 */
public class DebugPoint extends Collectable {
    public DebugPoint() {
        super("/images/debugPoint.png", Point2D.ZERO, new Point2D(1, 1));
    }

    @Override
    public void onCollision(Collidable other) {
    }
}