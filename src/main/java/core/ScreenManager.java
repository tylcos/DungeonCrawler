package core;

import javafx.geometry.Point2D;

/**
 * Used to convert between screen coordinates and game coordinates
 */
public final class ScreenManager {
    /**
     * Private constructor so no instances of ScreenManager can be created.
     */
    private ScreenManager() { }

    public static Point2D getScreenDimensions() {
        return new Point2D(SceneManager.getStage().getWidth(),
                           SceneManager.getStage().getHeight());
    }

    public static Point2D getScreenCenter() {
        return new Point2D(SceneManager.getStage().getWidth()  * .5d,
                           SceneManager.getStage().getHeight() * .5d);
    }
}
