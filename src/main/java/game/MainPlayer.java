package game;

import core.InputManager;
import javafx.geometry.Point2D;
import javafx.scene.control.TextArea;

/**
 * Controller for the player
 */
public class MainPlayer extends Entity {
    private String name;
    private Weapon weapon;
    private int money;
    private int health;

    private double speed = 750d;

    private static TextArea uiInfoText;

    // todo: fix weapon damage and price
    public MainPlayer(String image, String weaponName, String difficulty) {
        super("/images/Player.png", new Point2D(500, 500), new Point2D(5, 5));

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

        // todo: fix movement when not pressing button
        InputManager.addKeyListener(key -> {
            Point2D input;
            switch (key.getCode()) {
            case W:
            case UP:
                input = new Point2D(0, -1);
                break;
            case D:
            case RIGHT:
                input = new Point2D(1, 0);
                break;
            case S:
            case DOWN:
                input = new Point2D(0, 1);
                break;
            case A:
            case LEFT:
                input = new Point2D(-1, 0);
                break;
            default:
                input = new Point2D(0, 0);
            }

            input = input.multiply(speed);
            setVelocity(input);
        });
    }

    @Override
    public void update(double dt) {
        uiInfoText.setText(toStringFormatted());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public static void setUiInfoText(TextArea uiInfoText) {
        MainPlayer.uiInfoText = uiInfoText;
    }

    @Override
    public String toString() {
        return toStringFormatted();
    }

    public String toStringFormatted() {
        return String.format("Name: %s \nWeapon: %s \nMoney: %d \nHealth: %d",
                name, weapon, money, health);
    }
}
