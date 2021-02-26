package driver;

public class MainPlayer {
    private String name;
    private Weapon weapon;
    private int money;
    private int hitpoints;


    // TODO: fix weapon damage and price
    public MainPlayer(String name, String weapon, String difficulty) {
        this.name = name;
        this.weapon = new Weapon(weapon, 0, 0);

        switch (difficulty) {
            case "Boring" -> this.money = 100;
            case "Normal" -> this.money = 75;
            case "Hard"   -> this.money = 50;
            default -> throw new IllegalStateException("Unexpected value: " + difficulty);
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
