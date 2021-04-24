package game.level;

import javafx.geometry.Point2D;

/**
 * Direction describes one of the four cardinal compass directions and the unit
 * vector associated with them, as well as other useful, related data.
 */
public enum Direction {
    NORTH(0, 1), EAST(1, 0), SOUTH(0, -1), WEST(-1, 0);

    private final Point2D vector;

    Direction(int x, int y) {
        vector = new Point2D(x, y);
    }

    /**
     * Describes this Direction as a Point2D unit vector.
     * 
     * @return the unit vector for this Direction
     */
    public Point2D vector() {
        return vector;
    }
    
    /**
     * Converts this Point2D unit vector to a Direction
     * @param vec the vector to convert
     * @return the Direction of this vector
     */
    public static Direction vectorToDirection(Point2D vec) {
        for (Direction d : Direction.values()) {
            if (d.vector().equals(vec)) {
                return d;
            }
        }
        return null;
    }

    /**
     * Describes this Direction as a single letter.
     * 
     * @return a letter representing to this Direction
     */
    public char toLetter() {
        switch (this) {
        case NORTH:
            return 'N';
        case EAST:
            return 'E';
        case SOUTH:
            return 'S';
        case WEST:
            return 'W';
        default:
            throw new IllegalStateException("Unexpected value: " + this);
        }
    }

    /**
     * Describes this Direction as a unique integer 0 through 3.
     * 
     * @return 0 for NORTH, 1 for EAST, 2 for SOUTH, 3 for WEST
     */
    public int toValue() {
        switch (this) {
        case NORTH:
            return 0;
        case EAST:
            return 1;
        case SOUTH:
            return 2;
        case WEST:
            return 3;
        default:
            throw new IllegalStateException("Unexpected value: " + this);
        }
    }
    
    /**
     * Describes this number as a unique Direction.
     * 
     * @param value a number 0 through 3
     * @return NORTH for 0, EAST for 1, SOUTH for 2, WEST for 3
     */
    public static Direction valueToDirection(int value) {
        switch (value) {
        case 0:
            return NORTH;
        case 1:
            return EAST;
        case 2:
            return SOUTH;
        case 3:
            return WEST;
        default:
            throw new IllegalStateException("Unexpected value: " + value);
        }
    }

    /**
     * The Direction pointing in the opposite direction of this one.
     * 
     * @return the direction opposite of this one
     */
    public Direction opposite() {
        switch (this) {
        case NORTH:
            return SOUTH;
        case SOUTH:
            return NORTH;
        case EAST:
            return WEST;
        case WEST:
            return EAST;
        default:
            throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}