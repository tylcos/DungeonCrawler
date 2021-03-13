import core.DungeonCrawlerDriver;
import core.SceneManager;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import static org.junit.Assert.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;

public class WinScreenTests extends ApplicationTest {

    // Auto starts on the win screen
    @Before
    public void start() throws Exception {
        launch(DungeonCrawlerDriver.class, "--scene=WIN_SCREEN", "--NoDebug");
        assertEquals("Failed to load win screen.",
                SceneManager.WIN_SCREEN, SceneManager.getSceneName());
    }

    @Test
    public void testWinMessageVisible() {
        verifyThat("You Escaped the Dungeon!", NodeMatchers.isVisible());
    }

    @Test
    public void testPlayAgainVisible() {
        verifyThat("Play Again", NodeMatchers.isVisible());
    }

    @Test
    public void testExitVisible() {
        verifyThat("Exit", NodeMatchers.isVisible());
    }
}