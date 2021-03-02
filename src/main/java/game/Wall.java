package game;

import javafx.scene.image.Image;

public class Wall extends CollidableTile {

    public Wall(Image image, boolean isStatic) {
        super(image, isStatic);
    }

    @Override
    public void onCollision(Collidable other) {
        // TODO Behavior for walls upon collision
    }

}
