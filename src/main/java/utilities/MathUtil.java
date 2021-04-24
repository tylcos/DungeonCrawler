package utilities;

import javafx.geometry.Point2D;

public final class MathUtil {
    public static final double TAU = 2 * Math.PI;

    private MathUtil() { }

    public static double getAngle(Point2D vector) {
        return (Math.atan2(vector.getY(), vector.getX()) + TAU) % TAU;
    }

    public static double getAngleDeg(Point2D vector) {
        return Math.toDegrees(getAngle(vector));
    }

    public static Point2D getVector(double angle) {
        return new Point2D(Math.cos(angle), Math.sin(angle));
    }

    public static Point2D getVectorDeg(double angleDeg) {
        return getVector(Math.toRadians(angleDeg));
    }

    public static Point2D getVector(double angle, double scale) {
        return new Point2D(scale * Math.cos(angle), scale * Math.sin(angle));
    }
}
