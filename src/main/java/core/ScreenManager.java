package core;

import javafx.geometry.Point2D;

/**
 * Used to convert between screen, scene, and game coordinates
 */
public final class ScreenManager {
    /**
     * Private constructor so no instances of ScreenManager can be created.
     */
    private ScreenManager() { }

    static {
        updateScreen();
    }

    private static Point2D sceneDimensions;
    private static Point2D sceneCenter;

    /**
     * Converts scene coordinates, where (0,0) is the top left of the window, to game coordinates,
     * where (0,0) is the center of the window.
     *
     * @param sceneCoordinates scene coordinates
     * @return game coordinates
     */
    public static Point2D sceneToGame(Point2D sceneCoordinates) {
        return sceneCoordinates.subtract(sceneCenter);
    }

    /**
     * Converts screen coordinates, where (0,0) is the top left of the screen, to game coordinates,
     * where (0,0) is the center of the window.
     *
     * @param screenCoordinates screen coordinates
     * @return game coordinates
     */
    public static Point2D screenToGame(Point2D screenCoordinates) {
        Point2D sceneCoordinates = SceneManager.getRoot().screenToLocal(screenCoordinates);
        return sceneCoordinates != null ? sceneToGame(sceneCoordinates) : Point2D.ZERO;
    }

    public static void updateScreen() {
        sceneDimensions = new Point2D(SceneManager.getStage().getWidth(),
                                      SceneManager.getStage().getHeight());
        sceneCenter     = new Point2D(SceneManager.getStage().getWidth() * .5d,
                                      SceneManager.getStage().getHeight() * .5d);
    }

    public static double getWidth() {
        return sceneDimensions.getX();
    }

    public static double getHeight() {
        return sceneDimensions.getY();
    }

    /**
     * Returns scale of screen relative to a 1080p display
     *
     * @return scale of screen relative to a 1080p display
     */
    public static double getScale() {
        return sceneDimensions.getY() / 1080d;
    }
}
