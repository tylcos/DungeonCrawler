package game.entities;

import core.*;
import game.Weapon;
import game.collidables.Collidable;
import game.collidables.Wall;
import javafx.geometry.Point2D;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

/**
 * Singleton controller for the player
 */
public final class MainPlayer extends Entity {
    private static MainPlayer player;
    private        String     name;
    private        Weapon     weapon;
    private        String     difficulty;

    private boolean onAttackMode;
    private int     attackTime;

    private static TextArea uiInfoText;

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

        switch (difficulty) {
        case "Boring":
            money = 100;
            health = 5;
            break;
        case "Normal":
            money = 75;
            health = 4;
            break;
        case "Hard":
            money = 50;
            health = 3;
            break;
        default:
            throw new IllegalArgumentException("Unexpected difficulty: " + difficulty);
        }

        entityController = new MainPlayerEntityController(this);
    }

    @Override
    public void update() {
        uiInfoText.setText(toStringFormatted());
        if (isDead) {
            // Used for fading out when you die
            setOpacity(getOpacity() - .2d * GameEngine.getDt());
            if (getOpacity() <= 0) {
                SceneManager.loadScene(SceneManager.END);
            }

            return;
        }

        // Used for player movement and eventually attacking
        entityController.act();

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

        // Player attacks
        if (InputManager.get(KeyCode.SPACE)) {
            onAttackMode = true;
            attackMotion();
        } else {
            onAttackMode = false;
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

        attackTime++;
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Wall) {
            bounceBack(other, 30);
        }
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
        Image bow = new Image("images/PlayerBow2.gif");
        setNewImage(bow);
        weapon = new Weapon("Bow", 0, 0);
    }

    /**
     * change weapon to Axe
     */
    public void swapToAxe() {
        Image axe = new Image("images/PlayerAxe.gif");
        setNewImage(axe);
        weapon = new Weapon("Axe", 0, 0);
    }

    /**
     * change to weapon to sword
     */
    public void swapToSword() {
        Image sword = new Image("images/PlayerSwordAttack.png");
        setNewImage(sword);
        weapon = new Weapon("Sword", 0, 0);
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
        Image attack;
        switch (weapon.getName()) {
        case "Bow":
            attack = new Image("images/PlayerBowAttack.gif");
            break;
        case "Axe":
            attack = new Image("images/PlayerAxeAttack.gif");
            break;
        case "Sword":
            attack = new Image("images/PlayerSwordAttack.png");
            break;
        default:
            throw new IllegalStateException("Unexpected weapon: " + weapon.getName());
        }

        MainPlayer.getPlayer().setNewImage(attack);
    }

    /**
     * Sets the player's health to a new amount.
     *
     * @param health the new amount of health the player has
     */
    @Override
    public void setHealth(int health) {
        this.health = health;

        if (health == 0) {
            isDead = true;
            setRotate(90); // You can rotate the image instead of changing it to PlayerDead.png

            entityController.stop();
        }
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
        String formattedHealth = isDead ? "DEAD" : String.valueOf(health);
        return String.format("Name: %s \nWeapon: %s \nMoney: %d \nHealth: %s",
                             name, weapon, money, formattedHealth);
    }

    @Override
    public String toString() {
        return "Main Player";
    }

    public Weapon getWeapon() {
        return weapon;
    }
}
