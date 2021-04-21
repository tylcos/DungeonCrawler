package game.collectables;

import core.ImageManager;
import game.collidables.Collidable;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

/**
 * Used for Collidables that are collected once by the player
 */
public abstract class Collectable extends Collidable {
    protected boolean isCollected;
    protected Point2D position;

    /**
     * Creates an instance of the Collectable object
     *
     * @param imagePath  the path of the image of the object
     * @param position   the position of the object
     * @param dimensions the dimensions of the object
     */
    public Collectable(String imagePath, Point2D position, Point2D dimensions) {
        super(ImageManager.getImage(imagePath, dimensions, false), true);
        setPosition(position);
    }

    /**
     * Creates an instance of the Collectable object
     *
     * @param image    the image of the object
     * @param position the position of the object
     */
    public Collectable(Image image, Point2D position) {
        super(image, true);
        setPosition(position);
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

        setImage(ImageManager.getImage("blank.png"));
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
}