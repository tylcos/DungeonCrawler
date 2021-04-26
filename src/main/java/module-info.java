module org.dungeon_game {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.media;
    requires java.datatransfer;
    requires java.desktop;

    opens core;
    opens views to javafx.fxml;
}