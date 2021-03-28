package game.entities;

import core.GameEngine;
import core.InputManager;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

/**
 * MainPlayer behavior is to move and attack based on the user's input
 */
public class MainPlayerEntityController implements IEntityController {
    private Entity player;

    private double speed = 750d;
    // Smooths input over around 125ms
    // inputSmooth = 1d would remove smoothing
    // https://www.desmos.com/calculator/xjyyi5sndo
    private double  inputSmooth = .3d;

    public MainPlayerEntityController(Entity player) {
        this.player = player;
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
        Point2D velocity = player.getVelocity().interpolate(rawVelocity,
                                                     inputSmooth * (60d * dt + .0007d / dt));

        double dVelocity = velocity.subtract(player.getVelocity()).magnitude() / speed;
        player.setVelocity(dVelocity < .01 ? rawVelocity : velocity);
    }

    @Override
    public void stop() {
        player.setVelocity(Point2D.ZERO);

    }
}
