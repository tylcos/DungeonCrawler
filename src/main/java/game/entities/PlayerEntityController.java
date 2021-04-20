package game.entities;

import core.*;
import game.Inventory;
import game.Weapon;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

/**
 * Player behavior is to move and attack based on the user's input
 */
public class PlayerEntityController extends EntityController<Player> {
    // Variables for movement
    private double speed       = 600d;
    // Smooths input over around 125ms
    // inputSmooth = 1d would remove smoothing
    // https://www.desmos.com/calculator/xjyyi5sndo
    private double inputSmooth = .3d;

    /**
     * Create an EntityController to control an entity
     *
     * @param entity          the entity to be controlled
     */
    public PlayerEntityController(Player entity) {
        super(entity);

        SceneManager.getStage().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> attack());
    }

    public void act() {
        if (stopped) {
            return;
        }

        // User input logic
        // Movement input
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

        // Weapon input
        if (InputManager.get(KeyCode.TAB)) {
            Inventory.changeWeapon("images/PlayerAxe.png");
        }

        // TODO 3/24/2021 Use a better frame independent way of smoothing input
        double  dt            = GameEngine.getDt();
        double  modifiedSpeed = speed * entity.getSpeedMultiplier();
        Point2D rawVelocity   = input.normalize().multiply(modifiedSpeed);
        Point2D velocity = entity.getVelocity().interpolate(rawVelocity,
                                                            inputSmooth * (60d * dt + .0007d / dt));

        double dVelocity = velocity.subtract(entity.getVelocity()).magnitude() / modifiedSpeed;
        entity.setVelocity(dVelocity < .01 ? rawVelocity : velocity);
    }

    @Override
    void attack() {
        Weapon weapon = entity.getWeapon();
        switch (weapon.getType()) {
        case "Bow":

            break;
        default:

        }
    }
}
