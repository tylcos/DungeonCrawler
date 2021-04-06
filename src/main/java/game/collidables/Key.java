package game.collidables;

import game.entities.Player;
import javafx.geometry.Point2D;

public class Key extends Collectable {


    /**
     * Creates an instance of the Key object
     *
     */
    public Key() {
        //todo: change to random pos
        super("/images/key.png", new Point2D(100, 50), new Point2D(0.1, 0.1));
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player) {
            System.out.println("collided with key");
            setCollected();
        }

    }
}