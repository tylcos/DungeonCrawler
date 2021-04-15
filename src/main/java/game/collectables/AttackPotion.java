package game.collectables;

import core.SoundManager;
import game.IItem;
import game.Inventory;
import game.collidables.Collidable;
import game.entities.Player;
import javafx.geometry.Point2D;
import utilities.RandomUtil;
import utilities.TimerUtil;

/**
 * An attack potion that increases the player's weapon damage by a factor of 2.
 */
public class AttackPotion extends Collectable implements IItem {
    private static final int    ITEM_ID = 1;
    private static final String IMAGE   = "/images/PotionOfAttack.gif";

    private static final int    POTION_DURATION = 5000;
    private static final double POTION_STRENGTH = 2d;

    /**
     * Creates an instance of a item placed randomly within the room. 25% spawn probability.
     */
    public AttackPotion() {
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
        Player.getPlayer().getWeapon().addDamageMultiplier(POTION_STRENGTH);

        TimerUtil.schedule(POTION_DURATION,
                           () -> Player.getPlayer().getWeapon()
                                     .addDamageMultiplier(1 / POTION_STRENGTH));
    }

    public int getItemID() {
        return ITEM_ID;
    }

    public String getItemImage() {
        return IMAGE;
    }
}
