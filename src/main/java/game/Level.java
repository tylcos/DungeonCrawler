package game;

import core.GameManager;
import core.SceneManager;
import data.RandomUtil;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Level is a StackPane that renders all game objects in a room. Level should always be rendered
 * underneath the game HUD!
 */
public class Level extends StackPane {
    // TODO An in-game map that shows the layout of the rooms (very optional) (also very dependent
    // on how i'm feeling during that one week where we have a day off)

    public static final int MAX_DIAMETER     = 15; // Width/height of map. ODD NUMBERS ONLY
    public static final int MIN_END_DISTANCE = 6;  // Minimum distance away the exit must be

    // Layers are rendered from the bottom up, so stuff on layer 2 appears above
    // stuff on layer 1 and so on
    private      Pane[] renderingLayers;
    static final int    RENDERING_LAYERS = 4; // Size of renderingLayers array

    // Constants for all the render layers so we don't go insane
    public static final int ROOM   = 0;
    public static final int ITEM   = 1;
    public static final int ENTITY = 2;
    public static final int VFX    = 3;

    // TODO Using a similar, 2D array, we could also create a collision matrix if necessary

    /*
     * Quick explanation of map coordinates for those concerned: map can be thought of as a 2D grid
     * with an origin of (0,0) that spans the space between (-MAX_DIAMETER / 2, -MAX_DIAMETER / 2)
     * in the top left corner of the grid and (MAX_DIAMETER / 2, MAX_DIAMETER / 2) in the bottom
     * right corner of the grid. Any Point2D between those two points is a valid position in the
     * grid.
     *
     * Now, remember map is actually an array of arrays underneath its grid-like visage so we can't
     * directly access the Room at (-2, -2) with map[-2][-2]. Instead, we add mapOffset to our x and
     * y coordinates to convert from the coordinate grid to indicies of the array. So for (-2, -2)
     * we would use map[-2 + mapOffset][-2 + mapOffset].
     */
    private       Room[][] map;        // 2D array of all the rooms making up the level
    private final int      mapOffset;  // Offset used for calculating array indicies from positions
    private       Room     currentRoom;
    private       Room     exit;

    // We use ArrayBlockingQueues here to avoid threading issues

    // A list of all static Collidables that need to be collision checked. This
    // includes stationary things like walls and doors.
    private ArrayBlockingQueue<Collidable> staticBodies = new ArrayBlockingQueue<>(100);

    // A list of all dynamic (non-static) Collidables that need to be collision
    // checked. This includes moving things like enemies.
    private ArrayBlockingQueue<Collidable> dynamicBodies = new ArrayBlockingQueue<>(100);

    // Queue of rooms and their doors that need a connection to an adjacent room
    private Queue<QueueLink> roomLinkQueue = new LinkedList<>();

    /**
     * Create a new level without anything inside it.
     *
     * @param drawPane Pane used to render all entities
     */
    public Level(Pane drawPane) {
        map       = new Room[MAX_DIAMETER][MAX_DIAMETER];
        mapOffset = MAX_DIAMETER / 2;

        renderingLayers = new Pane[RENDERING_LAYERS];
        for (int i = 0; i < RENDERING_LAYERS; ++i) {
            renderingLayers[i] = new Pane();
            getChildren().add(renderingLayers[i]);
        }

        // We must place level on the bottom so that the UI renders on top of it. level should be
        // the only thing in drawPane at all, but I'm specifying to be safe.
        drawPane.getChildren().add(0, this);
    }

    // TODO implement adding stuff to rooms

    /**
     * Use this method to add stuff like items and entities to the rooms
     */
    private void addStuffToRooms() {
        for (int y = MAX_DIAMETER - 1; y >= 0; --y) {
            for (int x = 0; x < MAX_DIAMETER; ++x) {
                if (map[x][y] != null) {
                    map[x][y].addItem(new Coin(false));
                    /* PUT STUFF IN HERE */
                }
            }
        }
    }

