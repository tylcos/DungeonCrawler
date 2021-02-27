package game;

import core.GameManager;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Entity {
    private ImageView imageView;

    private Point2D position;
    private Point2D velocity = new Point2D(0, 0);
    private Point2D dimensions;
    private Point2D scale;

    public Entity(String image, Point2D position) {
        this(image, position, new Point2D(1, 1));
    }

    public Entity(String image, Point2D position, Point2D scale) {
        setImage(image);
        setPosition(position);
        setScale(scale);

        GameManager.spawnEntity(this);
    }

    // Overwritten in child classes
    public void update(double dt) { }

    public final void physicsUpdate(double dt) {
        // position = position + velocity * dt
        position = position.add(velocity.multiply(dt));
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
        dimensions = new Point2D(image.getWidth(), image.getHeight());
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;

        imageView.setX(position.getX());
        imageView.setY(position.getY());
    }

    public Point2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public Point2D getDimensions() {
        return dimensions;
    }

    public void setDimensions(Point2D dimensions) {
        this.dimensions = dimensions;
    }

    public Point2D getScale() {
        return scale;
    }

    public void setScale(Point2D scale) {
        this.scale = scale;

        imageView.setScaleX(scale.getX());
        imageView.setScaleY(scale.getY());
    }
}
