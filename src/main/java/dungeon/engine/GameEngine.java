package dungeon.engine;

public class GameEngine {
    private Cell[][] map;
    private GameState gameState;
    private final int size;
    private final int difficultyLevel;
    private GameState.Position lastExitPosition;
    private boolean gameCompleted; // New variable to track game completion

    // Add default constructor that uses size and default difficulty level
    public GameEngine(int size) {
        this(size, 1); // Use difficulty level 1 as default
    }

    // Existing constructor
    public GameEngine(int size, int difficultyLevel) {
        this.size = size;
        this.difficultyLevel = difficultyLevel;
        this.gameState = new GameState(difficultyLevel);
        this.gameCompleted = false; // Initialize as not completed
        initializeLevel(true); // Start first level
    }

    // Add getter for game completion status
    public boolean isGameCompleted() {
        return gameCompleted;
    }

    // Method to check if player reached exit
    private boolean isAtExit(GameState.Position pos) {
        return map[pos.getRow()][pos.getCol()].getEntityType() == EntityType.EXIT;
    }

private void initializeLevel(boolean isFirstLevel) {
    map = new Cell[size][size];
    
    // Create empty map
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            map[i][j] = new Cell(EntityType.EMPTY);
        }
    }

    if (isFirstLevel) {
        // Level 1 setup...
        map[0][0].setEntityType(EntityType.ENTRY);
        map[size-1][size-1].setEntityType(EntityType.EXIT);
        map[0][0].setEntityType(EntityType.PLAYER);
        gameState.getPlayerPosition().setRow(0);
        gameState.getPlayerPosition().setCol(0);
        lastExitPosition = new GameState.Position(size-1, size-1);
    } else {
        // Level 2 setup...
        map[lastExitPosition.getRow()][lastExitPosition.getCol()].setEntityType(EntityType.ENTRY);
        map[0][0].setEntityType(EntityType.EXIT);
        map[lastExitPosition.getRow()][lastExitPosition.getCol()].setEntityType(EntityType.PLAYER);
        gameState.getPlayerPosition().setRow(lastExitPosition.getRow());
        gameState.getPlayerPosition().setCol(lastExitPosition.getCol());
    }

    createMazeWalls();
    placeTraps();
    placeGold();
    placeHealthPotions();  // Add this line to place health potions
}

    public boolean movePlayer(String direction) {
        if (gameCompleted) {
            return false;
        }

        GameState.Position currentPos = gameState.getPlayerPosition();
        GameState.Position newPos = new GameState.Position(currentPos.getRow(), currentPos.getCol());

        // Calculate new position based on direction
        switch (direction) {
            case "UP" -> newPos.setRow(currentPos.getRow() - 1);
            case "DOWN" -> newPos.setRow(currentPos.getRow() + 1);
            case "LEFT" -> newPos.setCol(currentPos.getCol() - 1);
            case "RIGHT" -> newPos.setCol(currentPos.getCol() + 1);
        }

        if (isValidMove(newPos)) {
            // Check if next position is exit before moving
            boolean isExitReached = isAtExit(newPos);
        
            // Check for entities at new position
            EntityType nextCell = map[newPos.getRow()][newPos.getCol()].getEntityType();
            boolean isTrap = nextCell == EntityType.TRAP;
            boolean isGold = nextCell == EntityType.GOLD;
            boolean isPotion = nextCell == EntityType.HEALTH_POTION;
        
            // Update map
            map[currentPos.getRow()][currentPos.getCol()].setEntityType(EntityType.EMPTY);
            map[newPos.getRow()][newPos.getCol()].setEntityType(EntityType.PLAYER);

            // Update player position
            gameState.getPlayerPosition().setRow(newPos.getRow());
            gameState.getPlayerPosition().setCol(newPos.getCol());

            // Handle trap damage
            if (isTrap) {
                gameState.changeHP(-1);
                System.out.println("Ouch! You stepped on a trap!");
            }

            // Handle gold collection
            if (isGold) {
                gameState.addScore(10);
                System.out.println("You found gold! +10 points!");
            }

            // Handle health potion
            if (isPotion) {
                int oldHP = gameState.getPlayerHP();
                gameState.changeHP(4);
                int healedAmount = gameState.getPlayerHP() - oldHP;
                System.out.println("You found a health potion! Healed for " + healedAmount + " HP!");
            }

            // Handle level completion if exit was reached
            if (isExitReached) {
                handleLevelComplete();
            }

            gameState.decrementMoves();
            return true;
        }
        return false;
    }

    // Modified level completion handler
    private void handleLevelComplete() {
        if (gameState.getCurrentLevel() == 1) {
            // Store exit position before changing the map
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
        } else if (gameState.getCurrentLevel() == 2) {
            // Complete the game
            gameCompleted = true;
        }
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

private void placeTraps() {
    // We want to place 5 traps
    int trapsToPlace = 5;
    java.util.Random random = new java.util.Random();
    
    while (trapsToPlace > 0) {
        // Get random coordinates
        int row = random.nextInt(size);
        int col = random.nextInt(size);
        
        // Only place trap if the cell is empty
        if (map[row][col].getEntityType() == EntityType.EMPTY) {
            map[row][col].setEntityType(EntityType.TRAP);
            trapsToPlace--;
        }
    }
}
private void placeGold() {
    // Place 5 gold pieces
    int goldToPlace = 5;
    java.util.Random random = new java.util.Random();
    
    while (goldToPlace > 0) {
        int row = random.nextInt(size);
        int col = random.nextInt(size);
        
        // Only place gold if the cell is empty
        if (map[row][col].getEntityType() == EntityType.EMPTY) {
            map[row][col].setEntityType(EntityType.GOLD);
            goldToPlace--;
        }
    }
}
private void placeHealthPotions() {
    // Place 2 health potions per level
    int potionsToPlace = 2;
    java.util.Random random = new java.util.Random();
    
    while (potionsToPlace > 0) {
        int row = random.nextInt(size);
        int col = random.nextInt(size);
        
        // Only place potion if the cell is empty
        if (map[row][col].getEntityType() == EntityType.EMPTY) {
            map[row][col].setEntityType(EntityType.HEALTH_POTION);
            potionsToPlace--;
        }
    }
}
}