    /**
     * Generates a random map for this level.
     */
    public void generateMap() {
        // Generate the entrance room
        map[mapOffset][mapOffset] =
                new Room("/rooms/rectangle.room", new Point2D(0, 0), this, true);
        // Generate the rest of the rooms
        dequeueAndLinkRooms();
        // Adds stuff to rooms
        addStuffToRooms();
        // Sets the active room to the entrance
        setRoom(map[mapOffset][mapOffset]);
    }

    /**
     * Loads the specified room into the level.
     *
     * @param newRoom the room to switch to
     */
    public void setRoom(Room newRoom) {
        if (currentRoom != null) {
            // Unload the old room
            removeFromLayer(ITEM, currentRoom.getItems());
            removeFromPhysics(currentRoom.getItems());
            removeFromLayer(ENTITY, currentRoom.getEntities());
            removeFromPhysics(currentRoom.getEntities());
            removeFromPhysics(currentRoom.getBodies());
        }
        currentRoom = newRoom;

        if (newRoom.isExit()) {
            SceneManager.loadScene(SceneManager.WIN_SCREEN);
            return;
        }

        // Load the new room
        setRenderLayer(ROOM, currentRoom);
        addToLayer(ITEM, currentRoom.getItems());
        addToPhysics(currentRoom.getItems());
        addToLayer(ENTITY, currentRoom.getEntities());
        addToPhysics(currentRoom.getEntities());
        addToPhysics(currentRoom.getBodies());

        // Put the player in the "center" of the room
        MainPlayer player = GameManager.getPlayer();
        if (player != null) {
            player.setPosition(new Point2D(960, 540));
            player.setVelocity(Point2D.ZERO);
        }
    }

    /**
     * Replace the current pane on the given render layer with a new pane.
     *
     * @param layer   the render layer to set
     * @param newPane the pane the render layer is being set to
     */
    private void setRenderLayer(int layer, Pane newPane) {
        renderingLayers[layer] = newPane;
        getChildren().remove(layer);
        getChildren().add(layer, renderingLayers[layer]);
    }

    /**
     * Removes all given objects from the render layer.
     *
     * @param <T>     any type that extends Node
     * @param layer   the render layer to remove from
     * @param objects the objects to remove
     */
    private <T> void removeFromLayer(int layer, ArrayList<T> objects) {
        renderingLayers[layer].getChildren().removeAll(objects);
    }

    /**
     * Removes all given objects from the physics update system.
     *
     * @param <T>     any type that extends Collidable
     * @param objects the objects to remove
     */
    private <T extends Collidable> void removeFromPhysics(ArrayList<T> objects) {
        for (Collidable collidable : objects) {
            if (collidable.isStatic()) {
                staticBodies.remove(collidable);
            } else {
                dynamicBodies.remove(collidable);
            }
        }
    }

    /**
     * Adds all given objects to the render layer.
     *
     * @param <T>     any type that extends Node
     * @param layer   the render layer to add to
     * @param objects the objects to add
     */
    private <T extends Node> void addToLayer(int layer, ArrayList<T> objects) {
        renderingLayers[layer].getChildren().addAll(objects.toArray(new Node[0]));
    }

