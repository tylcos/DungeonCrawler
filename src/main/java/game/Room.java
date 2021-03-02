package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;

import javafx.geometry.Pos;
import javafx.scene.image.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * Room is a GridPane that displays a given room as a grid of Labels.
 * 
 * Room also contains all data relevant to a room, like the items and enemies
 * inside of it.
 * 
 * !!! THIS HAS ONLY BEEN TESTED ON RECTANGULAR ROOMS. DO NOT ATTEMPT TO CREATE
 * NON-RECTANGULAR ROOMS !!!
 */
public class Room extends GridPane {

    // A lookup table to translate between letters in .room files and sprites in
    // game
    private static Hashtable<String, Image> spriteTable = new Hashtable<String, Image>() {
        {
            put(".", new Image(Room.class.getResource("/images/debug square.png").toString()));
            put("x", new Image(Room.class.getResource("/images/debug wall.png").toString()));
            put("D", new Image(Room.class.getResource("/images/debug door.png").toString()));
        }
    };

    // A cache for all rooms that have been loaded before so that we don't have to
    // read from disk every time.
    private static Hashtable<String, ArrayList<String>> roomCache = new Hashtable<String, ArrayList<String>>();

    // A list of all collidable bodies making up this room
    private ArrayList<Collidable> bodies = new ArrayList<Collidable>();
    /*
     * Room will also hold data on the locations of items and enemies inside of it
     * for Level to place into layers Level will only keep one room loaded at a
     * time, so this will allow for object permanance
     */

    /**
     * Constructs a room from a premade blueprint file.
     * 
     * @param template the file to load the room from
     */
    public Room(String template) {
        super();
        // By default GridPanes are aligned to the top-left, which we don't want
        setAlignment(Pos.CENTER);

        ArrayList<String> lines = new ArrayList<String>();
        ArrayList<String> cache = roomCache.get(template);
        if (cache != null) {
            lines = cache;
        } else {
            // Read the file
            InputStream is = Room.class.getResourceAsStream(template);
            if (is == null) {
                System.err.println("Tried to open file " + template + ", but it does not exist!");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            try {
                while (reader.ready()) {
                    lines.add(reader.readLine());
                }
            } catch (IOException e) {
                System.err.println("Error occurred trying to read from " + template);
                e.printStackTrace();
            }
            roomCache.put(template, lines);
        }

        // Place the tiles
        for (int row = 0; row < lines.size(); ++row) {
            for (int col = 0; col < lines.get(row).length(); ++col) {
                if (lines.get(row).charAt(col) == ' ') {
                    continue;
                }
                Image img = spriteTable.get("" + lines.get(row).charAt(col));
                if (img == null) {
                    System.err.println("Detected an invalid character in room " + template
                            + "! Please check file for errors!");
                }

                StackPane cell = new StackPane();
                cell.setMaxSize(100.0, 100.0);
                cell.setMinSize(100.0, 100.0);
                switch (lines.get(row).charAt(col)) {
                case 'D':
                    Door d = new Door(img, true);
                    bodies.add(d);
                    cell.getChildren().add(d);
                    break;
                case 'x':
                    Wall w = new Wall(img, true);
                    bodies.add(w);
                    cell.getChildren().add(w);
                    break;
                default: // Is floor. We can just put down the image.
                    ImageView imageView = new ImageView(img);
                    cell.getChildren().add(imageView);
                }
                add(cell, col, row);
            }
        }
    }

    public ArrayList<Collidable> getBodies() {
        return bodies;
    }
}
