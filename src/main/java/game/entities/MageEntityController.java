package game.entities;

import core.GameEngine;
import data.RandomUtil;
import game.collidables.DebugPoint;
import javafx.geometry.Point2D;

import java.util.List;

/**
 * Defines the Mage AI.
 * The behavior is to move to where the player is predicted to be.
 */
public class MageEntityController extends EntityController {
    private State state = State.running;

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

    private              double previousTheta;
    private              double thetaDot      = .1d;
    private static final double TWO_PI        = 2 * Math.PI;

    // Small dot for debugging where the entity is moving towards
    private DebugPoint debugPoint;

    // All possible states of the entity
    private enum State {
        attacking, charging, running
    }

    /**
     * Sets up random parameters for the Controller
     *
     * @param entity the entity to control
     */
    public MageEntityController(Entity entity) {
        super(entity);

        speed       = RandomUtil.getInt(400, 500);
        inputSmooth = RandomUtil.get(0.02d, 0.03d);

        strafingDistance  = RandomUtil.get(50d, 100d);
        attackingDistance = 50;

        debugPoint = new DebugPoint();
    }

    public void act() {
        // Spawn debug point for the first time
        if (useDebugPoints && !debugPoint.isRendered() && !stopped) {
            GameEngine.addToLayer(GameEngine.VFX, List.of(debugPoint));
        }

        // Stop the entity
        if (stopped) {
            return;
        }

        Player  player         = Player.getPlayer();
        Point2D playerPosition = player.getPosition();
        // Points directly to the Player
        Point2D difference      = playerPosition.subtract(entity.getPosition());
        Point2D playerDirection = difference.normalize();
        double  distance        = difference.magnitude();

        // Predicts where the player will be
        double timeToReach     = difference.magnitude() / speed;
        double radius          = player.getPosition().magnitude();
        double currentTheta    = (Math.atan2(playerPosition.getY(), playerPosition.getX())
                                  + TWO_PI) % TWO_PI;
        double currentThetaDot = ((currentTheta - previousTheta) % TWO_PI) / GameEngine.getDt();
        thetaDot += (currentThetaDot - thetaDot) * .01d;

        double predictedTheta = currentTheta + .7d * timeToReach * thetaDot;
        Point2D prediction = new Point2D(radius * Math.cos(predictedTheta),
                                         radius * Math.sin(predictedTheta));

        debugPoint.setPosition(prediction);

        // Points to where the entity should move
        Point2D target          = prediction.subtract(entity.getPosition());
        Point2D targetDirection = target.normalize();

        // Change states based on position
        if (distance >= strafingDistance) {
            state = State.charging;
        } else if (state == State.charging && distance < strafingDistance) {
            state = State.attacking;
        } else if (state == State.attacking && distance < attackingDistance) {
            state = State.running;

            player.damage(1); // Attack player
        }

        // Swarm the Player on death
        if (player.isDead()) {
            strafingDistance = 10;
            biasScale        = 30;
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
        default:
            throw new IllegalStateException("Invalid state: " + state);
        }

        // Smoothly change velocity
        entity.setVelocity(entity.getVelocity().interpolate(velocity, inputSmooth));

        // Shows where the entity is moving towards
        // debugPoint.setPosition(entity.getPosition().add(target));

        previousTheta = currentTheta;
    }

    @Override
    public void stop() {
        stopped = true;

        if (useDebugPoints) {
            GameEngine.removeFromLayer(GameEngine.VFX, List.of(debugPoint));
        }
    }
}
