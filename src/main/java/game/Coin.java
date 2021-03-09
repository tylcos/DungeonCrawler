package game;
import core.GameManager;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

/**
 * A coin that contributes to the player's money
 */
public class Coin extends Entity {
    private boolean isCoinUsed = false;
    private static final int VALUE = 10;

    /**
     * Creates an instance of a coin placed randomly within the map.
     *
     * @param isCoinUsed true if coin used, false otherwise
     */
    public Coin(boolean isCoinUsed) {
        super("/images/coin.gif",
                new Point2D(((Math.random() * (500 - 200)) + 200),
                        ((Math.random() * (500 - 200)) + 200)), new Point2D(2, 2));
        if (!isCoinUsed) {
            this.setImage(new Image("images/Invisible.gif"));
            isCoinUsed = true;
        }
    }

    /**
     * Getter for value field
     *
     * @return value the value of the coin
     */
    public int getValue() {
        return VALUE;
    }

    /**
     * Updates the player's distance from the coin and makes it disappear
     * when the player collects it.
     *
     * @param dt the time
     */
    @Override
    public void update(double dt) {
        Point2D distance = this.getPosition().subtract(GameManager.getPlayer().getPosition());

        if (distance.getX() < 20
                && distance.getY() < 20
                && distance.getX() > -20
                && distance.getY() > -20
                && !isCoinUsed) {
            GameManager.getPlayer().setMoney(GameManager.getPlayer().getMoney()
                    + getRandomNumber(1, 25));
            isCoinUsed = true;
            this.setImage(new Image("images/Invisible.gif"));
        }
    }

    /**
     * Generates a random number within [min, max).
     *
     * @param min the minimum bound
     * @param max the maximum bound
     * @return a random number between minimum, inclusive, and maximum, exclusive
     */
    public int getRandomNumber(int min, int max) {
        if (min < 0 || max < 0) {
            throw new IllegalArgumentException("Minimum and maximum bounds should be positive.");
        }
        return (int) ((Math.random() * (max - min)) + min);
    }
}
