package game;

import javafx.scene.image.Image;

public class Door extends CollidableTile {

    public Door(Image image, boolean isStatic) {
        super(image, isStatic);
    }

    @Override
    public void onCollision(Collidable other) {
        System.out.println("Entered Door.");
        // TODO Behavior for doors upon collision
    }

}
