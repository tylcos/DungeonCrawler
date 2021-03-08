package game;

import core.GameManager;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Level is a StackPane that renders all game objects in a room. Level should
 * always be rendered underneath the game HUD!
 */
public class Level extends StackPane {
    // TODO Something is *definitely* happening when generating a room, but its
    // really hard to see what.
    // We need a map we can pull up that shows all the rooms and the connections
    // between them.
    // Will do this later, but for now, this appears to be working (kind of)

    public static final int MAX_DIAMETER = 15; // Width/height of map. ODD NUMBERS ONLY
    public static final int MIN_END_DISTANCE = 6; // Minimum distance away the exit must be

    // Layers are rendered from the bottom up, so stuff on layer 4 appears above
    // stuff on layer 3 and so on
    private Pane[] renderingLayers;
    static final int RENDERING_LAYERS = 4; // Size of renderingLayers array

    // Constants for all the render layers so we don't go insane
    public static final int ROOM = 0;
    public static final int ITEM = 1;
    public static final int ENTITY = 2;
    public static final int VFX = 3;

    // todo Using a similar, 2D array, we could also create a collision matrix

    private Room[][] map; // 2D array of all the rooms making up the level
    private final int mapOffset; // Offset used for calculating array indicies from positions
    private Room currentRoom;
    private Room exit;

    // We use ArrayBlockingQueues here to avoid threading issues

    // A list of all static Collidables that need to be collision checked. This
    // includes stationary things like walls and doors.
    private ArrayBlockingQueue<Collidable> staticBodies = new ArrayBlockingQueue<>(100);

    // A list of all dynamic (non-static) Collidables that need to be collision
    // checked. This includes moving things like enemies.
    // I haven't done anything with dynamic bodies yet, so this is really just a
    // placeholder
    private ArrayBlockingQueue<Collidable> dynamicBodies = new ArrayBlockingQueue<>(100);

    // Queue of rooms and their doors that need a connection to an adjacent room
    private Queue<QueueLink> roomLinkQueue = new LinkedList<QueueLink>();

    /**
     * Create a new level with a random layout.
     */
    public Level() {
        super();

        map = new Room[MAX_DIAMETER][MAX_DIAMETER];
        mapOffset = MAX_DIAMETER / 2;

        renderingLayers = new Pane[RENDERING_LAYERS];
        for (int i = 0; i < RENDERING_LAYERS; ++i) {
            renderingLayers[i] = new Pane();
            getChildren().add(renderingLayers[i]);
        }

        generateMap();
    }

