package game.collidables;

import core.GameEngine;
import core.InputManager;
import game.Weapon;
import javafx.geometry.Point2D;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import views.GameScreen;

/**
 * Singleton controller for the player
 */
public final class MainPlayer extends Entity {
    private static MainPlayer player;

    private static TextArea uiInfoText;

    private String name;
    private Weapon weapon;
    private int    money;
    private int    health;

    private double speed = 750d;

    // Smooths input over around 125ms
    // inputSmooth = 1d would remove smoothing
    // https://www.desmos.com/calculator/xjyyi5sndo
    private double  inputSmooth = .3d;
    private Point2D lastVelocity;

    private boolean onAttackMode;
    private String  difficulty;
    private int     attackTime;
    private boolean isDead;

    /**
     * Initializes the MainPlayer singleton
     *
     * @param image      the path of the image of a main player
     * @param weaponName the name of the weapon the player has
     * @param difficulty the difficulty of the game
     */
    public static void setPlayer(String image, String weaponName, String difficulty) {
        player = new MainPlayer(image, weaponName, difficulty);
    }

    private MainPlayer(String image, String weaponName, String difficulty) {
        // Position is overwritten when a new room is loaded
        super("/images/Player.png", Point2D.ZERO, new Point2D(5, 5));

        // todo: fix weapon damage and price
        name   = image;
        weapon = new Weapon(weaponName, 0, 0);
        health = 5;

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
    }

    @Override
    public void update() {
        if (health == 0){
            isDead = true;
            setImage(new Image("/images/PlayerDead.png"));
            //todo: go to game over screen
        }
        uiInfoText.setText(toStringFormatted());
        if (attackTime > 200) {
            String currentWeapon = weapon.getName();
            if ("Bow".equals(currentWeapon)) {
                swapToBow();
            }
            if ("Sword".equals(currentWeapon)) {
                swapToSword();
            }
            if ("Axe".equals(currentWeapon)) {
                swapToAxe();
            }
            attackTime = 0;
        }
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

        if (InputManager.get(KeyCode.DIGIT1)) {
            swapToBow();
        }
        if (InputManager.get(KeyCode.DIGIT2)) {
            swapToAxe();
        }
        if (InputManager.get(KeyCode.DIGIT3)) {
            swapToSword();
        }
        if (InputManager.get(KeyCode.R)) {
            switchToNoWeapon();
        }


        // Player attacks
        if (InputManager.get(KeyCode.SPACE)) {
            onAttackMode = true;
            attackMotion();
        } else {
            onAttackMode = false;
        }

        lastVelocity = getVelocity();

        // TODO 3/24/2021 Use a better frame independent way of smoothing input
        double  dt          = GameEngine.getDt();
        Point2D rawVelocity = input.normalize().multiply(speed);
        Point2D velocity = getVelocity().interpolate(rawVelocity,
                                                     inputSmooth * (60d * dt + .0007d / dt));

        double dVelocity = velocity.subtract(getVelocity()).magnitude() / speed;
        setVelocity(dVelocity < .01 ? rawVelocity : velocity);
        attackTime++;
    }

    @Override
    public void onCollision(Collidable other) {
        // todo: add collectable to collidable bodies in Room
        if (other instanceof Collectable) {
            Collectable collectable = (Collectable) other;

            collectable.setImage(new Image("images/Invisible.gif"));
            collectable.setCollected();
            collect(collectable);
        }

        // Moved this from Door class to prevent more type checking
        if (other instanceof Door) {
            GameScreen.getLevel().setRoom(((Door) other).getDestination());
        }

        if (other instanceof Wall) {
            bounceBack(other, 30);
        }

        // todo: add enemy to collidable bodies in Room
        /*if (other instanceof Enemy) {
            bounceBack(other, 1);
        }*/
    }

    /**
     * Updates players health or money based on item collected
     *
     * @param item the item collected
     */
    public void collect(Collectable item) {
        // todo: fix when coin and item are made Collectables
    }

