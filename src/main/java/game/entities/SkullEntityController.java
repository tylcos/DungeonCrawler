package game.entities;

import core.GameEngine;
import game.collidables.DebugPoint;
import javafx.geometry.Point2D;
import utilities.RandomUtil;

/**
 * Defines the Skull AI.
 *
 * The behavior is to move to where the player is predicted to be assuming the Player maintains
 * constant dx/dt and dy/dt.
 */
public class SkullEntityController extends EntityController<Skull> {
    private State state = State.relaxing;

    // Variables for movement
    private double speed;
    private double inputSmooth;

    // Variables for changing the state of the entity
    private double strafingDistance;
    private double attackingDistance;

    // Variables for generating bias
    private double biasScale;
    private double timeFactorX;
    private double timeFactorY;

    // All possible states of the entity
    private enum State {
        attacking, charging, running, relaxing
    }

    /**
     * Sets up random parameters for the Controller
     *
     * @param entity the entity to control
     */
    public SkullEntityController(Skull entity) {
        super(entity);

        speed       = RandomUtil.getInt(400, 500);
        inputSmooth = RandomUtil.get(0.02d, 0.03d);

        strafingDistance  = RandomUtil.get(50d, 100d);
        attackingDistance = 50;

        biasScale   = 100;
        timeFactorX = RandomUtil.get(1d, 2d);
        timeFactorY = RandomUtil.get(1d, 2d);
    }

    public void act() {
        // Stop the entity if needed
        if (stopped) {
            return;
        }

        // Delay the Entity from moving for 1 second
        timeSinceRoomLoad += GameEngine.getDt();
        if (state == State.relaxing && timeSinceRoomLoad > 1d) {
            state = State.running;
        }

        // Bias that follows a Lissajous figure, used for random movement
        double t = GameEngine.getT();
        Point2D bias = new Point2D(biasScale * Math.cos(t * timeFactorX),
                                   biasScale * Math.cos(t * timeFactorY));

        Player player = Player.getPlayer();
        // Points directly to the Player
        Point2D difference      = player.getPosition().subtract(entity.getPosition());
        Point2D playerDirection = difference.normalize();
        double  distance        = difference.magnitude();

        // Predicts where the player will be
        double  timeToReach = difference.magnitude() / speed;
        Point2D playerDp    = player.getVelocity().multiply(timeToReach);
        Point2D prediction  = difference.add(playerDp);

        // Points to where the entity should move
        Point2D target          = prediction.add(bias);
        Point2D targetDirection = target.normalize();

        // Change states based on position
        if (state != State.relaxing && distance >= strafingDistance) {
            state = State.charging;
        } else if (state == State.charging && distance < strafingDistance) {
            state = State.attacking;
        } else if (state == State.attacking && distance < attackingDistance) {
            state = State.running;

            attack();
        }

        // Swarm the Player on death
        if (player.isDead()) {
            strafingDistance = 50;
        }

        // Change velocity based on state
        Point2D velocity;
        switch (state) {
        case attacking:
            velocity = playerDirection.multiply(speed);
            break;
        case charging:
            velocity = targetDirection.multiply(speed);
            break;
        case running:
            velocity = targetDirection.multiply(-speed);
            break;
        case relaxing:
            velocity = bias.normalize().multiply(relaxingBiasScale);
            break;
        default:
            throw new IllegalStateException("Invalid state: " + state);
        }

        // Smoothly change velocity
        entity.setVelocity(entity.getVelocity().interpolate(velocity, inputSmooth));

        // Shows where the entity is moving towards
        if (useDebugPoints) {
            DebugPoint.debug(entity.getPosition().add(target));
        }
    }
}
