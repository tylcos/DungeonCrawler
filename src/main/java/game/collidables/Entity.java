package game.collidables;

import javafx.geometry.Point2D;

import java.awt.*;

import javafx.scene.image.Image;

/**
 * Game entity that will update every frame
 */
public class Entity extends Collidable {
    private Point2D position;
    private Point2D velocity = new Point2D(0, 0);
    private Point2D scale;

    /**
     * Creates an instance of an entity from an image and places it at a specific position.
     *
     * @param image    the path to the image of the entity
     * @param position the position to create the entity at
     */
    public Entity(String image, Point2D position) {
        this(image, position, new Point2D(1, 1));
    }

    /**
     * Creates an instance of an entity based on.
     *
     * @param image    the path to the image of the entity
     * @param position the position to create the entity at
     * @param scale    how much to scale the entity by
     */
    public Entity(String image, Point2D position, Point2D scale) {
        super(image, false);
        setPosition(position);
        setScale(scale);

    }

    /**
     * Updates the entity.
     * Overwritten in child classes.
     */
    public void update() {
    }

    /**
     * Sets the entity to a new position.
     *
     * @param dt the amount of time
     */
    public final void physicsUpdate(double dt) {
        // position = position + velocity * dt
        position = position.add(velocity.multiply(dt));
        setPosition(position);
    }

    /**
     * Returns the position of the entity.
     *
     * @return the position of the entity
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * Sets the position of the entity to a new position
     *
     * @param position the new point to put the entity at
     */
    public void setPosition(Point2D position) {
        this.position = position;

        setTranslateX(position.getX());
        setTranslateY(position.getY());
    }

    /**
     * Returns the current velocity of the entity.
     *
     * @return the current velocity of the entity
     */
    public Point2D getVelocity() {
        return velocity;
    }

    /**
     * Sets the velocity of the entity to a new velocity.
     *
     * @param velocity the new velocity
     */
    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    /**
     * Gets the scaling of the entity.
     *
     * @return the scale of the entity
     */
    public Point2D getScale() {
        return scale;
    }

    /**
     * Sets the scaling of the entity to a new scale.
     *
     * @param scale the new scale
     */
    public void setScale(Point2D scale) {
        this.scale = scale;

        setScaleX(scale.getX());
        setScaleY(scale.getY());
    }

    @Override
    public void onCollision(Collidable other) {
        // System.out.println("An entity hit something!");
    }

    /**
     * Set Image to new Image
     * @param image new Image to be change
     */
    public void newImage(Image image) {
        super.setNewImage(image);
    }

}
