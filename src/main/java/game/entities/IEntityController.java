package game.entities;

/**
 * Used for controlling the movement and attacking of an entity.
 */
public interface IEntityController {
    /**
     * Moves the entity and determines if it should attack.
     */
    void act();

    /**
     * Stops the entity from making decisions.
     * Used to make dead enemies stop moving.
     */
    void stop();
}
