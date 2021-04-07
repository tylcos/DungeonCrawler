package game.collidables;

import core.SceneManager;
import game.entities.Player;
import game.levels.Room;
import javafx.scene.image.Image;
import views.GameScreen;

/**
 * The door of a room
 */
public class Door extends CollidableTile {
    private Room destination;    // Room this door leads to
    private boolean locked;      // Whether this door can be entered
    private boolean win = false; // Whether touching this door with the key wins the game

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
        if (win && !locked) {
            // if door is win door try to win
            if (other instanceof Player) {
                if (Player.getPlayer().isKeyActivated()) {
                    SceneManager.loadScene(SceneManager.END);
                }
            }
        } else {
            // if door is not win door act normally
            if (!locked) {
                if (other instanceof Player) {
                    GameScreen.getLevel().loadRoom(destination);
                }
            }
        }
    }


    // victory if: keyActivated && unlocked
    // bounce back if: key not activated or locked

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
    
    /**
     * Make touching this door with a key the victory condition.
     */
    public void setWin() {
        win = true;
    }
}
