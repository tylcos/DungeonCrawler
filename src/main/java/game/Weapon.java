package game;

import core.InputManager;
import game.entities.Player;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Stores weapon info and utility methods
 */
public class Weapon {
    private String     name;
    private WeaponType type;
    private int        damage;
    private double     fireRate;

    private Image image;

    private static final double HOLD_DISTANCE = 90d;

    /**
     * Creates an instance of a weapon.
     *
     * @param name     the name
     * @param type     the type
     * @param damage   the damage done on attack
     * @param fireRate the fire rate
     * @param image    the image
     */
    public Weapon(String name, WeaponType type, int damage, double fireRate, Image image) {
        this.name     = name;
        this.type     = type;
        this.damage   = damage;
        this.fireRate = fireRate;

        this.image = image;
    }

    /**
     * Returns the name of the weapon.
     *
     * @return the name of the weapon
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the weapon to a new name.
     *
     * @param name the new name of the weapon
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The type of the weapon like ranged or sword.
     *
     * @return the type of the weapon
     */
    public WeaponType getType() {
        return type;
    }

    /**
     * Returns how much damage the weapon does.
     *
     * @return the amount of damage the weapon does
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Sets the damage of the weapon to a new amount.
     *
     * @param damage the new amount of damage the weapon does
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void addDamageMultiplier(double multiplier) {
        // Rounds (damage * multiplier) to the nearest int
        double modifiedDamage = damage * multiplier + .5d;

        damage = modifiedDamage < Integer.MAX_VALUE ? (int) modifiedDamage : Integer.MAX_VALUE;
    }

    public double getFireRate() {
        return fireRate;
    }

    public Image getImage() {
        return image;
    }

    /**
     * The String output of the weapon.
     *
     * @return the String display of the weapon
     */
    public String toStringFormatted() {
        return String.format("%s, %d damage", name, damage);
    }

    @Override
    public String toString() {
        return toStringFormatted();
    }

    public static void setWeaponPosition(ImageView heldWeapon) {
        Point2D playerPosition  = Player.getPlayer().getPosition();
        Point2D mousePosition   = InputManager.getMousePosition();
        Point2D facingDirection = mousePosition.subtract(playerPosition).normalize();
        Point2D weaponPosition  = playerPosition.add(facingDirection.multiply(HOLD_DISTANCE));
        double angle = Math.toDegrees(Math.atan2(facingDirection.getY(), facingDirection.getX()));

        heldWeapon.setTranslateX(weaponPosition.getX());
        heldWeapon.setTranslateY(weaponPosition.getY());
        heldWeapon.setRotate(angle + 45);
    }
}
