package game;

import core.GameManager;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Item extends Entity {
    private MainPlayer mainPlayer;
    boolean isItemUsed = false;

    public Item(boolean isItemExist) {
        super("resources/images/item.gif", new Point2D(((Math.random() * (500 - 300)) + 300), ((Math.random() * (500 - 300)) + 300)), new Point2D(3, 3));
        this.mainPlayer = mainPlayer;
        if (isItemExist == false) {
            this.setImage(new Image("images/Invisible.gif"));
            isItemExist = true;
        }
    }

    @Override
    public void update(double dt) {
        Point2D distance = this.getPosition().subtract(GameManager.getPlayer().getPosition());
        if (distance.getX() < 20 && distance.getY() < 20 && distance.getX() > -20 && distance.getY() > -20 && !isItemUsed) {
            //update once we have more item
            /*
            if(...)
            GameManager.getPlayer().setWeapon(////);
            else if(...)
             GameManager.getPlayer().setWeapon(////);
             */
            isItemUsed = true;
            this.setImage(new Image("images/Invisible.gif"));
        }
    }

    public int getRandomNumber(int min, int max) {
        if (min < 0 || max < 0) {
            throw new IllegalArgumentException("Put positive number only");
        }
        return (int) ((Math.random() * (max - min)) + min);
    }


    //obtain weapon etc .. later one.
}
