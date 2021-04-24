package game.collidables;

import core.GameEngine;
import game.collectables.Collectable;
import javafx.geometry.Point2D;

/**
 * An point used for debugging
 */
public class DebugPoint extends Collectable {
    public DebugPoint() {
        this(Point2D.ZERO);
    }

    public DebugPoint(Point2D position) {
        super("debugPoint.png", position, new Point2D(16, 16));
    }

    @Override
    public void onCollision(Collidable other) {
    }

    public static void debug(Point2D position) {
        GameEngine.addToLayer(GameEngine.DEBUG, new DebugPoint(position));
    }
}
