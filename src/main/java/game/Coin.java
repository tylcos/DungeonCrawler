package game;
import core.GameManager;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Coin extends Entity {

    boolean isCoinUsed = false;

    public Coin(boolean isCoinExist) {
        super("/images/coin.gif", new Point2D(((Math.random() * (500 - 200)) + 200), ((Math.random() * (500 - 200)) + 200)), new Point2D(2, 2));
        if(isCoinExist == false){
            this.setImage(new Image("images/Invisible.gif"));
            isCoinUsed = true;
        }
    }

    @Override
    public void update(double dt) {
        Point2D distance = this.getPosition().subtract(GameManager.getPlayer().getPosition());

        if (distance.getX() < 20 && distance.getY() < 20 && distance.getX() > -20 && distance.getY() > -20 && !isCoinUsed) {
            GameManager.getPlayer().setMoney(GameManager.getPlayer().getMoney() + getRandomNumber(1, 25));
            isCoinUsed =true;
            this.setImage(new Image("images/Invisible.gif"));
        }
    }
    //randomly give
    public int getRandomNumber(int min, int max) {
        if (min < 0 || max < 0) {
            throw new IllegalArgumentException("Put positive number only");
        }
        return (int) ((Math.random() * (max - min)) + min);
    }
}
