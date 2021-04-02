package game.entities;

import core.GameEngine;
import data.RandomUtil;
import game.collidables.Collidable;
import game.collidables.CollidableTile;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

/**
 * Skull enemy
 */
public class Skull extends Entity {
    /**
     * Creates an instance of Slime.
     */
    public Skull() {
        super(new Image("/images/skull_v2_3.png", 80, 80, true, false),
              RandomUtil.getPoint2D(300),
              new Point2D(1, 1));

        health = 2;
        money  = 10;

        entityController = new SkullEntityController(this);

        setOnMouseClicked(this::attackedByPlayer);
    }

    @Override
    public void update() {
        entityController.act();
    }

    /**
     * Enemy retreat one step after mainPlayer attack enemy.
     *
     * @param event Mouse click event
     */
    public void attackedByPlayer(MouseEvent event) {
        if (isDead || Player.getPlayer().isDead()) {
            return;
        }

        damage(1);
        bounceBack(-20, Player.getPlayer().getPosition());
    }

    /**
     * Destroys the entity
     */
    @Override
    public void onDeath() {
        setVisible(false);
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof CollidableTile) {
            double magnitude = Math.max(100d, velocity.magnitude());

            bounceBack((int) (magnitude * GameEngine.getDt()), Point2D.ZERO);
        }
    }

    /**
     * Makes the entity bounce back from wall or enemy
     *
     * @param bounceDistance the distance to bounce
     * @param fromPoint      the point to bounce back from
     */
    private void bounceBack(int bounceDistance, Point2D fromPoint) {
        Point2D difference = position.subtract(fromPoint);

        Point2D dp = new Point2D(-bounceDistance * Math.signum(difference.getX()),
                                 -bounceDistance * Math.signum(difference.getY()));

        setPosition(position.add(dp));
    }
}
