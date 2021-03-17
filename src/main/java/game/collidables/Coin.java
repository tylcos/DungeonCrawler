package game.collidables;

import data.RandomUtil;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

/**
 * A coin that contributes to the player's money
 */
public class Coin extends Entity {
    private boolean isCollected;
    private static final int VALUE = 10;

    /**
     * Creates an instance of a coin placed randomly within the map.
     *
     * @param isCollected true if coin used, false otherwise
     */
    public Coin(boolean isCollected) {
        super("/images/coin.gif",
                new Point2D(((Math.random() * (500 - 200)) + 200),
                        ((Math.random() * (500 - 200)) + 200)), new Point2D(2, 2));
        if (isCollected) {
            setImage(new Image("images/Invisible.gif"));
        }
        this.isCollected = isCollected;
    }

    /**
     * Getter for value field
     *
     * @return value the value of the coin
     */
    public int getValue() {
        return VALUE;
    }
    
    @Override
    public void onCollision(Collidable other) {
        if (isCollected) {
            // Might want to delete entity instead
            return;
        }

        MainPlayer.getPlayer().addMoney(RandomUtil.getInt(1, 25));

        isCollected = true;
        setImage(new Image("images/Invisible.gif"));
    }
}