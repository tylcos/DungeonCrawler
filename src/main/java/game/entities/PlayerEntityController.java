package game.entities;

import core.*;
import game.Weapon;
import game.collidables.DebugPoint;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import utilities.MathUtil;

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
    private WeaponHolder weaponHolder;

    /**
     * Create an EntityController to control an entity
     *
     * @param entity the entity to be controlled
     */
    public PlayerEntityController(Player entity) {
        super(entity);
        weaponHolder = entity.getWeaponHolder();

        SceneManager.getStage().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> attack());

        InputManager.addKeyHandler(KeyCode.TAB, () -> weaponHolder.changeWeapon());
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

        if (GameDriver.isDebug()) {
            Point2D playerPosition = entity.getPosition();
            Point2D range          = entity.getWeaponHolder().getWeapon().getAttackRange();

            Point2D mousePosition  = InputManager.getMousePosition();
            Point2D facingDirection = mousePosition.subtract(playerPosition).normalize();
            double  facingAngle    = MathUtil.getAngleDeg(facingDirection);

            DebugPoint.debug(MathUtil.getVectorDeg(facingAngle + range.getY()).multiply(
                range.getX()).add(playerPosition));
            DebugPoint.debug(MathUtil.getVectorDeg(facingAngle - range.getY()).multiply(
                range.getX()).add(playerPosition));
            DebugPoint.debug(MathUtil.getVectorDeg(facingAngle).multiply(
                range.getX()).add(playerPosition));
        }
    }

    @Override
    void attack() {
        if (attacking) {
            return;
        }

        Weapon weapon = weaponHolder.getWeapon();
        switch (weapon.getType()) {
        case Sword:
        case Spear:
            weaponHolder.getCollidingEnemies().forEach(e -> e.damage(weapon.getDamage()));
            break;
        case Bow:
        case Staff:

            break;
        default:
            throw new IllegalArgumentException("Illegal Weapon type: " + weapon.getType());
        }

        weaponHolder.getWeapon().attack();

        lastAttackTime = System.nanoTime();
    }
}
