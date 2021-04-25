package game.entities;

import core.GameEngine;
import game.collidables.Collidable;
import game.collidables.CollidableTile;
import javafx.geometry.Point2D;
import utilities.MathUtil;

import java.util.function.Consumer;

public class Projectile extends Entity {
    private Consumer<Entity> onHit;

    /**
     * Creates an instance of Projectile.
     *
     * @param image    the image
     * @param position position the projectile is spawned at
     * @param velocity initial velocity
     * @param onHit    called when the projectile hits a entity
     */
    public Projectile(String image, Point2D position, Point2D velocity, Consumer<Entity> onHit) {
        super(image, position, new Point2D(50, 50));

        setVelocity(velocity);
        setRotate(MathUtil.getAngleDeg(velocity));

        this.onHit = onHit;
    }

    @Override
    public void update() { }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof CollidableTile) {
            GameEngine.destroy(GameEngine.VFX, this);
        }

        if (other instanceof Entity && !(other instanceof Player)) {
            Entity entity = (Entity) other;

            if (!entity.isDead()) {
                onHit.accept(entity);

                GameEngine.destroy(GameEngine.VFX, this);
            }
        }
    }

    public static boolean canHit(Collidable other) {
        return (other instanceof CollidableTile)
               || ((other instanceof Entity && !(other instanceof Player))
                   && !((Entity) other).isDead());
    }
}
