package game.inventory;

import core.ImageManager;
import core.InputManager;
import game.entities.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.*;

/**
 * Manages the player inventory system.
 */
public final class Inventory {
    private static ImageView weapon;

    // Stores a queue of items for each slot
    private static List<Queue<IItem>> items;
    // The current displayed item image
    private static List<ImageView>    itemImages;
    private static List<Label>        itemCounters;

    private static final String EMPTY_ITEM_TEXT = "";

    private static final Image      BLANK      = ImageManager.getImage("blank.png");
    private static final Background BACKGROUND = new Background(new BackgroundFill(
        Color.gray(.5d, .7d),
        new CornerRadii(20),
        Insets.EMPTY));

    private Inventory() { }

    public static void changeWeapon(Weapon newWeapon) {
        weapon.setImage(newWeapon.getImage());
    }

    /**
     * Add a collected item to the inventory
     *
     * @param item the item to add
     */
    public static void addItem(IItem item) {
        int          slotIndex   = item.getItemID();
        Queue<IItem> currentSlot = items.get(slotIndex);

        currentSlot.add(item);

        // Only update image if the slot was empty
        if (currentSlot.size() == 1) {
            updateSlot(slotIndex);
        }

        itemCounters.get(slotIndex).setText(String.valueOf(currentSlot.size()));
    }

    /**
     * Remove an item from a specific slot
     *
     * @param slotIndex the index of the slot to remove from
     */
    public static void removeItem(int slotIndex) {
        if (!items.get(slotIndex).isEmpty()) {
            items.get(slotIndex).remove().activate();

            updateSlot(slotIndex);
        }
    }

    /**
     * Update the image and text for a specific slot
     *
     * @param index the index of the slot to update
     */
    private static void updateSlot(int index) {
        Queue<IItem> currentSlot = items.get(index);

        if (currentSlot.isEmpty()) {
            itemImages.get(index).setImage(BLANK);

            itemCounters.get(index).setText(EMPTY_ITEM_TEXT);
        } else {
            Image image = ImageManager.getImage(currentSlot.peek().getItemImage(), 64, 64, true);
            itemImages.get(index).setImage(image);

            itemCounters.get(index).setText(String.valueOf(currentSlot.size()));
        }
    }

    /**
     * Sets up hotbar slots and text
     *
     * @param hotbar hotbar from the scene
     */
    public static void initializeInventory(HBox hotbar) {
        // Setup slots
        items        = new ArrayList<>(4);
        itemImages   = new ArrayList<>(4);
        itemCounters = new ArrayList<>(4);
        hotbar.getChildren().clear();

        // Add weapon slot
        weapon = new ImageView(Player.getPlayer().getWeaponHolder().getWeapon().getImage());
        weapon.maxWidth(100);
        weapon.maxHeight(100);
        VBox column = new VBox(weapon);
        column.setBackground(BACKGROUND);
        hotbar.getChildren().add(column);

        // Add spacer between weapon and collectable
        Region spacer = new Region();
        spacer.setMinWidth(50);
        hotbar.getChildren().add(spacer);

        // Add collectable slots
        for (int i = 0; i < 4; i++) {
            items.add(new LinkedList<>());

            itemImages.add(new ImageView(BLANK));

            itemCounters.add(new Label(EMPTY_ITEM_TEXT));
            itemCounters.get(i).setFont(new Font(20));

            column = new VBox(itemImages.get(i), itemCounters.get(i));
            column.setAlignment(Pos.CENTER);
            column.setPadding(new Insets(10));
            column.setBackground(BACKGROUND);
            hotbar.getChildren().add(column);

            // Setup activating the collectables by pressing 1-4 or by clicking on them
            final int finalI  = i;
            KeyCode   keyCode = KeyCode.getKeyCode(String.valueOf(i + 1));
            InputManager.setKeyHandler(keyCode, () -> removeItem(finalI));
            column.setOnMouseClicked(event -> removeItem(finalI));
        }
    }
}
