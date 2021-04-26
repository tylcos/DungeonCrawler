package utilities;

import core.ImageManager;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public final class AnimationController {
    private static List<AnimationController> controllers = new ArrayList<>();

    private ImageView imageView;
    private String    spriteSheet;

    private int    row;
    private int    cols;
    private int    spacingX;
    private int    spacingY;
    private double scale;
    private int    crop;
    private int    totalImages;

    private Runnable onFinish;

    private int i;

    private static final long FPS = 10;

    static {
        new AnimationTimer() {
            private long lastFrameTime = System.nanoTime();
            private long frameLength = 1_000_000_000L / FPS;

            public void handle(long now) {
                long dt = now - lastFrameTime;

                if (dt > frameLength) {
                    for (int i = 0; i < controllers.size(); ) {
                        AnimationController controller = controllers.get(i);
                        if (controller.imageView.isVisible()) {
                            controller.updateImage();
                            i++;
                        } else {
                            if (controller.onFinish != null) {
                                controller.onFinish.run();
                            }

                            controllers.remove(i);
                        }
                    }

                    lastFrameTime = now;
                }
            }
        }.start();
    }

    private AnimationController() { }

    public static AnimationController add(ImageView imageView, String spriteSheet,
                                          int row, int rows, int cols, int spacing, int crop,
                                          double scale) {
        return add(imageView, spriteSheet, row, rows, cols, spacing, spacing, crop, scale);
    }

    public static AnimationController add(ImageView imageView, String spriteSheet,
                                          int row, int rows, int cols,
                                          int spacingX, int spacingY,
                                          int crop, double scale) {
        AnimationController ac = new AnimationController();
        ac.imageView   = imageView;
        ac.spriteSheet = spriteSheet;
        ac.row         = row;
        ac.cols        = cols;
        ac.crop        = crop;
        ac.spacingX    = spacingX;
        ac.spacingY    = spacingY;
        ac.scale       = scale;

        ac.totalImages = rows * cols;

        ac.updateImage();
        controllers.add(ac);

        return ac;
    }

    private void updateImage() {
        if (onFinish != null && i == totalImages - 1) {
            onFinish.run();
            controllers.remove(this);
            return;
        }

        i = ++i % totalImages;
        Image image = ImageManager.getSprite(spriteSheet, (i % cols), row + i / cols,
                                             spacingX, spacingY, crop, scale);
        imageView.setImage(image);
    }

    public void setOnFinish(Runnable onFinish) {
        this.onFinish = onFinish;
    }
}
