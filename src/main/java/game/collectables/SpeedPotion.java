package game.collectables;

import core.SoundManager;
import game.collidables.Collidable;
import game.entities.Player;
import game.inventory.IItem;
import game.inventory.Inventory;
import javafx.geometry.Point2D;
import utilities.RandomUtil;
import utilities.TimerUtil;
import views.EndScreen;

/**
 * A Item that speeds up the Player's movement
 */
public class SpeedPotion extends Collectable implements IItem {
    private static final int ITEM_ID = 2;
    private static final String IMAGE = "PotionOfSpeed.gif";

    private static final double POTION_DURATION = 5d;
    private static final double POTION_STRENGTH = 4d / 3d;

    public SpeedPotion() {
        super(IMAGE, RandomUtil.getPoint2D(300), new Point2D(64, 64));
    }

    @Override
    public void onCollision(Collidable other) {
        if (isCollected || !(other instanceof Player)) {
            return;
        }
        EndScreen.addTotalPotionsObtained();
        SoundManager.playPotionCollected();
        setCollected();

        Inventory.addItem(this);
    }

    public void activate() {
        Player.getPlayer().addSpeedMultiplier(POTION_STRENGTH);

        TimerUtil.schedule(POTION_DURATION,
                () -> Player.getPlayer().addSpeedMultiplier(1 / POTION_STRENGTH));
    }

    public int getItemID() {
        return ITEM_ID;
    }

    public String getItemImage() {
        return IMAGE;
    }
}
