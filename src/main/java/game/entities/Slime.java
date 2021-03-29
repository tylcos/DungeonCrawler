package game.entities;

import data.RandomUtil;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

/**
 * Basic slime enemy
 */
public class Slime extends Entity {
    private int slimeType;

    // @formatter:off
    private static final String[] SLIME_SPRITES = {
        "/images/enemy1.gif",
        "/images/enemy2.gif",
        "/images/enemy3.gif"
    };

    private static final String[] SLIME_DEAD_SPRITES = {
        "/images/enemyDead1.png",
        "/images/enemyDead2.png",
        "/images/enemyDead3.png"
    };
    // @formatter:on

    /**
     * Creates an instance of Slime.
     *
     * @param health the amount of health the Slime has
     * @param money  the amount of money the Slime holds
     */
    public Slime(int health, int money) {
        super(SLIME_SPRITES[0],
              RandomUtil.getPoint2D(-300, 300, -300, 300),
              new Point2D(5, 5));

        this.health = health;
        this.money  = money;

        slimeType = RandomUtil.getInt(SLIME_SPRITES.length);
        setImage(new Image(SLIME_SPRITES[slimeType]));

        entityController = new SlimeEntityController(this);

        setOnMouseClicked(this::attackedByPlayer);
    }

    /**
     * Updates the enemy health and alive state.
     */
    @Override
    public void update() {
        entityController.act();
    }

    /**
     * Enemy retreat one step after mainPlayer attack enemy.
     *
     * @param event Mouse click event
     */
    public void attackedByPlayer(MouseEvent event) {
        if (isDead || MainPlayer.getPlayer().isDead()) {
            return;
        }

        changeHealth(-1);
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

            entityController.stop();

            setImage(new Image(SLIME_DEAD_SPRITES[slimeType]));
            setMouseTransparent(true); // So that you can kill other slimes beneath a dead one
        }
    }
}
