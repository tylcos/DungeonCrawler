package game.levels;

import data.RandomUtil;
import game.collidables.*;
import game.entities.Entity;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.*;
import java.util.*;

/**
 * Room is a GridPane that displays a given room as a grid of Labels.
 *
 * Room also contains all data relevant to a room, like the items and enemies inside of it.
 */
public class Room extends GridPane {

    public static final double CELL_SIZE = 100.0; // Width and height of all cells in this Room

    /*
     * The chance of connecting a doorway to another room is BRANCH_CHANCE * (BRANCH_TAX ^ branches)
     * * (DISTANCE_TAX ^ (distanceFromEntrance - level.MIN_END_DISTANCE)).
     *
     * BRANCH_CHANCE is the base chance of branching.
     *
     * BRANCH_TAX is multiplied onto the base chance for every existing branch.
     *
     * DISTANCE_TAX is multiplied onto the base chance for every additional doorway separating this
     * room from the entrance past level.MIN_END_DISTANCE.
     *
     * Higher numbers mean higher chance of branching. All chances must be between 0.0 (exclusive)
     * and 1.0 (exclusive).
     *
     * At least one branch will always be created if an exit does not exist in this level yet. If
     * another room caused the creation of this one (in dequeueAndLinkRooms) then the door leading
     * to that room will always be created IN ADDITION to at least one other door if the exit
     * doesn't exist yet. This logic ensures that an exit room always gets created.
     */
    public static final float BRANCH_CHANCE = 0.3f;
    public static final float BRANCH_TAX    = 0.4f;
    public static final float DISTANCE_TAX  = 0.9f;

    // A table to translate between letters in .room files and sprites in game
    private static HashMap<String, Image> spriteTable = new HashMap<>() {
        {
            put(".", new Image(Room.class.getResource("/images/debug square.png").toString()));
            put("x", new Image(Room.class.getResource("/images/debug wall.png").toString()));
            put("N", new Image(Room.class.getResource("/images/debug door.png").toString()));
            put("E", new Image(Room.class.getResource("/images/debug door.png").toString()));
            put("S", new Image(Room.class.getResource("/images/debug door.png").toString()));
            put("W", new Image(Room.class.getResource("/images/debug door.png").toString()));
        }
    };

    /*
     * A cache for the contents of room files we have read before. With this, we don't have to make
     * read requests to a file more than once! Disks are slow and flash storage is fast :)
     */
    private static HashMap<String, ArrayList<String>> fileCache = new HashMap<>();

    private Level   level;                        // The level this room belongs to
    private Point2D position;                     // The position of this room in the level
    private boolean exit; // Whether this room is the exit
    private boolean entrance; // Whether this room is the entrance
    private int     distanceFromEntrance = 999;   // # of doorways separating this room from the
    //                                               entrance

    // A list of all collidable bodies making up this room
    private ArrayList<Collidable> bodies = new ArrayList<>();

    // Objects in this room. Names correspond to render layers in Level
    // WARNING: IF YOU GET ERRORS ABOUT CONCURRENCY, THERE'S A DECENT CHANCE THESE TWO ARRAYLISTS
    // NEED TO BE TURNED INTO ARRAYBLOCKINGQUEUES
    private ArrayList<Collectable> items    = new ArrayList<>();
    private ArrayList<Entity>      entities = new ArrayList<>();

    // Contains valid door tiles for each direction.
    // Used by createDoor() to add doors to rooms that are already generated.
    private EnumMap<Direction, ArrayList<StackPane>> doors = new EnumMap<>(Direction.class);

    {
        for (Direction direction : Direction.values()) {
            doors.put(direction, new ArrayList<>());
        }
    }

    // Holds which doors have been activated
    // A door which is active leads to another room.
    private boolean[] activeDoors = new boolean[4];

