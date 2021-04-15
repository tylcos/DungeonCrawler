package game.collectables;

import core.SceneManager;
import core.SoundManager;
import game.IItem;
import game.Inventory;
import game.collidables.Collidable;
import game.entities.Player;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utilities.RandomUtil;
import views.GameScreen;

import java.io.FileInputStream;

/**
 * A Item that kills all enemies and possibly the Player
 */
public class NukeItem extends Collectable implements IItem {
    private static final int    ITEM_ID = 3;
    private static final String IMAGE   = "/images/Nuke.png";

    private static final double BACKFIRE_CHANCE = .01d;

    public NukeItem() {
        super(IMAGE, RandomUtil.getPoint2D(300), new Point2D(2, 2));
    }

    @Override
    public void onCollision(Collidable other) {
        if (isCollected || !(other instanceof Player)) {
            return;
        }

        SoundManager.playPotionCollected();
        setCollected();

        Inventory.addItem(this);
    }

    public void activate() {
        GameScreen.getLevel().getCurrentRoom().getEntities().forEach(e -> e.damage(e.getHealth()));
        if (RandomUtil.get() < BACKFIRE_CHANCE) {
            Player.getPlayer().damage(Player.getPlayer().getHealth());
        }
    }

    public int getItemID() {
        return ITEM_ID;
    }

    public String getItemImage() {
        return IMAGE;
    }
}
