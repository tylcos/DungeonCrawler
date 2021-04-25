package game.entities;

import core.GameEngine;
import game.collidables.Collidable;
import game.collidables.CollidableTile;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import utilities.AnimationController;
import utilities.MathUtil;

public class TrackingProjectile extends Entity {
    private boolean hit;

    /**
     * Creates an instance of Projectile.
     *
     * @param image    the image
     * @param position position the projectile is spawned at
     * @param velocity initial velocity
     */
    public TrackingProjectile(String image, Point2D position, Point2D velocity) {
        super(image, position);

        setVelocity(velocity);
        setRotate(MathUtil.getAngleDeg(velocity));

        AnimationController.add(this, "GolemProjectile.png", 0, 2, 3, 100, 32, 2d);
    }

    @Override
    public void update() {
        Entity  player            = Player.getPlayer();
        Point2D directionToPlayer = player.position.subtract(position).normalize();

        double det = velocity.crossProduct(directionToPlayer)
                         .dotProduct(new Point3D(0, 0, 1));
        double rotate = 1 * Math.signum(det);
        double angle  = MathUtil.getAngle(velocity) + rotate * GameEngine.getDt();
        setVelocity(MathUtil.getVector(angle).multiply(400));
        setRotate(Math.toDegrees(angle));
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof CollidableTile) {
            GameEngine.destroy(GameEngine.VFX, this);
        }

        if (!hit && other instanceof Player) {
            Entity entity = (Entity) other;

            entity.damage(1);
            hit = true;

            GameEngine.destroy(GameEngine.VFX, this);
        }
    }
}
