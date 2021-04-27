package game.entities;

import core.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
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
    private double startTime;

    // Variables for attacking
    private double healthRatio = 1;

    private ImageView beam;
    private Point2D   beamDirection;
    private double    beamScale;
    private double    lastBeamAttackTime;
    private double    beamWait;
    private boolean   beamHit;

    private int lastReleaseHealth = entity.maxHealth;

    private double lastProjectileTime;

    private static final double BEAM_ANGLE = 3d;

    private Blend      roomEffect;
    private Blend      bottomInput;
    private ColorInput topInput2;

    /**
     * Sets up random parameters for the Controller
     *
     * @param entity the entity to control
     */
    public GolemEntityController(Golem entity) {
        super(entity);

        speed       = RandomUtil.get(200d, 300d);
        inputSmooth = 0.03d;

        timeFactorX = RandomUtil.get(1.5d, 2d);
        timeFactorY = RandomUtil.get(1d, 1.5d);
        startTime   = RandomUtil.get() * 500;

        entity.setPosition(Point2D.ZERO);
        act();
        entity.toFront();
    }

    public void act() {
        // Stop the entity if needed
        if (stopped) {
            return;
        }

        healthRatio = (double) entity.health / entity.maxHealth;
        double r = .4 + .2 * healthRatio;
        double g = .3 + .5 * healthRatio;
        entity.setEffect(new Blend(BlendMode.COLOR_BURN,
                                   new InnerShadow(255, new Color(r, g, 1, 1)),
                                   new Bloom(0)));

        // Delay the Entity from moving for 1 second
        timeSinceRoomLoad += GameEngine.getDt();
        if (timeSinceRoomLoad < 1d) {
            return;
        }

        // Change velocity
        double  t      = GameEngine.getT();
        Entity  player = Player.getPlayer();
        Point2D dPos   = player.position.subtract(entity.position);
        Point2D velocity = new Point2D(Math.cos(t * timeFactorX + startTime),
                                       Math.cos(t * timeFactorY + startTime));
        Point2D bias = dPos.multiply(.002d);
        velocity = velocity.add(bias);
        velocity = velocity.normalize().multiply(speed);

        entity.setVelocity(entity.getVelocity().interpolate(velocity, inputSmooth));

        // Attack

        if (t > 3d + lastBeamAttackTime + beamWait) {
            lastBeamAttackTime = t;
            beamWait           = RandomUtil.get() * healthRatio;

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

        double scale = 1.5d - .5d * healthRatio;
        scale += (scale - entity.getScaleX()) * .001d;
        entity.setScaleX(scale);
        entity.setScaleY(scale);
    }

    private void releaseProjectiles() {
        int count = 12 - (int) (11 * healthRatio);

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
        beamScale = 2d - healthRatio;
        Entity player = Player.getPlayer();
        Point2D position = entity.position.add(5 * entity.getScaleY(),
                                               -70 * entity.getScaleX());
        beamDirection = player.position.subtract(position).normalize();
        Point2D center = position.add(beamDirection.multiply(beamScale * 560));

        if (beam != null) {
            beam.setVisible(false);
        }

        beam = new ImageView();
        beam.setTranslateX(center.getX());
        beam.setTranslateY(center.getY());
        beam.setRotate(MathUtil.getAngleDeg(beamDirection) - 3.6d);
        beam.setScaleX(beamScale);
        beam.setScaleY(beamScale);

        beam.setEffect(new Blend(BlendMode.COLOR_BURN,
                                 new DropShadow(255, new Color(beamScale - 1, 0, 1, 1)),
                                 new Bloom(0)));

        var ac = AnimationController.add(beam, "GolemLaser.png",
                                         0, 15, 1, 600, 100, 0, 2d);

        ac.setOnFinish(() -> {
            double beamTime = entity.isDead() ? 3d : 1d;

            TimerUtil.lerp(beamTime, t -> {
                if (beam != null) {
                    beam.setOpacity(1 - t);

                    if (!player.isDead) {
                        ((MotionBlur) bottomInput.getBottomInput()).setRadius(10 * (1 - t));

                        topInput2.setPaint(new Color(0, 0, 1, .1 * (1 - t)));
                    }
                }
            }, () -> {
                if (beam != null) {
                    GameEngine.removeFromLayer(GameEngine.VFX, beam);
                    beam.setVisible(false);
                    beam = null;

                    if (!player.isDead) {
                        roomEffect.setBottomInput(null);
                    }
                }
            });
        });
        GameEngine.addToLayer(GameEngine.VFX, beam);

        beamHit = false;

        SoundManager.playBeam();
    }

    private void updateBeam() {
        if (beam == null) {
            return;
        }

        Entity player = Player.getPlayer();
        Point2D position = entity.position.add(5 * entity.getScaleY(),
                                               -70 * entity.getScaleX());
        Point2D directionToPlayer = player.position.subtract(position).normalize();

        double det = beamDirection.crossProduct(directionToPlayer)
                         .dotProduct(new Point3D(0, 0, 1));
        double rotate = 1 * Math.signum(det);
        beamDirection =
            MathUtil.getVector(MathUtil.getAngle(beamDirection)
                               + rotate * GameEngine.getDt());

        Point2D center = position.add(beamDirection.multiply(beamScale * 560));

        beam.setTranslateX(center.getX());
        beam.setTranslateY(center.getY());
        beam.setRotate(MathUtil.getAngleDeg(beamDirection) - 3.6d);

        // Collision detection
        if (!beamHit && GameEngine.getT() - lastBeamAttackTime > .8d) {
            Parent root = SceneManager.getStage().getScene().getRoot();
            Node   room = root.getChildrenUnmodifiable().get(0);
            roomEffect  = (Blend) room.getEffect();
            topInput2   = new ColorInput(0, 0, ScreenManager.getWidth(),
                                         ScreenManager.getHeight(),
                                         new Color(0, 0, 1, .1));
            bottomInput = new Blend(BlendMode.COLOR_BURN, new MotionBlur(),
                                    topInput2);
            if (!player.isDead) {
                roomEffect.setBottomInput(bottomInput);
            }

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

    @Override
    void stop() {
        if (!stopped && beam == null) {
            beamAttack();
        }

        TimerUtil.lerp(5, t -> {
            if (beam == null) {
                return;
            }

            beamDirection = MathUtil.getVector(MathUtil.getAngle(beamDirection)
                                               + t * 3 * GameEngine.getDt());

            Point2D position = entity.position.add(5 * entity.getScaleY(),
                                                   -70 * entity.getScaleX());
            Point2D center = position.add(beamDirection.multiply(beamScale * 560));

            beam.setTranslateX(center.getX());
            beam.setTranslateY(center.getY());
            beam.setRotate(MathUtil.getAngleDeg(beamDirection) - 3.6d);
        });

        super.stop();
    }
}
