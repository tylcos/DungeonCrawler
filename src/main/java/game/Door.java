package game;

import core.GameManager;
import javafx.scene.image.Image;

/**
 * The door of a room
 */
public class Door extends CollidableTile {
    private Room destination;

    /**
     * Constructor taking an Image of a door.
     *
     * @param image the Image to use for the door
     * @param isStatic if the body is static or not
     */
    public Door(Image image, boolean isStatic) {
        super(image, isStatic);
    }

    /**
     * Constructor taking an Image of a door that links the door to another room.
     *
     * @param image the Image to use for the door
     * @param isStatic if the body is static or not
     * @param destination the room that the door leads to
     */
    public Door(Image image, boolean isStatic, Room destination) {
        super(image, isStatic);

        this.destination = destination;
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof MainPlayer) {
            GameManager.getLevel().setRoom(destination);
        }
    }

    /**
     * Sets the room that a door leads to a new room.
     *
     * @param destination the new room for the door to lead to
     */
    public void setDestination(Room destination) {
        this.destination = destination;
    }

    /**
     * Getter for the destination field
     *
     * @return destination the room next in the queue
     */
    public Room getDestination() {
        return destination;
    }
}
