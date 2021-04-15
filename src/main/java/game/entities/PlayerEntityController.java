package game.entities;

import core.GameEngine;
import core.InputManager;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

import java.util.function.DoubleSupplier;

/**
 * Player behavior is to move and attack based on the user's input
 */
public class PlayerEntityController extends EntityController {
    // Variables for movement
    private double         speed       = 600d;
    private DoubleSupplier speedMultiplier;
    // Smooths input over around 125ms
    // inputSmooth = 1d would remove smoothing
    // https://www.desmos.com/calculator/xjyyi5sndo
    private double         inputSmooth = .3d;

    /**
     * Create an EntityController to control an entity
     *
     * @param entity          the entity to be controlled
     * @param speedMultiplier a supplier for the speed multiplier
     */
    public PlayerEntityController(Entity entity, DoubleSupplier speedMultiplier) {
        super(entity);

        this.speedMultiplier = speedMultiplier;
    }

    public void act() {
        if (stopped) {
            return;
        }

        // User input logic
        Point2D input = Point2D.ZERO;
        if (InputManager.get(KeyCode.W) || InputManager.get(KeyCode.UP)) {
            input = input.add(0, -1);
        }
        if (InputManager.get(KeyCode.A) || InputManager.get(KeyCode.LEFT)) {
            input = input.add(-1, 0);
        }
        if (InputManager.get(KeyCode.S) || InputManager.get(KeyCode.DOWN)) {
            input = input.add(0, 1);
        }
        if (InputManager.get(KeyCode.D) || InputManager.get(KeyCode.RIGHT)) {
            input = input.add(1, 0);
        }

        // TODO 3/24/2021 Use a better frame independent way of smoothing input
        double  dt            = GameEngine.getDt();
        double  modifiedSpeed = speed * speedMultiplier.getAsDouble();
        Point2D rawVelocity   = input.normalize().multiply(modifiedSpeed);
        Point2D velocity = entity.getVelocity().interpolate(rawVelocity,
                                                            inputSmooth * (60d * dt + .0007d / dt));

        double dVelocity = velocity.subtract(entity.getVelocity()).magnitude() / modifiedSpeed;
        entity.setVelocity(dVelocity < .01 ? rawVelocity : velocity);
    }
}
