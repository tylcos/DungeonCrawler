package game.collectables;

import core.InputManager;
import core.SoundManager;
import game.collidables.Collidable;
import game.entities.Player;
import game.level.Room;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class ChallengeShrine extends Collectable {
    
    private Room room;

    public ChallengeShrine(Room room) {
        super("debug wall.png", new Point2D(0, 0), new Point2D(64, 64));
        this.room = room;
    }

    @Override
    public void onCollision(Collidable other) {
        if (isCollected || !(other instanceof Player) || !InputManager.getKeyDown(KeyCode.E)) {
            return;
        }

        setCollected();
        room.activateChallenge();
    }

}
