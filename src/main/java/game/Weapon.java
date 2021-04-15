package game;

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
