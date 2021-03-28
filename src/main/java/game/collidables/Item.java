package game.collidables;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

/**
 * An item in the dungeon crawler.
 */
public class Item extends Entity {
    private MainPlayer mainPlayer;
    private boolean    isItemUsed;

    /**
     * Creates an instance of an item
     *
     * @param isItemExist true if the item exists; false otherwise
     */
    public Item(boolean isItemExist) {
        super("images/item.gif", new Point2D(((Math.random() * (500 - 300)) + 300),
                                             ((Math.random() * (500 - 300)) + 300)),
              new Point2D(3, 3));

        if (!isItemExist) {
            setImage(new Image("images/Invisible.gif"));
            isItemUsed = true;
        }
    }

    @Override
    public void update() {
        Point2D distance = getPosition().subtract(MainPlayer.getPlayer().getPosition());
        if (distance.getX() < 20 && distance.getY() < 20 && distance.getX() > -20
            && distance.getY() > -20 && !isItemUsed) {
            //update once we have more item
            /*
            if(...)
            MainPlayer.getPlayer().setWeapon(////);
            else if(...)
             MainPlayer.getPlayer().setWeapon(////);
             */
            isItemUsed = true;
            setImage(new Image("images/Invisible.gif"));
        }
    }

    // todo obtain weapon etc .. later implementations.
}
