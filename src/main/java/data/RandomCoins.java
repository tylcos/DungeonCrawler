package data;

import java.util.Random;

public class RandomCoins {
    private static final Random RAND = new Random();
    private static final boolean[] RandomCoins = {
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
            true,
    };
    public static boolean getCoin() {
        return RandomCoins[RAND.nextInt(RandomCoins.length)];
    }
}
