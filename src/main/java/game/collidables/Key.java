package game.collidables;

import core.SoundManager;
import game.entities.Player;
import javafx.geometry.Point2D;

public class Key extends Collectable {

    public static int NUM_SPAWNED;

    /**
     * Creates an instance of the Key object
     *
     */
    public Key() {
        //todo: change to random pos
        super("/images/key.png", new Point2D(100, 50), new Point2D(0.1, 0.1));
        NUM_SPAWNED++;
    }

    @Override
    public void onCollision(Collidable other) {
        if (isCollected || !(other instanceof Player)) {
            return;
        }
        if (other instanceof Player) {
            System.out.println("collided with key");
            setCollected();
            SoundManager.playCoinOrKeyCollected();
        }

    }
}