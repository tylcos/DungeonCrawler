package game.entities;

import data.RandomUtil;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

/**
 * Basic slime enemy
 */
public class Slime extends Entity {
    /**
     * Creates an instance of Slime.
     *
     * @param health the amount of health the Slime has
     * @param money  the amount of money the Slime holds
     */
    public Slime(int health, int money) {
        super(RandomUtil.getRandomSlime(),
              RandomUtil.getPoint2D(-300, 300, -300, 300),
              new Point2D(5, 5));

        this.health = health;
        this.money  = money;

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

        //todo: find a better way of doing this
        if (RandomUtil.get() < .2d) {
            MainPlayer.getPlayer().changeHealth(-1);
        }

        setHealth(health - 1);
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

            setImage(new Image("images/enemyDead.gif"));
            setMouseTransparent(true); // So that you can kill other slimes beneath a dead one
        }
    }
}
