package game;

import core.GameManager;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.vecmath.Vector2d;

public class Entity {
    private ImageView imageView;

    private Vector2d position;
    private Vector2d velocity = new Vector2d(0, 0);
    private Vector2d dimensions;
    private Vector2d scale;

    public Entity(String image, Vector2d position) {
        this(image, position, new Vector2d(1, 1));
    }

    public Entity(String image, Vector2d position, Vector2d scale) {
        setImage(image);
        setPosition(position);
        setScale(scale);

        GameManager.spawnEntity(this);
    }

    // Overwritten in child classes
    public void update(double dt) { }

    public final void physicsUpdate(double dt) {
        // position = position + velocity * dt
        position.scaleAdd(dt, velocity, position);
        setPosition(position);

        update(dt);
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(position.getX(), position.getY(),
                dimensions.getX(), dimensions.getY());
    }

    public boolean intersects(Entity s) {
        return s.getBoundary().intersects(this.getBoundary());
    }

    public ImageView getImage() {
        return imageView;
    }

    public void setImage(String imagePath) {
        Image image = new Image(getClass().getResource(imagePath).toString());

        imageView = new ImageView(image);
        dimensions = new Vector2d(image.getWidth(), image.getHeight());
    }

    public Vector2d getPosition() {
        return position;
    }

    public void setPosition(Vector2d position) {
        this.position = position;

        imageView.setX(position.getX());
        imageView.setY(position.getY());
    }

    public Vector2d getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2d velocity) {
        this.velocity = velocity;
    }

    public Vector2d getDimensions() {
        return dimensions;
    }

    public void setDimensions(Vector2d dimensions) {
        this.dimensions = dimensions;
    }

    public Vector2d getScale() {
        return scale;
    }

    public void setScale(Vector2d scale) {
        this.scale = scale;

        imageView.setScaleX(scale.getX());
        imageView.setScaleY(scale.getY());
    }
}
