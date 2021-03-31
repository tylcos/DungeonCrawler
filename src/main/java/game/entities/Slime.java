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
     */
    public Slime() {
        super(SLIME_SPRITES[0],
              RandomUtil.getPoint2D(-300, 300, -300, 300),
              new Point2D(5, 5));

        health = 3;
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

        damage(1);
    }

    /**
     * Sets the image to a dead slime
     */
    @Override
    public void onDeath() {
        setImage(new Image(SLIME_DEAD_SPRITES[slimeType]));
    }
}
