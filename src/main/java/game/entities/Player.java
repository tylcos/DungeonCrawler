package game.entities;

import core.*;
import game.*;
import game.collectables.Key;
import game.collidables.Collidable;
import game.collidables.CollidableTile;
import javafx.geometry.Point2D;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import utilities.*;

/**
 * Singleton-like controller for the player
 */
public final class Player extends Entity {
    private static Player player;

    private String name;
    private Weapon weapon;
    private int    difficulty;
    private int    maxHealth;

    private double speedMultiplier = 1d;

    private Key     key;
    private boolean keyActivated;

    private        ImageView heldWeapon;
    private static TextArea  uiInfoText;

    /**
     * Initializes the Player
     *
     * @param playerName the name of the main player
     * @param weaponName the name of the weapon the player has
     * @param difficulty the difficulty of the game
     */
    public static void setPlayer(String playerName, String weaponName, String difficulty) {
        player      = new Player(weaponName, difficulty);
        player.name = playerName;
    }

    private Player(String weaponName, String difficulty) {
        super("Player.png", Point2D.ZERO, new Point2D(65, 70));

        Image weaponImage = ImageManager.getSprite("weapons.png", RandomUtil.getInt(3),
                                                   RandomUtil.getInt(11), 32, 3);
        weapon = new Weapon("Starting " + weaponName, WeaponType.valueOf(weaponName), 1, 1d,
                            weaponImage);

        switch (difficulty) {
        case "Boring":
            money = 100;
            maxHealth = 100;
            this.difficulty = 0;
            break;
        case "Normal":
            money = 75;
            maxHealth = 5;
            this.difficulty = 1;
            break;
        case "Hard":
            money = 50;
            maxHealth = 3;
            this.difficulty = 2;
            break;
        case "Debug":
            money = 10000;
            maxHealth = 10000;
            this.difficulty = 1;
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

        if (InputManager.get(KeyCode.K)) {
            handleKey();
        }

        // Used for player movement and attacking
        entityController.act();

        if (heldWeapon == null || heldWeapon.getParent() == null) {
            heldWeapon = new ImageView(weapon.getImage());
            GameEngine.addToLayer(GameEngine.VFX, heldWeapon);
        }
        Weapon.setWeaponPosition(heldWeapon);
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof CollidableTile) {
            bounceBack((int) (getVelocity().magnitude() * GameEngine.getDt()));
        }

        if (other instanceof Key) {
            key = (Key) other;
        }
    }

    /**
     * Makes the entity bounce back from wall or enemy
     *
     * @param bounceDistance the distance to bounce
     */
    private void bounceBack(int bounceDistance) {
        Point2D dp = new Point2D(-bounceDistance * Math.signum(position.getX()),
                                 -bounceDistance * Math.signum(position.getY()));

        setPosition(position.add(dp));
    }

    @Override
    public void damage(int amount) {
        if (isDead) {
            return;
        }

        SoundManager.playPlayerAttacked();
        getScene().getRoot().setEffect(GameEffects.RED_EDGES);
        TimerUtil.lerp(1, t -> GameEffects.RED_EDGES.setColor(Color.color(1, 0, 0, 1 - t)));

        super.damage(amount);
    }

    /**
     * Increases the Player's health by two HP upon drinking the health potion. The Player cannot
     * regain more health than they started out with.
     */
    public void regenerate() {
        health = Math.min(health + 2, maxHealth);
    }

    /**
     * Blurs the screen and switches to the end scene.
     */
    @Override
    protected void onDeath() {
        setRotate(90); // You can rotate the image instead of changing it to PlayerDead.png

        // Blurs and changes color to red
        getScene().getRoot().setEffect(GameEffects.DEATH);
        TimerUtil.lerp(5, t -> {
            double x = 1 - .5 * t;

            GameEffects.GAME_BLUR.setRadius(20 * t);
            GameEffects.DEATH_COLOR.setPaint(Color.color(1, x, x));
        }, () -> SceneManager.loadScene(SceneManager.END));
    }

    /**
     * Updates player sprite to show key
     */
    private void handleKey() {
        if (key != null) {
            setImage(ImageManager.getImage("rightPlayerWithKey.png", 125, 80, true));
            keyActivated = true;
            SoundManager.playKeyActivated();
        }
    }

    public void changeWeapon(Weapon weapon) {
        heldWeapon.setImage(weapon.getImage());

        Inventory.changeWeapon(weapon);
    }

    public void addSpeedMultiplier(double multiplier) {
        speedMultiplier *= multiplier;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public boolean isKeyActivated() {
        return keyActivated;
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
                             name, weapon, money, formattedHealth);
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
