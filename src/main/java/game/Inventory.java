package game;

import core.InputManager;
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
    private static HBox hotbar;

    private static ImageView weapon;

    // Stores a queue of items for each slot
    private static List<Queue<IItem>> items        = new ArrayList<>(4);
    // The current displayed item image
    private static List<ImageView>    itemImages   = new ArrayList<>(4);
    private static List<Label>        itemCounters = new ArrayList<>(4);

    private static final String EMPTY_ITEM_TEXT = "";

    private static final Image      BLANK      = new Image("/images/blank.png");
    private static final Background BACKGROUND = new Background(new BackgroundFill(
            Color.gray(1d, .3d),
            new CornerRadii(20),
            Insets.EMPTY));

    private Inventory() { }

    public static void addItem(IItem item) {
        items.get(item.getItemID()).add(item);

        updateHotbarUI(item.getItemID());
    }

    public static void removeItem(int slotIndex) {
        if (!items.get(slotIndex).isEmpty()) {
            items.get(slotIndex).remove().activate();

            updateHotbarUI(slotIndex);
        }
    }

    private static void updateHotbarUI(int index) {
        Queue<IItem> currentSlot = items.get(index);

        if (currentSlot.isEmpty()) {
            itemImages.get(index).setImage(BLANK);

            itemCounters.get(index).setText(EMPTY_ITEM_TEXT);
        } else {
            Image image = new Image(currentSlot.peek().getItemImage(), 64d, 64d, true, false);
            itemImages.get(index).setImage(image);

            itemCounters.get(index).setText(String.valueOf(currentSlot.size()));
        }
    }

    public static void setHotbar(HBox hotbar) {
        Inventory.hotbar = hotbar;

        // Setup slots
        items.clear();
        hotbar.getChildren().clear();
        for (int i = 0; i < 4; i++) {
            items.add(new LinkedList<>());

            itemImages.add(new ImageView(BLANK));

            itemCounters.add(new Label(EMPTY_ITEM_TEXT));
            itemCounters.get(i).setFont(new Font(20));

            VBox column = new VBox(itemImages.get(i), itemCounters.get(i));
            column.setAlignment(Pos.CENTER);
            column.setPadding(new Insets(10));
            column.setBackground(BACKGROUND);
            hotbar.getChildren().add(column);

            // Setup key events for 1-4
            final int finalI  = i;
            KeyCode   keyCode = KeyCode.getKeyCode(String.valueOf(i + 1));
            InputManager.setEventHandler(keyCode, () -> removeItem(finalI));
        }
    }
}