    /**
     * makes player bounce back from wall or enemy
     *
     * @param other          the wall or enemy
     * @param bounceDistance the distance to bounce
     */
    private void bounceBack(Collidable other, int bounceDistance) {
        int dx = -bounceDistance;
        int dy = -bounceDistance;
        if (getTranslateX() > other.getParent().getBoundsInParent().getCenterX()) {
            dx *= -1;
        }
        if (getTranslateY() > other.getParent().getBoundsInParent().getCenterY()) {
            dy *= -1;
        }

        setPosition(getPosition().add(new Point2D(dx, dy)));
        /* Tyler test
        Bounds  otherBounds   = other.localToScene(other.getBoundsInLocal());
        Point2D otherPosition = new Point2D(otherBounds.getCenterX(), otherBounds.getCenterY());

        // Vector from player to other
        Point2D playerToOther = ScreenManager.screenToGame(otherPosition).subtract(getPosition());
        System.out.println(ScreenManager.screenToGame(otherPosition));
        System.out.println(getPosition());
        System.out.println(playerToOther);

        Point2D dp = new Point2D(-bounceDistance * Math.signum(playerToOther.getX()),
                                 bounceDistance * Math.signum(playerToOther.getY()));
        setPosition(getPosition().add(dp));*/
    }

    /**
     * change weapon to Bow
     */
    public void swapToBow() {

        Image Bow = new Image("images/PlayerBow2.gif");
        setNewImage(Bow);
        //change damage and price later
        Weapon newBow = new Weapon("Bow", 0, 0); //TODO fix damage and price later
        weapon = newBow;
    }

    /**
     * change weapon to Axe
     */
    public void swapToAxe() {
        Image Axe = new Image("images/PlayerAxe.gif");
        setNewImage(Axe);
        //change damage and price later
        Weapon newBow = new Weapon("Axe", 0, 0); //ToDO fix damage and price later
        weapon = newBow;
    }

    /**
     * change to weapon to sword
     */
    public void swapToSword() {
        Image Sword = new Image("images/PlayerSwordAttack.png");
        setNewImage(Sword);
        //change damage and price later
        Weapon newBow = new Weapon("Sword", 0, 0); //ToDo fix damge and Price later
        weapon = newBow;
    }

    public void switchToNoWeapon() {
        Image png = new Image("images/Player.png");
        MainPlayer.getPlayer().setNewImage(png);
        weapon = new Weapon("weaponName", 0, 0);
    }

    /*
     * Active AttackMotion
     */
    public void attackMotion() {
        Weapon currentWeapon = weapon;
        switch (currentWeapon.getName()) {
        case "Bow":
            Image BowAttack = new Image("images/PlayerBowAttack.gif");
            MainPlayer.getPlayer().setNewImage(BowAttack);
            break;
        case "Axe":
            Image swordAttack = new Image("images/PlayerAxeAttack.gif");
            MainPlayer.getPlayer().setNewImage(swordAttack);
            break;
        case "Sword":
            Image AxeAttack = new Image("images/PlayerSwordAttack.png");
            MainPlayer.getPlayer().setNewImage(AxeAttack);
            break;
        }
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
     * Returns the player's balance
     *
     * @return the player's balance
     */
    public int getMoney() {
        return money;
    }

    /**
     * Sets the player's balance
     *
     * @param money the new balance
     */
    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * Adds money to the player's balance
     *
     * @param money the amount of money to add
     */
    public void addMoney(int money) {
        this.money += money;
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
     * Gets the main player.
     *
     * @return the main player
     */
    public static MainPlayer getPlayer() {
        return player;
    }

    public String toStringFormatted() {
        return String.format("Name: %s \nWeapon: %s \nMoney: %d \nHealth: %d",
                             name, weapon, money, health);
    }

    @Override
    public String toString() {
        return "Main Player";
    }
}
