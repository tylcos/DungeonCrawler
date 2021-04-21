package game.collectables;

import core.SoundManager;
import game.collidables.Collidable;
import game.entities.Player;
import javafx.geometry.Point2D;
import utilities.RandomUtil;

public class Key extends Collectable {
    /**
     * Creates an instance of the Key object
     *
     */
    public Key() {
        super("key.png", RandomUtil.getPoint2D(300), new Point2D(64, 32));
    }

    @Override
    public void onCollision(Collidable other) {
        if (isCollected || !(other instanceof Player)) {
            return;
        }

        setCollected();
        SoundManager.playCoinOrKeyCollected();
    }
}