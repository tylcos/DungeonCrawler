package game.entities;

import javafx.geometry.Point2D;

/**
 * Used for controlling the movement and attacking of an entity.
 */
public abstract class EntityController {
    protected Entity  entity;
    protected boolean stopped;

    protected boolean useDebugPoints;

    /**
     * Create an EntityController to control an entity
     *
     * @param entity the entity to be controlled
     */
    public EntityController(Entity entity) {
        this.entity = entity;
    }

    /**
     * Moves the entity and determines if it should attack.
     */
    abstract void act();

    /**
     * Allows the entity to make decisions.
     */
    void start() {
        stopped = false;
    }

    /**
     * Stops the entity from making decisions.
     * Used to make dead enemies stop moving.
     */
    void stop() {
        stopped = true;

        entity.setVelocity(Point2D.ZERO);
    }
}
