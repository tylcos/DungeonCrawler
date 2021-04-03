package game.entities;

import core.GameEngine;
import core.SceneManager;
import data.GameEffects;
import data.LerpTimer;
import game.Weapon;
import game.collidables.Collidable;
import game.collidables.CollidableTile;
import javafx.geometry.Point2D;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Singleton controller for the player
 */
public final class Player extends Entity {
    private static Player player;

    private String name;
    private Weapon weapon;
    private int difficulty;

    private boolean onAttackMode;
    private int     attackTime;

    private static TextArea uiInfoText;

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
        super("/images/Player.png", Point2D.ZERO, new Point2D(.7, .7));

        // todo: fix weapon damage and price
        name   = image;
        weapon = new Weapon(weaponName, 0, 0);

        switch (difficulty) {
        case "Boring":
            money = 100;
            health = 10;
            this.difficulty = 0;
            break;
        case "Normal":
            money = 75;
            health = 5;
            this.difficulty = 1;
            break;
        case "Hard":
            money = 50;
            health = 3;
            this.difficulty = 2;
            break;
        default:
            throw new IllegalArgumentException("Unexpected difficulty: " + difficulty);
        }

        entityController = new PlayerEntityController(this);
    }

    @Override
    public void update() {
        uiInfoText.setText(toStringFormatted());

        // Used for player movement and eventually attacking
        entityController.act();

        /*
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

        attackTime++;*/
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof CollidableTile) {
            bounceBack((int) (getVelocity().magnitude() * GameEngine.getDt()), Point2D.ZERO);
        }
    }

    /**
     * Makes the entity bounce back from wall or enemy
     *
     * @param bounceDistance the distance to bounce
     * @param fromPoint      the point to bounce back from
     */
    private void bounceBack(int bounceDistance, Point2D fromPoint) {
        Point2D difference = position.subtract(fromPoint);

        Point2D dp = new Point2D(-bounceDistance * Math.signum(difference.getX()),
                                 -bounceDistance * Math.signum(difference.getY()));

        setPosition(position.add(dp));
    }

    /**
     * change weapon to Bow
     */
    public void swapToBow() {
        Image bow = new Image("images/PlayerBow2.gif");
        setImage(bow);
        weapon = new Weapon("Bow", 0, 0);
    }

    /**
     * change weapon to Axe
     */
    public void swapToAxe() {
        Image axe = new Image("images/PlayerAxe.gif");
        setImage(axe);
        weapon = new Weapon("Axe", 0, 0);
    }

    /**
     * change to weapon to sword
     */
    public void swapToSword() {
        Image sword = new Image("images/PlayerSwordAttack.png");
        setImage(sword);
        weapon = new Weapon("Sword", 0, 0);
    }

    public void switchToNoWeapon() {
        Image png = new Image("images/Player.png");
        Player.getPlayer().setImage(png);
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

        Player.getPlayer().setImage(attack);
    }

    @Override
    public void damage(int amount) {
        if (isDead) {
            return;
        }

        getScene().getRoot().setEffect(GameEffects.RED_EDGES);
        new LerpTimer(1, t -> GameEffects.RED_EDGES.setColor(Color.color(1, 0, 0, 1 - t)));

        super.damage(amount);
    }

    /**
     * Blurs the screen and switches to the end scene.
     */
    @Override
    protected void onDeath() {
        setRotate(90); // You can rotate the image instead of changing it to PlayerDead.png

        // Blurs and changes color to red
        getScene().getRoot().setEffect(GameEffects.DEATH);
        new LerpTimer(5, t -> {
            double x = 1 - .5 * t;

            GameEffects.GAME_BLUR.setRadius(20 * t);
            GameEffects.DEATH_COLOR.setPaint(Color.color(1, x, x));
        }, () -> SceneManager.loadScene(SceneManager.END));
    }

    /**
     * Returns if the player is attacking.
     *
     * @return true if the player is attacking; false otherwise
     */
    public boolean getIsOnAttack() {
        return onAttackMode;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    /**
     * Returns the current difficulty [0,2]. 0 is Boring, 1 is Normal, 2 is Hard.
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
