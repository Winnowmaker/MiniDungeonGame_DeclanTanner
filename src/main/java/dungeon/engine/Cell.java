package dungeon.engine;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class Cell extends StackPane
{
    private EntityType entityType;
    private Text displayText;

    public Cell()
    {
        this(EntityType.EMPTY); // Default empty cell
    }

    public Cell(EntityType type)
    {
        this.entityType = type;
        this.displayText = new Text();
        getChildren().add(displayText);
        updateDisplay();
    }

    public EntityType getEntityType()
    {
        return entityType;
    }

    public void setEntityType(EntityType type)
    {
        this.entityType = type;
        updateDisplay();
    }

    private void updateDisplay()
    {
        displayText.setText(entityType.toString());
    }
}
