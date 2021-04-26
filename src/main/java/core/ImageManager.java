package core;

import game.level.Level;
import javafx.geometry.Point2D;
import javafx.scene.image.*;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.function.Supplier;

public final class ImageManager {
    private static final HashMap<String, InputStream> DATA   = new HashMap<>();
    private static final HashMap<String, Image>       IMAGES = new HashMap<>();

    // Load any images beforehand for better performance
    static {
        // Preload all images in a background thread for better performance while running
        new Thread(() -> {
            Level.ENEMIES.forEach(tier -> tier.forEach(Supplier::get));
            Level.COLLECTABLES.forEach(Supplier::get);
        }).start();
    }

    private ImageManager() { }

    public static Image getImage(String name) {
        if (IMAGES.containsKey(name)) {
            return IMAGES.get(name);
        }

        Image image = new Image(getInputStream(name));
        IMAGES.put(name, image);
        return image;
    }

    public static Image getImage(String name, Point2D dimensions, boolean preserveRatio) {
        return getImage(name, (int) dimensions.getX(), (int) dimensions.getY(), preserveRatio);
    }

    public static Image getImage(String name, int width, int height, boolean preserveRatio) {
        String key = String.format("%s_%d_%d_%b", name, width, height, preserveRatio);
        if (IMAGES.containsKey(key)) {
            return IMAGES.get(key);
        }

        Image image = new Image(getInputStream(name), width, height, preserveRatio, false);
        IMAGES.put(key, image);
        return image;
    }

    public static Image getSprite(String name, int x, int y, int spacing) {
        return getSprite(name, x, y, spacing, spacing, 0, 1d);
    }

    public static Image getSprite(String name, int x, int y, int spacing, double scale) {
        return getSprite(name, x, y, spacing, spacing, 0, scale);
    }

    public static Image getSprite(String name, int x, int y, int spacing, int crop, double scale) {
        return getSprite(name, x, y, spacing, spacing, crop, scale);
    }

    public static Image getSprite(String name, int x, int y,
                                  int spacingX, int spacingY,
                                  int crop, double scale) {
        String key = String.format("%s_%d_%d_%d_%d_%d_%f", name, x, y, spacingX, spacingY, crop,
                                   scale);
        if (IMAGES.containsKey(key)) {
            return IMAGES.get(key);
        }

        Image sheet = getImage(name);
        if (Math.abs(scale - 1d) > 1e-9) {
            sheet = getImage(name, (int) (sheet.getWidth() * scale),
                             (int) (sheet.getHeight() * scale), false);
        }

        int scaledSpacingX = (int) (scale * spacingX);
        int scaledSpacingY = (int) (scale * spacingY);
        int scaledCrop     = (int) (scale * crop);
        int trueSizeX      = scaledSpacingX - 2 * scaledCrop;
        int trueSizeY      = scaledSpacingY - 2 * scaledCrop;
        if (trueSizeX <= 0 || trueSizeY <= 0) {
            throw new IllegalArgumentException("Invalid crop: " + crop);
        }

        WritableImage sprite      = new WritableImage(trueSizeX, trueSizeY);
        PixelWriter   pixelWriter = sprite.getPixelWriter();
        pixelWriter.setPixels(0, 0, trueSizeX, trueSizeY, sheet.getPixelReader(),
                              scaledSpacingX * x + scaledCrop, scaledSpacingY * y + scaledCrop);

        IMAGES.put(key, sprite);
        return sprite;
    }

    private static InputStream getInputStream(String name) {
        try {
            if (DATA.containsKey(name)) {
                InputStream inputStream = DATA.get(name);
                inputStream.reset();
                return inputStream;
            }

            InputStream fis = getURL(name).openStream();
            InputStream bis = new ByteArrayInputStream(fis.readAllBytes());
            fis.close();

            DATA.put(name, bis);
            return bis;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Failed to load: /images/" + name);
        }
    }

    private static URL getURL(String name) {
        URL nameURL = ImageManager.class.getResource("/images/" + name);

        if (nameURL != null) {
            return nameURL;
        }

        throw new IllegalArgumentException("Resource does not exist: /images/" + name);
    }
}
