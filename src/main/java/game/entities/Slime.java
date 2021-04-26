package game.entities;

import core.*;
import javafx.geometry.Point2D;
import utilities.RandomUtil;
import views.EndScreen;

import java.util.List;

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

        health = 5;
        money  = 20;

        slimeType = RandomUtil.getInt(SLIME_SPRITES.length);
        setImage(ImageManager.getImage(SLIME_SPRITES[slimeType], 300, 60, true));

        entityController = new SlimeEntityController(this);
    }

    @Override
    public void update() {
        entityController.act();
    }

    /**
     * Sets the image to a dead slime
     */
    @Override
    public void onDeath() {
        toBack();
        setImage(ImageManager.getImage(SLIME_DEAD_SPRITES[slimeType], 300, 60, true));
        EndScreen.addTotalKilled();
        GameEngine.removeFromPhysics(List.of(this));
        SoundManager.playEnemyKilled();
    }
}
