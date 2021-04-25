package game.entities;

import core.SoundManager;
import javafx.geometry.Point2D;
import utilities.RandomUtil;
import views.EndScreen;

/**
 * Skull enemy
 */
public class Skull extends Entity {
    /**
     * Creates an instance of Skull.
     */
    public Skull() {
        super("skull_v2_3.png", RandomUtil.getPoint2D(300), new Point2D(80, 80));

        health = 2;
        money  = 10;

        entityController = new SkullEntityController(this);
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
