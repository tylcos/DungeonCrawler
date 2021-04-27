package game.collectables;

import core.*;
import game.collidables.Collidable;
import game.entities.Golem;
import game.entities.Player;
import game.inventory.IItem;
import game.inventory.Inventory;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import utilities.RandomUtil;
import utilities.TimerUtil;
import views.EndScreen;
import views.GameScreen;

/**
 * A Item that kills all enemies and possibly the Player
 */
public class NukeItem extends Collectable implements IItem {
    private static final int ITEM_ID = 3;
    private static final String IMAGE = "Nuke.png";

    private static final double BACKFIRE_CHANCE = .01d;

    public NukeItem() {
        super(IMAGE, RandomUtil.getPoint2D(300), new Point2D(64, 64));
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
        SoundManager.playNuke();

        GameScreen.getLevel().getCurrentRoom().getEntities().forEach(e -> {
            // Jank
            if (!(e instanceof Golem)) {
                e.damage(e.getHealth());
            }
        });

        if (RandomUtil.get() < BACKFIRE_CHANCE) {
            Player.getPlayer().damage(Player.getPlayer().getHealth());
        }
        EndScreen.addTotalNukesUsed();

        ImageView blast = new ImageView(ImageManager.getImage("nukeImage.gif"));
        blast.setScaleX(ScreenManager.getScale());
        blast.setScaleY(ScreenManager.getScale());
        blast.setTranslateX(RandomUtil.get(-200, 200));
        blast.setTranslateY(RandomUtil.get(-200, 200));

        GameEngine.addToLayer(GameEngine.VFX, blast);
        TimerUtil.lerp(5d, t -> blast.setOpacity(1 - t),
                () -> GameEngine.removeFromLayer(GameEngine.VFX, blast));
    }

    public int getItemID() {
        return ITEM_ID;
    }

    public String getItemImage() {
        return IMAGE;
    }
}
