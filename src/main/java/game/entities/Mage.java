package game.entities;

import core.SoundManager;
import javafx.geometry.Point2D;
import utilities.RandomUtil;
import views.EndScreen;

/**
 * Mage enemy
 */
public class Mage extends Entity {
    /**
     * Creates an instance of Mage.
     */
    public Mage() {
        super("skeleton2_v2_1.png", RandomUtil.getPoint2D(300), new Point2D(100, 100));

        health = 5;
        money  = 40;

        entityController = new MageEntityController(this);
    }

    @Override
    public void update() {
        entityController.act();
    }

    /**
     * Destroys the entity
     */
    @Override
    public void onDeath() {
        setVisible(false);
        EndScreen.addTotalKilled();
        SoundManager.playEnemyKilled();
    }
}
