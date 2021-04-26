package utilities;

import javafx.animation.AnimationTimer;

import java.util.Timer;
import java.util.function.Consumer;

/**
 * Timer utilities used for scheduling events
 */
public final class TimerUtil {
    private static final Timer TIMER = new Timer();

    private TimerUtil() { }

    /**
     * Schedule an action to run after a delay.
     *
     * @param delay  the delay before running the action
     * @param action the action to run
     */
    public static void schedule(double delay, Runnable action) {
        lerp(delay, t -> { }, action);
    }

    /**
     * Calls an action every frame with a parameterized time [0-1].
     * Used for fading effects or changing any value linearly over time.
     *
     * @param totalTime The total time in seconds the timer will run for
     * @param action    An action that gets called every frame
     */
    public static void lerp(double totalTime, Consumer<Double> action) {
        lerp(totalTime, action, null);
    }

    /**
     * Calls an action every frame with a parameterized time [0-1] and a finish action once done.
     * Used for fading effects or changing any value linearly over time.
     *
     * @param totalTime The total time in seconds the timer will run for
     * @param action    An consumer that gets called every frame
     * @param onFinish  A runnable that gets called when the timer ends
     */
    public static void lerp(double totalTime, Consumer<Double> action, Runnable onFinish) {
        new AnimationTimer() {
            private long startTime = System.nanoTime();
            private double time;

            public void handle(long now) {
                time = (now - startTime) * 1e-9d;

                if (time > totalTime) {
                    action.accept(1d);
                    stop();

                    if (onFinish != null) {
                        onFinish.run();
                    }
                } else {
                    action.accept(time / totalTime);
                }
            }
        }.start();

        action.accept(0d);
    }
}