    /**
     * Adds all given objects to the physics update system.
     *
     * @param <T>     any type that extends Collidable
     * @param objects the objects to add
     */
    private <T extends Collidable> void addToPhysics(ArrayList<T> objects) {
        for (Collidable collidable : objects) {
            if (collidable.isStatic()) {
                staticBodies.add(collidable);
            } else {
                dynamicBodies.add(collidable);
            }
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
        for (Collidable collidable : staticBodies) {
            if (collidable == target) { // We don't want to accidentally collide with ourselves
                continue;
            }
            if (collidable.intersects(target)) {
                collidable.onCollision(target);
                target.onCollision(collidable);
            }
        }
        for (Collidable collidable : dynamicBodies) {
            if (collidable == target) {
                continue;
            }
            if (collidable.intersects(target)) {
                collidable.onCollision(target);
                target.onCollision(collidable);
            }
        }
    }

    /**
     * Checks if a room that can be linked to exists in the given direction from the room. If not,
     * checks if a room could be created there.
     *
     * @param from      the room we're checking from
     * @param direction the direction we're checking in
     * @return if a doorway connection can be made from the given room in the specified direction
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
        return map[(int) (testRoomPos.getX() + mapOffset)][(int) (testRoomPos.getY() + mapOffset)];
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
     * Dequeues and links rooms that have requested creation of an adjacent room until an exit room
     * has been made and the queue has been emptied.
     */
    private void dequeueAndLinkRooms() {
        while (!roomLinkQueue.isEmpty()) {
            QueueLink next     = roomLinkQueue.remove();
            Room      existing = getRoomIfExists(next.from, next.dir);
            if (existing == null) {
                Point2D newRoomPos = next.from.getPosition().add(next.dir.vector());
                Room newRoom = new Room(RandomUtil.getRandomRoomBlueprint(), newRoomPos, this,
                                        false, next.from);
                map[(int) (newRoomPos.getX() + mapOffset)][(int) (newRoomPos.getY() + mapOffset)] =
                        newRoom;
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
     * Returns a basic text minimap
     *
     * @return text minimap
     */
    public String getMinimapString() {
        // Find bounds of generated rooms
        int leftBound   = MAX_DIAMETER;
        int rightBound  = 0;
        int bottomBound = MAX_DIAMETER;
        int topBound    = 0;
        for (int y = 0; y < MAX_DIAMETER; y++) {
            for (int x = 0; x < MAX_DIAMETER; x++) {
                if (map[x][y] == null) {
                    continue;
                }

                if (leftBound > x) {
                    leftBound = x;
                }
                if (rightBound < x) {
                    rightBound = x;
                }
                if (bottomBound > y) {
                    bottomBound = y;
                }
                if (topBound < y) {
                    topBound = y;
                }
            }
        }

        rightBound += 1;
        topBound += 1;
        int xRange = rightBound - leftBound;
        int yRange = topBound - bottomBound;

        StringBuilder minimap = new StringBuilder(xRange * yRange * 4);
        for (int y = topBound - 1; y >= bottomBound; y--) {
            StringBuilder line2 = new StringBuilder(MAX_DIAMETER * 2);
            StringBuilder line1 = new StringBuilder(MAX_DIAMETER * 2);

            for (int x = leftBound; x < rightBound; x++) {
                Room room = map[x][y];
                line2.append("  ");

                if (room == null) {
                    line1.append("  ");
                    continue;
                } else if (room.isEntrance()) {
                    line1.append("E ");
                } else if (room.isExit()) {
                    line1.append("X ");
                } else {
                    line1.append("O ");
                }

                boolean[] info = room.getActiveDoors();
                if (info[Direction.NORTH.toValue()]) {
                    line2.setCharAt((x - leftBound) * 2, '|');
                }
                if (info[Direction.EAST.toValue()]) {
                    line1.setCharAt((x - leftBound) * 2 + 1, '\u2015');
                }
            }

            minimap.append(line2.substring(0, xRange * 2));
            minimap.append('\n');
            minimap.append(line1.substring(0, xRange * 2));
            minimap.append('\n');
        }

        return minimap.toString();
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
     * Returns the exit room of this level.
     *
     * @return the exit room
     */
    public Room getExit() {
        return exit;
    }

    /**
     * Returns the entrance room of this level.
     *
     * @return the entrance room
     */
    public Room getEntrance() {
        return map[mapOffset][mapOffset];
    }

    /**
     * Data class containing information needed to create doors between rooms.
     */
    private static final class QueueLink {
        private Room      from;
        private Direction dir;
        private Door      door;

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
        private QueueLink(Room from, Direction dir, Door door) {
            this.from = from;
            this.dir  = dir;
            this.door = door;
        }
    }
}
