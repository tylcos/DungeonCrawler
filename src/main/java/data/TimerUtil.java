package data;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Timer utilities used for scheduling events
 */
public final class TimerUtil {
    private static final Timer TIMER = new Timer();

    private TimerUtil() { }

    public static void schedule(Runnable action, long delay) {
        TIMER.schedule(new RunnableTimerTask(action), delay);
    }

    /**
     * Wrapper to convert a Runnable into a TimerTask
     */
    private static final class RunnableTimerTask extends TimerTask {
        private Runnable action;

        private RunnableTimerTask(Runnable action) {
            this.action = action;
        }

        @Override
        public void run() {
            action.run();
        }
    }
}
