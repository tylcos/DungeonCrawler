package game.entities;

import core.SoundManager;
import utilities.AnimationController;
import utilities.RandomUtil;

/**
 * Golem enemy
 */
public class Golem extends Entity {
    public Golem() {
        super("blank.png", RandomUtil.getPoint2D(300));
        setPickOnBounds(false);

        health = 50;
        money  = 500;

        AnimationController.add(this, "Golem.png", 1, 1, 8, 100, 24, 4);

        entityController = new GolemEntityController(this);
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

        SoundManager.playEnemyKilled();
    }
}
