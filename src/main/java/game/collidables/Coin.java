package game.collidables;

import data.RandomUtil;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

/**
 * A coin that the player can collect to increase their money
 */
public class Coin extends Entity {
    private       boolean isCollected;
    private final int     value;

    /**
     * Creates an instance of a coin placed randomly within the map.
     */
    public Coin() {
        super("/images/coin.gif", new Point2D(((Math.random() * (500 - 200)) + 200),
                                              ((Math.random() * (500 - 200)) + 200)),
              new Point2D(2, 2));

        value = RandomUtil.getInt(1, 25);
    }

    @Override
    public void onCollision(Collidable other) {
        if (isCollected) {
            return; // Eventually might want to delete entity instead
        }

        MainPlayer.getPlayer().addMoney(value);

        isCollected = true;
        setImage(new Image("images/Invisible.gif"));
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
