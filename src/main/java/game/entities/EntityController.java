package game.entities;

import javafx.geometry.Point2D;

/**
 * Used for controlling the movement and attacking of an entity.
 */
public abstract class EntityController<T extends Entity> {
    protected T       entity;
    protected boolean stopped;

    protected double timeSinceRoomLoad;
    protected double relaxingBiasScale = 150;

    protected boolean useDebugPoints;

    /**
     * Create an EntityController to control an entity
     *
     * @param entity the entity to be controlled
     */
    public EntityController(T entity) {
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

    /**
     * Makes the Entity attack the Player by default.
     */
    void attack() {
        Player.getPlayer().damage(1);
    }
}
