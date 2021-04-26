package game.collidables;

import core.SceneManager;
import core.SoundManager;
import game.entities.Player;
import game.level.Room;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import utilities.GameEffects;
import utilities.TimerUtil;
import views.GameScreen;

/**
 * The door of a room
 */
public class Door extends CollidableTile {
    private Room    destination; // Room this door leads to
    private boolean locked;      // Whether this door can be entered
    private boolean win;         // Whether touching this door with the key wins the game

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
        Player player = Player.getPlayer();
        if (!(other instanceof Player) || locked || player.hasWon()) {
            return;
        }

        if (player.isKeyActivated()) {
            SoundManager.playVictory();

            Parent root = getScene().getRoot();
            Node   room = root.getChildrenUnmodifiable().get(0);
            root.setEffect(GameEffects.GAME_BLUR);
            TimerUtil.lerp(1, t -> player.setOpacity(1 - t));
            TimerUtil.lerp(5, t -> GameEffects.GAME_BLUR.setRadius(50 * t),
                           () -> TimerUtil.lerp(5, t -> room.setOpacity(1 - t)));

            TimerUtil.schedule(2, () -> SceneManager.loadPane(SceneManager.END));

            player.stop();
            player.setHasWon(true);
        } else if (destination != null) {
            GameScreen.getLevel().loadRoom(destination);

            SoundManager.playDoorCreak();
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

    /**
     * Checks if touching this door with a key the victory condition.
     *
     * @return true if this is the victory door
     */
    public boolean getWin() {
        return win;
    }
}
