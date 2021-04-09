package game.potions;

import core.SoundManager;
import data.RandomUtil;
import game.collidables.Collectable;
import game.collidables.Collidable;
import game.entities.Player;
import javafx.geometry.Point2D;

import java.util.Timer;
import java.util.TimerTask;

/**
 * An attack potion that increases the player's weapon damage by a factor of 2.
 */
public class AttackPotion extends Collectable {
    private static final long POTION_DURATION = 5000;

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

        SoundManager.playPotionCollected();
        setCollected();

        Player.getPlayer().getWeapon().addDamageMultiplier(2d);
        new Timer().schedule(new EndPotionEffect(), POTION_DURATION);
    }

    private static class EndPotionEffect extends TimerTask {
        @Override
        public void run() {
            Player.getPlayer().getWeapon().addDamageMultiplier(.5d);
        }
    }
}
