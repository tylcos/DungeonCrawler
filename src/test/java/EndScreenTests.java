import core.GameDriver;
import core.SceneManager;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import static org.junit.Assert.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;

public class EndScreenTests extends ApplicationTest {

    // Auto starts on the win screen
    @Before
    public void start() throws Exception {
        launch(GameDriver.class, "--scene=END");
        assertEquals("Failed to load win screen.",
                     SceneManager.END, SceneManager.getSceneName());
    }

    @Test
    public void testWinMessageVisible() {
        verifyThat("You Escaped The Dungeon!", NodeMatchers.isVisible());
    }

    @Test
    public void testPlayAgainVisible() {
        verifyThat("Play Again", NodeMatchers.isVisible());
    }

    @Test
    public void testExitVisible() {
        verifyThat("Exit Game", NodeMatchers.isVisible());
    }
}