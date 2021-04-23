package game.entities;

import core.GameEngine;
import core.InputManager;
import game.Inventory;
import game.Weapon;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

public class WeaponHolder {
    private Weapon[]  weapons;
    private int       weaponIndex;
    private ImageView weaponImageView;

    private double radiusOffset;
    private double angleOffset;

    private static final double WEAPON_HOLD_DISTANCE = 90d;

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

    public void setOffsets(double radiusOffset, double angleOffset) {
        this.radiusOffset = radiusOffset;
        this.angleOffset = angleOffset;
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
        weaponImageView.setRotate(adjustedAngleDeg + 45);
    }

    public Weapon getWeapon() {
        return weapons[weaponIndex];
    }

    public ImageView getWeaponImageView() {
        return weaponImageView;
    }
}
