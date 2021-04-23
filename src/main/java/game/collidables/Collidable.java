package game.collidables;

import core.ImageManager;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * An object or entity that can be collided with by other objects or entities.
 * Extends ImageView and uses the bounds of the image to detect collision.
 * <p>
 * Collidables with the isStatic variable set cannot collide with other static
 * Collidables which is useful for level geometry and other stationary objects.
 * <p>
 * Generally, objects that don't move should be set as static.
 */
public abstract class Collidable extends ImageView {
    private boolean isStatic;

    /**
     * Constructor taking a path to an image.
     *
     * @param imagePath the path to the image to use
     * @param isStatic  if the body is static or not
     */
    public Collidable(String imagePath, boolean isStatic) {
        super(ImageManager.getImage(imagePath));
        this.isStatic = isStatic;
    }

    /**
     * Constructor taking an Image.
     *
     * @param image    the Image to use
     * @param isStatic if the body is static or not
     */
    public Collidable(Image image, boolean isStatic) {
        super(image);
        this.isStatic = isStatic;
    }

    /**
     * Handles collision detection and takes appropriate action.
     *
     * @param other the Collidable that hit this Collidable
     */
    public abstract void onCollision(Collidable other);

    /**
     * Detects if the given Node is intersecting this Collidable.
     *
     * @param target the Node to check for intersection
     * @return whether the target is intersecting this Collidable
     */
    public boolean intersects(Node target) {
        return getBoundsInParent().intersects(target.getBoundsInParent());

        // This works for everything but runs slower
        // localToScene(getBoundsInLocal()).intersects(
        //              target.localToScene(target.getBoundsInLocal()));
    }

    /**
     * Return whether or not this Collidable is static.
     *
     * @return true if the body is static, false otherwise
     */
    public final boolean isStatic() {
        return isStatic;
    }

    /**
     * Returns whether the collidable is not being rendered.
     *
     * @return whether the collidable is not being rendered
     */
    public final boolean isNotRendered() {
        return getParent() == null;
    }
}
