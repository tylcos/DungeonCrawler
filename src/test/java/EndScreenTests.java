import core.GameDriver;
import core.SceneManager;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import views.EndScreen;

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

    @Test
    public void testKillLabelExist() {
        assertEquals(0, EndScreen.getTotalKill());
    }

    @Test
    public void testPotionLabelExist() {
        assertEquals(0, EndScreen.getTotalPotionsObtained());
    }

    @Test
    public void testNukeLabelExist() {
        assertEquals(0, EndScreen.getTotalNukesUsed());
    }

    @Test
    public void testMonsterKillIncreased() {
        EndScreen.addTotalKilled();
        assertEquals(EndScreen.getTotalKill(), 1);
    }

    @Test
    public void testTotalNukeIncreased() {
        EndScreen.addTotalNukesUsed();
        assertEquals(EndScreen.getTotalNukesUsed(), 1);
    }

    @Test
    public void testTotalPotionsIncreased() {
        EndScreen.addTotalPotionsObtained();
        assertEquals(EndScreen.getTotalPotionsObtained(), 1);
    }
}