package dungeon.engine;

import java.io.Serializable;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Constants
    private static final int MAX_HP = 10;
    private static final int MAX_MOVES = 100;

    // Game state variables
    private int currentLevel;
    private int playerHP;
    private int score;
    private int movesLeft;
    private int difficultyLevel;
    private Position playerPosition;

    public GameState(int difficultyLevel) {
        this.currentLevel = 1;
        this.playerHP = MAX_HP;
        this.score = 0;
        this.movesLeft = MAX_MOVES;
        this.difficultyLevel = difficultyLevel;
        this.playerPosition = new Position(0, 0);
    }

    // Required getter methods
    public int getCurrentLevel() { return currentLevel; }
    public int getPlayerHP() { return playerHP; }
    public int getScore() { return score; }
    public int getMovesLeft() { return movesLeft; }
    public int getDifficultyLevel() { return difficultyLevel; }
    public Position getPlayerPosition() { return playerPosition; }

    // Required game state modification methods
    public void decrementMoves() {
        if (movesLeft > 0) movesLeft--;
    }

    public void addScore(int points) {
        score += points;
    }

    public void changeHP(int amount) {
        playerHP = Math.min(MAX_HP, Math.max(0, playerHP + amount));
    }

    public void advanceLevel() {
        currentLevel++;
    }

    public boolean isGameOver() {
        return playerHP <= 0 || movesLeft <= 0;
    }

    // Position inner class
    public static class Position implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private int row;
        private int col;

        public Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() { return row; }
        public int getCol() { return col; }
        public void setRow(int row) { this.row = row; }
        public void setCol(int col) { this.col = col; }
    }
}
