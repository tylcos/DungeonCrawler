package game;

public class MainPlayer {
    private String name;
    private Weapon weapon;
    private int money;
    private int health;


    // todo: fix weapon damage and price
    public MainPlayer(String name, String weapon, String difficulty) {
        this.name = name;
        this.weapon = new Weapon(weapon, 0, 0);

        this.money = switch (difficulty) {
        case "Boring" -> 100;
        case "Normal" -> 75;
            case "Hard" -> 50;
            default -> throw new IllegalArgumentException("Unexpected difficulty: " + difficulty);
        };

        this.health = 100;
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


    @Override
    public String toString() {
        return toStringFormatted();
    }

    public String toStringFormatted() {
        return "Name: %s \nWeapon: %s \nMoney: %d \nHealth: %d"
                .formatted(name, weapon, money, health);
    }
}
