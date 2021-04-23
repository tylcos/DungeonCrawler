package utilities;

import java.awt.geom.Point2D;

public final class MathUtil {
    public static final double TAU = 2 * Math.PI;

    private MathUtil() { }

    public static double getAngle(Point2D vector) {
        return (Math.atan2(vector.getY(), vector.getX()) + TAU) % TAU;
    }
}
