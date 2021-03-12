import core.DungeonCrawlerDriver;
import core.SceneManager;
import org.junit.Before;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.assertEquals;

public class GameScreenTests extends ApplicationTest {

    // Auto starts on the game screen
    @Before
    public void start() throws Exception {
        launch(DungeonCrawlerDriver.class, "--scene=GAME");
        assertEquals(SceneManager.getSceneName(), SceneManager.GAME);
    }
}