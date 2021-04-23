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
    private ImageView weaponImage;

    private double radiusOffset;
    private double angleOffset;

    private static final double WEAPON_HOLD_DISTANCE = 90d;

    public WeaponHolder(int size) {
        weapons = new Weapon[size];
    }

    public Weapon add(Weapon weapon) {
        for (int i = 0; i < weapons.length; i++) {
            if (weapons[i] == null) {
                weapons[i] = weapon;
                changeWeapon(i);

                return null;
            }
        }

        Weapon current = getWeapon();
        weapons[weaponIndex] = weapon;
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
        weaponIndex = index;

        if (weaponImage != null) {
            weaponImage.setImage(weapons[index].getImage());
            Inventory.changeWeapon(weapons[index]);
        }
    }

    public void render() {
        if (weaponImage == null || weaponImage.getParent() == null) {
            weaponImage = new ImageView(getWeapon().getImage());
            GameEngine.addToLayer(GameEngine.VFX, weaponImage);
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

        weaponImage.setTranslateX(weaponPosition.getX());
        weaponImage.setTranslateY(weaponPosition.getY());
        weaponImage.setRotate(adjustedAngleDeg + 45);
    }

    public Weapon getWeapon() {
        return weapons[weaponIndex];
    }
}
