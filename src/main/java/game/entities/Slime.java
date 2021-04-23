package game.entities;

import core.*;
import game.collidables.Collidable;
import game.collidables.CollidableTile;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import utilities.RandomUtil;

/**
 * Basic slime enemy
 */
public class Slime extends Entity {
    private int slimeType;

    // @formatter:off
    private static final String[] SLIME_SPRITES = {
        "enemy1.gif",
        "enemy2.gif",
        "enemy3.gif"
    };

    private static final String[] SLIME_DEAD_SPRITES = {
        "enemyDead1.png",
        "enemyDead2.png",
        "enemyDead3.png"
    };
    // @formatter:on

    /**
     * Creates an instance of Slime.
     */
    public Slime() {
        super(SLIME_SPRITES[0], RandomUtil.getPoint2D(300), new Point2D(96, 36));

        health = 2;
        money  = 20;

        slimeType = RandomUtil.getInt(SLIME_SPRITES.length);
        setImage(ImageManager.getImage(SLIME_SPRITES[slimeType], 300, 60, true));

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

        damage(1);
        bounceBack(-10, Player.getPlayer().getPosition());
    }

    /**
     * Sets the image to a dead slime
     */
    @Override
    public void onDeath() {
        setImage(ImageManager.getImage(SLIME_DEAD_SPRITES[slimeType], 300, 60, true));
        SoundManager.playEnemyKilled();
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
