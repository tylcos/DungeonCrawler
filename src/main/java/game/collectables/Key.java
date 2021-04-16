package game.collectables;

import core.SoundManager;
import game.collidables.Collidable;
import game.entities.Player;
import javafx.geometry.Point2D;
import utilities.RandomUtil;

public class Key extends Collectable {
    private static int numSpawned;

    /**
     * Creates an instance of the Key object
     *
     */
    public Key() {
        super("/images/key.png", RandomUtil.getPoint2D(300), new Point2D(64, 32));
        numSpawned++;
    }

    @Override
    public void onCollision(Collidable other) {
        if (isCollected || !(other instanceof Player)) {
            return;
        }

        setCollected();
        SoundManager.playCoinOrKeyCollected();
    }

    public static int getNumSpawned() {
        return numSpawned;
    }
}