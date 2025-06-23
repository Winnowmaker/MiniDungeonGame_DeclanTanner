package dungeon.gui;

import dungeon.engine.Cell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class CellView extends StackPane {
    private static final String IMAGE_PATH = "/images/";
    private final ImageView imageView;
    private final Cell cell;
    
    public CellView(Cell cell) {
        this.cell = cell;
        this.imageView = new ImageView();
        imageView.setFitWidth(60);
        imageView.setFitHeight(60);
        updateImage();
        getChildren().add(imageView);
    }
    
    private void updateImage() {
        String imageName = switch (cell.getType()) {
            case "PLAYER" -> "player.png";
            case "WALL" -> "wall.png";
            case "EMPTY" -> "floor.png";
            case "EXIT" -> "exit.png";
            case "TRAP" -> "trap.png";
            case "GOLD" -> "gold.png";
            case "HEALTH_POTION" -> "potion.png";
            case "MELEE_MUTANT" -> "mutant.png";
            case "RANGED_MUTANT" -> "ranger.png";
            default -> "floor.png";
        };
        
        String imagePath = IMAGE_PATH + imageName;
        var imageStream = getClass().getResourceAsStream(imagePath);
        if (imageStream == null) {
            throw new IllegalStateException("Cannot find image: " + imagePath);
        }
        Image image = new Image(imageStream);
        imageView.setImage(image);
    }
}