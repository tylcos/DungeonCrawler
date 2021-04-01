package game.entities;

import core.GameEngine;
import core.InputManager;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

/**
 * Player behavior is to move and attack based on the user's input
 */
public class PlayerEntityController implements IEntityController {
    private Entity entity;

    private double speed = 600d;
    // Smooths input over around 125ms
    // inputSmooth = 1d would remove smoothing
    // https://www.desmos.com/calculator/xjyyi5sndo
    private double  inputSmooth = .2d;

    public PlayerEntityController(Entity entity) {
        this.entity = entity;
    }

    public void act() {
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
        double  dt          = GameEngine.getDt();
        Point2D rawVelocity = input.normalize().multiply(speed);
        Point2D velocity = entity.getVelocity().interpolate(rawVelocity,
                                                     inputSmooth * (60d * dt + .0007d / dt));

        double dVelocity = velocity.subtract(entity.getVelocity()).magnitude() / speed;
        entity.setVelocity(dVelocity < .01 ? rawVelocity : velocity);
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        entity.setVelocity(Point2D.ZERO);
    }
}
