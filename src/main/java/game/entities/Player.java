package game.entities;

import core.*;
import game.Inventory;
import game.Weapon;
import game.collectables.Key;
import game.collidables.Collidable;
import game.collidables.CollidableTile;
import javafx.geometry.Point2D;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import utilities.GameEffects;
import utilities.TimerUtil;

/**
 * Singleton controller for the player
 */
public final class Player extends Entity {
    private static Player player;
    private String name;
    private Weapon weapon;
    private int difficulty;
    private int maxHealth;

    private double speedMultiplier = 1d;

    private Key key;
    private boolean keyActivated;
    private boolean pressOnce = false;

    private static TextArea uiInfoText;
    private boolean weaponObtained;

    /**
     * Initializes the Player singleton
     *
     * @param image      the path of the image of a main player
     * @param weaponName the name of the weapon the player has
     * @param difficulty the difficulty of the game
     */
    public static void setPlayer(String image, String weaponName, String difficulty) {
        player = new Player(image, weaponName, difficulty);
    }

    private Player(String image, String weaponName, String difficulty) {
        // Position is overwritten when a new room is loaded
        super("images/PlayerSwordAttack.png", Point2D.ZERO, new Point2D(5, 5));

        // todo: fix weapon damage and price
        name = image;
        weapon = new Weapon(weaponName, 1, 0);

        switch (difficulty) {
            case "Boring":
                money = 100;
                maxHealth = 10;
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

        entityController = new PlayerEntityController(this, this::getSpeedMultiplier);
    }

    @Override
    public void update() {
        uiInfoText.setText(toStringFormatted());

        if (InputManager.get(KeyCode.K)) {
            handleKey();
        }
        if (InputManager.get(KeyCode.TAB) && weaponObtained == true){
            swapToAxe();
            Inventory.changeWeapon("images/PlayerAxe.png");
        }
        if(InputManager.get(KeyCode.Q) && weaponObtained == true){
            swapToSword();
            Inventory.changeWeapon("images/PlayerSwordAttack.png");
        }
        // Used for player movement and eventually attacking
        entityController.act();
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof CollidableTile) {
            bounceBack((int) (getVelocity().magnitude() * GameEngine.getDt()));
        }

        if (other instanceof Key) {
            key = (Key) other;
            System.out.println("collected key");
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
            setImage(new Image("/images/PlayerWithKey (2).png"));
            keyActivated = true;
            SoundManager.playKeyActivated();
        }
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

    /**
     * change weapon to Axe
     */
    public void swapToAxe() {
        Image axe = new Image("images/PlayerAxe.png");
        setImage(axe);
        weapon = new Weapon("Axe", 1, 0);
        Point2D newScale = new Point2D(5, 5);
        super.setScale(newScale);
    }

    /**
     * change to weapon to sword
     */
    public void swapToSword() {
        Image sword = new Image("images/PlayerSwordAttack.png");
        setImage(sword);
        weapon = new Weapon("Sword", 2, 0);
        Point2D newScale = new Point2D(5, 5);
        super.setScale(newScale);
    }
    public void setWeaponObtained(){ this.weaponObtained = true; }
}
