package game.entities;

import core.GameEngine;
import javafx.geometry.*;
import javafx.scene.image.ImageView;
import utilities.*;
import views.GameScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Defines the Mage AI.
 *
 * The behavior is to move to where the player is predicted to be assuming the player maintains
 * a constant radius and constant dθ/dt around the center of the room.
 */
public class GolemEntityController extends EntityController<Golem> {
    // Variables for movement
    private double speed;
    private double inputSmooth;

    // Variables for generating bias
    private double timeFactorX;
    private double timeFactorY;

    // Variables for attacking
    private ImageView beam;
    private Point2D   beamDirection;
    private double    lastBeamAttackTime;
    private double    beamWait;
    private boolean   beamHit;

    private int lastReleaseHealth = entity.maxHealth;

    private double lastProjectileTime;

    private static final double BEAM_ANGLE = 3d;

    /**
     * Sets up random parameters for the Controller
     *
     * @param entity the entity to control
     */
    public GolemEntityController(Golem entity) {
        super(entity);

        speed       = 200;
        inputSmooth = 0.03d;

        timeFactorX = RandomUtil.get(1d, 2d);
        timeFactorY = RandomUtil.get(1d, 2d);

        entity.setPosition(Point2D.ZERO);
    }

    public void act() {
        // Stop the entity if needed
        if (stopped) {
            return;
        }

        // Delay the Entity from moving for 1 second
        timeSinceRoomLoad += GameEngine.getDt();
        if (timeSinceRoomLoad < 1d) {
            return;
        }

        // Change velocity
        double  t            = GameEngine.getT();
        Point2D velocity     = new Point2D(Math.cos(t * timeFactorX), Math.cos(t * timeFactorY));
        Point2D biasToCenter = entity.position.normalize().multiply(-.1);
        velocity = velocity.add(biasToCenter);
        velocity = velocity.normalize().multiply(speed);

        entity.setVelocity(entity.getVelocity().interpolate(velocity, inputSmooth));

        // Attack
        if (t > 2d + lastBeamAttackTime + beamWait) {
            lastBeamAttackTime = t;
            beamWait           = RandomUtil.get(0, 6d * entity.health / entity.maxHealth);

            beamAttack();
        } else {
            updateBeam();
        }

        // Release
        if (lastReleaseHealth - entity.health > 15) {
            lastReleaseHealth = entity.health;

            List<Entity> enemies = List.of(new Skull(), new Skull(), new Mage(), new Mage());
            GameScreen.getLevel().getCurrentRoom().addEntity(enemies);
            GameEngine.instantiate(GameEngine.ENTITY, enemies);
        }

        if (t > 3d + lastProjectileTime) {
            lastProjectileTime = t;

            releaseProjectiles();
        }

        double scale = 1.5d - .5d * entity.health / entity.maxHealth;
        scale += (scale - entity.getScaleX()) * .001d;
        entity.setScaleX(scale);
        entity.setScaleY(scale);
    }

    private void releaseProjectiles() {
        int count = RandomUtil.getInt(1, 7);

        Entity  player         = Player.getPlayer();
        Point2D startDirection = player.position.subtract(entity.position).normalize();
        double  startAngle     = MathUtil.getAngle(startDirection);
        double  dAngle         = MathUtil.TAU / count;

        List<TrackingProjectile> projectiles = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Point2D direction = MathUtil.getVector(startAngle + dAngle * i);
            projectiles.add(new TrackingProjectile("GolemProjectile.png",
                                                   entity.position,
                                                   direction));
        }

        GameEngine.instantiate(GameEngine.VFX, projectiles);
    }

    private void beamAttack() {
        Entity  player   = Player.getPlayer();
        Point2D position = entity.position.add(5, -70).add(RandomUtil.getPoint2D(50));
        beamDirection = player.position.subtract(position).normalize();
        Point2D center = position.add(beamDirection.multiply(1200 - 46));

        beam = new ImageView();
        beam.setTranslateX(center.getX());
        beam.setTranslateY(center.getY());
        beam.setRotate(MathUtil.getAngleDeg(beamDirection) - 1.8d);
        double scale = 2d + 4d * entity.health / entity.maxHealth;
        var    ac    = AnimationController.add(beam, "GolemLaser.png", 0, 15, 1, 1200, 100, 0, 2d);
        ac.setOnFinish(() -> {
            TimerUtil.lerp(.5, t -> {
                if (beam != null) {
                    beam.setOpacity(1 - t);
                }
            }, () -> {
                GameEngine.removeFromLayer(GameEngine.VFX, beam);
                beam = null;
            });
        });
        GameEngine.addToLayer(GameEngine.VFX, beam);

        beamHit = false;
    }

    private void updateBeam() {
        if (beam == null) {
            return;
        }

        Entity  player            = Player.getPlayer();
        Point2D position          = entity.position.add(5, -70);
        Point2D directionToPlayer = player.position.subtract(position).normalize();

        double det = beamDirection.crossProduct(directionToPlayer)
                         .dotProduct(new Point3D(0, 0, 1));
        double rotate = 1 * Math.signum(det);
        beamDirection =
            MathUtil.getVector(MathUtil.getAngle(beamDirection)
                               + rotate * GameEngine.getDt());

        Point2D center = position.add(beamDirection.multiply(1200 - 46));

        beam.setTranslateX(center.getX());
        beam.setTranslateY(center.getY());
        beam.setRotate(MathUtil.getAngleDeg(beamDirection) - 1.8d);

        // Collision detection
        if (!beamHit && GameEngine.getT() - lastBeamAttackTime > .8d) {
            Point2D enemyPosition = player.getPosition().subtract(position);

            Bounds localBounds = player.getBoundsInLocal();
            double width       = localBounds.getWidth() / 2;
            double height      = localBounds.getHeight() / 2;
            Stream<Point2D> bounds = Stream.of(
                enemyPosition,
                enemyPosition.add(new Point2D(-width, -height)),
                enemyPosition.add(new Point2D(width, -height)),
                enemyPosition.add(new Point2D(-width, height)),
                enemyPosition.add(new Point2D(width, height)));

            if (bounds.anyMatch(b -> beamDirection.angle(b) < BEAM_ANGLE)) {
                beamHit = true;
                attack();
            }
        }
    }
}
