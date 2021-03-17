package game.collidables;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Collectable extends Collidable {

    private boolean isCollected;
    private Point2D position;
    private Point2D scale;

    /**
     * Creates an instance of the Collectable object
     *
     * @param image    the image of the object
     * @param position the position of the object
     * @param scale    the scale of the object
     */
    public Collectable(Image image, Point2D position, Point2D scale) {
        super(image, true);
        this.position = position;
        this.scale = scale;
    }

    /**
     * Creates an instance of the Collectable object
     *
     * @param imagePath the path of the image of the object
     * @param position  the position of the object
     * @param scale     the scale of the object
     */
    public Collectable(String imagePath, Point2D position, Point2D scale) {
        super(imagePath, true);
        this.position = position;
        this.scale = scale;
    }

    /**
     * Getter for isCollected field
     *
     * @return isCollected
     */
    public boolean getCollected() {
        return isCollected;
    }

    /**
     * Sets isCollected to true
     */
    public void setCollected() {
        this.isCollected = true;
    }

    @Override
    public void onCollision(Collidable other) {
        //todo behaviour for collectables upon collision with mainplayer: make sound
    }

    /**
     * Getter for the position field
     *
     * @return position
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * Setter for the position field
     *
     * @param position the position to set to
     */
    public void setPosition(Point2D position) {
        this.position = position;
    }
}