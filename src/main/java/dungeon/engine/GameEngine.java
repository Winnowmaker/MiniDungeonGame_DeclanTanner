package dungeon.engine;

import javafx.scene.text.Text;

public class GameEngine {

    private Cell[][] map;

    public GameEngine(int size) {
        map = new Cell[size][size];

        //Initialize map with empty cells
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = new Cell(EntityType.EMPTY);
            }
        }

        //Set the starting and ending cells
        map[0][0].setEntityType(EntityType.ENTRY);
        map[size-1][size-1].setEntityType(EntityType.EXIT);

        createMazeWalls();
    }

    private void createMazeWalls()
    {
        int[][] wallCoordinates = new int[][] {
                {0, 1}, {1, 1}, {2, 1}, {3, 1}, {4, 1}, {5, 1}, {6, 1},
                {4, 4}, {4, 5}, {4, 6},
                {7, 2}, {8, 2}, {9, 2}
        };

        for (int[] coordinate : wallCoordinates)
        {
            map[coordinate[0]][coordinate[1]].setEntityType(EntityType.WALL);
        }
    }

    public Cell[][] getMap()
    {
        return map;
    }

    public int getSize() {
        return map.length;
    }

    public static void main(String[] args) {
        GameEngine engine = new GameEngine(10);
        TextUI ui = new TextUI(engine);
        ui.start();
    }
}
