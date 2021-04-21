package core;

import javafx.geometry.Point2D;
import javafx.scene.image.*;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.HashMap;
import java.util.stream.Stream;

public final class ImageManager {
    private static final HashMap<String, InputStream> IMAGES = new HashMap<>();

    // Load any images beforehand for better performance
    static {
        // Preload all images in a background thread for better performance while running
        Thread startupThread = new Thread(() -> {
            File imagesDir = getPath("");

            try (Stream<Path> paths = Files.walk(imagesDir.toPath())) {
                paths.filter(p -> p.toString().endsWith(".png") || p.toString().endsWith(".gif"))
                    .forEach(p -> getInputStream(p.getFileName().toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        startupThread.setDaemon(true);
        startupThread.start();
    }

    private ImageManager() {
    }

    public static Image getImage(String name) {
        return new Image(getInputStream(name));
    }

    public static Image getImage(String name, Point2D dimensions, boolean preserveRatio) {
        return getImage(name, (int) dimensions.getX(), (int) dimensions.getY(), preserveRatio);
    }

    public static Image getImage(String name, int width, int height, boolean preserveRatio) {
        return new Image(getInputStream(name), width, height, preserveRatio, false);
    }

    public static Image getSprite(String name, int x, int y, int spacing) {
        Image sheet = getImage(name);

        WritableImage wr = new WritableImage(spacing, spacing);
        PixelWriter   pw = wr.getPixelWriter();
        pw.setPixels(0, 0, spacing, spacing, sheet.getPixelReader(), spacing * x, spacing * y);

        return wr;
    }

    public static Image getSprite(String name, int x, int y, int spacing, double scale) {
        Image sheet = getImage(name);
        sheet = getImage(name, (int) (sheet.getWidth() * scale),
                         (int) (sheet.getHeight() * scale), false);

        int scaledSpacing = (int) (spacing * scale);
        WritableImage wr = new WritableImage(scaledSpacing, scaledSpacing);
        PixelWriter   pw = wr.getPixelWriter();
        pw.setPixels(0, 0, scaledSpacing, scaledSpacing, sheet.getPixelReader(),
                     scaledSpacing * x, scaledSpacing * y);

        return wr;
    }

    private static InputStream getInputStream(String name) {
        try {
            if (IMAGES.containsKey(name)) {
                InputStream inputStream = IMAGES.get(name);
                inputStream.reset();
                return inputStream;
            }

            InputStream fis = new FileInputStream(getPath(name));
            InputStream bis = new ByteArrayInputStream(fis.readAllBytes());
            fis.close();

            IMAGES.put(name, bis);
            return bis;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Failed to load: images/" + name);
        }
    }

    private static File getPath(String name) {
        try {
            URL nameURL = ImageManager.class.getClassLoader().getResource("images/" + name);
            assert nameURL != null;

            return Paths.get(nameURL.toURI()).toFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Failed to load: images/" + name);
        }
    }
}
