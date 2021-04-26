package game.entities;

import core.GameEngine;
import core.ImageManager;
import game.collidables.Collidable;
import game.collidables.CollidableTile;
import game.level.Room;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import views.GameScreen;

/**
 * Used for Collidables with complex moving and attacking behaviors
 */
public abstract class Entity extends Collidable {
    protected Point2D position;
    protected Point2D velocity = new Point2D(0, 0);

    protected int     health;
    protected int     maxHealth;
    protected boolean isDead;
    protected int     money;

    protected EntityController<?> entityController;

    /**
     * Initializes the Image and position of an entity.
     *
     * @param imagePath the path to the image of the entity
     * @param position  the position to create the entity at
     */
    protected Entity(String imagePath, Point2D position) {
        super(ImageManager.getImage(imagePath), false);
        setPosition(position);
        toBack();
    }

    /**
     * Initializes the Image and position of an entity.
     *
     * @param imagePath  the path to the image of the entity
     * @param position   the position to create the entity at
     * @param dimensions the dimensions of the image
     */
    protected Entity(String imagePath, Point2D position, Point2D dimensions) {
        super(ImageManager.getImage(imagePath, dimensions, false), false);
        setPosition(position);
        toBack();
    }

    /**
     * Initializes the Image and position of an entity.
     *
     * @param image    the image of the entity
     * @param position the position to create the entity at
     */
    protected Entity(Image image, Point2D position) {
        super(image, false);
        setPosition(position);
        toBack();
    }

    /**
     * Called each frame so that the entity can be updated.
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
        if (other instanceof CollidableTile) {
            double magnitude = Math.max(100d, velocity.magnitude());

            bounceBack((int) (-magnitude * GameEngine.getDt()), Point2D.ZERO);
        }
    }

    /**
     * Runs once the health is non-positive
     */
    protected void onDeath() { }

    /**
     * Gets the entity's health.
     *
     * @return the entity's health
     */
    public final int getHealth() {
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
    public void damage(int amount) {
        if (isDead) {
            return;
        }

        health -= amount;

        // This is beyond jank
        if (this instanceof Golem) {
            bounceBack(10, Player.getPlayer().getPosition());
        } else {
            bounceBack(Player.getPlayer().getPosition());
        }

        if (health <= 0) {
            isDead = true;
            if (entityController != null) {
                entityController.stop();
            }

            // So that you can kill other entities beneath a dead one
            setMouseTransparent(true);

            Room currentRoom = GameScreen.getLevel().getCurrentRoom();
            if (!currentRoom.isChallenge() && currentRoom.isClear()) {
                currentRoom.unlockDoors();
            } else if (currentRoom.isChallenge() && currentRoom.isClear()) {
                currentRoom.endChallenge();
            }

            onDeath();
        }
    }

    public final void start() {
        entityController.start();
    }

    public final void stop() {
        entityController.stop();
    }

    /**
     * Returns the entity's balance
     *
     * @return the player's balance
     */
    public final int getMoney() {
        return money;
    }

    /**
     * Sets the entity's balance
     *
     * @param money the new balance
     */
    public final void setMoney(int money) {
        this.money = money;
    }

    /**
     * Adds money to the entity's balance
     *
     * @param money the amount of money to add
     */
    public final void addMoney(int money) {
        this.money += money;
    }

    /**
     * Returns if the entity is dead.
     *
     * @return if the entity is dead
     */
    public final boolean isDead() {
        return isDead;
    }

    /**
     * Returns the position of the entity.
     *
     * @return the position of the entity
     */
    public final Point2D getPosition() {
        return position;
    }

    /**
     * Sets the position of the entity to a new position
     *
     * @param position the new point to put the entity at
     */
    public final void setPosition(Point2D position) {
        this.position = position;

        setTranslateX(position.getX());
        setTranslateY(position.getY());
    }

    /**
     * Returns the current velocity of the entity.
     *
     * @return the current velocity of the entity
     */
    public final Point2D getVelocity() {
        return velocity;
    }

    /**
     * Sets the velocity of the entity to a new velocity.
     *
     * @param velocity the new velocity
     */
    public final void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public final boolean isMainPlayer() {
        return this == Player.getPlayer();
    }

    /**
     * Makes the entity bounce back from a point
     *
     * @param fromPoint the point to bounce back from
     */
    protected final void bounceBack(Point2D fromPoint) {
        bounceBack(40, fromPoint);
    }

    /**
     * Makes the entity bounce back from a point
     *
     * @param bounceDistance the distance to bounce
     * @param fromPoint      the point to bounce back from
     */
    protected final void bounceBack(int bounceDistance, Point2D fromPoint) {
        Point2D difference = position.subtract(fromPoint);

        Point2D dp = new Point2D(bounceDistance * Math.signum(difference.getX()),
                                 bounceDistance * Math.signum(difference.getY()));

        setPosition(position.add(dp));
    }
}
