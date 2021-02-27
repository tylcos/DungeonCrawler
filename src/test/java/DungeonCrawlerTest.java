import javafx.scene.control.ComboBox;
import org.junit.Test;
import driver.DungeonCrawlerDriver;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxRobotException;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.base.WindowMatchers;
import org.testfx.matcher.control.ButtonMatchers;
import org.testfx.matcher.control.ComboBoxMatchers;
import org.testfx.matcher.control.LabeledMatchers;

import static org.testfx.api.FxAssert.assertContext;
import static org.testfx.api.FxAssert.verifyThat;

import org.testfx.matcher.control.TextMatchers;

import static org.junit.Assert.*;


import javafx.stage.Stage;
import view.ConfigScreen;

public class DungeonCrawlerTest extends ApplicationTest {

    @Override
    public void start(Stage primaryStage) throws Exception {
        DungeonCrawlerDriver dcd = new DungeonCrawlerDriver();
        dcd.start(primaryStage);
    }

    @Test
    public void testStartNameFieldExists() {
        clickOn("Start");
        verifyThat("Name", NodeMatchers.isNotNull());
    }

    @Test
    public void testStartDifficultyFieldExists() {
        clickOn("Start");
        verifyThat("Difficulty", NodeMatchers.isNotNull());
    }

    @Test
    public void testStartWeaponFieldExists() {
        clickOn("Start");
        verifyThat("Weapon", NodeMatchers.isNotNull());
    }

    //throws this exception when node is not found
    @Test(expected = FxRobotException.class)
    public void testEmptyName() {
        clickOn("Start");
        clickOn("Go to first room");
        clickOn("Shop");
    }

    @Test(expected = FxRobotException.class)
    public void testWhitespaceName() {
        clickOn("Start");
        clickOn(".text").write(" ");
        clickOn("Go to first room");
        clickOn("Shop");
    }

    @Test
    public void testPlayerNamePopulation() {
        clickOn("Start");
        clickOn(".text").write("Azula");
        clickOn("Go to first room");
        verifyThat(".label", LabeledMatchers.hasText("Name: Azula"));
    }

    @Test
    public void testWindowExist() {
        verifyThat(window("Dungeon Crawler"), WindowMatchers.isShowing());
    }

    @Test
    public void testInitialButtonVisible() {
        verifyThat(".button", NodeMatchers.isVisible());
    }

    @Test
    public void testCongifScreenObejectVisible() {
        clickOn("Start");
        verifyThat(".text", NodeMatchers.isVisible());
        verifyThat("Difficulty", NodeMatchers.isVisible());
        verifyThat("Weapon", NodeMatchers.isVisible());
    }

    @Test
    public void testWeaponContains() {
        int WIDTH = 750;
        int HEIGHT = 550;
        ConfigScreen dcd = new ConfigScreen(WIDTH, HEIGHT);
        ComboBox<String> weaponTest = dcd.getWeaponOptions();
        assertThat(weaponTest, ComboBoxMatchers.containsItems("Knife", "Axe", "Sword"));
    }

    @Test
    public void testDifficultyContains() {
        int WIDTH = 750;
        int HEIGHT = 550;
        ConfigScreen dcd = new ConfigScreen(WIDTH, HEIGHT);
        ComboBox<String> difficultyTest = dcd.getDifficultyOptions();
        assertThat(difficultyTest, ComboBoxMatchers.containsItems("Easy","Medium","Hard"));
    }


}