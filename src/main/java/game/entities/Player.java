package game.entities;

import core.*;
import game.collectables.*;
import game.collidables.Collidable;
import game.inventory.*;
import game.level.Level;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import utilities.GameEffects;
import utilities.TimerUtil;
import views.GameScreen;

/**
 * Singleton-like controller for the player
 */
public final class Player extends Entity {
    private static Player player;

    private String name;
    private int    difficulty;
    private int    maxHealth;

    private double speedMultiplier = 1d;

    private Key     key;
    private boolean keyActivated;
    private boolean hasWon;

    private WeaponHolder weaponHolder;

    private static TextArea uiInfoText;

    /**
     * Initializes the Player
     *
     * @param playerName the name of the main player
     * @param weaponType the name of the weapon the player has
     * @param difficulty the difficulty of the game
     */
    public static void setPlayer(String playerName, WeaponType weaponType, String difficulty) {
        player = new Player(playerName, weaponType, difficulty);
    }

    private Player(String playerName, WeaponType weaponType, String difficulty) {
        super("Player.png", Point2D.ZERO, new Point2D(65, 70));
        name = playerName;

        weaponHolder = new WeaponHolder(2);
        weaponHolder.add(new Weapon("Starting", weaponType, 0));

        switch (difficulty) {
        case "Boring":
            money = 100;
            maxHealth = 20;
            this.difficulty = 0;
            break;
        case "Normal":
            money = 75;
            maxHealth = 10;
            this.difficulty = 1;
            break;
        case "Hard":
            money = 50;
            maxHealth = 5;
            this.difficulty = 2;
            break;
        case "Debug":
            money = 10000;
            maxHealth = 10000;
            this.difficulty = 1;
            weaponHolder.add(new Weapon("Starting", weaponType, 11));
            break;
        default:
            throw new IllegalArgumentException("Unexpected difficulty: " + difficulty);
        }
        health = maxHealth;

        entityController = new PlayerEntityController(this);
    }

    @Override
    public void update() {
        uiInfoText.setText(toStringFormatted());
        if (isDead) {
            return;
        }

        if (InputManager.getKeyDown(KeyCode.F1)) {
            maxHealth = 500;
            health    = maxHealth;

            Inventory.addItem(new AttackPotion());
            Inventory.addItem(new SpeedPotion());
        }

        if (InputManager.getKeyDown(KeyCode.F2)) {
            Level level = GameScreen.getLevel();
            level.loadRoom(level.getExit());
        }

        if (InputManager.getKeyDown(KeyCode.F3)) {
            weaponHolder.add(new Weapon("Cheat ", WeaponType.Spear, 11));
        }

        if (InputManager.get(KeyCode.F4)) {
            Inventory.addItem(new NukeItem());
        }

        if (InputManager.getKeyDown(KeyCode.F5)) {
            GameScreen.getLevel().getExit().addEntity(new Golem());
            GameScreen.getLevel().getExit().addEntity(new Golem());
        }

        if (InputManager.get(KeyCode.L)) {
            damage(health);
        }

        if (InputManager.get(KeyCode.K)) {
            handleKey();
        }

        // Display weapon image
        weaponHolder.render();

        // Used for player movement and attacking
        entityController.act();
    }

    @Override
    public void onCollision(Collidable other) {
        super.onCollision(other);

        if (other instanceof Key) {
            key = (Key) other;
        }
    }

    @Override
    public void damage(int amount) {
        super.damage(amount);
        if (isDead) {
            return;
        }

        SoundManager.playPlayerAttacked();
        getScene().getRoot().setEffect(GameEffects.RED_EDGES);
        TimerUtil.lerp(1, t -> GameEffects.RED_EDGES.setColor(Color.color(1, 0, 0, 1 - t)));
    }

    /**
     * Increases the Player's health. The Player cannot regain more health than they started out
     * with.
     *
     * @param health health to add
     */
    public void addHealth(int health) {
        this.health += health;

        if (this.health > maxHealth) {
            this.health = maxHealth;
        }
    }

    /**
     * Blurs the screen and switches to the end scene.
     */
    @Override
    protected void onDeath() {
        setRotate(90);

        // Blurs and changes color to red
        Parent root = getScene().getRoot();
        Node   room = root.getChildrenUnmodifiable().get(0);
        root.setEffect(GameEffects.DEATH);
        TimerUtil.lerp(5, t -> {
            double x = 1 - .5 * t;

            GameEffects.GAME_BLUR.setRadius(50 * t);
            GameEffects.DEATH_COLOR.setPaint(Color.color(1, x, x));
        }, () -> {
            SceneManager.loadPane(SceneManager.END);
            SoundManager.playDeath();
        });

        TimerUtil.schedule(15, () -> {
            TimerUtil.lerp(5, t -> room.setOpacity(1 - t));

            // Make sure a new game hasn't started
            if (Player.getPlayer().isDead) {
                SoundManager.playSound(false);
            }
        });
    }

    /**
     * Updates player sprite to show key
     */
    private void handleKey() {
        if (key != null) {
            setImage(ImageManager.getImage("rightPlayerWithKey.png", 100, 70, true));
            keyActivated = true;

            SoundManager.playKeyActivated();
        }
    }

    public Weapon addWeapon(Weapon weapon) {
        return weaponHolder.add(weapon);
    }

    public void addDamageMultiplier(double multiplier, double effectTime) {
        // Rounds (damage * multiplier) to the nearest int
        Weapon currentWeapon  = weaponHolder.getWeapon();
        double modifiedDamage = currentWeapon.getDamage() * multiplier + .5d;

        currentWeapon.setDamage((int) modifiedDamage);
        TimerUtil.schedule(effectTime, () -> {
            currentWeapon.setDamage((int) (currentWeapon.getDamage() / multiplier + .5d));
        });
    }

    public WeaponHolder getWeaponHolder() {
        return weaponHolder;
    }

    public void addSpeedMultiplier(double multiplier) {
        speedMultiplier *= multiplier;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public boolean isKeyActivated() {
        return keyActivated;
    }

    public boolean hasWon() {
        return hasWon;
    }

    public void setHasWon(boolean hasWon) {
        this.hasWon = hasWon;
    }

    /**
     * Returns the current difficulty [0,2]. 0 is Boring, 1 is Normal, 2 is Hard.
     *
     * @return the current difficulty [0,2]. 0 is Boring, 1 is Normal, 2 is Hard.
     */
    public int getDifficulty() {
        return difficulty;
    }

    public String toStringFormatted() {
        String formattedHealth = isDead ? "DEAD" : String.valueOf(health);
        return String.format("Name: %s \nWeapon: %s \nMoney: %d \nHealth: %s",
                             name, weaponHolder.getWeapon(), money, formattedHealth);
    }

    @Override
    public String toString() {
        return "Main Player";
    }

    /**
     * Sets TextArea uiInfoText so that the UI can be updated when the player is attacked
     *
     * @param uiInfoText TextArea that displays the information
     */
    public static void setUiInfoText(TextArea uiInfoText) {
        Player.uiInfoText = uiInfoText;
    }

    /**
     * Gets the main player instance.
     *
     * @return the main player
     */
    public static Player getPlayer() {
        return player;
    }
}
