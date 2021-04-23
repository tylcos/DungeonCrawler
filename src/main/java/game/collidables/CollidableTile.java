package game.collidables;

import javafx.scene.Node;
import javafx.scene.image.Image;

/**
 * CollidableTile is for Collidables that are also tiles in a Room. Because
 * tiles are contained in a StackPane wrapper, their version of intersects is
 * different from the one defined in Collidable.
 */
public abstract class CollidableTile extends Collidable {

    /**
     * Constructor taking an image of a CollidableTile.
     *
     * @param image the Image to use
     * @param isStatic if the body is static or not
     */
    public CollidableTile(Image image, boolean isStatic) {
        super(image, isStatic);
    }

    /**
     * Detects if the given Collidable is touching this CollidableTile.
     *
     * @param target the CollidableTile to check for collision
     * @return true if collision is occurring; false otherwise
     */
    @Override
    public boolean intersects(Node target) {
        return getParent().getBoundsInParent().intersects(target.getBoundsInParent());
    }
}
