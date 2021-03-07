package game;

import core.GameManager;
import javafx.geometry.Point2D;

import java.awt.image.PixelInterleavedSampleModel;

/**
 * Game entity that will update every frame
 */
public class Entity extends Collidable {

    private Point2D position;
    private Point2D velocity = new Point2D(0, 0);
    private Point2D scale;

    public Entity(String image, Point2D position) {
        this(image, position, new Point2D(1, 1));
    }

    public Entity(String image, Point2D position, Point2D scale) {
        super(image, false);
        setPosition(position);
        setScale(scale);


        GameManager.spawnEntity(this);
    }

    // Overwritten in child classes
    public void update(double dt) {
    }

    public final void physicsUpdate(double dt) {
        // position = position + velocity * dt
        position = position.add(velocity.multiply(dt));
        setPosition(position);

        update(dt);
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;

        setX(position.getX());
        setY(position.getY());
    }

    public Point2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public Point2D getScale() {
        return scale;
    }

    public void setScale(Point2D scale) {
        this.scale = scale;

        setScaleX(scale.getX());
        setScaleY(scale.getY());
    }

    @Override
    public void onCollision(Collidable other) {
        //System.out.println("An entity hit something!");
    }
}
