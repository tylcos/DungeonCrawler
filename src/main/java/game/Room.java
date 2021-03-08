package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * Room is a GridPane that displays a given room as a grid of Labels.
 * 
 * Room also contains all data relevant to a room, like the items and enemies
 * inside of it.
 * 
 * !!! THIS HAS ONLY BEEN TESTED ON RECTANGULAR ROOMS. DO NOT ATTEMPT TO CREATE
 * NON-RECTANGULAR ROOMS !!!
 */
public class Room extends GridPane {
    /*
     * The chance of connecting a doorway to another room is BRANCH_CHANCE *
     * (BRANCH_TAX ^ branches) * (DISTANCE_TAX ^ (distanceFromEntrance -
     * level.MIN_END_DISTANCE)).
     * 
     * BRANCH_CHANCE is the base chance of branching.
     * 
     * BRANCH_TAX is multiplied onto the base chance for every existing branch.
     * 
     * DISTANCE_TAX is multiplied onto the base chance for every additional doorway
     * separating this room from the entrance past level.MIN_END_DISTANCE.
     * 
     * Higher numbers mean higher chance of branching. All chances must be between
     * 0.0 (exclusive) and 1.0 (exclusive).
     * 
     * At least one branch will always be created if an exit does not exist in this
     * level yet.
     */
    public static final float BRANCH_CHANCE = 0.3f;
    public static final float BRANCH_TAX = 0.4f;
    public static final float DISTANCE_TAX = 0.9f;
    private int branches = 0;

    // A table to translate between letters in .room files and sprites in game
    @SuppressWarnings("serial")
    private static Hashtable<String, Image> spriteTable = new Hashtable<String, Image>() {
        {
            put(".", new Image(Room.class.getResource("/images/debug square.png").toString()));
            put("x", new Image(Room.class.getResource("/images/debug wall.png").toString()));
            put("N", new Image(Room.class.getResource("/images/debug door.png").toString()));
            put("E", new Image(Room.class.getResource("/images/debug door.png").toString()));
            put("S", new Image(Room.class.getResource("/images/debug door.png").toString()));
            put("W", new Image(Room.class.getResource("/images/debug door.png").toString()));
        }
    };

    // A cache for all rooms that have been loaded before so that we don't have to
    // read from disk every time.
    private static Hashtable<String,
            ArrayList<String>> roomCache = new Hashtable<String, ArrayList<String>>();

    private Level level; // The level this room belongs to
    private Point2D position; // The position of this room in the level
    private boolean exit; // Whether this room is the exit
    private boolean entrance; // Whether this room is the entrance
    private int distanceFromEntrance = 999; // # of doorways separating this room from the entrance

    // A list of all collidable bodies making up this room
    private ArrayList<Collidable> bodies = new ArrayList<Collidable>();

    // Contains valid door tiles for each direction.
    // Used by createDoor() to add doors to rooms that are already generated.
    @SuppressWarnings("serial")
    private Hashtable<Direction,
            ArrayList<StackPane>> doors = new Hashtable<Direction, ArrayList<StackPane>>() {
                {
                    put(Direction.NORTH, new ArrayList<StackPane>());
                    put(Direction.SOUTH, new ArrayList<StackPane>());
                    put(Direction.EAST, new ArrayList<StackPane>());
                    put(Direction.WEST, new ArrayList<StackPane>());
                }
            };

    // The following are used by generateDoor() for random room connections
    // Checks if a door has been randomly tested to be active
    private boolean[] testedDoors = new boolean[4];
    // Checks if a door has been set as active
    private boolean[] activeDoors = new boolean[4];

    /*
     * TODO Room will also hold data on the locations of items and enemies inside of
     * it for Level to place into layers. Level will only keep one room loaded at a
     * time, so this will allow for object permanance
     */

