import core.GameDriver;
import core.SceneManager;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.ComboBoxMatchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;

public class ConfigScreenTests extends ApplicationTest {

    // Auto starts on the config screen
    @Before
    public void start() throws Exception {
        launch(GameDriver.class, "--scene=CONFIG");
        assertEquals("Failed to load config screen.",
                     SceneManager.CONFIG, SceneManager.getSceneName());
    }

    @Test
    public void testConfigFieldsExist() {
        verifyThat("Name:", NodeMatchers.isNotNull());
        verifyThat("Difficulty:", NodeMatchers.isNotNull());
        verifyThat("Weapon:", NodeMatchers.isNotNull());
    }

    @Test
    public void testEmptyName() {
        moveTo("#inputTextName").push(KeyCode.BACK_SPACE);
        clickOn("Start Adventure");

        assertEquals(SceneManager.CONFIG, SceneManager.getSceneName());
    }

    @Test
    public void testWhitespaceName() {
        moveTo("#inputTextName").write(" ");
        clickOn("Start Adventure");

        assertEquals(SceneManager.CONFIG, SceneManager.getSceneName());
    }

    @Test
    public void testValidName() {
        moveTo("#inputTextName").write("Team Azula");
        clickOn("Start Adventure");

        assertEquals(SceneManager.GAME, SceneManager.getSceneName());
        assertTrue(lookup("#uiInfoText").<TextArea>query().getText().contains("Name: Team Azula"));
    }

    @Test
    public void testConfigScreenObjectVisible() {
        verifyThat("Name:", NodeMatchers.isVisible());
        verifyThat("Difficulty:", NodeMatchers.isVisible());
        verifyThat("Weapon:", NodeMatchers.isVisible());
    }

    @Test
    public void testWeaponContains() {
        ComboBox<String> weaponTest = lookup("#inputWeapon").queryComboBox();

        verifyThat(weaponTest, ComboBoxMatchers.containsItems(
                "Spear", "Sword", "Bow", "Staff"));
    }

    @Test
    public void testDifficultyContains() {
        ComboBox<String> weaponTest = lookup("#inputDifficulty").queryComboBox();

        verifyThat(weaponTest, ComboBoxMatchers.containsItems("Boring", "Normal", "Hard"));
    }
}