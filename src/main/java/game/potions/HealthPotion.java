package game.potions;

import core.SoundManager;
import game.IItem;
import game.Inventory;
import game.collidables.Collectable;
import game.collidables.Collidable;
import game.entities.Player;
import javafx.geometry.Point2D;
import utilities.RandomUtil;

/**
 * A health potion that allows the player to regain two HP.
 */
public class HealthPotion extends Collectable implements IItem {
    private static final int    ITEM_ID = 0;
    private static final String IMAGE   = "/images/PotionOfHealth.gif";

    /**
     * Creates an instance of a health potion placed randomly within the room. 25% spawn rate.
     */
    public HealthPotion() {
        super(IMAGE, RandomUtil.getPoint2D(300), new Point2D(2, 2));
    }

    @Override
    public void onCollision(Collidable other) {
        if (isCollected || !(other instanceof Player)) {
            return;
        }

        SoundManager.playPotionCollected();
        setCollected();

        Inventory.addItem(this);
    }

    public void activate() {
        Player.getPlayer().regenerate();
    }

    public int getItemID() {
        return ITEM_ID;
    }

    public String getItemImage() {
        return IMAGE;
    }
}
