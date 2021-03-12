import core.DungeonCrawlerDriver;
import core.GameManager;
import core.SceneManager;
import data.RandomUtil;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.base.WindowMatchers;

import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;

// Could use a better name
public class GeneralTests extends ApplicationTest {

    @Before
    public void start() throws Exception {
        launch(DungeonCrawlerDriver.class, "");
    }

    @Test
    public void testWindowExist() {
        verifyThat(window(DungeonCrawlerDriver.GAME_TITLE), WindowMatchers.isShowing());
    }

    @Test
    public void testInitialButtonVisible() {
        verifyThat("Start", NodeMatchers.isVisible());
    }

    /**
     * Tests if the SceneManager can load every scene in the game
     */
    @Test
    public void testSceneManager() {
        assertEquals(SceneManager.getSceneName(), SceneManager.TITLE);
        clickOn("Start");

        assertEquals(SceneManager.getSceneName(), SceneManager.CONFIG);
        moveTo("#inputTextName").write("Team Azula");
        clickOn("Start Adventure");

        assertEquals(SceneManager.getSceneName(), SceneManager.GAME);
    }

    /**
     * Tests if all necessary resources can be loaded
     */
    @Test
    public void testLoadResources() {
        Class<GameManager> gameManagerClass = GameManager.class;

        assertNotNull(gameManagerClass.getResource(SceneManager.TITLE));
        assertNotNull(gameManagerClass.getResource(SceneManager.CONFIG));
        assertNotNull(gameManagerClass.getResource(SceneManager.GAME));
        assertNotNull(gameManagerClass.getResource(SceneManager.WIN_SCREEN));

        assertNotNull(gameManagerClass.getResource("/images/IntroPage.gif"));
        assertNotNull(gameManagerClass.getResource("/images/Player.png"));
        assertNotNull(gameManagerClass.getResource("/images/Title.gif"));
        assertNotNull(gameManagerClass.getResource("/images/Title with background.gif"));

        assertNotNull(gameManagerClass.getResource("/styles/ConfigStyle.css"));
        assertNotNull(gameManagerClass.getResource("/styles/TitleStyle.css"));
        assertNotNull(gameManagerClass.getResource("/styles/GameStyle.css"));
    }

    /**
     * Tests all the current random utilities in the game
     */
    @Test
    public void testRandomUtil() {
        assertTrue(RandomUtil.getRandomName().length() > 2);
        assertTrue(RandomUtil.getRandomRoomBlueprint().length() > 2);
    }
}