package data;

import java.util.Random;

/**
 * Generates a random coin (whether or not it is show based off of true/false).
 */
public class RandomCoins {
    private static final Random RAND = new Random();
    private static final boolean[] RANDOM_COINS = {
        true,
        false,
        true,
        true,
        false,
        false,
        false,
        true,
        true,
        true,
        false,
        false,
        true,
        false,
        true,
        false,
        true,
        false,
        true,
        true,
        false,
        true,
        true,
        true,
        true,
        true
    };

    /**
     * Gets a random state of which a coin is displayed.
     * @return if true, then coin is displayed; if false, coin isn't displayed
     */
    public static boolean getCoin() {
        return RANDOM_COINS[RAND.nextInt(RANDOM_COINS.length)];
    }
}
