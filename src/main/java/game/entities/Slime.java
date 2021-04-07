package game.entities;

import core.GameEngine;
import data.RandomUtil;
import game.collidables.Collidable;
import game.collidables.CollidableTile;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

/**
 * Basic slime enemy
 */
public class Slime extends Entity {
    private int slimeType;
    private int attackDuration;

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
     */
    public Slime() {
        super(SLIME_SPRITES[0], RandomUtil.getPoint2D(300), new Point2D(5, 5));

        health = 2;
        money  = 20;

        slimeType = RandomUtil.getInt(SLIME_SPRITES.length);
        setImage(new Image(SLIME_SPRITES[slimeType]));

        entityController = new SlimeEntityController(this);

        setOnMouseClicked(this::attackedByPlayer);
    }

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
        if (isDead || Player.getPlayer().isDead()) {
            return;
        }

        // Replace with the Player calling this method directly if we move away from clicking to
        // attack
        int damage = Player.getPlayer().getWeapon().getDamage();
        damage(damage);
        bounceBack(-10, Player.getPlayer().getPosition());




    }

    /**
     * Sets the image to a dead slime
     */
    @Override
    public void onDeath() {
        setImage(new Image(SLIME_DEAD_SPRITES[slimeType]));
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof CollidableTile) {
            // Stop dead slimes from moving through walls
            double magnitude = Math.max(100d, velocity.magnitude());

            bounceBack((int) (magnitude * GameEngine.getDt()), Point2D.ZERO);
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
}
