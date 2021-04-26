package game.entities;

import core.GameEngine;
import game.collidables.DebugPoint;
import javafx.geometry.Point2D;
import utilities.MathUtil;
import utilities.RandomUtil;

/**
 * Defines the Mage AI.
 *
 * The behavior is to move to where the player is predicted to be assuming the player maintains
 * a constant radius and constant dθ/dt around the center of the room.
 */
public class MageEntityController extends EntityController<Mage> {
    private State state = State.relaxing;

    // Variables for movement
    private double speed;
    private double inputSmooth;

    // Variables for changing the state of the entity
    private double strafingDistance;
    private double attackingDistance;

    // Variables for generating bias
    private double timeFactorX;
    private double timeFactorY;

    // Variables for predicting the Player's position
    private double predictionGuessScale;
    private double previousTheta;
    private double thetaDot = .1d;

    // All possible states of the entity
    private enum State {
        attacking, charging, running, relaxing
    }

    /**
     * Sets up random parameters for the Controller
     *
     * @param entity the entity to control
     */
    public MageEntityController(Mage entity) {
        super(entity);

        speed       = RandomUtil.getInt(400, 500);
        inputSmooth = RandomUtil.get(0.02d, 0.03d);

        strafingDistance  = RandomUtil.get(100d, 150d);
        attackingDistance = 50;

        predictionGuessScale = RandomUtil.get(.6d, 1d);

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

        // Start predicting the player position
        Player  player         = Player.getPlayer();
        Point2D playerPosition = player.getPosition();
        // Points directly to the Player
        Point2D difference      = playerPosition.subtract(entity.getPosition());
        Point2D playerDirection = difference.normalize();
        double  distance        = difference.magnitude();

        // Predicts where the player will be
        double timeToReach  = difference.magnitude() / speed;
        double radius       = player.getPosition().magnitude();
        double currentTheta = MathUtil.getAngle(playerPosition);

        double dTheta = (currentTheta - previousTheta);
        if (Math.abs(dTheta) > 3) {
            dTheta = MathUtil.TAU - currentTheta - previousTheta;
        }
        double currentThetaDot = dTheta / GameEngine.getDt();
        thetaDot += (currentThetaDot - thetaDot) * .02d;

        // Iteratively approximate time to reach player and prediction
        double  predictedTheta = currentTheta + timeToReach * thetaDot * predictionGuessScale;
        Point2D prediction     = MathUtil.getVector(predictedTheta, radius);
        for (int i = 0; i < 3; i++) {
            if (useDebugPoints) {
                DebugPoint.debug(prediction);
            }

            difference     = prediction.subtract(entity.getPosition());
            timeToReach    = difference.magnitude() / speed;
            predictedTheta = currentTheta + timeToReach * thetaDot * predictionGuessScale;
            prediction     = MathUtil.getVector(predictedTheta, radius);
        }
        if (useDebugPoints) {
            DebugPoint.debug(prediction);
        }

        // Points to where the entity should move
        Point2D target          = prediction.subtract(entity.getPosition());
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
            strafingDistance = 100;
            state = State.relaxing;
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
            double t = GameEngine.getT();
            velocity = new Point2D(relaxingBiasScale * Math.cos(t * timeFactorX),
                                   relaxingBiasScale * Math.cos(t * timeFactorY));
            break;
        default:
            throw new IllegalStateException("Invalid state: " + state);
        }

        // Smoothly change velocity
        entity.setVelocity(entity.getVelocity().interpolate(velocity, inputSmooth));

        previousTheta = currentTheta;
    }
}
