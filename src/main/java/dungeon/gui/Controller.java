package dungeon.gui;

import dungeon.engine.GameEngine;
import dungeon.engine.GameState;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import java.io.*;

public class Controller {
    @FXML private GridPane gridPane;
    @FXML private VBox statsPane;
    @FXML private Label healthLabel;
    @FXML private Label scoreLabel;
    @FXML private Label movesLabel;
    @FXML private Label levelLabel;
    @FXML private Button saveButton;
    @FXML private Button loadButton;
    @FXML private Button upButton;
    @FXML private Button downButton;
    @FXML private Button leftButton;
    @FXML private Button rightButton;
    
    private GameEngine engine;
    
    @FXML
    public void initialize() {
        engine = new GameEngine(10);
        setupEventHandlers();
        updateGui();
    }
    
    private void setupEventHandlers() {
        // Existing handlers
        saveButton.setOnAction(e -> saveGame());
        loadButton.setOnAction(e -> loadGame());
        
        // Direction button handlers
        upButton.setOnAction(e -> movePlayer("UP"));
        downButton.setOnAction(e -> movePlayer("DOWN"));
        leftButton.setOnAction(e -> movePlayer("LEFT"));
        rightButton.setOnAction(e -> movePlayer("RIGHT"));
    }
    
    private void movePlayer(String direction) {
        if (!engine.isGameCompleted() && engine.movePlayer(direction)) {
            updateGui();
        }
    }
    
    // Update handleKeyPress to use the new movePlayer method
    private void handleKeyPress(KeyEvent event) {
        if (engine.isGameCompleted()) return;
        
        switch (event.getCode()) {
            case UP -> movePlayer("UP");
            case DOWN -> movePlayer("DOWN");
            case LEFT -> movePlayer("LEFT");
            case RIGHT -> movePlayer("RIGHT");
            default -> { }
        }
        
        event.consume();
    }

public void setUpKeyHandlers() {
    if (gridPane.getScene() != null) {
        gridPane.getScene().setOnKeyPressed(this::handleKeyPress);
    }
}

    // Implementation of step 4 - Save/Load functionality
    private void saveGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Game");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Game saves", "*.sav")
        );
        
        File file = fileChooser.showSaveDialog(gridPane.getScene().getWindow());
        if (file != null) {
            try (ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(file))) {
                out.writeObject(engine.getGameState());
            } catch (IOException e) {
                showError("Error saving game: " + e.getMessage());
            }
        }
    }

    private void loadGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Game");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Game saves", "*.sav")
        );
        
        File file = fileChooser.showOpenDialog(gridPane.getScene().getWindow());
        if (file != null) {
            try (ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream(file))) {
                GameState loadedState = (GameState) in.readObject();
                engine = new GameEngine(10, loadedState);
                updateGui();
            } catch (IOException | ClassNotFoundException e) {
                showError("Error loading game: " + e.getMessage());
            }
        }
    }

    // Implementation of step 5 - Keyboard handling
    // Implementation of step 6 - GUI updates
    private void updateGui() {
        gridPane.getChildren().clear();
        
        GameState state = engine.getGameState();
        
        // Update status labels
        healthLabel.setText("Health: " + state.getPlayerHP());
        scoreLabel.setText("Score: " + state.getScore());
        movesLabel.setText("Moves Left: " + state.getMovesLeft());
        levelLabel.setText("Level " + state.getCurrentLevel());
        
        // Update grid
        for (int i = 0; i < engine.getSize(); i++) {
            for (int j = 0; j < engine.getSize(); j++) {
                CellView cellView = new CellView(engine.getMap()[i][j]);
                gridPane.add(cellView, j, i);
            }
        }
        
        // Check for game over
        if (engine.isGameCompleted()) {
            showGameOver();
        }
    }

    public void onSceneSet() {
    setUpKeyHandlers();
}

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showGameOver() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Game Completed!");
        
        GameState state = engine.getGameState();
        String content = String.format("""
            Final Score: %d
            HP Remaining: %d
            Moves Remaining: %d
            Level Reached: %d
            """, 
            state.getScore(), 
            state.getPlayerHP(), 
            state.getMovesLeft(), 
            state.getCurrentLevel()
        );
        
        alert.setContentText(content);
        alert.showAndWait();
    }
}