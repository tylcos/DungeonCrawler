package game.entities;

import core.GameEngine;
import core.InputManager;
import game.Inventory;
import game.Weapon;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import utilities.TimerUtil;

public class WeaponHolder {
    private Weapon[]  weapons;
    private int       weaponIndex;
    private ImageView weaponImageView;

    private double radiusOffset;
    private double angleOffset;
    private double rotationOffset;

    private static final double WEAPON_HOLD_DISTANCE = 90d;

    public WeaponHolder(int size) {
        weapons = new Weapon[size];
    }

    public void updateWeaponOffsets() {
        Weapon weapon     = getWeapon();
        double attackTime = weapon.getFireRate() / 8d;

        switch (weapon.getType()) {
        case Sword:
            radiusOffset = 0;
            rotationOffset = 0;
            double direction = angleOffset >= 0 ? 1 : -1;
            TimerUtil.lerp(attackTime, t -> angleOffset = direction * -45 * (2 * t - 1));
            break;
        case Spear:
            angleOffset = 0;
            rotationOffset = 0;
            TimerUtil.lerp(attackTime, t -> radiusOffset = 200d * (.5d - Math.abs(t - .5d)));
            break;
        case Bow:
            radiusOffset = 0;
            rotationOffset = 0;
            rotationOffset = 180;
            break;
        case Staff:
            angleOffset = 0;
            rotationOffset = 0;
            TimerUtil.lerp(attackTime, t -> radiusOffset = -50d * (.5d - Math.abs(t - .5d)));
            break;
        default:
            throw new IllegalArgumentException("Illegal Weapon type: " + weapon.getType());
        }
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

    public void setOffsets(double radiusOffset, double angleOffset) {
        this.radiusOffset = radiusOffset;
        this.angleOffset  = angleOffset;
    }

    public void changeWeapon() {
        changeWeapon((weaponIndex + 1) % weapons.length);
    }

    private void changeWeapon(int index) {
        if (weapons[index] == null) {
            return;
        }

        weaponIndex = index;
        updateWeaponOffsets();

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

        Point2D playerPosition  = Player.getPlayer().getPosition();
        Point2D mousePosition   = InputManager.getMousePosition();
        Point2D facingDirection = mousePosition.subtract(playerPosition).normalize();

        double angleDeg = Math.toDegrees(Math.atan2(facingDirection.getY(),
                                                    facingDirection.getX()));
        double  adjustedAngleDeg  = angleDeg + angleOffset;
        double  adjustedAngle     = Math.toRadians(adjustedAngleDeg);
        Point2D adjustedDirection = new Point2D(Math.cos(adjustedAngle), Math.sin(adjustedAngle));
        Point2D weaponPosition = playerPosition.add(
            adjustedDirection.multiply(WEAPON_HOLD_DISTANCE + radiusOffset));

        weaponImageView.setTranslateX(weaponPosition.getX());
        weaponImageView.setTranslateY(weaponPosition.getY());
        weaponImageView.setRotate(adjustedAngleDeg + rotationOffset + 45);
    }

    public void addDamageMultiplier(double multiplier, double effectTime) {
        // Rounds (damage * multiplier) to the nearest int
        Weapon currentWeapon  = getWeapon();
        double modifiedDamage = currentWeapon.getDamage() * multiplier + .5d;

        currentWeapon.setDamage((int) modifiedDamage);
        TimerUtil.schedule(effectTime, () -> {
            currentWeapon.setDamage((int) (currentWeapon.getDamage() / multiplier + .5d));
        });
    }

    public Weapon getWeapon() {
        return weapons[weaponIndex];
    }

    public ImageView getWeaponImageView() {
        return weaponImageView;
    }
}
