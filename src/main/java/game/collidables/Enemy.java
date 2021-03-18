package game.collidables;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

/**
 * The enemy/monster in the dungeon.
 */
public class Enemy extends Entity {
    private static final String IMAGE = "resources/images/enemy1.gif";

    private MainPlayer mainPlayer;
    private int        health;
    private int        money;

    // Since this variable identify whether player is attacking enemy,
    // the variable will be declare in MainPlayer Class.
    // Once "attack" motion implemented in mainPlayer Class, move it.
    private boolean isPlayerAttackEnemy;
    private boolean isAttackUp;
    private boolean isAttackDown;
    private boolean isAttackLeft;
    private boolean isAttackRight;
    // CHANGE ABOVE LINE LATER INTO MainPlayer class

    private boolean isDead;
    private double  speed = 300d;

    // Smooths movement over around 150 frames
    private double inputSmooth = .02d;

    /**
     * Creates an instance of an enemy.
     *
     * @param health the amount of health the enemy has
     * @param money  the amount of money the enemy holds
     */
    public Enemy(int health, int money) {
        super("/images/enemy1.gif", new Point2D(0, 0), new Point2D(5, 5));

        this.health = health;
        this.money  = money;

        // todo change this later
        isPlayerAttackEnemy = false;

        // todo change this later
        isDead = false;
    }

    /**
     * Updates the enemy health and alive state.
     *
     * @param dt the time
     */
    @Override
    public void update(double dt) {
        mainPlayer = MainPlayer.getPlayer();
        if (mainPlayer == null) {
            return;
        }

        if (health < 0) {
            isDead = true;
        }

        if (!MainPlayer.getPlayer().getIsOnAttack() && !isDead) {
            enemyMovement();
        }

        if (isPlayerAttackEnemy && !isDead) {
            playerAttackEnemy(mainPlayer);
        }
    }

    /**
     * This method moves enemy towards the player.
     */
    public void enemyMovement() {
        Point2D enemyPosition      = getPosition();
        Point2D mainPlayerPosition = mainPlayer.getPosition();

        Point2D difference = mainPlayerPosition.subtract(enemyPosition);
        difference = difference.normalize().multiply(speed);

        setVelocity(getVelocity().interpolate(difference, inputSmooth));
    }

    // todo Once attack method from MainPlayer fully implemented, update this method.

    /**
     * Enemy retreat one step after mainPlayer attack enemy.
     *
     * @param mainPlayer the main player
     */
    public void playerAttackEnemy(MainPlayer mainPlayer) {
        Point2D enemyPosition      = getPosition();
        Point2D mainPlayerPosition = this.mainPlayer.getPosition();

        Point2D difference = enemyPosition.subtract(mainPlayerPosition);
        // if enemy was attacked by the player
        if (difference.equals(new Point2D(1, 1)) && MainPlayer.getPlayer().getIsOnAttack()) {
            difference = difference.normalize().multiply(speed);
            setVelocity(getVelocity().interpolate(difference, inputSmooth));
        }
        health--;

        if (health <= 0) {
            isDead = true;
            setImage(new Image("images/enemyDead.gif"));
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
     * Returns if the player is attacking the enemy.
     *
     * @return true if the player is attacking the enemy; false otherwise
     */
    public boolean isPlayerAttackEnemy() {
        return isPlayerAttackEnemy;
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