    /**
     * Constructs a room from a .room file at the given position in the specified level.
     *
     * {@code creator} will have a doorway into this room.
     *
     * @param template the file to load the room from
     * @param position the position to make the room at
     * @param level    the level this room was created in
     * @param start    if this is the start room and all doors should be active
     * @param creator  the room that triggered the creation of this one
     */
    public Room(String template, Point2D position, Level level, boolean start, Room creator) {
        // Initialize our distance from the entrance room
        if (creator != null) {
            distanceFromEntrance = creator.distanceFromEntrance + 1;
            // The first room far enough away from the entrance to be an exit will become
            // the exit
            if (distanceFromEntrance >= Level.MIN_END_DISTANCE && !level.hasExit()) {
                exit = true;
            }
        }
        if (start) {
            distanceFromEntrance = 0;
            entrance             = true;
        }
        this.position = position;
        this.level    = level;

        // By default GridPanes are aligned to the top-left, but we want the room's
        // tiles centered
        setAlignment(Pos.CENTER);
        setMouseTransparent(true);

        // Create the walls, floors, and doors that compose this room
        constructRoomFromBlueprint(template, creator);
    }

    /**
     * Constructs a room from a .room file at the given position in the specified level.
     *
     * @param template the file to load the room from
     * @param position the position to make the room at
     * @param level    the level this room was created in
     * @param start    if this is the start room and all doors should be active
     */
    public Room(String template, Point2D position, Level level, boolean start) {
        this(template, position, level, start, null);
    }

    /**
     * Reads the specified file and extracts the blueprint text within. The file's contents are
     * automatically cached in memory for future access.
     *
     * @param name the path to the file to open
     * @return an ArrayList where each index {@code n} is the {@code n}th line of the file
     */
    private ArrayList<String> readFile(String name) {
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<String> cache = fileCache.get(name);
        if (cache != null) {
            // File is cached. Load from cache.
            lines = cache;
        } else {
            // File does not exist in cache. Read from file.
            InputStream is = Room.class.getResourceAsStream(name);
            if (is == null) {
                System.err.println("Tried to open file " + name + ", but it does not exist!");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            try {
                while (reader.ready()) {
                    lines.add(reader.readLine());
                }
            } catch (IOException e) {
                System.err.println("Error occurred trying to read from " + name + "!");
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                    is.close();
                } catch (IOException e) {
                    System.err.println("Error occurred trying to close file " + name + "!");
                    e.printStackTrace();
                }
            }
            // Add file to cache.
            fileCache.put(name, lines);
        }
        return lines;
    }

