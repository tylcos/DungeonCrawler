package data;

import javafx.animation.AnimationTimer;

import java.util.function.Consumer;

public class LerpTimer {
    public LerpTimer(double totalTime, Consumer<Double> action) {
        this(totalTime, action, () -> { });
    }

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
    }
}
