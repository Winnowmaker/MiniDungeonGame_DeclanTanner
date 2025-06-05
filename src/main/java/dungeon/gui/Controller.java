package dungeon.gui;

import dungeon.engine.Cell;
import dungeon.engine.GameEngine;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class Controller {
    @FXML
    private GridPane gridPane;

    GameEngine engine;

    @FXML
    public void initialize()
    {
        engine = new GameEngine(10);

        updateGui();
    }

    private void updateGui()
    {
        //Clear old GUI grid pane
        gridPane.getChildren().clear();

        // Set a fixed cell size
        double cellSize = 60; // This will make each cell 60x60 pixels

        //Loop through mapboard and add each cell into gridpane
        for (int i = 0; i < engine.getSize(); i++)
        {
            for (int j = 0; j < engine.getSize(); j++)
            {
                Cell cell = engine.getMap()[i][j];
                cell.setPrefWidth(cellSize);
                cell.setPrefHeight(cellSize);
                cell.setMinWidth(cellSize);
                cell.setMinHeight(cellSize);
                cell.setMaxWidth(cellSize);
                cell.setMaxHeight(cellSize);
                gridPane.add(cell, j, i);
            }
        }
        gridPane.setGridLinesVisible(true);

        // Center the grid
        gridPane.setAlignment(javafx.geometry.Pos.CENTER);
    }
}