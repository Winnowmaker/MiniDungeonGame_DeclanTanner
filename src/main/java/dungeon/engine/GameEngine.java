package dungeon.engine;

import javafx.scene.text.Text;

public class GameEngine {
    private Cell[][] map;
    private GameState gameState;

    public GameEngine(int size) {
        map = new Cell[size][size];
        int difficultyLevel = 5;
        gameState = new GameState(difficultyLevel);

        //Initialize map with empty cells
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = new Cell(EntityType.EMPTY);
            }
        }

        //Set the starting and ending cells
        map[0][0].setEntityType(EntityType.ENTRY);
        map[size-1][size-1].setEntityType(EntityType.EXIT);

        //Place the player
        map[0][0].setEntityType(EntityType.PLAYER);

        createMazeWalls();
    }

    public boolean movePlayer(String direction)
    {
        GameState.Position currentPos = gameState.getPlayerPosition();
        GameState.Position newPos = new GameState.Position(currentPos.getRow(), currentPos.getCol());

        // Calculate new position based on direction
        switch (direction)
        {
            case "UP" -> newPos.setRow(currentPos.getRow() - 1);
            case "DOWN" -> newPos.setRow(currentPos.getRow() + 1);
            case "LEFT" -> newPos.setCol(currentPos.getCol() - 1);
            case "RIGHT" -> newPos.setCol(currentPos.getCol() + 1);
        }

        // Check if move is valid
        if (isValidMove(newPos))
        {
            // Update map
            map[currentPos.getRow()][currentPos.getCol()].setEntityType(EntityType.EMPTY);
            map[newPos.getRow()][newPos.getCol()].setEntityType(EntityType.PLAYER);

            // Update player position
            gameState.getPlayerPosition().setRow(newPos.getRow());
            gameState.getPlayerPosition().setCol(newPos.getCol());

            // Decrement moves
            gameState.decrementMoves();
            return true;
        }
        return false;
    }

    private boolean isValidMove(GameState.Position pos)
    {
        if (pos.getRow() < 0 || pos.getRow() >= map.length || pos.getCol() < 0 || pos.getCol() >= map.length)
        {
            return false;
        }
        // Check if destination is a wall
        return map[pos.getRow()][pos.getCol()].getEntityType() != EntityType.WALL;
    }

    // Getters for new GameState
    public GameState getGameState()
    {
        return gameState;
    }

    private void createMazeWalls()
    {
        int[][] wallCoordinates = new int[][] {
                {0, 1}, {1, 1}, {2, 1}, {2, 2}, {2, 3}, {1, 3}, {2, 5}, {2, 6},
                {4, 3}, {4, 4}, {4, 5}, {4, 6}, {3, 5}, {1, 5}, {0, 7}, {0, 8},
                {4, 1}, {5, 1}, {6, 1}, {6, 0}, {8, 1}, {8, 2}, {9, 2}, {8, 3},
                {2, 8}, {2, 7}, {4, 8}, {5, 8}, {6, 8}, {6, 9}, {6, 7}, {6, 6},
                {6, 3}, {6, 4}, {5, 4}, {8, 5}, {8, 6}, {7, 6}, {8, 8}, {9, 8},
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

    public static void main(String[] args)
    {
        GameEngine engine = new GameEngine(10);
        TextUI ui = new TextUI(engine);
        ui.start();
    }
}