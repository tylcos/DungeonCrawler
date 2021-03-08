package game;

import core.GameManager;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

/**
 * An item in the dungeon crawler.
 */
public class Item extends Entity {
    private MainPlayer mainPlayer;
    private boolean isItemUsed = false;

    /**
     * Creates an instance of an item
     * @param isItemExist true if the item exists; false otherwise
     */
    public Item(boolean isItemExist) {
        super("resources/images/item.gif", new Point2D(((Math.random() * (500 - 300)) + 300), ((Math.random() * (500 - 300)) + 300)), new Point2D(3, 3));

        if (!isItemExist) {
            this.setImage(new Image("images/Invisible.gif"));
            isItemExist = true;
        }
    }

    @Override
    public void update(double dt) {
        Point2D distance = this.getPosition().subtract(GameManager.getPlayer().getPosition());
        if (distance.getX() < 20 && distance.getY() < 20 && distance.getX() > -20 && distance.getY() > -20 && !isItemUsed) {
            //update once we have more item
            /*
            if(...)
            GameManager.getPlayer().setWeapon(////);
            else if(...)
             GameManager.getPlayer().setWeapon(////);
             */
            isItemUsed = true;
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
            throw new IllegalArgumentException("Put positive number only");
        }
        return (int) ((Math.random() * (max - min)) + min);
    }

    // todo obtain weapon etc .. later implementations.
}
