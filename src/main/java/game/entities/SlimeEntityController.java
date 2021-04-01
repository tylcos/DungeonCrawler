package game.entities;

import core.GameDriver;
import core.GameEngine;
import data.RandomUtil;
import game.collidables.DebugPoint;
import javafx.geometry.Point2D;

import java.util.List;

/**
 * Defines the Slime AI.
 * The behavior is to move closely around the player while attacking periodically.
 */
public class SlimeEntityController extends EntityController {
    private double speed;
    private double inputSmooth;

    private double strafingDistance;
    private double attackingDistance;

    private double biasScale;
    private double timeFactorX;
    private double timeFactorY;

    private State state = State.running;

    private DebugPoint debugPoint;

    private enum State {
        attacking, charging, running
    }

    /**
     * @param entity the entity to control
     */
    public SlimeEntityController(Entity entity) {
        super(entity);

        speed       = RandomUtil.getInt(300, 400);
        inputSmooth = RandomUtil.get() * .01d + 0.01d;

        strafingDistance  = RandomUtil.get() * 100 + 100;
        attackingDistance = 50;

        biasScale   = 200;
        timeFactorX = RandomUtil.get() + 1d;
        timeFactorY = RandomUtil.get() + 1d;

        debugPoint = new DebugPoint();
    }

    public void act() {
        if (GameDriver.isDebug() && !debugPoint.isRendered() && !stopped) {
            GameEngine.addToLayer(GameEngine.VFX, List.of(debugPoint));
        }

        if (stopped) {
            entity.setVelocity(entity.getVelocity().interpolate(Point2D.ZERO, .01d));
            return;
        }


        double t = GameEngine.getT();
        Point2D bias = new Point2D(biasScale * Math.cos(t * timeFactorX),
                                   biasScale * Math.cos(t * timeFactorY));

        Player  player           = Player.getPlayer();
        Point2D difference       = player.getPosition().subtract(entity.getPosition());
        Point2D biasedDifference = difference.add(bias);
        double  distance         = difference.magnitude();

        difference       = difference.normalize();
        biasedDifference = biasedDifference.normalize();

        // Change states based on position
        if (distance >= strafingDistance) {
            state = State.charging;
        } else if (state == State.charging && distance < strafingDistance) {
            state = State.attacking;
        } else if (state == State.attacking && distance < attackingDistance) {
            state = State.running;

            player.damage(1); // Attack player
        }

        if (player.isDead()) {
            strafingDistance = 10;
            biasScale        = 20;
        }

        // Change velocity based on state
        Point2D velocity;
        switch (state) {
        case attacking:
            velocity = difference.multiply(speed);
            break;
        case charging:
            velocity = biasedDifference.multiply(speed);
            break;
        case running:
            velocity = biasedDifference.multiply(-speed);
            break;
        default:
            throw new IllegalStateException("Invalid state: " + state);
        }

        entity.setVelocity(entity.getVelocity().interpolate(velocity, inputSmooth));

        debugPoint.setPosition(bias.add(player.getPosition()));
    }

    @Override
    public void stop() {
        stopped = true;

        if (GameDriver.isDebug()) {
            GameEngine.removeFromLayer(GameEngine.VFX, List.of(debugPoint));
        }
    }
}
