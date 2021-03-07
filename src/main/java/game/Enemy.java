package game;

import core.GameManager;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Enemy extends Entity {
    MainPlayer mainPlayer;
    int health;
    int money;
    String image = "resources/images/enemy1.gif";
    //Since this varible identify whther play is attacking enemeny, the variable will be delcare in Mainplayer Class.
    boolean isPlayerAttackEnemy; // Once "attack" motion implemented in mainPlayer Class, move it.
    boolean isAttackUp;
    boolean isAttackDown;
    boolean isAttackLeft;
    boolean isAttackRight;
    //CHANGE ABOVE LINE LATER INTO MainPlayer class
    boolean isDead;
    private double speed = 200d;
    // Smooths input over around 15 frames
    // https://www.desmos.com/calculator/x9dexcgwnr
    private double inputSmooth = .2d;

    public Enemy(int health, int money) {
        super("/images/enemy1.gif", new Point2D(300, 300), new Point2D(5, 5));
        this.mainPlayer = GameManager.getPlayer();
        this.health = health;
        this.money = money;
        //Chage this later
        this.isPlayerAttackEnemy = false;
        //Chnage this later
        this.isDead = false;
    }

    @Override
    public void update(double dt) {
        if (health < 0) {
            isDead = true;
        }
        if (!GameManager.getPlayer().getIsOnAttack() && isDead == false) {
            EnemyMovement();
        }
        if (isPlayerAttackEnemy && isDead == false) {
            playerAttackEnemy(mainPlayer);
        }
    }
    /**
     * this method moves enemy towards the player.
     *
     * @return position of the enemy for every clock cycle.
     */
    public void EnemyMovement() {
        Point2D enemyPosition = this.getPosition();
        Point2D mainPlayerPosition = this.mainPlayer.getPosition();
        Point2D difference = mainPlayerPosition.subtract(enemyPosition);
        difference = difference.normalize().multiply(speed);
        setVelocity(getVelocity().interpolate(difference, inputSmooth));
    }

    //TODO: Once attack method from mainplayer fully implemented, update this method.
    /**
     * Enemy retreat one step after mainPlayer attack enemy
     *
     * @param mainPlayer
     * @return position after being attacked.
     */
    public void playerAttackEnemy(MainPlayer mainPlayer) {
        Point2D enemyPosition = this.getPosition();
        Point2D mainPlayerPosition = this.mainPlayer.getPosition();

        Point2D difference = enemyPosition.subtract(mainPlayerPosition);
        //if enemy was attacked by the player
        if (difference.equals(new Point2D(1,1)) && GameManager.getPlayer().getIsOnAttack()) {
            difference = difference.normalize().multiply(speed);
            setVelocity(getVelocity().interpolate(difference, inputSmooth));
        }
        health--;
        if(health <= 0){
            isDead = true;
            this.setImage(new Image("images/enemyDead.gif"));
        }

    }
    public int getHealth() {
        return health;
    }

    public int getMoney() {
        return money;
    }

    public boolean isPlayerAttackEnemy() {
        return isPlayerAttackEnemy;
    }

    public boolean isDead() {
        return isDead;
    }
}
