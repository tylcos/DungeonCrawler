package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * An object or entity that can be collided with by other objects or entities.
 * Extends ImageView and uses the bounds of the image to detect collision.
 * 
 * Collidables with the isStatic variable set cannot collide with other static
 * Collidables which is useful for level geometry and other stationary objects.
 * 
 * Generally, objects that don't move should be set as static.
 *
 */
public abstract class Collidable extends ImageView {

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
     * Constructor taking a path to an image.
     * 
     * @param image    the path to the image to use
     * @param isStatic if the body is static or not
     */
    public Collidable(String image, boolean isStatic) {
        super(image);
        this.isStatic = isStatic;
    }

    private boolean isStatic;

    /**
     * Handle collision detection and take appropriate action.
     * 
     * @param other the Collidable that hit this Collidable
     */
    public abstract void onCollision(Collidable other);

    /**
     * Return whether or not this Collidable is static.
     * 
     * @return true if the body is static, false otherwise
     */
    public boolean isStatic() {
        return isStatic;
    }

    /**
     * Detects if the given Collidable is touching this Collidable.
     * 
     * @param target the Collidable to check for collision
     * @return true if collision is occurring, false otherwise
     */
    public boolean intersects(Collidable target) {
        return getBoundsInParent().intersects(target.getBoundsInParent());
    }

}
