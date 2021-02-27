package game;

import core.InputManager;
import javafx.scene.control.TextArea;

import javax.vecmath.Vector2d;

public class MainPlayer extends Entity {
    private String name;
    private Weapon weapon;
    private int money;
    private int health;


    private final double speed = 750d;


    private static TextArea uiInfoText;


    // todo: fix weapon damage and price
    public MainPlayer(String name, String weapon, String difficulty) {
        super("/images/Player.png", new Vector2d(500, 500), new Vector2d(5, 5));


        this.name = name;
        this.weapon = new Weapon(weapon, 0, 0);
        this.health = 100;

        switch (difficulty) {
        case "Boring":
            this.money = 100;
            break;
        case "Normal":
            this.money = 75;
            break;
        case "Hard":
            this.money = 50;
            break;
        default:
            throw new IllegalArgumentException("Unexpected difficulty: " + difficulty);
        }


        InputManager.addKeyListener(key -> {
            // Cant use switch expression because of Checkstyle bug
            // https://github.com/checkstyle/checkstyle/issues/9302
            Vector2d input;
            switch (key.getCode()) {
            case W, UP:
                input = new Vector2d(0, -1);
                break;
            case D, RIGHT:
                input = new Vector2d(1, 0);
                break;
            case S, DOWN:
                input = new Vector2d(0, 1);
                break;
            case A, LEFT:
                input = new Vector2d(-1, 0);
                break;
            default:
                input = new Vector2d(0, 0);
            }

            input.scale(speed);
            this.setVelocity(input);
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
        return "Name: %s \nWeapon: %s \nMoney: %d \nHealth: %d"
                .formatted(name, weapon, money, health);
    }
}
