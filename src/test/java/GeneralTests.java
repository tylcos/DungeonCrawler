import core.*;
import javafx.scene.input.KeyCode;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.base.WindowMatchers;
import utilities.RandomUtil;

import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;

// Could use a better name
public class GeneralTests extends ApplicationTest {

    @Before
    public void start() throws Exception {
        launch(GameDriver.class);
        assertEquals("Failed to load title screen.",
                     SceneManager.TITLE, SceneManager.getSceneName());
    }

    @Test
    public void testWindowExist() {
        verifyThat(window(GameDriver.GAME_TITLE), WindowMatchers.isShowing());
    }

    @Test
    public void testInitialButtonVisible() {
        verifyThat("Start", NodeMatchers.isVisible());
    }

    /**
     * Tests if the SceneManager can load every scene in the game
     * todo implement win/end game screen test
     */
    @Test
    public void testSceneManager() {
        assertEquals(SceneManager.TITLE, SceneManager.getSceneName());
        clickOn("Start");

        assertEquals(SceneManager.CONFIG, SceneManager.getSceneName());
        moveTo("#inputTextName").write("Team Azula");
        clickOn("Start Adventure");

        assertEquals(SceneManager.GAME, SceneManager.getSceneName());
    }

    /**
     * Tests if all necessary resources can be loaded
     */
    @Test
    public void testLoadResources() {
        Class<GameEngine> gameManagerClass = GameEngine.class;

        assertNotNull(gameManagerClass.getResource(SceneManager.TITLE));
        assertNotNull(gameManagerClass.getResource(SceneManager.CONFIG));
        assertNotNull(gameManagerClass.getResource(SceneManager.GAME));
        assertNotNull(gameManagerClass.getResource(SceneManager.END));

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

    /**
     * Tests the InputManager
     */
    @Test
    public void testInputManager() {
        Stream.of(KeyCode.values()).forEach(keyCode -> assertFalse(InputManager.get(keyCode)));

        press(KeyCode.W);
        assertTrue(InputManager.get(KeyCode.W));

        release(KeyCode.W);
        assertFalse(InputManager.get(KeyCode.W));
    }



}