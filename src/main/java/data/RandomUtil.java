package data;

import javafx.scene.image.Image;

import java.util.Random;

/**
 * Provides utility for generating random values need by the game.
 */
public final class RandomUtil {
    private static final Random RAND = new Random();

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

    private RandomUtil() {
    }

    /**
     * Returns a random name from the String of names.
     *
     * @return a random name from the String of names
     */
    public static String getRandomName() {
        return USER_NAMES[RAND.nextInt(USER_NAMES.length)];
    }

    public static String getRandomRoomBlueprint() {
        return ROOM_BLUEPRINTS[RAND.nextInt(ROOM_BLUEPRINTS.length)];
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

    public static String getRandomEnemy() {
        int randomInt = getInt(0, 4);
        String enemyType = "/images/enemy2.gif";
        switch (randomInt) {
            case 1:
                enemyType = "/images/enemy1.gif";
                break;
            case 2:
                enemyType = "/images/enemy2.gif";
                break;
            case 3:
                enemyType = "images/enemy3.gif";
                break;
            case 4:
                enemyType = "images/invisible.gif";
                break;

        }
        return enemyType;
    }
}
