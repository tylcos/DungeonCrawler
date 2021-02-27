import core.DungeonCrawlerDriver;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import static org.testfx.api.FxAssert.verifyThat;

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
}