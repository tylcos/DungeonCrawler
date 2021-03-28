package game.entities;

import core.GameEngine;
import data.RandomUtil;
import javafx.geometry.Point2D;

/**
 * Slime behavior is to circle closely around the player while attacking periodically.
 */
public class SlimeEntityController implements IEntityController {
    private Entity slime;

    private double speed;
    private double inputSmooth;

    private double  strafingDirection;
    private double  strafingDistance;
    private Point2D chargingBias;

    private State state = State.running;

    private enum State {
        stopped, attacking, charging, strafing, running
    }

    public SlimeEntityController(Entity slime) {
        this.slime = slime;

        speed       = RandomUtil.getInt(200, 300);
        inputSmooth = RandomUtil.get() * .01d + 0.005d;

        // 1 = clockwise, -1 = counterclockwise
        strafingDirection = RandomUtil.getInt(2) * 2 - 1;
        strafingDistance  = RandomUtil.get() * 100 + 100;
        chargingBias      = RandomUtil.getPoint2D(200);
    }

    public void act() {
        Point2D enemyPosition      = slime.getPosition();
        Point2D mainPlayerPosition = MainPlayer.getPlayer().getPosition().add(0, -50);
        Point2D difference         = mainPlayerPosition.subtract(enemyPosition);

        if (state != State.stopped) {
            if (difference.magnitude() > strafingDistance) {
                state = State.charging;
            } else if ((state == State.charging || state == State.strafing)
                       && difference.magnitude() <= strafingDistance) {
                state = State.strafing;

                if (RandomUtil.get() < .2 * GameEngine.getDt()) {
                    state = State.attacking;
                }
            } else if (state == State.attacking
                       && difference.magnitude() < .5d * strafingDistance) {
                state = State.running;
            }

            if (MainPlayer.getPlayer().isDead()) {
                strafingDistance = 20;
                chargingBias = Point2D.ZERO;
            }
        }

        Point2D velocity;
        switch (state) {
        case stopped:
            velocity = Point2D.ZERO;
            break;
        case attacking:
            velocity = difference.normalize().multiply(speed);
            break;
        case charging:
            velocity = difference.magnitude() > 2d * strafingDistance
                       ? difference.add(chargingBias).normalize().multiply(speed)
                       : difference.normalize().multiply(speed);
            break;
        case strafing:
            velocity = difference.normalize().multiply(speed);
            velocity = new Point2D(strafingDirection * velocity.getY(),
                                   strafingDirection * -velocity.getX());
            break;
        case running:
            velocity = difference.normalize().multiply(-speed);
            break;
        default:
            throw new IllegalStateException("Invalid state: " + state);
        }

        slime.setVelocity(slime.getVelocity().interpolate(velocity, inputSmooth));
    }

    @Override
    public void stop() {
        state = State.stopped;
    }
}
