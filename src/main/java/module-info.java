module dungeon.gui {
    requires javafx.controls;
    requires javafx.fxml;
    
    opens dungeon.gui to javafx.fxml;
    exports dungeon.gui;
}