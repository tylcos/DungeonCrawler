import core.DungeonCrawlerDriver;
import core.GameManager;
import core.SceneManager;
import game.Door;
import game.MainPlayer;
import game.Room;
import javafx.scene.input.KeyCode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.assertEquals;

public class GameScreenTests extends ApplicationTest {

    // Auto starts on the game screen
    @Before
    public void start() throws Exception {
        launch(DungeonCrawlerDriver.class, "--scene=GAME");
        assertEquals(SceneManager.getSceneName(), SceneManager.GAME);
    }

    @Test
    public void testMoveLeft() {
        double initialX = GameManager.getPlayer().getX();
        press(KeyCode.LEFT);
        double finalX = GameManager.getPlayer().getX();
        Assert.assertTrue(finalX < initialX);
    }

    @Test
    public void testMoveRight(){
        double initialX = GameManager.getPlayer().getX();
        press(KeyCode.RIGHT);
        double finalX = GameManager.getPlayer().getX();
        Assert.assertTrue(initialX < finalX);
    }

    @Test
    public void testMoveUp(){
        double initialY = GameManager.getPlayer().getY();
        press(KeyCode.UP);
        double finalY = GameManager.getPlayer().getY();
        Assert.assertTrue(initialY > finalY);
    }

    @Test
    public void testMoveDown(){
        double initialY = GameManager.getPlayer().getY();
        press(KeyCode.DOWN);
        double finalY = GameManager.getPlayer().getY();
        Assert.assertTrue(initialY < finalY);
    }




}


