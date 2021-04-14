package game.collidables;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

/**
 * Used for Collidables that are collected once by the player
 */
public abstract class Collectable extends Collidable {
    protected boolean isCollected;
    protected Point2D position;
    protected Point2D scale;

    /**
     * Creates an instance of the Collectable object
     *
     * @param imagePath the path of the image of the object
     * @param position  the position of the object
     * @param scale     the scale of the object
     */
    public Collectable(String imagePath, Point2D position, Point2D scale) {
        super(imagePath, true);
        setPosition(position);
        setScale(scale);
    }

    /**
     * Gets the isCollected field
     *
     * @return isCollected
     */
    public boolean isCollected() {
        return isCollected;
    }

    /**
     * Sets isCollected to true
     */
    public void setCollected() {
        isCollected = true;

        setImage(new Image("images/Invisible.gif"));
    }

    /**
     * Gets the position field
     *
     * @return position
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * Sets the position field
     *
     * @param position the position to set to
     */
    public void setPosition(Point2D position) {
        this.position = position;

        setTranslateX(position.getX());
        setTranslateY(position.getY());
    }

    /**
     * Gets the scaling of the Collectable.
     *
     * @return the current scale
     */
    public Point2D getScale() {
        return scale;
    }

    /**
     * Sets the scaling of the Collectable.
     *
     * @param scale the new scale
     */
    public void setScale(Point2D scale) {
        this.scale = scale;

        setScaleX(scale.getX());
        setScaleY(scale.getY());
    }
}