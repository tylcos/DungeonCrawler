package game;

import core.GameManager;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Entity {
    private ImageView imageView;

    private double posX;
    private double posY;

    private double velX = 0;
    private double velY = 0;

    private double width;
    private double height;
    private double scaleX;
    private double scaleY;


    public Entity(String image, double posX, double posY) {
        this(image, posX, posY, 1, 1);
    }

    public Entity(String image, double posX, double posY, double scaleX, double scaleY) {
        setImage(image);
        setPos(posX, posY);
        setScale(scaleX, scaleY);


        GameManager.spawnEntity(this);
    }


    public void update(double dt) {
        setPos(posX + velX * dt, posY + velY * dt);
    }


    public Rectangle2D getBoundary() {
        return new Rectangle2D(posX, posY, width, height);
    }

    public boolean intersects(Entity s) {
        return s.getBoundary().intersects(this.getBoundary());
    }


    public ImageView getImage() {
        return imageView;
    }

    public void setImage(String imagePath) {
        Image image = new Image(getClass().getResource(imagePath).toString());

        imageView = new ImageView(image);

        width = image.getWidth();
        height = image.getHeight();
    }


    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getVelX() {
        return velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setPos(double x, double y) {
        this.posX = x;
        this.posY = y;

        imageView.setX(x);
        imageView.setY(y);
    }

    public void setVel(double x, double y) {
        this.velX = x;
        this.velY = y;
    }


    public double getScaleX() {
        return scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setScale(double x, double y) {
        this.scaleX = x;
        this.scaleY = y;

        imageView.setScaleX(x);
        imageView.setScaleY(y);
    }
}
