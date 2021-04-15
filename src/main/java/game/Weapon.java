package game;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Stores weapon info and utility methods
 */
public class Weapon {
    private String name;
    private int    damage;
    private int    price;

    /**
     * Creates an instance of a weapon with the name, damage it does, and its price.
     *
     * @param name   the name of the weapon
     * @param damage the damage the weapon does
     * @param price  the price of the weapon
     */
    public Weapon(String name, int damage, int price) {
        this.name   = name;
        this.damage = damage;
        this.price  = price;
    }

    /**
     * Displays the weapon on the screen
     *
     * @return the StackPane of the weapon
     */
    public StackPane getWeaponDisplay() {
        StackPane weaponDisplay = new StackPane();
        Circle    weapon        = new Circle();
        weapon.setFill(Color.BLUE);
        weapon.setRadius(35);
        Text exitText = new Text("Weapon: " + name + "\ndamage: " + damage + "\nprice: " + price);
        exitText.setFill(Color.YELLOW);
        exitText.setFont(new Font(12));
        weaponDisplay.getChildren().addAll(weapon, exitText);
        return weaponDisplay;
    }

    //    /**
    //     * change weapon to Bow
    //     */
    //    public void swapToBow() {
    //        Image bow = new Image("images/PlayerBow2.gif");
    //        setImage(bow);
    //        weapon = new Weapon("Bow", 0, 0);
    //    }
    //
    //    /**
    //     * change weapon to Axe
    //     */
    //    public void swapToAxe() {
    //        Image axe = new Image("images/PlayerAxe.gif");
    //        setImage(axe);
    //        weapon = new Weapon("Axe", 0, 0);
    //    }
    //
    //    /**
    //     * change to weapon to sword
    //     */
    //    public void swapToSword() {
    //        Image sword = new Image("images/PlayerSwordAttack.png");
    //        setImage(sword);
    //        weapon = new Weapon("Sword", 0, 0);
    //    }
    //
    //    public void switchToNoWeapon() {
    //        Image png = new Image("images/Player.png");
    //        Player.getPlayer().setImage(png);
    //        weapon = new Weapon("weaponName", 0, 0);
    //    }
    //
    //    /*
    //     * Active AttackMotion
    //     */
    //    public void attackMotion() {
    //        Image attack;
    //        switch (weapon.getName()) {
    //        case "Bow":
    //            attack = new Image("images/PlayerBowAttack.gif");
    //            break;
    //        case "Axe":
    //            attack = new Image("images/PlayerAxeAttack.gif");
    //            break;
    //        case "Sword":
    //            attack = new Image("images/PlayerSwordAttack.png");
    //            break;
    //        default:
    //            throw new IllegalStateException("Unexpected weapon: " + weapon.getName());
    //        }
    //
    //        Player.getPlayer().setImage(attack);
    //    }

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

    /**
     * Returns the price of the weapon.
     *
     * @return the price of the weapon
     */
    public int getPrice() {
        return price;
    }

    /**
     * Sets the price of the weapon to a new price.
     *
     * @param price the new price of the weapon
     */
    public void setPrice(int price) {
        this.price = price;
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
}
