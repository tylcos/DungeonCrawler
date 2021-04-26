module main {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.media;
    requires java.datatransfer;
    requires java.desktop;

    opens core;
    opens views to javafx.fxml;

    exports core;
    exports game.entities;
    exports game.inventory;
    exports views;
    exports game.level;
    exports game.collidables;
    exports utilities;
}