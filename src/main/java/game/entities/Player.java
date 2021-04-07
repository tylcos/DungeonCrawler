package game.entities;

import core.GameEngine;
import core.InputManager;
import core.SceneManager;
import core.SoundManager;
import data.GameEffects;
import data.LerpTimer;
import game.Weapon;
import game.collidables.Collidable;
import game.collidables.CollidableTile;
import game.collidables.Key;
import javafx.geometry.Point2D;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;


/**
 * Singleton controller for the player
 */
public final class Player extends Entity {
    private static Player player;

    private String name;
    private Weapon weapon;
    private Key key;
    private boolean keyActivated = false;
    private int difficulty;

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
        weapon = new Weapon(weaponName, 1, 0);

        switch (difficulty) {
        case "Boring":
            money = 100;
            health = 1000000000;
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

    private void handleKey() {
        if (key != null) {
            setImage(new Image("/images/key.png"));
            keyActivated = true;
            SoundManager.playKeyActivated();
        } else {
            System.out.println("player has not collected key");
        }
    }



    @Override
    public void update() {
        uiInfoText.setText(toStringFormatted());

        // Used for player movement and eventually attacking
        entityController.act();

        if (InputManager.get(KeyCode.K)) {
            handleKey();
        }
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
        if (other instanceof Key) {
            this.key = (Key) other;
            System.out.println("collected key");
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

    @Override
    public void damage(int amount) {
        if (isDead) {
            return;
        }
        SoundManager.playPlayerAttacked();
        getScene().getRoot().setEffect(GameEffects.RED_EDGES);
        new LerpTimer(1, t -> GameEffects.RED_EDGES.setColor(Color.color(1, 0, 0, 1 - t)));

        super.damage(amount);
    }

    /**
     * Increases the Player's health by two HP upon drinking the health potion. The Player cannot
     * regain more health than they started out with.
     */
    public void regenerate() {
        int maxHealth;
        if (difficulty == 0) {
            maxHealth = 10;
        } else if (difficulty == 2) {
            maxHealth = 3;
        } else {
            maxHealth = 5;
        }

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
        new LerpTimer(5, t -> {
            double x = 1 - .5 * t;

            GameEffects.GAME_BLUR.setRadius(20 * t);
            GameEffects.DEATH_COLOR.setPaint(Color.color(1, x, x));
        }, () -> SceneManager.loadScene(SceneManager.END));
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
