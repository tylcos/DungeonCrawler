package utilities;

import javafx.geometry.Point2D;

import java.util.Random;

/**
 * Provides utility for generating random values need by the game.
 */
public final class RandomUtil {
    private static final Random RAND = new Random();

    // To abide by the checkstyle expecting 2 indents for the strings instead of 3
    // @formatter:off
    private static final String[] USER_NAMES = {
        "Bishop Walt",
        "Cardinal Guntard",
        "Chancellor Solomon",
        "Chinda The Mammoth",
        "Count Robert",
        "Emperor Trustram",
        "Graidy Longtail",
        "Jeoddrinyg Lord Of The Red",
        "Orruntig The Dark",
        "Prince Consort Ogier",
        "Prince Tericius",
        "Qekog The Voiceless One",
        "Reeve Emericus",
        "Reeve Galien",
        "Sir Sym",
        "Vicar Maynard",
        "Viscount Audemar",
        "Xonem Lord Of Ice"
    };

    private static final String[] ROOM_BLUEPRINTS = {
        "/rooms/rectangle.room",
        "/rooms/cross.room",
        "/rooms/vertical.room",
        "/rooms/horizontal.room"
    };
    // @formatter:on

    private RandomUtil() {
    }

    /**
     * Returns a random name.
     *
     * @return a random name
     */
    public static String getRandomName() {
        return USER_NAMES[RAND.nextInt(USER_NAMES.length)];
    }

    /**
     * Returns a room blueprint file path.
     *
     * @return a room blueprint file path
     */
    public static String getRandomRoomBlueprint() {
        return ROOM_BLUEPRINTS[RAND.nextInt(ROOM_BLUEPRINTS.length)];
    }

    /**
     * Generates a random double within [0, 1).
     *
     * @return a random double within [0, 1).
     */
    public static double get() {
        return RAND.nextDouble();
    }

    /**
     * Generates a random double within [min, max).
     *
     * @param min the minimum bound
     * @param max the maximum bound
     * @return a random double within [min, max)
     */
    public static double get(double min, double max) {
        return RAND.nextDouble() * (max - min) + min;
    }

    /**
     * Generates a random integer within [0, max).
     *
     * @param max the maximum bound
     * @return a random integer within [0, max)
     */
    public static int getInt(int max) {
        return RAND.nextInt(max);
    }

    /**
     * Generates a random integer within [min, max).
     *
     * @param min the minimum bound
     * @param max the maximum bound
     * @return a random integer within [min, max)
     */
    public static int getInt(int min, int max) {
        return RAND.nextInt(max - min) + min;
    }

    /**
     * Generates a random boolean with 50% chance of being true
     *
     * @return a random boolean
     */
    public static boolean getBoolean() {
        return RAND.nextBoolean();
    }

    /**
     * Generates a random boolean with chance of being true
     *
     * @param chance Probability of returning true
     * @return a random boolean
     */
    public static boolean getBoolean(double chance) {
        return RAND.nextDouble() < chance;
    }

    /**
     * Generates a random Point2D within <[-max, max), [-max, max)>
     *
     * @param max the bound for x and y
     * @return a random Point2D
     */
    public static Point2D getPoint2D(int max) {
        return new Point2D(getInt(-max, max), getInt(-max, max));
    }

    /**
     * Generates a random Point2D within <[0, xMax), [0, yMax)>
     *
     * @param xMax the maximum bound for x
     * @param yMax the maximum bound for y
     * @return a random Point2D
     */
    public static Point2D getPoint2D(int xMax, int yMax) {
        return new Point2D(getInt(xMax), getInt(yMax));
    }

    /**
     * Generates a random Point2D within <[xMin, xMax), [yMin, yMax)>
     *
     * @param xMin the minimum bound for x
     * @param xMax the maximum bound for x
     * @param yMin the minimum bound for y
     * @param yMax the maximum bound for y
     * @return a random Point2D
     */
    public static Point2D getPoint2D(int xMin, int xMax, int yMin, int yMax) {
        return new Point2D(getInt(xMin, xMax), getInt(yMin, yMax));
    }
}
