package utilities;

import core.SceneManager;
import core.ScreenManager;
import javafx.scene.effect.*;
import javafx.scene.paint.Color;

/**
 * Game effects used for VFX which are only instantiated once for performance
 */
public final class GameEffects {
    // Blurs title screen
    public static final BoxBlur HORIZONTAL_BLUR = new BoxBlur(100, 0, 1);

    // Blurs and changes color on death
    public static final GaussianBlur GAME_BLUR   = new GaussianBlur(0);
    public static final ColorInput   DEATH_COLOR = new ColorInput(0, 0,
                                                                  ScreenManager.getWidth(),
                                                                  ScreenManager.getHeight(),
                                                                  Color.WHITE);
    public static final Blend        DEATH       = new Blend(BlendMode.COLOR_BURN,
                                                             GAME_BLUR,
                                                             DEATH_COLOR);

    // Adds red edges when the player is damaged
    public static final InnerShadow RED_EDGES = new InnerShadow(200 * ScreenManager.getScale(),
                                                                Color.RED);

    static {
        SceneManager.getStage().widthProperty().addListener((observable, oldValue, newValue) -> {
            DEATH_COLOR.setWidth(newValue.doubleValue());
        });
        SceneManager.getStage().heightProperty().addListener((observable, oldValue, newValue) -> {
            DEATH_COLOR.setHeight(newValue.doubleValue());
        });
    }

    private GameEffects() { }
}