    /**
     * Constructs a room from a .room file at the given position in the specified
     * level.
     * <p>
     * {@code creator} is assumed to have a doorway into this room.
     * 
     * @param template the file to load the room from
     * @param position the position to make the room at
     * @param level    the level this room was created in
     * @param start    if this is the start room and all doors should be active
     * @param creator  the room that triggered the creation of this one
     */
    public Room(String template, Point2D position, Level level, boolean start, Room creator) {
        super();
        if (creator != null) {
            distanceFromEntrance = creator.distanceFromEntrance + 1;
            if (distanceFromEntrance >= Level.MIN_END_DISTANCE && !level.hasExit()) {
                this.exit = true;
            }
        }
        this.position = position;
        this.level = level;
        if (start) {
            distanceFromEntrance = 0;
            entrance = true;
        }

        // By default GridPanes are aligned to the top-left, which we don't want
        setAlignment(Pos.CENTER);

        ArrayList<String> lines = new ArrayList<String>();
        ArrayList<String> cache = roomCache.get(template);
        if (cache != null) {
            lines = cache;
        } else {
            // Read the file
            InputStream is = Room.class.getResourceAsStream(template);
            if (is == null) {
                System.err.println("Tried to open file " + template + ", but it does not exist!");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            try {
                while (reader.ready()) {
                    lines.add(reader.readLine());
                }
            } catch (IOException e) {
                System.err.println("Error occurred trying to read from " + template);
                e.printStackTrace();
            }
            roomCache.put(template, lines);
        }

        for (int row = 0; row < lines.size(); ++row) {
            for (int col = 0; col < lines.get(row).length(); ++col) {
                switch (lines.get(row).charAt(col)) {
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
                default: // satisfy checkstyle
                    break;
                }
            }
        }

        if (creator != null) {
            Direction creatorDirection = Direction
                    .vectorToDirection(creator.position.subtract(position));
            activeDoors[creatorDirection.toValue()] = true;
            testedDoors[creatorDirection.toValue()] = false;
            ++branches;
        }

        int numberOfDoors = 0;
        for (int i = 0; i < testedDoors.length; ++i) {
            if (testedDoors[i]) {
                ++numberOfDoors;
            }
        }

        boolean branched = false;
        while (numberOfDoors > 0) {
            boolean build = (!level.hasExit() && !branched) || Math.random() < (BRANCH_CHANCE
                    * Math.pow(BRANCH_TAX, branches)
                    * ((Level.MIN_END_DISTANCE < distanceFromEntrance)
                            ? Math.pow(DISTANCE_TAX, distanceFromEntrance - Level.MIN_END_DISTANCE)
                            : 1)); // i love you too checkstyle <3
            Random random = new Random();
            int randomInt = random.nextInt(numberOfDoors);
            int counter = 0;
            while (randomInt > 0 || !testedDoors[counter]) {
                ++counter;
                if (testedDoors[counter]) {
                    --randomInt;
                }
            }
            testedDoors[counter] = false;
            --numberOfDoors;
            if (!build) {
                activeDoors[counter] = false;
            } else {
                activeDoors[counter] = true;
                ++branches;
                branched = true;
            }
        }

        // Place the tiles
        for (int row = 0; row < lines.size(); ++row) {
            for (int col = 0; col < lines.get(row).length(); ++col) {
                if (lines.get(row).charAt(col) == ' ') {
                    continue;
                }
                Image img = spriteTable.get("" + lines.get(row).charAt(col));
                if (img == null) {
                    System.err.println("Detected an invalid character in room " + template
                            + "! Please check file for errors!");
                }

                StackPane cell = new StackPane();
                cell.setMaxSize(100.0, 100.0);
                cell.setMinSize(100.0, 100.0);
                switch (lines.get(row).charAt(col)) {
                case 'E':
                    generateDoor(img, cell, Direction.EAST);
                    break;
                case 'W':
                    generateDoor(img, cell, Direction.WEST);
                    break;
                case 'S':
                    generateDoor(img, cell, Direction.SOUTH);
                    break;
                case 'N':
                    generateDoor(img, cell, Direction.NORTH);
                    break;
                case 'x':
                    addWall(img, cell);
                    break;
                default: // Is floor. We can just put down the image.
                    ImageView imageView = new ImageView(img);
                    cell.getChildren().add(imageView);
                }
                add(cell, col, row);
            }
        }
    }

    /**
     * Constructs a room from a .room file at the given position in the specified
     * level.
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
     * Generates a door.
     *
     * @param image     the image of the door
     * @param cell      the cell of where to put the door
     * @param direction the direction of the door
     */
    private void generateDoor(Image image, StackPane cell, Direction direction) {
        if (entrance) {
            Room room = level.getRoomIfExists(this, direction);
            addDoor(image, cell, direction, room);
        } else if (exit) {
            addWall(spriteTable.get("x"), cell);
        } else {
            boolean valid = level.getRoomExistsOrAvailable(this, direction);
            if (!valid) {
                addWall(spriteTable.get("x"), cell);
            } else {
                if (activeDoors[direction.toValue()]) {
                    Room room = level.getRoomIfExists(this, direction);
                    addDoor(image, cell, direction, room);
                } else {
                    addWall(spriteTable.get("x"), cell);
                }
            }
        }
        doors.get(direction).add(cell);
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
     * Adds the door to the room.
     *
     * @param image       the image of the door
     * @param cell        the cell where to put the door
     * @param direction   the direction of the door
     * @param destination the room the door leads to
     */
    private void addDoor(Image image, StackPane cell, Direction direction, Room destination) {
        if (exit) {
            addWall(spriteTable.get("x"), cell);
        } else {
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
    }

    /**
     * Creates a door from this room to the given room on the side of this room
     * specified by {@code direction}.
     * 
     * @param direction the side of the room to generate the door on
     * @param to        the room the door will lead to
     */
    public void createDoor(Direction direction, Room to) {
        ArrayList<StackPane> doorList = doors.get(direction);
        if (to.distanceFromEntrance + 1 < this.distanceFromEntrance) {
            this.distanceFromEntrance = to.distanceFromEntrance + 1;
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
}
