package game.levels;

import core.GameEngine;
import core.SceneManager;
import data.RandomUtil;
import game.collidables.*;
import game.entities.*;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Level generates a random level and manages what is currently loaded on the screen
 */
public class Level {
    public static final int MAX_DIAMETER     = 15; // Width/height of map. ODD NUMBERS ONLY
    public static final int MIN_END_DISTANCE = 6;  // Minimum distance away the exit must be

    /* CHEATS */
    private static boolean spawnEnemiesInEntrance;
    private static boolean doorsNeverLock;

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

    // Queue of rooms and their doors that need a connection to an adjacent room
    private Queue<QueueLink> roomLinkQueue = new LinkedList<>();

    // Used to update the ui minimap when a new room is loaded
    private static EventHandler<? extends Event> uiEventHandler;

    /**
     * Create a new level without anything inside it.
     */
    public Level() {
        map       = new Room[MAX_DIAMETER][MAX_DIAMETER];
        mapOffset = MAX_DIAMETER / 2;
    }

    /**
     * Adds items and entities to the rooms.
     */
    private void spawnGameElements() {
        for (int y = MAX_DIAMETER - 1; y >= 0; --y) {
            for (int x = 0; x < MAX_DIAMETER; ++x) {
                Room room = map[x][y];

                if (room != null && (spawnEnemiesInEntrance || !room.isEntrance())) {
                    int numberOfCoins = RandomUtil.getInt(3, 5);
                    for (int i = 0; i < numberOfCoins; i++) {
                        room.addCollectable(new Coin());
                    }

                    int numberOfItems = RandomUtil.getInt(2);
                    for (int i = 0; i < numberOfItems; i++) {
                        room.addCollectable(new Item());
                    }

                    int numberOfEnemies = RandomUtil.getInt(2, 5);
                    for (int i = 0; i < numberOfEnemies; i++) {
                        room.addEntity(new Slime());
                    }
                }
            }
        }
    }

    /**
     * Generates a random map for this level.
     *
     * Separating from the constructor to prevent objects spawned in the level from referencing
     * level before its returned from the constructor
     */
    public void generateMap() {
        // Generate the entrance room
        map[mapOffset][mapOffset] = new Room("/rooms/rectangle.room", Point2D.ZERO, this, true);
        // Generate the rest of the rooms
        dequeueAndLinkRooms();
        // Add game elements to rooms
        spawnGameElements();
        // Set the active room to the entrance
        loadRoom(map[mapOffset][mapOffset]);
        // Spawn the player
        GameEngine.instantiate(GameEngine.ENTITY, Player.getPlayer());
    }

    /**
     * Loads the specified room into the level.
     *
     * @param newRoom the room to switch to
     */
    public void loadRoom(Room newRoom) {
        Direction fromDir = null;
        if (currentRoom != null) {
            unloadCurrentRoom();
            fromDir = Direction.vectorToDirection(
                    currentRoom.getPosition().subtract(newRoom.getPosition()));
        }
        currentRoom = newRoom;

        if (newRoom.isExit()) {
            SceneManager.loadScene(SceneManager.END);
            return;
        }

        // Load the new room
        GameEngine.setRenderLayer(GameEngine.ROOM, currentRoom);
        GameEngine.instantiate(GameEngine.ITEM, currentRoom.getCollectables());
        GameEngine.instantiate(GameEngine.ENTITY, currentRoom.getEntities());
        GameEngine.addToPhysics(currentRoom.getBodies());

        currentRoom.getEntities().forEach(Entity::start);

        // Put the player in the "center" of the room
        Player player = Player.getPlayer();
        player.toFront();
        player.setPosition(Point2D.ZERO);

        // Lock doors for rooms other than the entrance room
        if (fromDir != null) {
            if (!currentRoom.isClear() && !doorsNeverLock) {
                currentRoom.lockDoors(fromDir);
            } else {
                currentRoom.unlockDoors();
            }
        }

        // Update the minimap
        if (uiEventHandler != null) {
            uiEventHandler.handle(null);
        }
    }

    /**
     * Unloads the specified room from the level.
     */
    public void unloadCurrentRoom() {
        currentRoom.getEntities().forEach(Entity::stop);

        GameEngine.destroy(GameEngine.ITEM, currentRoom.getCollectables());
        GameEngine.destroy(GameEngine.ENTITY, currentRoom.getEntities());
        GameEngine.removeFromPhysics(currentRoom.getBodies());
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

    public static <T extends Event> void addUiEventHandler(EventHandler<T> handler) {
        uiEventHandler = handler;
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
            StringBuilder line2 = new StringBuilder(xRange * 2);
            StringBuilder line1 = new StringBuilder(xRange * 2);

            for (int x = leftBound; x < rightBound; x++) {
                Room room = map[x][y];
                line2.append("  ");

                if (room == null) {
                    line1.append("  ");
                    continue;
                } else if (room == currentRoom) {
                    line1.append("C ");
                } else if (room.isEntrance()) {
                    line1.append("S ");
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

            minimap.append(line2);
            minimap.append('\n');
            minimap.append(line1);
            minimap.append('\n');
        }
        return minimap.toString();
    }

    /**
     * Checks if this level is missing an exit room.
     *
     * @return {@code true} if this level is missing an exit room
     */
    public boolean missingExit() {
        return exit == null;
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
     * Returns the currently loaded room.
     *
     * @return the level loaded
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    public static void setSpawnEnemiesInEntrance(boolean spawnEnemiesInEntrance) {
        Level.spawnEnemiesInEntrance = spawnEnemiesInEntrance;
    }

    public static void setDoorsNeverLock(boolean doorsNeverLock) {
        Level.doorsNeverLock = doorsNeverLock;
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
