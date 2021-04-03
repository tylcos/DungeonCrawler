package game.collidables;

import data.RandomUtil;
import game.entities.Player;
import javafx.geometry.Point2D;

/**
 * An attack potion that increases the player's weapon damage by a factor of 2.
 */
public class AttackPotion extends Collectable {
    /**
     * Creates an instance of a item placed randomly within the room. 25% spawn probability.
     */
    public AttackPotion() {
        super("/images/PotionOfAttack.gif", RandomUtil.getPoint2D(300), new Point2D(2, 2));
    }

    // todo this needs to be put in inventory. currently immediate regeneration.
    @Override
    public void onCollision(Collidable other) {
        if (isCollected || !(other instanceof Player)) {
            return;
        }

        int currDamage = Player.getPlayer().getWeapon().getDamage();
        long potionLasting = 30000; // 30,000 milliseconds = 30 seconds
        long currTime = System.currentTimeMillis();
        if (currTime > potionLasting) {
            Player.getPlayer().getWeapon().setDamage(currDamage * 2);
        }

        setCollected();
    }
}
