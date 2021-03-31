package game.entities;

import game.collidables.Collidable;
import game.levels.Room;
import javafx.geometry.Point2D;
import views.GameScreen;

/**
 * Used for Collidables with complex moving and attacking behaviors
 */
public abstract class Entity extends Collidable {
    protected Point2D position;
    protected Point2D velocity = new Point2D(0, 0);
    protected Point2D scale;

    protected int     health;
    protected boolean isDead;
    protected int     money;

    protected IEntityController entityController;

    /**
     * Creates an instance of an entity from an image and places it at a specific position.
     *
     * @param image    the path to the image of the entity
     * @param position the position to create the entity at
     */
    protected Entity(String image, Point2D position) {
        this(image, position, new Point2D(1, 1));
    }

    /**
     * Creates an instance of an entity based on.
     *
     * @param image    the path to the image of the entity
     * @param position the position to create the entity at
     * @param scale    how much to scale the entity by
     */
    protected Entity(String image, Point2D position, Point2D scale) {
        super(image, false);
        setPosition(position);
        setScale(scale);
    }

    /**
     * Updates the entity.
     * Overwritten in child classes.
     */
    public abstract void update();

    /**
     * Sets the entity to a new position.
     *
     * @param dt the amount of time since the last frame in seconds
     */
    public final void physicsUpdate(double dt) {
        setPosition(position.add(velocity.multiply(dt)));
    }

    @Override
    public void onCollision(Collidable other) {
    }

    /**
     * Gets the entity's health.
     *
     * @return the entity's health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Decreases the entity's health by amount and calls onDeath() when the entity dies.
     *
     * If the health is non-positive then sets isDead, stops the entity controller, and unlocks
     * doors when if all the monsters are killed.
     *
     * @param amount the amount to decrease entity's health by
     */
    public final void damage(int amount) {
        if (isDead) {
            return;
        }

        health -= amount;

        if (health <= 0) {
            isDead = true;
            entityController.stop();

            // So that you can kill other entities beneath a dead one
            setMouseTransparent(true);

            Room currentRoom = GameScreen.getLevel().getCurrentRoom();
            if (currentRoom.isClear()) {
                currentRoom.unlockDoors();
            }

            onDeath();
        }
    }

    /**
     * Runs once the health is non-positive
     */
    protected abstract void onDeath();

    /**
     * Returns the entity's balance
     *
     * @return the player's balance
     */
    public int getMoney() {
        return money;
    }

    /**
     * Sets the entity's balance
     *
     * @param money the new balance
     */
    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * Adds money to the entity's balance
     *
     * @param money the amount of money to add
     */
    public void addMoney(int money) {
        this.money += money;
    }

    /**
     * Returns if the entity is dead.
     *
     * @return if the entity is dead
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Returns the position of the entity.
     *
     * @return the position of the entity
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * Sets the position of the entity to a new position
     *
     * @param position the new point to put the entity at
     */
    public void setPosition(Point2D position) {
        this.position = position;

        setTranslateX(position.getX());
        setTranslateY(position.getY());
    }

    /**
     * Returns the current velocity of the entity.
     *
     * @return the current velocity of the entity
     */
    public Point2D getVelocity() {
        return velocity;
    }

    /**
     * Sets the velocity of the entity to a new velocity.
     *
     * @param velocity the new velocity
     */
    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    /**
     * Gets the scaling of the entity.
     *
     * @return the scale of the entity
     */
    public Point2D getScale() {
        return scale;
    }

    /**
     * Sets the scaling of the entity to a new scale.
     *
     * @param scale the new scale
     */
    public void setScale(Point2D scale) {
        this.scale = scale;

        setScaleX(scale.getX());
        setScaleY(scale.getY());
    }

    public boolean isMainPlayer() {
        return this == MainPlayer.getPlayer();
    }
}
