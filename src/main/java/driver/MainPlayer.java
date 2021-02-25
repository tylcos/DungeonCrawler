package driver;

public class MainPlayer {
    private String name;
    private Weapon weapon;
    private int money;
    private int hitpoints;

    public MainPlayer(String name, Weapon weapon, String difficulty) {
        this.name = name;
        this.weapon = weapon;

        switch (difficulty) {
        case "Easy":
            this.money = 100;
            break;
        case "Medium":
            this.money = 75;
            break;
        case "Hard":
            this.money = 50;
            break;
        default:
            throw new IllegalStateException("Unexpected value: " + difficulty);
        }
        this.hitpoints = 100;
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

    public int getHitpoints() {
        return hitpoints;
    }

    public void setHitpoints(int hitpoints) {
        this.hitpoints = hitpoints;
    }
}
