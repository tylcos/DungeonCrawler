package game;

import core.InputManager;
import javafx.geometry.Point2D;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.util.EnumMap;

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
    // https://www.desmos.com/calculator/x9dexcgwnr
    private double inputSmooth = .2d;

    private static TextArea uiInfoText;
    private boolean onAttackMode;

    // todo: fix weapon damage and price
    public MainPlayer(String image, String weaponName, String difficulty) {
        super("/images/Player.png", new Point2D(500, 700), new Point2D(5, 5));

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
        EnumMap<KeyCode, Boolean> inputState = InputManager.getInputState();

        Point2D velocity = Point2D.ZERO;
        if (inputState.get(KeyCode.W) || inputState.get(KeyCode.UP)) {
            velocity = velocity.add(0, -1);
        }
        if (inputState.get(KeyCode.D) || inputState.get(KeyCode.RIGHT)) {
            velocity = velocity.add(1, 0);
        }
        if (inputState.get(KeyCode.S) || inputState.get(KeyCode.DOWN)) {
            velocity = velocity.add(0, 1);
        }
        if (inputState.get(KeyCode.A) || inputState.get(KeyCode.LEFT)) {
            velocity = velocity.add(-1, 0);
        }
        //player attack enemy
        if (inputState.get(KeyCode.SPACE)) {
            onAttackMode = true;
        }

        velocity = velocity.normalize().multiply(speed);
        setVelocity(getVelocity().interpolate(velocity, inputSmooth));
    }

    private void mouseClickEvent(MouseEvent event) {

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

    public boolean getIsOnAttack() {
        return onAttackMode;
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
