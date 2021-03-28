package game.entities;

import data.RandomUtil;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

/**
 * The enemy/monster in the dungeon no longer used.
 */
@Deprecated
public class Enemy extends Entity {
    // Since this variable identify whether player is attacking enemy,
    // the variable will be declare in MainPlayer Class.
    // Once "attack" motion implemented in mainPlayer Class, move it.
    private boolean isPlayerAttackEnemy;
    private boolean isAttackUp;
    private boolean isAttackDown;
    private boolean isAttackLeft;
    private boolean isAttackRight;
    // CHANGE ABOVE LINE LATER INTO MainPlayer class

    private double speed       = 300d;
    // Smooths movement over around 150 frames
    private double inputSmooth = .02d;

    /**
     * Creates an instance of an enemy.
     *
     * @param health the amount of health the enemy has
     * @param money  the amount of money the enemy holds
     */
    public Enemy(int health, int money) {
        super(RandomUtil.getRandomSlime(),
              new Point2D(RandomUtil.getInt(-300, 300),
                          RandomUtil.getInt(-300, 300)),
              new Point2D(5, 5));

        this.health = health;
        this.money  = money;

        setOnMouseClicked(event -> attackedByPlayer(event));
    }

    /**
     * Updates the enemy health and alive state.
     */
    @Override
    public void update() {
        if (!isDead) {
            enemyMovement();
        }
    }

    /**
     * This method moves enemy towards the player.
     */
    public void enemyMovement() {
        Point2D enemyPosition      = getPosition();
        Point2D mainPlayerPosition = MainPlayer.getPlayer().getPosition();

        Point2D difference = mainPlayerPosition.subtract(enemyPosition);
        difference = difference.normalize().multiply(speed);

        setVelocity(getVelocity().interpolate(difference, inputSmooth));

    }

    // todo Once attack method from MainPlayer fully implemented, update this method.

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

        health--;
        if (health == 0) {
            isDead = true;
            setVelocity(Point2D.ZERO);
            setImage(new Image("images/enemyDead.gif"));
            setMouseTransparent(true); // So that you can kill other slimes beneath a dead one
        }
    }

    /**
     * Returns the health of the enemy.
     *
     * @return the health of the enemy
     */
    public int getHealth() {
        return health;
    }

    /**
     * Returns how much money the enemy has.
     *
     * @return the amount of money the enemy has
     */
    public int getMoney() {
        return money;
    }

    /**
     * Returns if the enemy is dead.
     *
     * @return true if the enemy is dead; false otherwise
     */
    public boolean isDead() {
        return isDead;
    }
}
