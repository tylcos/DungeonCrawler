package core;

import game.level.Level;
import javafx.geometry.Point2D;
import javafx.scene.image.*;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.HashMap;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class ImageManager {
    private static final HashMap<String, InputStream> DATA   = new HashMap<>();
    private static final HashMap<String, Image>       IMAGES = new HashMap<>();

    // Load any images beforehand for better performance
    static {
        // Preload all images in a background thread for better performance while running
        new Thread(() -> {
            File imagesDir = getPath("");

            try (Stream<Path> paths = Files.walk(imagesDir.toPath())) {
                paths.filter(p -> (p.toString().endsWith(".png") || p.toString().endsWith(".gif")))
                    .forEach(p -> getInputStream(p.getFileName().toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }

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
        return getSprite(name, x, y, spacing, 1d);
    }

    public static Image getSprite(String name, int x, int y, int spacing, double scale) {
        return getSprite(name, x, y, spacing, 0, scale);
    }

    public static Image getSprite(String name, int x, int y, int spacing, int crop, double scale) {
        String key = String.format("%s_%d_%d_%d_%d_%f", name, x, y, spacing, crop, scale);
        if (IMAGES.containsKey(key)) {
            return IMAGES.get(key);
        }

        Image sheet = getImage(name);
        if (Math.abs(scale - 1d) > 1e-9) {
            sheet = getImage(name, (int) (sheet.getWidth() * scale),
                             (int) (sheet.getHeight() * scale), false);
        }

        int scaledSpacing = (int) (scale * spacing);
        int scaledCrop    = (int) (scale * crop);
        int trueSize      = scaledSpacing - 2 * scaledCrop;
        if (trueSize <= 0) {
            throw new IllegalArgumentException("Invalid crop: " + crop);
        }

        WritableImage sprite      = new WritableImage(trueSize, trueSize);
        PixelWriter   pixelWriter = sprite.getPixelWriter();
        pixelWriter.setPixels(0, 0, trueSize, trueSize, sheet.getPixelReader(),
                              scaledSpacing * x + scaledCrop, scaledSpacing * y + scaledCrop);

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

            InputStream fis = new FileInputStream(getPath(name));
            InputStream bis = new ByteArrayInputStream(fis.readAllBytes());
            fis.close();

            DATA.put(name, bis);
            return bis;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Failed to load: images/" + name);
        }
    }

    private static File getPath(String name) {
        try {
            URL nameURL = ImageManager.class.getClassLoader().getResource("images/" + name);

            if (nameURL != null) {
                return Paths.get(nameURL.toURI()).toFile();
            }

            throw new IllegalArgumentException("Failed to load: images/" + name);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Failed to load: images/" + name);
        }
    }
}
