package dungeon.engine;

public class GameEngine
{
    private Cell[][] map;
    private GameState gameState;
    private final int size;
    private final int difficultyLevel;
    private GameState.Position lastExitPosition;
    private boolean gameCompleted;
    private final HighScoreManager highScoreManager;

    public GameEngine(int size)
    {
        this(size, 1); // Use difficulty level 1 as default
    }

    public GameEngine(int size, int difficultyLevel)
    {
        this.size = size;
        this.difficultyLevel = difficultyLevel;
        this.gameState = new GameState(difficultyLevel);
        this.gameCompleted = false;
        this.highScoreManager = new HighScoreManager();
        initializeLevel(true); // Start first level
    }

    public GameEngine(int size, GameState loadedState)
    {
        this.size = size;
        this.difficultyLevel = loadedState.getDifficultyLevel();
        this.gameState = loadedState;
        this.gameCompleted = false;
        this.highScoreManager = new HighScoreManager();

        // Initialize the level based on the loaded state
        initializeLevel(loadedState.getCurrentLevel() == 1);

        // Restore player position from loaded state
        GameState.Position playerPos = loadedState.getPlayerPosition();
        map[playerPos.getRow()][playerPos.getCol()] = CellFactory.createCell("PLAYER");
    }

    public HighScoreManager getHighScoreManager() {
        return highScoreManager;
    }

    // Add getter for game completion status
    public boolean isGameCompleted() {
        return gameCompleted;
    }

    // Method to check if player reached exit
    private boolean isAtExit(GameState.Position pos)
    {
        return map[pos.getRow()][pos.getCol()].getType().equals("EXIT");
    }

