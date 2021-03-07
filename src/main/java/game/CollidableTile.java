package game;

import javafx.scene.image.Image;

/**
 * CollidableTile is for Collidables that are also tiles in a Room. Because
 * tiles are contained in a StackPane wrapper, their version of intersects is
 * different from the one defined in Collidable.
 */
public abstract class CollidableTile extends Collidable {

    public CollidableTile(Image image, boolean isStatic) {
        super(image, isStatic);
    }

    public CollidableTile(String image, boolean isStatic) {
        super(image, isStatic);
    }

    @Override
    public boolean intersects(Collidable target) {
        return getParent().getBoundsInParent().intersects(target.getBoundsInParent());
    }
}
