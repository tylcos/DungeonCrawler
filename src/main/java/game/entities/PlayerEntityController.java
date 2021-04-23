package game.entities;

import core.*;
import game.Weapon;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import utilities.TimerUtil;

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

    private boolean      attacking;
    private long         lastAttackTime;
    private double       radiusOffset;
    private double       angleOffset;
    private WeaponHolder weaponHolder;

    /**
     * Create an EntityController to control an entity
     *
     * @param entity the entity to be controlled
     */
    public PlayerEntityController(Player entity) {
        super(entity);
        weaponHolder = entity.getWeaponHolder();

        updateWeaponOffsets(); // Set initial offsets for current weapon
        SceneManager.getStage().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> attack());

        InputManager.addKeyHandler(KeyCode.TAB, () -> {
            weaponHolder.changeWeapon();
            updateWeaponOffsets();
        });
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

        // TODO 3/24/2021 Use a better frame independent way of smoothing input
        double  dt            = GameEngine.getDt();
        double  modifiedSpeed = speed * entity.getSpeedMultiplier();
        Point2D rawVelocity   = input.normalize().multiply(modifiedSpeed);
        Point2D velocity = entity.getVelocity()
                               .interpolate(rawVelocity, -inputSmooth * (60d * dt + .0007d / dt));

        double dVelocity = velocity.subtract(entity.getVelocity()).magnitude() / modifiedSpeed;
        entity.setVelocity(dVelocity < .01 ? rawVelocity : velocity);

        // Weapon input
        Weapon weapon = weaponHolder.getWeapon();
        attacking = System.nanoTime() - lastAttackTime < weapon.getFireRate() * 1e9;
        weaponHolder.setOffsets(radiusOffset, angleOffset);
    }

    @Override
    void attack() {
        if (attacking) {
            return;
        }

        updateWeaponOffsets();

        lastAttackTime = System.nanoTime();
    }

    private void updateWeaponOffsets() {
        Weapon weapon = weaponHolder.getWeapon();
        switch (weapon.getType()) {
        case Bow:

            break;
        case Sword:
            double sign = angleOffset >= 0 ? 1 : -1;
            TimerUtil.lerp(weapon.getFireRate() / 8, t -> angleOffset = sign * -45 * (2 * t - 1));

            break;
        default:
            break;
        }
    }
}
