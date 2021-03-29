package game.collidables;

import game.entities.MainPlayer;
import game.levels.Room;
import javafx.scene.image.Image;
import views.GameScreen;

/**
 * The door of a room
 */
public class Door extends CollidableTile {
    private Room destination;
    private boolean locked;

    /**
     * Constructor taking an Image of a door.
     *
     * @param image    the Image to use for the door
     * @param isStatic if the body is static or not
     */
    public Door(Image image, boolean isStatic) {
        super(image, isStatic);
    }

    /**
     * Constructor taking an Image of a door that links the door to another room.
     *
     * @param image       the Image to use for the door
     * @param isStatic    if the body is static or not
     * @param destination the room that the door leads to
     */
    public Door(Image image, boolean isStatic, Room destination) {
        super(image, isStatic);

        this.destination = destination;
    }

    @Override
    public void onCollision(Collidable other) {
        if (!locked) {
            if (other instanceof MainPlayer) {
                GameScreen.getLevel().setRoom(destination);
            }
        } else {
            // this needs door behavior
            int a;
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
    
    /**
     * Lock the door.
     */
    public void lock() {
        locked = true;
    }
    
    /**
     * Unlock the door.
     */
    public void unlock() {
        locked = false;
    }
}