    /**
     * Chooses random doors in this room to activate. If {@code creator} is set, the door leading to
     * {@code creator} will always be set active. If the level containing this room does not yet
     * have an exit, at least one door in addition to {@code creator}'s door will be activated.
     *
     * @param blueprint the blueprint of this room
     * @param creator   the room that created this room
     */
    private void activateRandomDoors(ArrayList<String> blueprint, Room creator) {
        boolean[] testedDoors = new boolean[4]; // tracks doors that can be activated

        // Check which doors exist in this blueprint
        for (int row = 0; row < blueprint.size(); ++row) {
            for (int col = 0; col < blueprint.get(row).length(); ++col) {
                switch (blueprint.get(row).charAt(col)) {
                case 'E':
                    testedDoors[Direction.EAST.toValue()] = true;
                    break;
                case 'W':
                    testedDoors[Direction.WEST.toValue()] = true;
                    break;
                case 'S':
                    testedDoors[Direction.SOUTH.toValue()] = true;
                    break;
                case 'N':
                    testedDoors[Direction.NORTH.toValue()] = true;
                    break;
                default:
                    break;
                }
            }
        }

        int branches = 0; // tracks number of doors we have activated

        // Activate door to the creator, if applicable
        if (creator != null) {
            Direction creatorDirection =
                    Direction.vectorToDirection(creator.position.subtract(position));
            activeDoors[creatorDirection.toValue()] = true;
            testedDoors[creatorDirection.toValue()] = false;
            ++branches;
        }

        int numberOfDoors = 0; // tracks number of doors that can be activated
        // Initialize numberOfDoors
        for (int i = 0; i < testedDoors.length; ++i) {
            if (testedDoors[i]) {
                ++numberOfDoors;
            }
        }

        boolean branched = false; // tracks if at least a new (non-creator) door has been activated
        // Randomly choose doors to activate
        while (numberOfDoors > 0) {
            // Decide if we should activate a door. Exits never activate; entrances always activate
            /* @formatter:off */
            boolean build =
                !exit && (entrance || (!level.hasExit() && !branched)
                || Math.random() < (BRANCH_CHANCE * Math.pow(BRANCH_TAX, branches)
                * ((Level.MIN_END_DISTANCE < distanceFromEntrance)
                ? Math.pow(DISTANCE_TAX, distanceFromEntrance - Level.MIN_END_DISTANCE) : 1)));
            /* @formatter:on */
            // The magic below will randomly choose a door to make active/inactive
            // Beginning of magic
            int randomInt = RandomUtil.getInt(0, numberOfDoors);
            int counter   = 0;
            while (randomInt > 0 || !testedDoors[counter]) {
                ++counter;
                if (testedDoors[counter]) {
                    --randomInt;
                }
            }
            // End of magic
            if (build) {
                // Set the door as active
                activeDoors[counter] = true;
                ++branches;
                branched = true;
            } else {
                // Set the door as inactive
                activeDoors[counter] = false;
            }
            // Remove the door from the list of doors we can still activate
            testedDoors[counter] = false;
            --numberOfDoors;
        }
    }

    /**
     * Constructs this room based on the blueprint in the given file and ensures that it will have a
     * door leading to the room that triggered the creation of this one.
     *
     * @param fileName the file holding the blueprint
     * @param creator  the room that requested the creation of this one
     */
    private void constructRoomFromBlueprint(String fileName, Room creator) {
        // Get the blueprint out of the file
        ArrayList<String> blueprint = readFile(fileName);
        // Randomly choose doors to activate in addition to the door leading to the creator room
        activateRandomDoors(blueprint, creator);
        // Read the blueprint character by character and add the appropriate tiles to the room
        for (int row = 0; row < blueprint.size(); ++row) {
            for (int col = 0; col < blueprint.get(row).length(); ++col) {
                Image     img  = spriteTable.get("" + blueprint.get(row).charAt(col));
                StackPane cell = new StackPane();
                // By setting min and max size to the same thing, the StackPane will always take
                // up the same amount of space in the GridPane
                cell.setMaxSize(CELL_SIZE, CELL_SIZE);
                cell.setMinSize(CELL_SIZE, CELL_SIZE);
                switch (blueprint.get(row).charAt(col)) {
                case 'E':
                    addDoor(img, cell, Direction.EAST);
                    break;
                case 'W':
                    addDoor(img, cell, Direction.WEST);
                    break;
                case 'S':
                    addDoor(img, cell, Direction.SOUTH);
                    break;
                case 'N':
                    addDoor(img, cell, Direction.NORTH);
                    break;
                case 'x':
                    addWall(img, cell);
                    break;
                case '.':
                    addFloor(img, cell);
                    break;
                case ' ':
                    // Add nothing
                    break;
                default:
                    System.err.println("Detected an invalid character in room " + fileName
                                       + "! Please check file for errors!");
                }
                add(cell, col, row);
            }
        }
    }

    /**
     * Adds a door in the given direction. If a door cannot be created in that direction, a wall
     * will be placed there instead, but it will still be added to the door table.
     *
     * @param image     the image of the door
     * @param cell      the cell of where to put the door
     * @param direction the direction of the door
     */
    private void addDoor(Image image, StackPane cell, Direction direction) {
        boolean valid = level.getRoomExistsOrAvailable(this, direction);
        if (!valid) {
            addWall(spriteTable.get("x"), cell);
        } else {
            if (activeDoors[direction.toValue()]) {
                Room room = level.getRoomIfExists(this, direction);
                generateDoor(image, cell, direction, room);
            } else {
                addWall(spriteTable.get("x"), cell);
            }
        }
        doors.get(direction).add(cell);
    }

