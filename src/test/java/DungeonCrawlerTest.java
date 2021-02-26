import org.junit.Test;
import driver.DungeonCrawlerDriver;
import org.testfx.api.FxRobotException;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import static org.testfx.api.FxAssert.verifyThat;

import javafx.stage.Stage;

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
}