package data;

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

    private static final String[] ENEMY_SPRITES = {
        "/images/enemy1.gif",
        "/images/enemy2.gif",
        "/images/enemy3.gif"
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
     * Returns a random enemy file path.
     *
     * @return a random name file path
     */
    public static String getRandomEnemy() {
        return ENEMY_SPRITES[RAND.nextInt(ENEMY_SPRITES.length)];
    }

    /**
     * Generates a random number within [0, max).
     *
     * @param max the maximum bound
     * @return a random number between 0 and maximum, exclusive
     */
    public static int getInt(int max) {
        return RAND.nextInt(max);
    }

    /**
     * Generates a random number within [min, max).
     *
     * @param min the minimum bound
     * @param max the maximum bound
     * @return a random number between minimum, inclusive, and maximum, exclusive
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
}
