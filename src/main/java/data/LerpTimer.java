package data;

import javafx.animation.AnimationTimer;

import java.util.function.Consumer;

/**
 * Timer class that calls an action every frame with a normalized time [0-1].
 * Used for fading effects or changing any value linearly over time.
 */
public class LerpTimer {
    /**
     * Creates a LerpTimer that calls action every frame until the total time is up.
     *
     * @param totalTime The total time in seconds the timer will run for
     * @param action    An action that gets called every frame
     */
    public LerpTimer(double totalTime, Consumer<Double> action) {
        this(totalTime, action, () -> { });
    }

    /**
     * Creates a LerpTimer that calls action every frame until the total time is up with a
     * finish event that gets called when the timer ends.
     *
     * @param totalTime The total time in seconds the timer will run for
     * @param action    An consumer that gets called every frame
     * @param onFinish  A runnable that gets called when the timer ends
     */
    @SuppressWarnings("AnonymousInnerClassMayBeStatic")
    public LerpTimer(double totalTime, Consumer<Double> action, Runnable onFinish) {
        new AnimationTimer() {
            private long startTime = System.nanoTime();
            private double time;

            public void handle(long now) {
                time = (now - startTime) * 1e-9d;

                if (time > totalTime) {
                    onFinish.run();
                    stop();
                } else {
                    action.accept(time / totalTime);
                }
            }
        }.start();

        action.accept(0d);
    }
}
