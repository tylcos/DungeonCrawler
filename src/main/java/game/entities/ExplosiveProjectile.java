package game.entities;

import core.GameEngine;
import game.collidables.Collidable;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import utilities.AnimationController;
import utilities.MathUtil;
import views.GameScreen;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ExplosiveProjectile extends Entity {
    private double                   radius;
    private Consumer<Iterable<Entity>> onHit;

    private boolean isExploded;

    /**
     * Creates an instance of Projectile.
     *
     * @param image    the image
     * @param position position the projectile is spawned at
     * @param velocity initial velocity
     * @param radius   radius of explosion
     * @param onHit    called when the projectile hits a entity
     */
    public ExplosiveProjectile(String image, Point2D position, Point2D velocity,
                               double radius, Consumer<Iterable<Entity>> onHit) {
        super(image, position);
        this.radius = radius;

        setVelocity(velocity);
        setRotate(MathUtil.getAngleDeg(velocity));

        this.onHit = onHit;
    }

    @Override
    public void update() { }

    @Override
    public void onCollision(Collidable other) {
        if (!isExploded && Projectile.canHit(other)) {
            List<Entity> enemies = GameScreen.getLevel().getCurrentRoom().getEntities();

            Set<Entity> hitEnemies = enemies.stream()
                                         .filter(e -> (e.getPosition().subtract(position))
                                                          .magnitude() < radius)
                                         .collect(Collectors.toSet());
            if (other instanceof Entity) {
                hitEnemies.add((Entity) other);
            }

            onHit.accept(hitEnemies);
            GameEngine.destroy(GameEngine.VFX, this);

            physicsUpdate(.1d);
            ImageView impact = new ImageView();
            impact.setTranslateX(position.getX());
            impact.setTranslateY(position.getY());
            impact.setRotate(getRotate());

            var ac = AnimationController.add(impact, "EnergyBallImpact.png", 0, 8, 1, 128, 0, 2d);
            ac.setOnFinish(() -> impact.setVisible(false));

            GameEngine.addToLayer(GameEngine.VFX, impact);
            isExploded = true;
        }
    }
}
