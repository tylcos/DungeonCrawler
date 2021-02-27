import core.DungeonCrawlerDriver;
import core.GameManager;
import core.SceneManager;
import data.RandomNames;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.base.WindowMatchers;
import org.testfx.matcher.control.ComboBoxMatchers;

import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;

public class DungeonCrawlerTest extends ApplicationTest {
    @Override
    public void start(Stage primaryStage) {
        new DungeonCrawlerDriver().start(primaryStage);
    }

    @Test
    public void testConfigFieldsExist() {
        clickOn("Start");
        verifyThat("Name:", NodeMatchers.isNotNull());
        verifyThat("Difficulty:", NodeMatchers.isNotNull());
        verifyThat("Weapon:", NodeMatchers.isNotNull());
    }

    @Test
    public void testEmptyName() {
        clickOn("Start");
        moveTo("#inputTextName").press(KeyCode.BACK_SPACE);
        clickOn("Start Adventure");

        assertEquals(SceneManager.getSceneName(), SceneManager.CONFIG);
    }

    @Test()
    public void testWhitespaceName() {
        clickOn("Start");
        moveTo("#inputTextName").write(" ");
        clickOn("Start Adventure");

        assertEquals(SceneManager.getSceneName(), SceneManager.CONFIG);
    }

    @Test
    public void testValidName() {
        clickOn("Start");
        moveTo("#inputTextName").write("Azula");
        clickOn("Start Adventure");

        assertEquals(SceneManager.getSceneName(), SceneManager.GAME);
        assertTrue(lookup("#uiInfoText").<TextArea>query().getText().contains("Name: Azula"));
    }

    @Test
    public void testWindowExist() {
        verifyThat(window("Dungeon Crawler"), WindowMatchers.isShowing());
    }

    @Test
    public void testInitialButtonVisible() {
        verifyThat("Start", NodeMatchers.isVisible());
    }

    @Test
    public void testConfigScreenObjectVisible() {
        clickOn("Start");
        verifyThat("Name:", NodeMatchers.isVisible());
        verifyThat("Difficulty:", NodeMatchers.isVisible());
        verifyThat("Weapon:", NodeMatchers.isVisible());
    }

    @Test
    public void testWeaponContains() {
        clickOn("Start");
        ComboBox<String> weaponTest = lookup("#inputWeapon").queryComboBox();

        verifyThat(weaponTest, ComboBoxMatchers.containsItems(
                "Knife", "Axe", "Sword", "Bow", "Staff"));
    }

    @Test
    public void testDifficultyContains() {
        clickOn("Start");
        ComboBox<String> weaponTest = lookup("#inputDifficulty").queryComboBox();

        verifyThat(weaponTest, ComboBoxMatchers.containsItems("Boring", "Normal", "Hard"));
    }

    /**
     * Tests if the SceneManager can load every scene in the game
     */
    @Test
    public void testSceneManager() {
        assertEquals(SceneManager.getSceneName(), SceneManager.TITLE);
        clickOn("Start");

        assertEquals(SceneManager.getSceneName(), SceneManager.CONFIG);
        moveTo("#inputTextName").write("Azula");
        clickOn("Start Adventure");

        assertEquals(SceneManager.getSceneName(), SceneManager.GAME);
    }

    /**
     * Tests if all necessary resources can be loaded
     */
    @Test
    public void testLoadResources() {
        assertNotNull(GameManager.class.getResource(SceneManager.TITLE));
        assertNotNull(GameManager.class.getResource(SceneManager.CONFIG));
        assertNotNull(GameManager.class.getResource(SceneManager.GAME));

        assertNotNull(GameManager.class.getResource("/images/IntroPage.gif"));
        assertNotNull(GameManager.class.getResource("/images/Player.png"));
        assertNotNull(GameManager.class.getResource("/images/Title.gif"));
        assertNotNull(GameManager.class.getResource("/images/Title with background.gif"));

        assertNotNull(GameManager.class.getResource("/styles/ConfigStyle.css"));
        assertNotNull(GameManager.class.getResource("/styles/TitleStyle.css"));
        assertNotNull(GameManager.class.getResource("/styles/GameStyle.css"));
    }

    /**
     * Tests all the current random utilities in the game
     */
    @Test
    public void testRandomUtil() {
        assertTrue(RandomNames.getRandomName().length() > 2);
    }
}