    /**
     * Generates a random map for this level.
     */
    private void generateMap() {
        // Rooms need to be set in the map, THEN linked with doors
        map[mapOffset][mapOffset] = new Room("/rooms/test.room", new Point2D(0, 0), this,
                true);
        dequeueAndLinkRooms();
        setRoom(map[mapOffset][mapOffset]);
        for (int j = MAX_DIAMETER - 1; j >= 0; --j) {
            for (int i = 0; i < MAX_DIAMETER; ++i) {
                if (map[i][j] != null) {
                    if (map[i][j].isExit()) {
                        System.out.print("X");
                    } else if (map[i][j].isEntrance()) {
                        System.out.print("E");
                    } else {
                        System.out.print(map[i][j].getDistanceFromEntrance());
                    }
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }

    /**
     * Change the room this level has loaded.
     * 
     * @param newRoom the room to switch to
     */
    public void setRoom(Room newRoom) {
        currentRoom = newRoom;
        renderingLayers[ROOM] = currentRoom;
        getChildren().remove(ROOM);
        getChildren().add(ROOM, renderingLayers[ROOM]);
        staticBodies.clear();
        dynamicBodies.clear();
        for (Collidable c : newRoom.getBodies()) {
            if (c.isStatic()) {
                staticBodies.add(c);
            } else {
                dynamicBodies.add(c);
            }
        }

        MainPlayer player = GameManager.getPlayer();
        if (player != null) {
            player.setPosition(new Point2D(750, 500));
            player.setVelocity(Point2D.ZERO);
        }
    }

    /**
     * Adds an entity on the given layer.
     * 
     * @param layer the layer this entity will be displayed on
     * @param e     the entity to add
     */
    public void addEntity(int layer, Entity e) {
        renderingLayers[layer].getChildren().add(e);

    }

    /**
     * Checks and handles collisions on the target collidable.
     * 
     * @param target the collidable to check for collisions on
     */
    public void runCollisionCheck(Collidable target) {
        for (Collidable c : staticBodies) {
            if (c.intersects(target)) {
                c.onCollision(target);
                target.onCollision(c);
            }
        }
    }

    /**
     * Checks if a room that can be linked to exists in the given direction from the
     * room. If not, checks if a room could be created there.
     * 
     * @param from      the room we're checking from
     * @param direction the direction we're checking in
     * @return if a doorway connection can be made from the given room in the
     *         specified direction
     */
    public boolean getRoomExistsOrAvailable(Room from, Direction direction) {
        int x = (int) (from.getPosition().getX() + direction.vector().getX());
        int y = (int) (from.getPosition().getY() + direction.vector().getY());
        boolean valid = x + mapOffset < MAX_DIAMETER && y + mapOffset < MAX_DIAMETER
                && x + mapOffset >= 0 && y + mapOffset >= 0;
        if (valid && exit != null && map[x + mapOffset][y + mapOffset] == exit) {
            return false;
        }
        return valid;
    }

    /**
     * Returns the room adjacent to the given room in the specified direction.
     * 
     * @param from      the room we're checking from
     * @param direction the direction we're checking in
     * @return the room there, if it exists, or {@code null} if it doesn't
     */
    public Room getRoomIfExists(Room from, Direction direction) {
        Point2D testRoomPos = from.getPosition().add(direction.vector());
        return map[(int) (testRoomPos.getX() + mapOffset)][(int) (testRoomPos.getY()
                + mapOffset)];
    }

    /**
     * Queues a room's door to be linked in a specified direction.
     * 
     * @param from      the room we're linking from
     * @param direction the direction we're linking in
     * @param d         the door we're linking from
     */
    public void queueLinkRoom(Room from, Direction direction, Door d) {
        QueueLink ql = new QueueLink(from, direction, d);
        roomLinkQueue.add(ql);
    }

    /**
     * Dequeues and links rooms until an exit room has been made and the queue has
     * been emptied.
     */
    private void dequeueAndLinkRooms() {
        while (!roomLinkQueue.isEmpty()) {
            QueueLink next = roomLinkQueue.remove();
            Room existing = getRoomIfExists(next.from, next.dir);
            if (existing == null) {
                Point2D newRoomPos = next.from.getPosition().add(next.dir.vector());
                Room newRoom = new Room("/rooms/test.room", newRoomPos, this, false, next.from);
                map[(int) (newRoomPos.getX() + mapOffset)][(int) (newRoomPos.getY()
                        + mapOffset)] = newRoom;
                next.door.setDestination(newRoom);
                newRoom.createDoor(next.dir.opposite(), next.from);
                if (newRoom.isExit()) {
                    exit = newRoom;
                }
            } else {
                next.door.setDestination(existing);
                existing.createDoor(next.dir.opposite(), next.from);
            }
        }
    }

    /**
     * Checks if this level has an exit room.
     * 
     * @return {@code true} if this level has an exit
     */
    public boolean hasExit() {
        return exit != null;
    }

    /**
     * Data class containing information needed to create doors between rooms
     */
    private class QueueLink {
        private Room from;
        private Direction dir;
        private Door door;

        public Room getFrom() {
            return from;
        }

        public Direction getDir() {
            return dir;
        }

        public Door getDoor() {
            return door;
        }

        /**
         * Creates a new QueueLink with given data.
         * 
         * @param from room we're connecting from
         * @param dir  direction the connection is going
         * @param door door we're connecting from
         */
        public QueueLink(Room from, Direction dir, Door door) {
            super();
            this.from = from;
            this.dir = dir;
            this.door = door;
        }
    }
}
