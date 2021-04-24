package game.inventory;

/**
 * Interface for items that can be stored in the inventory
 */
public interface IItem {
    // Which hotbar slot the item is stored in
    int getItemID();

    // Path to the item image
    String getItemImage();

    // Activates the item
    void activate();
}
