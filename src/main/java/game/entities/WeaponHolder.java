package game.entities;

import core.GameEngine;
import core.InputManager;
import game.inventory.Inventory;
import game.inventory.Weapon;
import javafx.geometry.*;
import javafx.scene.image.ImageView;
import utilities.MathUtil;
import utilities.TimerUtil;
import views.GameScreen;

import java.util.List;
import java.util.stream.Stream;

public class WeaponHolder {
    private Weapon[]  weapons;
    private int       weaponIndex;
    private ImageView weaponImageView;

    private Point2D weaponPosition;
    private Point2D facingDirection;

    public WeaponHolder(int size) {
        weapons = new Weapon[size];
    }

    public Weapon add(Weapon addedWeapon) {
        // Free slot
        for (int i = 0; i < weapons.length; i++) {
            if (weapons[i] == null) {
                weapons[i] = addedWeapon;
                changeWeapon(i);

                return null;
            }
        }

        // Replace current weapon
        Weapon current = weapons[weaponIndex];

        weapons[weaponIndex] = addedWeapon;
        changeWeapon(weaponIndex);
        return current;
    }

    public void changeWeapon() {
        changeWeapon((weaponIndex + 1) % weapons.length);
    }

    private void changeWeapon(int index) {
        if (weapons[index] == null) {
            return;
        }

        weaponIndex = index;

        if (weaponImageView != null) {
            TimerUtil.lerp(.5d, t -> weaponImageView.setOpacity(t));

            weaponImageView.setImage(weapons[index].getImage());
            Inventory.changeWeapon(weapons[index]);
        }
    }

    public void render() {
        if (weaponImageView == null || weaponImageView.getParent() == null) {
            weaponImageView = new ImageView(weapons[weaponIndex].getImage());
            GameEngine.addToLayer(GameEngine.VFX, weaponImageView);
        }

        Player  player         = Player.getPlayer();
        Point2D playerPosition = player.getPosition();
        Point2D mousePosition  = InputManager.getMousePosition();
        facingDirection = mousePosition.subtract(playerPosition).normalize();

        Point3D offsets          = getWeapon().getOffsets();
        double  facingAngleDeg   = Math.toDegrees(MathUtil.getAngle(facingDirection));
        double  adjustedAngleDeg = facingAngleDeg + offsets.getY();
        Point2D adjustedFacing   = MathUtil.getVectorDeg(adjustedAngleDeg);
        weaponPosition = playerPosition.add(
            adjustedFacing.multiply(Weapon.WEAPON_HOLD_DISTANCE + offsets.getX()));

        weaponImageView.setTranslateX(weaponPosition.getX());
        weaponImageView.setTranslateY(weaponPosition.getY());
        weaponImageView.setRotate(adjustedAngleDeg + offsets.getZ() + 45);
        weaponImageView.setOpacity(player.getOpacity());
    }

    public Stream<Entity> getCollidingEnemies() {
        List<Entity> enemies = GameScreen.getLevel().getCurrentRoom().getEntities();

        Point2D playerPosition = Player.getPlayer().getPosition();
        Point2D range          = getWeapon().getAttackRange();

        return enemies.stream().filter(enemy -> {
            if (enemy.isDead()) {
                return false;
            }

            Bounds  localBounds   = enemy.getBoundsInLocal();
            double  width         = localBounds.getWidth() / 2;
            double  height        = localBounds.getHeight() / 2;
            Point2D enemyPosition = enemy.getPosition().subtract(playerPosition);
            Stream<Point2D> bounds = Stream.of(
                enemyPosition,
                enemyPosition.add(new Point2D(-width, -height)),
                enemyPosition.add(new Point2D(width, -height)),
                enemyPosition.add(new Point2D(-width, height)),
                enemyPosition.add(new Point2D(width, height)));

            return bounds.anyMatch(b -> b.magnitude() < range.getX()
                                        && facingDirection.angle(b) < range.getY());
        });
    }

    public Weapon getWeapon() {
        return weapons[weaponIndex];
    }

    public Point2D getWeaponPosition() {
        return weaponPosition;
    }

    public Point2D getFacingDirection() {
        return facingDirection;
    }
}
