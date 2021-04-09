package game.potions;

import core.SoundManager;
import data.RandomUtil;
import game.collidables.Collectable;
import game.collidables.Collidable;
import game.entities.Player;
import javafx.geometry.Point2D;

/**
 * A health potion that allows the player to regain two HP.
 */
public class HealthPotion extends Collectable {
    /**
     * Creates an instance of a health potion placed randomly within the room. 25% spawn rate.
     */
    public HealthPotion() {
        super("/images/PotionOfHealth.gif", RandomUtil.getPoint2D(300), new Point2D(2, 2));
    }

    // todo this needs to be put in inventory. currently immediate regeneration.
    @Override
    public void onCollision(Collidable other) {
        if (isCollected || !(other instanceof Player)) {
            return;
        }

        SoundManager.playPotionCollected();
        setCollected();

        Player.getPlayer().regenerate();
    }
}