    private void initializeLevel(boolean isFirstLevel)
    {
        map = new Cell[size][size];

        // Create empty map
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                map[i][j] = CellFactory.createCell("EMPTY");
            }
        }

        if (isFirstLevel)
        {
            map[0][0] = CellFactory.createCell("ENTRY");
            map[size - 1][size - 1] = CellFactory.createCell("EXIT");
            map[0][0] = CellFactory.createCell("PLAYER");
            gameState.getPlayerPosition().setRow(0);
            gameState.getPlayerPosition().setCol(0);
            lastExitPosition = new GameState.Position(size - 1, size - 1);
        }
        else
        {
            map[lastExitPosition.getRow()][lastExitPosition.getCol()] = CellFactory.createCell("ENTRY");
            map[0][0] = CellFactory.createCell("EXIT");
            map[lastExitPosition.getRow()][lastExitPosition.getCol()] = CellFactory.createCell("PLAYER");
            gameState.getPlayerPosition().setRow(lastExitPosition.getRow());
            gameState.getPlayerPosition().setCol(lastExitPosition.getCol());
        }
        createMazeWalls();
        placeTraps();
        placeGold();
        placeHealthPotions();
        placeMeleeMutants();
        placeRangedMutants();
    }

    public boolean movePlayer(String direction)
    {
        if (gameCompleted)
        {
            return false;
        }

        // Check for game over conditions first
        if (gameState.isGameOver())
        {
            handleGameOver();
            return false;
        }

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

        if (isValidMove(newPos))
        {
            boolean isExitReached = isAtExit(newPos);

            Cell nextCell = map[newPos.getRow()][newPos.getCol()];
            boolean handled = nextCell.interact(gameState);

            // Update positions
            map[currentPos.getRow()][currentPos.getCol()] = CellFactory.createCell("EMPTY");
            map[newPos.getRow()][newPos.getCol()] = CellFactory.createCell("PLAYER");

            gameState.getPlayerPosition().setRow(newPos.getRow());
            gameState.getPlayerPosition().setCol(newPos.getCol());

            handleRangedAttacks();

            // Check for game over after interactions and attacks
            if (gameState.isGameOver())
            {
                handleGameOver();
                return true;
            }

            if (isExitReached)
            {
                handleLevelComplete();
            }

            gameState.decrementMoves();
            return true;
        }
        return false;
    }

    private void handleGameOver()
    {
        gameCompleted = true;
        System.out.println("\n=========================");
        System.out.println("      GAME OVER!");
        System.out.println("=========================");
        if (gameState.getPlayerHP() <= 0)
        {
            System.out.println("You ran out of health!");
        }
        else
        {
            System.out.println("You ran out of moves!");
        }
        System.out.println("Final Score: " + gameState.getScore());
        System.out.println("Level Reached: " + gameState.getCurrentLevel());
        System.out.println("=========================\n");
    }

    private void handleLevelComplete()
    {
        if (gameState.getCurrentLevel() == 1)
        {
            GameState.Position exitPos = new GameState.Position(
                    gameState.getPlayerPosition().getRow(),
                    gameState.getPlayerPosition().getCol()
            );

            // Advance to level 2
            gameState.advanceLevel();

            // Initialize new level with the stored position
            lastExitPosition = exitPos;
            initializeLevel(false);

            System.out.println("\n=== LEVEL " + gameState.getCurrentLevel() + " ===");
            System.out.println("You found the exit! Moving to next level...\n");
        }
        else if (gameState.getCurrentLevel() == 2)
        {
            gameCompleted = true;
        }
    }

    private boolean isValidMove(GameState.Position pos)
    {
        if (pos.getRow() < 0 || pos.getRow() >= map.length ||
                pos.getCol() < 0 || pos.getCol() >= map.length)
        {
            return false;
        }
        return !map[pos.getRow()][pos.getCol()].blocksMovement();
    }

    public GameState getGameState()
    {
        return gameState;
    }

    private void createMazeWalls()
    {
        int[][] wallCoordinates = new int[][]{
                {0, 1}, {1, 1}, {2, 1}, {2, 2}, {2, 3}, {1, 3}, {2, 5}, {2, 6},
                {4, 3}, {4, 4}, {4, 5}, {4, 6}, {3, 5}, {1, 5}, {0, 7}, {0, 8},
                {4, 1}, {5, 1}, {6, 1}, {6, 0}, {8, 1}, {8, 2}, {9, 2}, {8, 3},
                {2, 8}, {2, 7}, {4, 8}, {5, 8}, {6, 8}, {6, 9}, {6, 7}, {6, 6},
                {6, 3}, {6, 4}, {5, 4}, {8, 5}, {8, 6}, {7, 6}, {8, 8}, {9, 8},
        };

        for (int[] coordinate : wallCoordinates)
        {
            map[coordinate[0]][coordinate[1]] = CellFactory.createCell("WALL");
        }
    }

    public Cell[][] getMap()
    {
        return map;
    }

    public int getSize()
    {
        return map.length;
    }

    public static void main(String[] args)
    {
        GameEngine engine = new GameEngine(10);
        TextUI ui = new TextUI(engine);
        ui.start();
    }

    private void placeTraps()
    {
        int trapsToPlace = 5;
        java.util.Random random = new java.util.Random();

        while (trapsToPlace > 0)
        {
            int row = random.nextInt(size);
            int col = random.nextInt(size);

            if (map[row][col].getType().equals("EMPTY"))
            {
                map[row][col] = CellFactory.createCell("TRAP");
                trapsToPlace--;
            }
        }
    }

    private void placeGold()
    {
        int goldToPlace = 5;
        java.util.Random random = new java.util.Random();

        while (goldToPlace > 0)
        {
            int row = random.nextInt(size);
            int col = random.nextInt(size);

            if (map[row][col].getType().equals("EMPTY"))
            {
                map[row][col] = CellFactory.createCell("GOLD");
                goldToPlace--;
            }
        }
    }

    private void placeHealthPotions()
    {
        int potionsToPlace = 2;
        java.util.Random random = new java.util.Random();

        while (potionsToPlace > 0)
        {
            int row = random.nextInt(size);
            int col = random.nextInt(size);

            if (map[row][col].getType().equals("EMPTY"))
            {
                map[row][col] = CellFactory.createCell("HEALTH_POTION");
                potionsToPlace--;
            }
        }
    }

    private void placeMeleeMutants()
    {
        int mutantsToPlace = 3;
        java.util.Random random = new java.util.Random();

        while (mutantsToPlace > 0)
        {
            int row = random.nextInt(size);
            int col = random.nextInt(size);

            if (map[row][col].getType().equals("EMPTY"))
            {
                map[row][col] = CellFactory.createCell("MELEE_MUTANT");
                mutantsToPlace--;
            }
        }
    }

    private void placeRangedMutants()
    {
        int mutantsToPlace = 3;
        java.util.Random random = new java.util.Random();

        while (mutantsToPlace > 0)
        {
            int row = random.nextInt(size);
            int col = random.nextInt(size);

            if (map[row][col].getType().equals("EMPTY"))
            {
                map[row][col] = CellFactory.createCell("RANGED_MUTANT");
                mutantsToPlace--;
            }
        }
    }

    private boolean canRangedMutantShoot(int mutantRow, int mutantCol, int playerRow, int playerCol)
    {
        int distance = Math.abs(mutantRow - playerRow) + Math.abs(mutantCol - playerCol);
        if (distance > 2 || distance == 0) return false;

        if (mutantRow == playerRow)
        {
            int step = (playerCol > mutantCol) ? 1 : -1;
            for (int col = mutantCol + step; col != playerCol; col += step)
            {
                if (map[mutantRow][col].getType().equals("WALL"))
                {
                    return false;
                }
            }
        }
        else if (mutantCol == playerCol)
        {
            int step = (playerRow > mutantRow) ? 1 : -1;
            for (int row = mutantRow + step; row != playerRow; row += step)
            {
                if (map[row][mutantCol].getType().equals("WALL"))
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
        return true;
    }

    private void handleRangedAttacks()
    {
        GameState.Position playerPos = gameState.getPlayerPosition();
        java.util.Random random = new java.util.Random();

        for (int row = 0; row < size; row++)
        {
            for (int col = 0; col < size; col++)
            {
                if (map[row][col].getType().equals("RANGED_MUTANT"))
                {
                    if (canRangedMutantShoot(row, col, playerPos.getRow(), playerPos.getCol())) {
                        if (random.nextBoolean())
                        {
                            gameState.changeHP(-2);
                            System.out.println("A ranged mutant hit you for 2 damage!");
                        }
                        else
                        {
                            System.out.println("A ranged mutant missed their shot!");
                        }
                    }
                }
            }
        }
    }
}