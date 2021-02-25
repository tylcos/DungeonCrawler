package view;

import driver.MainPlayer;
import driver.Weapon;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;

public class InitialRoom {
    private int width;
    private int height;
    private MainPlayer player;
    private Weapon weapon;
    private Button shop;
    private Rectangle playerSquare;
    private Rectangle exitRectangle;

    public InitialRoom(int width, int height, MainPlayer player) {
        this.width = width;
        this.height = height;
        this.player = player;
    }
}
