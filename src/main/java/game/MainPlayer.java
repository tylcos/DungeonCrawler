package game;

import core.InputManager;
import javafx.geometry.Point2D;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

/**
 * Controller for the player
 */
public class MainPlayer extends Entity {
    private String name;
    private Weapon weapon;
    private int money;
    private int health;

    private double speed = 750d;

    // Smooths input over around 15 frames
    // inputSmooth = 1d would remove smoothing
    // https://www.desmos.com/calculator/x9dexcgwnr
    private double inputSmooth = .2d;

    private static TextArea uiInfoText;
    private boolean onAttackMode;

    // todo: fix weapon damage and price
    /**
     * Creates an instance of the MainPlayer
     * @param image the path of the image of a main player
     * @param weaponName the name of the weapon the player has
     * @param difficulty the difficulty of the game
     */
    public MainPlayer(String image, String weaponName, String difficulty) {
        super("/images/Player.png", new Point2D(1000, 1000), new Point2D(5, 5));

        name = image;
        weapon = new Weapon(weaponName, 0, 0);
        health = 100;

        switch (difficulty) {
        case "Boring":
            money = 100;
            break;
        case "Normal":
            money = 75;
            break;
        case "Hard":
            money = 50;
            break;
        default:
            throw new IllegalArgumentException("Unexpected difficulty: " + difficulty);
        }

        InputManager.addMouseClickListener(this::mouseClickEvent);
    }

    @Override
    public void update(double dt) {
        uiInfoText.setText(toStringFormatted());

        // User input logic
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

        // player attacks enemy
        if (InputManager.get(KeyCode.SPACE)) {
            onAttackMode = true;
        }

        input = input.normalize().multiply(speed);
        setVelocity(getVelocity().interpolate(input, inputSmooth));
    }

    /**
     * When player clicks, the player attacks the enemy.
     *
     * @param event the mouse event
     */
    private void mouseClickEvent(MouseEvent event) {
        // todo implement
    }

    /**
     * Returns the name of the player.
     *
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the player to a new name.
     *
     * @param name the new name of the player
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the weapon the player has.
     *
     * @return the weapon the player has
     */
    public Weapon getWeapon() {
        return weapon;
    }

    /**
     * Sets the player's weapon to a new weapon.
     *
     * @param weapon the new weapon for the player to have
     */
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    /**
     * Returns the amount of money the player has.
     *
     * @return the amount of money the player has
     */
    public int getMoney() {
        return money;
    }

    /**
     * Sets the amount of money the player has to a new amount.
     *
     * @param money the new amount of money the player has
     */
    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * Returns the health the player has.
     *
     * @return the player's health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets the player's health to a new amount.
     *
     * @param health the new amount of health the player has
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Returns if the player is attacking.
     *
     * @return true if the player is attacking; false otherwise
     */
    public boolean getIsOnAttack() {
        return onAttackMode;
    }

    /**
     * Sets the UI information text of the main player.
     *
     * @param uiInfoText the information text of the main player
     */
    public static void setUiInfoText(TextArea uiInfoText) {
        MainPlayer.uiInfoText = uiInfoText;
    }

    /**
     * Formats the information of the player.
     *
     * @return the information of the player
     */
    public String toStringFormatted() {
        return String.format("Name: %s \nWeapon: %s \nMoney: %d \nHealth: %d",
                name, weapon, money, health);
    }

    @Override
    public String toString() {
        return toStringFormatted();
    }
}
