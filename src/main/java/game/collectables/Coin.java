package game.collectables;

import core.SoundManager;
import game.collidables.Collidable;
import game.entities.Player;
import javafx.geometry.Point2D;
import utilities.RandomUtil;

/**
 * A coin that the player can collect to increase their money
 */
public class Coin extends Collectable {
    private final int value;

    /**
     * Creates an instance of a coin placed randomly within the room.
     */
    public Coin() {
        super("coin.gif", RandomUtil.getPoint2D(300), new Point2D(64, 64));

        value = RandomUtil.getInt(1, 25);
    }

    @Override
    public void onCollision(Collidable other) {
        if (isCollected || !(other instanceof Player)) {
            return;
        }

        Player.getPlayer().addMoney(value);
        setCollected();
        SoundManager.playCoinOrKeyCollected();
    }

    /**
     * Getter for value field
     *
     * @return value the value of the coin
     */
    public int getValue() {
        return value;
    }
}
