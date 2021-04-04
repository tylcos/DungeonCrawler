package game.entities;

import core.GameEngine;
import data.RandomUtil;
import game.collidables.Collidable;
import game.collidables.CollidableTile;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Mage enemy
 */
public class Mage extends Entity {
    /**
     * Creates an instance of Mage.
     */
    public Mage() {
        super(new Image("/images/skeleton2_v2_1.png", 100, 100, true, false),
              RandomUtil.getPoint2D(300),
              new Point2D(1, 1));

        health = 5;
        money  = 40;

        entityController = new MageEntityController(this);

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
        Player.getPlayer().attackMotion();

        double Xval = event.getScreenX();
        double Yval = event.getSceneY();

        ImageView imageView = new ImageView();
        Image newImage = new Image("images/testAttackMotion(From anotherGame).gif");
        imageView.setImage(newImage);
        imageView.setX(Xval);
        imageView.setY(Yval);
        












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
