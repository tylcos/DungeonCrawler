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

    private int     row;
    private int     cols;
    private int     spacing;
    private double  scale;
    private int     crop;
    private int     totalImages;
    private boolean repeat = true;

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
                        if (controllers.get(i).imageView.isVisible()) {
                            controllers.get(i).updateImage();
                            i++;
                        } else {
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
        AnimationController ac = new AnimationController();
        ac.imageView   = imageView;
        ac.spriteSheet = spriteSheet;
        ac.row         = row;
        ac.cols        = cols;
        ac.crop        = crop;
        ac.spacing     = spacing;
        ac.scale       = scale;

        ac.totalImages = rows * cols;

        ac.updateImage();
        controllers.add(ac);

        return ac;
    }

    private void updateImage() {
        if (!repeat && i == totalImages - 1) {
            imageView.setVisible(false);
            return;
        }

        i = ++i % totalImages;
        Image image = ImageManager.getSprite(spriteSheet, (i % cols), row + i / cols,
                                             spacing, crop, scale);
        imageView.setImage(image);
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }
}
