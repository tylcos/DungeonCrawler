package game;

import java.util.ArrayList;

import javafx.scene.layout.*;

/**
 * Level is a StackPane that renders all game objects in a room.
 * Level should always be rendered underneath the game HUD!
 */
public class Level extends StackPane {
    private Room currentRoom;
    
    // Layers are rendered from the bottom up, so stuff on layer 4 appears above stuff on layer 3 and so on
    private Pane[] renderingLayers;
    static final int RENDERING_LAYERS = 4; // size of renderingLayers array
    // Constants for all the render layers so we don't go insane
    public static final int ROOM = 0;
    public static final int ITEM = 1;
    public static final int ENTITY = 2;
    public static final int VFX = 3;
    // Using a similar, 2D array, we could also create a collision matrix
    
    // A list of all static Collidables that need to be collision checked. This includes stationary things like walls and doors.
    private ArrayList<Collidable> staticBodies = new ArrayList<Collidable>();
    // A list of all dynamic (non-static) Collidables that need to be collision checked. This includes moving things like enemies.
    // I haven't done anything with dynamic bodies yet, so this is really just a placeholder
    private ArrayList<Collidable> dynamicBodies = new ArrayList<Collidable>();
    
    public Level() {
        super();
        
        renderingLayers = new Pane[RENDERING_LAYERS];
        for (int i = 0; i < RENDERING_LAYERS; ++i) {
            renderingLayers[i] = new Pane();
            getChildren().add(renderingLayers[i]);
        }
        setRoom(new Room("/rooms/test.room"));
    }
    
    public void setRoom(Room newRoom) {
        currentRoom = newRoom;
        renderingLayers[ROOM] = currentRoom;
        getChildren().remove(ROOM);
        getChildren().add(ROOM, renderingLayers[ROOM]);
        staticBodies.clear();
        dynamicBodies.clear();
        for (Collidable c : newRoom.bodies) {
            if (c.isStatic()) {
                staticBodies.add(c);
            } else {
                dynamicBodies.add(c);
            }
        }
    }
    
    public void addEntity(int layer, Entity e) {
        renderingLayers[layer].getChildren().add(e);
    }
    
    public void runCollisionCheck(Collidable target) {
        for (Collidable c : staticBodies) {
            if (c.intersects(target)) {
                c.onCollision(target);
                target.onCollision(c);
            }
        }
    }
    
}
