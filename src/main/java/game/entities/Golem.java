package game.entities;

import core.GameEngine;
import core.SoundManager;
import game.collectables.Key;
import javafx.scene.image.ImageView;
import utilities.*;

/**
 * Golem enemy
 */
public class Golem extends Entity {
    public Golem() {
        super("blank.png", RandomUtil.getPoint2D(300));
        setPickOnBounds(false);

        health    = 50;
        maxHealth = health;
        money     = 500;

        AnimationController.add(this, "Golem.png", 1, 1, 8, 100, 24, 5);

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
        SoundManager.playEnemyKilled();

        Key key = new Key();
        key.setTranslateX(position.getX());
        key.setTranslateY(position.getY());
        ImageView death = new ImageView();
        death.setTranslateX(position.getX());
        death.setTranslateY(position.getY());

        AnimationController.add(death, "GolemDeath.png", 0, 2, 5, 400, 0, 2);

        TimerUtil.lerp(5, key::setOpacity);
        TimerUtil.lerp(5, t -> setOpacity(1 - t), () -> setVisible(false));
        TimerUtil.lerp(5, t -> death.setOpacity(1 - t), () -> setVisible(false));

        GameEngine.instantiate(GameEngine.VFX, key);
        GameEngine.addToLayer(GameEngine.ENTITY, death);
        death.toBack();
        toFront();
    }
}
