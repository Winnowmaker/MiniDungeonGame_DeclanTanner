package dungeon.engine;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public abstract class Cell extends StackPane {
    protected Text displayText;
    
    public Cell() {
        this.displayText = new Text();
        getChildren().add(displayText);
        updateDisplay();
    }
    
    // Method to interact with player, returns true if interaction was handled
    public abstract boolean interact(GameState gameState);
    
    // Method to check if cell blocks movement
    public abstract boolean blocksMovement();
    
    // Method to get cell type (for UI to determine display)
    public abstract String getType();
    
    protected void updateDisplay() {
        displayText.setText(getType());
    }
}