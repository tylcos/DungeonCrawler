package game;

import core.GameManager;
import javafx.scene.image.Image;

public class Door extends CollidableTile {
    private Room destination;

    public Door(Image image, boolean isStatic) {
        super(image, isStatic);
    }

    public Door(Image image, boolean isStatic, Room destination) {
        super(image, isStatic);

        this.destination = destination;
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof MainPlayer) {
            GameManager.getLevel().setRoom(destination);
        }
    }

    public void setDestination(Room destination) {
        this.destination = destination;
    }
}
