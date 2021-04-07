package game.entities;

import game.collidables.Collidable;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class AttackAnimation extends Entity {
    private static AttackAnimation animation;

    public AttackAnimation(String image) {
        super("images/attackAnimation1.gif", new Point2D(0, 0), new Point2D(.7, .7));
        animation = this;
    }

    @Override
    public void update() {
    }

    @Override
    public void onCollision(Collidable other) {
    }

    @Override
    protected void onDeath() {
    }

    /**
     * Gets the main player instance.
     *
     * @return the main player
     */
    public static AttackAnimation getAttackAnimation() {
        return animation;
    }


}