    /**
     * Adds a floor.
     *
     * @param image the image of the floor
     * @param cell  the cell where to place the floor
     */
    private void addFloor(Image image, StackPane cell) {
        ImageView imageView = new ImageView(image);
        cell.getChildren().add(imageView);
    }

    /**
     * Adds a wall.
     *
     * @param image the image of the wall
     * @param cell  the cell where to place the wall
     */
    private void addWall(Image image, StackPane cell) {
        Wall w = new Wall(image, true);
        bodies.add(w);
        cell.getChildren().add(w);
    }

    /**
     * Generates a door in the given direction leading to the room given by {@code destination}.
     *
     * @param image       the image of the door
     * @param cell        the cell where to put the door
     * @param direction   the direction of the door
     * @param destination the room the door leads to
     */
    private void generateDoor(Image image, StackPane cell, Direction direction, Room destination) {
        if (destination == null) {
            Door d = new Door(image, true);
            bodies.add(d);
            cell.getChildren().add(d);
            level.queueLinkRoom(this, direction, d);
        } else {
            Door d = new Door(image, true, destination);
            bodies.add(d);
            cell.getChildren().add(d);
            destination.createDoor(direction.opposite(), this);
        }
    }

    /**
     * Creates a door from this room to the given room on the side of this room specified by
     * {@code direction}.
     *
     * @param direction the side of the room to generate the door on
     * @param to        the room the door will lead to
     */
    public void createDoor(Direction direction, Room to) {
        ArrayList<StackPane> doorList = doors.get(direction);
        if (to.distanceFromEntrance + 1 < distanceFromEntrance) {
            distanceFromEntrance = to.distanceFromEntrance + 1;
        }
        for (StackPane p : doorList) {
            for (Node child : p.getChildren()) {
                bodies.remove(child);
            }
            p.getChildren().clear();
            Door d = new Door(spriteTable.get(direction.toLetter()), true, to);
            bodies.add(d);
            p.getChildren().add(d);
        }
    }

    /**
     * Returns a list of all collidables contained in this room.
     *
     * @return ArrayList of Collidables making up this room
     */
    public ArrayList<Collidable> getBodies() {
        return bodies;
    }

    /**
     * Returns a list of all items in this room.
     *
     * @return the items in this room
     */
    public ArrayList<Collectable> getItems() {
        return items;
    }

    /**
     * Adds an item to this room.
     *
     * @param item the item to add
     */
    public void addItem(Collectable item) {
        items.add(item);
    }

    /**
     * Returns a list of all entities in this room.
     *
     * @return the entities in this room
     */
    public ArrayList<Entity> getEntities() {
        return entities;
    }

    /**
     * Adds an entity to this room.
     *
     * @param entity the entity to add
     */
    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    /**
     * Returns position of this room in the level containing it.
     *
     * @return Point2D representing the position of this room in the level
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * Checks if this room is the exit for its level.
     *
     * @return true if this room is the exit
     */
    public boolean isExit() {
        return exit;
    }

    /**
     * Checks if this room is the entrance for its level.
     *
     * @return true if this room is the entrance
     */
    public boolean isEntrance() {
        return entrance;
    }

    /**
     * Returns the number of doors separating this room from the entrance room.
     *
     * @return number of doors between this room and the entrance
     */
    public int getDistanceFromEntrance() {
        return distanceFromEntrance;
    }

    /**
     * @return the doors
     */
    public EnumMap<Direction, ArrayList<StackPane>> getDoors() {
        return doors;
    }

    /**
     * @return the activeDoors
     */
    public boolean[] getActiveDoors() {
        return activeDoors;
    }
}
