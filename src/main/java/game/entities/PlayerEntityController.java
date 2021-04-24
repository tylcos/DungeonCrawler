package game.entities;

import core.*;
import game.collidables.DebugPoint;
import game.inventory.Weapon;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utilities.AnimationController;
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

        Stage stage = SceneManager.getStage();
        stage.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> attacking = true);
        stage.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> attacking = false);

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
        if (GameDriver.isDebug()) {
            Point2D playerPosition = entity.getPosition();
            Point2D range          = entity.getWeaponHolder().getWeapon().getAttackRange();

            Point2D mousePosition   = InputManager.getMousePosition();
            Point2D facingDirection = mousePosition.subtract(playerPosition).normalize();
            double  facingAngle     = MathUtil.getAngleDeg(facingDirection);

            DebugPoint.debug(MathUtil.getVectorDeg(facingAngle + range.getY()).multiply(
                range.getX()).add(playerPosition));
            DebugPoint.debug(MathUtil.getVectorDeg(facingAngle - range.getY()).multiply(
                range.getX()).add(playerPosition));
            DebugPoint.debug(MathUtil.getVectorDeg(facingAngle).multiply(
                range.getX()).add(playerPosition));
        }

        if (attacking) {
            attack();
        }
    }

    @Override
    void attack() {
        Weapon weapon          = weaponHolder.getWeapon();
        double timeSinceAttack = 1e-9d * (System.nanoTime() - lastAttackTime);
        if (timeSinceAttack < weapon.getFireRate()) {
            return;
        }

        lastAttackTime = System.nanoTime();

        switch (weapon.getType()) {
        case Sword:
        case Spear:
            weaponHolder.getCollidingEnemies().forEach(e -> e.damage(weapon.getDamage()));
            break;
        case Bow:
            Projectile arrow =
                new Projectile("Arrow.png",
                               weaponHolder.getWeaponPosition(),
                               weaponHolder.getFacingDirection().multiply(2000d),
                               e -> e.damage(weapon.getDamage()));
            GameEngine.instantiate(GameEngine.VFX, arrow);
            break;
        case Staff:
            ExplosiveProjectile energyBall =
                new ExplosiveProjectile("blank.png",
                                        weaponHolder.getWeaponPosition(),
                                        weaponHolder.getFacingDirection().multiply(1000d),
                                        200d,
                                        enemies -> enemies
                                                       .forEach(e -> e.damage(weapon.getDamage())));
            AnimationController.add(energyBall, "EnergyBall.png", 0, 1, 9, 128, 20, 1);
            GameEngine.instantiate(GameEngine.VFX, energyBall);
            break;
        default:
            throw new IllegalArgumentException("Illegal Weapon type: " + weapon.getType());
        }

        weapon.attack();
    }
}
