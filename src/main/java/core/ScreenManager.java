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

    static {
        updateScreen();
    }

    private static Point2D screenDimensions;
    private static Point2D screenCenter;

    public static Point2D getScreenDimensions() {
        return screenDimensions;
    }

    public static Point2D getScreenCenter() {
        return screenCenter;
    }

    public static Point2D gameToScreen(Point2D gameCoordinates) {
        return gameCoordinates.add(screenCenter);
    }

    public static Point2D screenToGame(Point2D screenCoordinates) {
        return screenCoordinates.subtract(screenCenter);
    }

    public static void updateScreen() {
        screenDimensions = new Point2D(SceneManager.getStage().getWidth(),
                                       SceneManager.getStage().getHeight());
        screenCenter = new Point2D(SceneManager.getStage().getWidth() * .5d,
                                   SceneManager.getStage().getHeight() * .5d);
    }
}
