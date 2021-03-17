package game.collidables;

import javafx.scene.image.Image;

/**
 * A wall in the dungeon crawler.
 */
public class Wall extends CollidableTile {

    /**
     * Creates an instance of a wall based off the image.
     * @param image the image of the wall
     * @param isStatic true if the wall is static; false otherwise
     */
    public Wall(Image image, boolean isStatic) {
        super(image, isStatic);
    }

    @Override
    public void onCollision(Collidable other) {
        // TODO Behavior for walls upon collision
    }
}
